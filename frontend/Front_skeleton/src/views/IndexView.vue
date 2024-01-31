<template>
  <div class="snowContainer-wrapper">
    <div ref="snowContainer" class="snow-background"></div>
    <div class="content-main">
      <div class="content-main1">
        <h2>우리 서비스 소개</h2>
        <h3>캐릭터와 대화를 하며 말로 일기를 작성할 수 있는 앱</h3>
      </div>
      <img src="@/img/메인제품소개.png" alt="" class="main-product-img">
      
    </div>
    
  </div>
</template>

<script>
import { onMounted, ref } from 'vue';

export default {
  setup() {
    const snowContainer = ref(null);
    
    onMounted(() => {
      const Snow = new SnowCanvas();

      // Set number of flakes (optional)
      // default: window.innerWidth / 6
      Snow.createSnowFlakes(1200);

      const canvas = Snow.getCanvas(); // return HTMLCanvasElement
      snowContainer.value.appendChild(canvas);
    });

    return { snowContainer };
  }
}
</script>

<style scoped>
.snowContainer-wrapper {
  position: relative; /* 기준점 설정 */
  height: 100vh;
}

.snow-background {
  background-image: url('@/img/winterMain.png');
  background-size: cover;
  background-position: center;
  height: 100vh;
  position: absolute; /* 전체 wrapper에 대해 절대 위치 설정 */
  top: 0;
  left: 0;
  width: 100%; /* 전체 너비를 차지하도록 설정 */
  z-index: 0; /* 다른 내용 뒤에 배치 */
}

.content-main {
  display: grid; /* Grid 시스템 사용 선언 */
  grid-template-columns: 1fr 2fr; /* 2개의 컬럼, 비율은 1:2 */
  gap: 20px; /* 컬럼 사이 간격 설정 */
  align-items: center; /* 세로 방향 중앙 정렬 */
  position: relative; /* snow-background 위에 상대적 위치 설정 */
  z-index: 1; /* snow-background보다 위에 표시 */
  padding: 120px; /* 내용 주변 여백 추가 */
}

.content-main1 {
  color: aliceblue;
  grid-column: 1 / 3; /* 첫 번째 컬럼에 배치 */
}

.main-product-img {
  width: 800px; /* 그리드 컬럼 안에서 너비 100% 사용 */
  height: 600px; /* 높이는 자동으로 설정 (비율 유지) */
  grid-column: 3 / 3; /* 두 번째 컬럼에 배치 */
}
</style>
