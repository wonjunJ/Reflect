<script setup>
import { useRoute } from 'vue-router'
import { computed, watch } from 'vue';
import { RouterLink, RouterView } from 'vue-router'
import { usePositionStore } from '@/stores/pet.js';
import Navbar from './components/Navbar.vue';
import Footer from './components/Footer.vue';

// const positionStore = usePositionStore();
// positionStore.setPosition(100, 200);
</script>

<template>

  <div id="app">
    <Navbar/>
    <RouterView/>
    <img id="pet" class="pet" :src="getPetImage" alt="Pet"/>
    <!-- 조건부 렌더링으로 /Diary 경로일 때만 Diary 컴포넌트를 표시 -->
    <Footer/>
  </div>
</template>

<script>
import WalkImage from './img/corgiWalk.gif';
import HitImage from './img/corgiClick.gif';
import IdleImage from './img/corgi.gif';
import AttackImage from './img/corgiSitDown.gif'
import { usePositionStore } from '@/stores/pet.js';
export default {
  data() {
    return {
      isDiaryRoute: false,
      pet: null,
      deg: 0,
      deg_y: 0,
      mousePosition: { x: 0, y: 0 },
      position: { x: 850, y: 400 },
      count: 0,
      speed: 50,
      isCatchUp: false,
      isClick: false,
      isIdle: true,
      attack: { current: 0, max: 140 },
      click: { current: 0, max: 110 },
      walk: { current: 0, max: 100 },
    };
  },
  mounted() {
    const petStore = usePositionStore();
    
    // position 객체의 변화를 감시
    watch(() => this.position, (newPosition) => {
      petStore.setPosition(newPosition.x, newPosition.y);
    }, {
      deep: true, // 객체 내부의 변화도 감지하기 위해 deep 옵션 사용
    });

    // 기존에 정의된 mounted 로직...
  
    this.pet = document.getElementById('pet');

    this.pet.onclick = () => {
      this.isClick = true;
    };

    window.addEventListener('mousemove', (event) => {
      this.mousePosition.x = event.x - this.pet.offsetLeft - this.pet.clientWidth / 2;
      this.mousePosition.y = event.y - this.pet.offsetTop - this.pet.clientHeight / 2;
      this.deg = (360 * Math.atan(this.mousePosition.y / this.mousePosition.x)) / (2 * Math.PI);

      if (this.pet.offsetLeft > event.clientX) {
        this.deg_y = 0;
      } else {
        this.deg_y = -180;
      }

      this.count = 0;
    });

    setInterval(this.updatePet, 10);
  },
  computed: {
    getPetImage() {
      if (this.isCatchUp) {
        return AttackImage;
      } else if (this.isClick) {
        return HitImage;
      } else if (this.isIdle) {
        return IdleImage;
      // } else if (this.isIdle) {
      //   return AttackImage;
      } else {
        return WalkImage; // Modify this according to your requirements
      }
    },
  },
  methods: {
    // components: {
    // Diary
    // },
    // setup() {
    //   const route = useRoute();
      
    //   const isDiaryRoute = computed(() => {
    //     return route.path === '/Diary'; // '/diary'는 Diary 컴포넌트가 활성화될 경로
    //   });

    //   return {
    //     isDiaryRoute,
    //     position
    //     // ...
    //   };
    // },
    updatePet() {
      if (!this.isCatchUp && !this.isIdle) {
        this.catchUpState();
      } else if (this.isCatchUp && !this.isIdle) {
        this.attackState();
      } else if (this.isClick) {
        this.clickState();
      }
    },
    catchUpState() {
      if (this.walk.current === 0) {
        // this.pet.src = './img/Walk.gif';
        
        this.walk.current++;
      } else if (this.walk.current < this.walk.max) {
        this.pet.style.transform = `rotateZ(${this.deg}deg) rotateY(${this.deg_y}deg)`;

        if (this.count < this.speed) {
          this.position.x += this.mousePosition.x / this.speed;
          this.position.y += this.mousePosition.y / this.speed;
        } else {
          this.isCatchUp = true;
          this.walk.current = 0;
        }

        this.pet.style.left = `${this.position.x}px`;
        this.pet.style.top = `${this.position.y}px`;
        
        this.count++;
        this.walk.current++;
      } else if (this.walk.current >= this.walk.max) {
        this.walk.current = 0;
        
        // this.pet.src = './img/Walk.gif';
      }
    },
    attackState() {
      if (this.attack.current === 0) {
        this.pet.style.width = '200px';        
        this.attack.current++;
      } else if (this.attack.current < this.attack.max) {
        this.attack.current++;
      } else if (this.attack.current >= this.attack.max) {
        this.isCatchUp = false;
        this.isIdle = true;
        this.attack.current = 0;
        this.pet.style.width = '150px';
        // this.pet.src = './img/Idle.gif';
        
      }
    },
    clickState() {
      if (this.click.current === 0) {
        //this.pet.src = './img/Hit.gif';
        
        this.click.current++;
      } else if (this.click.current < this.click.max) {
        this.click.current++;
      } else if (this.click.current >= this.click.max) {
        this.click.current = 0;
        // this.pet.src = './img/Walk.gif';
        
        this.isClick = false;
        this.isIdle = false;
      }
    },
  },
};
</script>

<style scoped>
@import "https://unpkg.com/@yaireo/tagify/dist/tagify.css";
.pet {
    width: 150px;
    height: 165px;
    position: fixed;
    left: 850px; /* X 좌표 */
    top: 400px; /* Y 좌표 */
    background-size: cover;
    z-index: 1000;
}
#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content {
  flex: 1;
}
</style>