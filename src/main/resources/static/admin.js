function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('active');
}

function loadProjects() {
    fetch('/api/projects')
        .then(response => response.json())
        .then(data => {
            const projectList = document.getElementById('project-list');
            projectList.innerHTML = '';
            data.forEach(project => {
                const stackNames = project.stacks ? project.stacks.map(stack => stack.name).join(', ') : '';
                const projectUrlLink = project.projectUrl ? 
                    `<div class="mt-2">
                        <a href="${project.projectUrl}" target="_blank" class="text-blue-600 hover:text-blue-800">
                            <i class="fas fa-external-link-alt mr-1"></i>프로젝트 링크
                        </a>
                    </div>` : '';
                
                projectList.innerHTML += `
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1">${project.title}</h5>
                            <p class="mb-1 text-muted">${project.description}</p>
                            <small class="text-muted">
                                <span class="badge bg-primary me-1">${stackNames}</span>
                                <span>${new Date(project.createdAt).toLocaleDateString()}</span>
                            </small>
                            ${projectUrlLink}
                        </div>
                        <div>
                            <button class="btn btn-sm btn-outline-primary me-2" onclick="editProject(${project.id})">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <button class="btn btn-sm btn-outline-danger" onclick="deleteProject(${project.id})">
                                <i class="fas fa-trash"></i> Delete
                            </button>
                        </div>
                    </li>`;
            });
        })
        .catch(error => console.error('Error loading projects:', error));
}

document.getElementById('project-form').addEventListener('submit', event => {
    event.preventDefault();
    const projectId = document.getElementById('projectId').value;
    const selectedTechStack = Array.from(document.querySelectorAll('#selectedTechStack span'))
        .map(chip => chip.textContent.replace('×', '').trim());
    
    // 선택된 스택들을 Stack 객체 배열로 변환
    const stacks = selectedTechStack.map(stackName => ({ name: stackName }));
    
    const project = {
        id: projectId || null,
        title: document.getElementById('title').value,
        description: document.getElementById('description').value,
        stacks: stacks,
        projectUrl: document.getElementById('projectUrl').value || null
    };
    
    console.log('Sending project data:', project); // 디버깅용 로그
    
    const method = projectId ? 'PUT' : 'POST';
    const url = projectId ? `/api/projects/${projectId}` : '/api/projects';
    
    fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(project)
    })
    .then(response => {
        console.log('Response status:', response.status); // 디버깅용 로그
        if (response.ok) {
            return response.json();
        } else {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
    })
    .then(data => {
        console.log('Project saved successfully:', data); // 디버깅용 로그
        loadProjects();
        event.target.reset();
        document.getElementById('projectId').value = '';
        document.getElementById('cancelEdit').style.display = 'none';
        document.getElementById('selectedTechStack').innerHTML = '';
        alert(projectId ? 'Project updated successfully!' : 'Project created successfully!');
    })
    .catch(error => {
        console.error('Error saving project:', error);
        alert('Failed to save project. Please try again. Error: ' + error.message);
    });
});

function editProject(id) {
    fetch(`/api/projects/${id}`)
        .then(response => response.json())
        .then(project => {
            document.getElementById('projectId').value = project.id;
            document.getElementById('title').value = project.title;
            document.getElementById('description').value = project.description;
            document.getElementById('projectUrl').value = project.projectUrl || '';
            
            // Tech stack 칩 초기화 및 설정
            document.getElementById('selectedTechStack').innerHTML = '';
            if (project.stacks && project.stacks.length > 0) {
                project.stacks.forEach(stack => {
                    addTechStackChip(stack.name);
                });
            }
            
            document.getElementById('cancelEdit').style.display = 'inline-block';
        })
        .catch(error => console.error('Error loading project:', error));
}

function deleteProject(id) {
    if (confirm('Are you sure you want to delete this project?')) {
        fetch(`/api/projects/${id}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    loadProjects();
                    alert('Project deleted successfully!');
                } else {
                    throw new Error('Failed to delete project');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Failed to delete project. Please try again.');
            });
    }
}

document.getElementById('cancelEdit').addEventListener('click', () => {
    document.getElementById('project-form').reset();
    document.getElementById('projectId').value = '';
    document.getElementById('cancelEdit').style.display = 'none';
    document.getElementById('selectedTechStack').innerHTML = '';
});

function handleTechStackSelection() {
    const select = document.getElementById('techStack');
    const selectedValue = select.value;
    
    if (selectedValue && !Array.from(document.querySelectorAll('#selectedTechStack span'))
        .some(chip => chip.textContent.replace('×', '').trim() === selectedValue)) {
        addTechStackChip(selectedValue);
    }
    
    select.value = '';
}

function addTechStackChip(tech) {
    const chip = document.createElement('span');
    chip.className = 'bg-blue-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded-full flex items-center mr-2 mb-2';
    chip.textContent = tech;
    
    const removeBtn = document.createElement('button');
    removeBtn.className = 'ml-1 text-red-500 hover:text-red-700 focus:outline-none';
    removeBtn.innerHTML = '×';
    removeBtn.onclick = () => {
        chip.remove();
    };
    
    chip.appendChild(removeBtn);
    document.getElementById('selectedTechStack').appendChild(chip);
}

window.addEventListener('load', () => {
    document.querySelectorAll('.fade-in').forEach((el, index) => {
        el.style.animationDelay = `${index * 0.2}s`;
    });
    loadProjects();
    
    const select = document.getElementById('techStack');
    select.addEventListener('change', handleTechStackSelection);
});