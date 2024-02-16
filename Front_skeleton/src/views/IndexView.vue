<template>
  <div class="">

    <vueper-slides autoplay :infinite="true" class="sliders" fixed-height="84vh">

      <vueper-slide :key="1" :style="'background-color: #F9F7C9'">
        <template v-slot:content>
          <div class="slider">
            <div class="title">일기쓰기</div>
            <div class="sub-title">나의 하루를 음성으로 편하게 남겨 보아요</div>
            <div class="img-section"> <img src="../img/diary.PNG" alt="로고"></div>
          </div>
        </template>
      </vueper-slide>
      <vueper-slide :key="2" :style="'background-color: #F9F7C9'">
        <template v-slot:content>
          <div class="slider">
            <div class="title">일기확인</div>
            <div class="sub-title">기록된 일기를 확인하고 분석된 나를 알아보아요</div>
            <div class="img-section"> <img src="../img/mypage.PNG" alt="로고"></div>
          </div>
        </template>
      </vueper-slide>
      <vueper-slide :key="3" :style="'background-color: #F9F7C9'">
        <template v-slot:content>
          <div class="slider">
            <div class="title">커뮤니티</div>
            <div class="sub-title">다른 사람들과 함께 대화하며 나를 알아가보아요</div>
            <div class="img-section"> <img src="../img/community.PNG" alt="로고"></div>
          </div>
        </template>
      </vueper-slide>
      <vueper-slide :key="4" :style="'background-color: #F9F7C9'">
        <template v-slot:content>
          <div class="slider">
            <div class="title">다양한 캐릭터</div>
            <div class="sub-title">다양한 캐릭터를 모으고 소통해 보아요</div>
            <div class="img-section"> <img src="../img/character.PNG" alt="로고"></div>
          </div>
        </template>
      </vueper-slide>
    </vueper-slides>




    <!-- 전체 페이지 -->
  </div>
</template>

<script>
import { VueperSlides, VueperSlide } from 'vueperslides'
import 'vueperslides/dist/vueperslides.css'
import img1 from '@/img/일기쓰기페이지.png';
import img2 from '@/img/메인제품소개.png';
import img3 from '@/img/커뮤니티페이지.png';
import img4 from '@/img/펫고르기.png';
import { ref, onMounted } from 'vue';

export default {

  components: {
    VueperSlides,
    VueperSlide
  },

  setup() {

    const slides = [
      {
        title: 'Slide #1',
        content: 'Slide 1 content.',
        image: img1
      },
      {
        title: 'Slide #2',
        content: 'Slide 2 content.',
        image: img2
      },
      {
        title: 'Slide #3',
        content: 'Slide 2 content.',
        image: img3
      },
      {
        title: 'Slide #3',
        content: 'Slide 2 content.',
        image: img4
      }
    ]

    const snowContainer = ref(null);

    onMounted(() => {
      const Snow = new SnowCanvas();
      Snow.createSnowFlakes(1000);

      const canvas = Snow.getCanvas();

    });
    return { snowContainer, slides };

  },
  data() {

    return {
      pauseOnHover: true,
      autoPlaying: true,
      internalAutoPlaying: true,
      activeMenu: 0,

    };
  },
  mounted() {
    this.setSectionHeight();
    window.addEventListener('resize', this.setSectionHeight);
    window.addEventListener('scroll', this.handleScroll);

  },

  methods: {
    setSectionHeight() {
      let ht = window.innerHeight;
      document.querySelectorAll('section').forEach((section) => {
        section.style.height = `${ht}px`;
      });
    },


  },
};
</script>

<style scoped>
.slider {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 80vh;
}

/* .sliders {
  width: 70%;
  height: 100%;
} */

.img-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow: hidden;
  /* 이미지가 부모 요소를 넘어가지 않도록 설정 */
}

.img-section img {
  width: 40%;
  /* 이미지가 부모 요소에 맞게 크기 조정 */
  height: auto;
  margin-top: 5px;
  /* 가로 비율 유지 */
}

.title {
  font-family: "YJ_Obang";
  font-size: 45px;
  padding-top: 30px;
}

.sub-title {
  font-family: "Yeongdeok Sea";
  margin: 10px;
  font-size: 20px;
}

.vueperslides {
  height: 100%;
}
</style>