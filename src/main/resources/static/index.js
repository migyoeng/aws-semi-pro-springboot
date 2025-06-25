function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('active');
}

function loadProjects() {
    fetch('/projects')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.text(); // JSP에서 렌더링된 HTML 반환
        })
        .then(html => {
            const projectList = document.getElementById('project-list');
            projectList.innerHTML = html; // 동적 로드 대신 서버 렌더링 사용
        })
        .catch(error => console.error('Error loading projects:', error));
}

// project-form이 존재할 때만 이벤트 리스너 추가
const projectForm = document.getElementById('project-form');
if (projectForm) {
    projectForm.addEventListener('submit', event => {
        event.preventDefault();
        const projectId = document.getElementById('projectId').value;
        const project = {
            project_idx: projectId || null,
            title: document.getElementById('title').value,
            content: document.getElementById('content').value,
            stacks: document.getElementById('selectedStacks').value,
            thumbnail: document.getElementById('thumbnail').value || null
        };
        const method = projectId ? 'PUT' : 'POST';
        const url = projectId ? `/saveProject/${projectId}` : '/saveProject';
        fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(project)
        }).then(() => {
            window.location.reload(); // 페이지 새로고침으로 갱신
        });
    });
}

function editProject(id) {
    fetch(`/saveProject/${id}`, { method: 'GET' })
        .then(response => {
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            return response.json();
        })
        .then(project => {
            document.getElementById('projectId').value = project.project_idx;
            document.getElementById('title').value = project.title;
            document.getElementById('content').value = project.content;
            document.getElementById('selectedStacks').value = project.stacks;
            document.getElementById('thumbnail').value = project.thumbnail || '';
            document.getElementById('cancelEdit').style.display = 'inline-block';
            updateSelectedStacks();
        });
}

function deleteProject(id) {
    if (confirm('Are you sure you want to delete this project?')) {
        fetch(`/saveProject/${id}`, { method: 'DELETE' })
            .then(() => window.location.reload());
    }
}

// cancelEdit 버튼이 존재할 때만 이벤트 리스너 추가
const cancelEditBtn = document.getElementById('cancelEdit');
if (cancelEditBtn) {
    cancelEditBtn.addEventListener('click', () => {
        const projectForm = document.getElementById('project-form');
        if (projectForm) {
            projectForm.reset();
        }
        const projectIdInput = document.getElementById('projectId');
        if (projectIdInput) {
            projectIdInput.value = '';
        }
        if (cancelEditBtn) {
            cancelEditBtn.style.display = 'none';
        }
        const selectedStacksInput = document.getElementById('selectedStacks');
        if (selectedStacksInput) {
            selectedStacksInput.value = '';
        }
    });
}

function updateSelectedStacks() {
    const select = document.getElementById('stacks');
    const selectedStacksInput = document.getElementById('selectedStacks');
    const selectedValues = Array.from(select.selectedOptions).map(option => option.value);
    selectedStacksInput.value = selectedValues.join(',');
}

// 프로젝트 카드 클릭 이벤트
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, setting up event listeners');
    
    // 프로젝트 카드 클릭 이벤트 리스너 추가
    document.addEventListener('click', function(e) {
        const projectCard = e.target.closest('.project-card');
        if (projectCard) {
            const projectUrl = projectCard.getAttribute('data-url');
            console.log('Project card clicked, URL:', projectUrl);
            if (projectUrl && projectUrl.trim() !== '') {
                console.log('Opening project URL:', projectUrl);
                window.open(projectUrl, '_blank');
            } else {
                console.log('No project URL found');
            }
        }
    });
    
    // 더보기 버튼 이벤트 리스너 추가 (버튼이 존재할 때만)
    const showMoreBtn = document.getElementById('showMoreBtn');
    if (showMoreBtn) {
        console.log('Show more button found, adding event listener');
        showMoreBtn.addEventListener('click', function(e) {
            e.preventDefault();
            toggleAdditionalStacks();
        });
    } else {
        console.log('Show more button not found');
    }
    
    // 필터 버튼들에 이벤트 리스너 추가
    const filterButtons = document.querySelectorAll('.filter-btn');
    console.log('Found filter buttons:', filterButtons.length);
    filterButtons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const stackName = this.getAttribute('data-stack');
            console.log('Filter button clicked:', stackName);
            
            // 더보기 버튼은 필터링하지 않음
            if (stackName === 'more') {
                return;
            }
            
            filterProjects(stackName);
        });
    });
});

