// 펫 요소 가져오기
let pet = document.getElementById('pet');
// 회전 각도
let deg = 0;
// Y축 회전 각도
let deg_y = 0;
// 마우스까지 펫이 이동해야 할 거리 기록
let mousePosition = {
    x: 0,
    y: 0,
}
// 펫 위치 기록
let position = {
    x: 0,
    y: 0,
}

// 현재 이동 횟수 기록
let count = 0;
// 마우스 위치까지 펫이 이동하는 데 필요한 횟수
let speed = 50;

// 추격 상태인지 여부
let isCatchUp = false;
// 클릭 상태인지 여부
let isClick = false;

// 대기 상태인지 여부
let isIdle = true;

// 공격 애니메이션 상태 기록
let attack = {
    // 현재 프레임
    current: 0,
    // 지속 시간
    max: 340,
}
// 클릭 애니메이션 상태 기록
let click = {
    current: 0,
    max: 40
}

// 걷기 애니메이션 상태 기록
let walk = {
    current: 0,
    max: 130
}

// 클릭 이벤트 처리
document.getElementById("pet").onclick = function () {
    isClick = true;
};

// 마우스 이동 이벤트 처리
window.addEventListener('mousemove', function (event) {

    // 기대하는 이미지의 마우스 X 축 위치 = 현재 마우스 위치 - 펫 왼쪽으로부터의 거리 - 펫 너비의 중심 위치
    mousePosition.x = event.x - pet.offsetLeft - pet.clientWidth / 2;
    mousePosition.y = event.y - pet.offsetTop - pet.clientHeight / 2;
    // 회전 각도
    deg = 360 * Math.atan(mousePosition.y / mousePosition.x) / (2 * Math.PI);
    // clientX 이벤트 속성은 이벤트가 발생했을 때 마우스 포인터의 브라우저 페이지(또는 클라이언트 영역)에 대한 수평 좌표를 반환합니다.
    // 여기에서 이미지 위치 설정에 관한 주의사항: gif 이미지의 방향을 고려하십시오. 원본 이미지가 왼쪽을 향하면 여기는 작음(<)이어야하고, 원본 이미지가 오른쪽을 향하면 여기는 큼(>)이어야합니다.
    // 이미지 뒤집기
    if (pet.offsetLeft > event.clientX) {
        deg_y = - 180;
    } else {
        deg_y = 0;
    }
    // 여기에서 매번 마우스를 이동할 때마다 거리를 다시 계산해야하므로 여기서는 count를 0으로 설정해야합니다.
    count = 0;
})

// 매 10밀리초마다 반복 실행
setInterval(() => {
    // 마우스 추적 상태
    if (isCatchUp == false && isIdle == false) {
        catchUpState();
    }
    // 공격 상태
    else if (isCatchUp == true && isIdle == false) {
        attackState();
    }
    // 클릭 상태
    else if (isClick == true) {
        clickState();
    }
}, 10)

function catchUpState() {
    // 추격 상태 애니메이션 재생 준비
    if (walk.current == 0) {
        // 현재 펫은 추격 상태이므로 gif 이미지를 Walk.gif로 전환
        document.getElementById("pet").src = "./img/Walk.gif"
        // 서로 다른 GIF 이미지에 대한 폭과 높이가 다를 수 있으므로 필요에 따라 여기에서 조정할 수 있습니다.
        pet.style.width = 50 + "px"
        // 현재 애니메이션 프레임 +1
        walk.current++
    }
    // 추격 상태 GIF 재생 시작
    else if (walk.current < walk.max) {
        // 펫 각도 조절
        pet.style.transform = "rotateZ(" + deg + "deg) rotateY(" + deg_y + "deg)"
        // 만약 마우스를 추적하지 못했다면
        if (count < speed) {
            // position.x와 y는 각각 현재 펫의 가로 및 세로 좌표 위치를 기록합니다.
            // 현재의 가로 및 세로 좌표 위치이며, += 방식으로 이동하고 speed가 클수록 이동 속도가 느려집니다.
            // 두 지점 사이의 위치를 speed로 나누어 speed 부분으로 이동합니다. 매번 새로 고려하기 때문에 여기에서는 count를 0으로 설정해야합니다.
            position.x += mousePosition.x / speed
            position.y += mousePosition.y / speed
        }
        // 마우스를 추적했다면
        else {
            // 상태 변경
            isCatchUp = true
            walk.current = 0
        }
        // 현재 펫 위치를 실제로 그립니다.
        pet.style.left = position.x + "px"
        pet.style.top = position.y + "px"
        count++
        walk.current++;
    }
    // 추격 상태 GIF 재생 종료 및 추적 상태 GIF 재생 다시 시작
    else if (walk.current >= walk.max) {
        walk.current = 0;
        pet.style.width = 50 + "px"
        document.getElementById("pet").src = "./img/Walk.gif"
    }
}

function attackState() {
    // 공격 상태 애니메이션 재생 준비
    if (attack.current == 0) {
        // 애니메이션 설정 조정
        document.getElementById("pet").src = "./img/Attack.gif"
        pet.style.width = 100 + "px"
        attack.current++
    }
    // 공격 상태 애니메이션 재생 시작, 여기에서 특정 프레임(current 카운트)에서 공격 범위 및 공격 처리 함수를 설정할 수 있습니다.
    else if (attack.current < attack.max) {
        attack.current++;
    }
    // 공격 애니메이션 재생 종료, 해당 상태 변경 및 대기 애니메이션 재생 시작
    else if (attack.current >= attack.max) {
        // 애니메이션 설정 초기화
        isCatchUp = false;
        isIdle = true;
        attack.current = 0;
        document.getElementById("pet").src = "./img/Idle.gif"
        pet.style.width = 50 + "px"
    }
}

function clickState() {
    // 클릭 상태 애니메이션 재생 준비
    if (click.current == 0) {
        document.getElementById("pet").src = "./img/Hit.gif"
        pet.style.width = 60 + "px"
        click.current++
    }
    // 클릭 상태 애니메이션 재생 시작, 여기에서 해당하는 피격 동작 함수를 처리할 수 있습니다.
    else if (click.current < click.max) {
        click.current++;
    }
    // 클릭 상태 애니메이션 재생 종료, 상태 변경 및 걷기 애니메이션 재생
    else if (click.current >= click.max) {
        click.current = 0;
        document.getElementById("pet").src = "./img/Walk.gif"
        pet.style.width = 50 + "px"
        isClick = false;
        isIdle = false;
    }
}