// 프로젝트 필터링 함수
function filterProjects(stackName) {
    console.log('Filtering by:', stackName);
    
    // 모든 필터 버튼에서 active 클래스 제거
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('bg-blue-500', 'text-white');
        btn.classList.add('bg-gray-100', 'text-gray-700');
    });
    
    // 클릭된 버튼에 active 클래스 추가
    event.target.classList.remove('bg-gray-100', 'text-gray-700');
    event.target.classList.add('bg-blue-500', 'text-white');
    
    // 프로젝트 아이템들 숨기기/보이기
    const projectItems = document.querySelectorAll('.project-item');
    console.log('Total projects found:', projectItems.length);
    
    projectItems.forEach((item, index) => {
        if (stackName === 'all') {
            item.style.display = 'block';
            console.log(`Project ${index + 1}: Showing (all filter)`);
        } else {
            // 해당 프로젝트의 스택 배지들을 확인
            const stackBadges = item.querySelectorAll('[data-stack]');
            console.log(`Project ${index + 1}: Found ${stackBadges.length} stack badges`);
            
            let hasMatchingStack = false;
            
            stackBadges.forEach(badge => {
                const badgeStackName = badge.getAttribute('data-stack');
                console.log(`Project ${index + 1}: Checking badge "${badgeStackName}" against "${stackName}"`);
                if (badgeStackName === stackName) {
                    hasMatchingStack = true;
                    console.log(`Project ${index + 1}: MATCH FOUND!`);
                }
            });
            
            if (hasMatchingStack) {
                item.style.display = 'block';
                console.log(`Project ${index + 1}: Showing (has matching stack)`);
            } else {
                item.style.display = 'none';
                console.log(`Project ${index + 1}: Hiding (no matching stack)`);
            }
        }
    });
}

// 더보기 버튼 토글 함수
function toggleAdditionalStacks() {
    console.log('Toggle additional stacks clicked');
    const additionalStacks = document.getElementById('additionalStacks');
    const showMoreBtn = document.getElementById('showMoreBtn');
    
    if (additionalStacks && showMoreBtn) {
        if (additionalStacks.style.display === 'none' || additionalStacks.style.display === '') {
            additionalStacks.style.display = 'flex';
            showMoreBtn.innerHTML = '<i class="fas fa-chevron-up mr-1"></i>접기';
            showMoreBtn.classList.remove('bg-blue-500');
            showMoreBtn.classList.add('bg-gray-500');
            console.log('Showing additional stacks');
        } else {
            additionalStacks.style.display = 'none';
            showMoreBtn.innerHTML = '<i class="fas fa-chevron-down mr-1"></i>더보기';
            showMoreBtn.classList.remove('bg-gray-500');
            showMoreBtn.classList.add('bg-blue-500');
            console.log('Hiding additional stacks');
        }
        
        // 추가된 스택 버튼들에 이벤트 리스너 추가
        setTimeout(() => {
            document.querySelectorAll('#additionalStacks .filter-btn').forEach(btn => {
                if (!btn.hasAttribute('data-listener-added')) {
                    btn.addEventListener('click', function(e) {
                        e.preventDefault();
                        const stackName = this.getAttribute('data-stack');
                        console.log('Additional button clicked:', stackName);
                        filterProjects(stackName);
                    });
                    btn.setAttribute('data-listener-added', 'true');
                }
            });
        }, 100);
    }
}

// 페이지 로드 시 애니메이션 효과 설정
window.addEventListener('load', () => {
    console.log('Page loaded, setting up animations');
    
    document.querySelectorAll('.fade-in').forEach((el, index) => {
        el.style.animationDelay = `${index * 0.2}s`;
    });
});