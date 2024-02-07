<template>
  <div>
    <h1>Reflect</h1>

    <ul id="menu">
      <li v-for="(item, index) in menuItems" :key="index" :class="{ 'on': index === activeMenu }">
        <a @click.prevent="scrollToSection(index)">{{ item }}</a>
      </li>
    </ul>

    <!-- 전체 페이지 -->
  <div id="wap">
    <section v-for="(section, index) in sections" :key="index" :style="{ backgroundColor: section.backgroundColor }">
      <article>
        <p>{{ section.title }}</p>
      </article>
      <img :src="section.images" alt="" style="position: relative; top: 150px; left: 450px; width: 60%; height: 70%;">
    </section>
  </div>
</div>
</template>
<script>
import img1 from '@/img/일기쓰기페이지.png';
import img2 from '@/img/메인제품소개.png';
import img3 from '@/img/커뮤니티페이지.png';
import img4 from '@/img/펫고르기.png';
import { ref, onMounted } from 'vue';

export default {

data() {
   
  return {
    menuItems: ['일기 쓰기', '일기 관리', '커뮤 니티', '다양한 캐릭터'],
    activeMenu: 0,
    sections: [
      {
        title: '나의 하루를 음성으로 편하게 남겨봐요!',
        images: img1,
        backgroundColor: '#AAD9BB'
      },
      {
        title: '일기에 나만의 영상과 사진을 남겨봐요!',
        images: img2,
        backgroundColor: '#D5F0C1'
      },
      {
        title: '다른 사람들과 함께  나를 알아가봐요!',
        images: img3,
        backgroundColor: '#80BCBD'
      },
      {
        title: '다양한 캐릭터를    모아봐요!',
        images: img4,
        backgroundColor: '#F9F7C9'
      },
    ],
  };
},

methods: {
  
    scrollToSection(index) {
      let ht = window.innerHeight;
      let nowTop = index * ht + 56;
      window.scrollTo({
        top: nowTop,
        behavior: 'smooth',
      });
    },

  
  },
};
</script>

<style scoped>
@charset "utf-8";
@import url(//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css);

.snowContainer-wrapper {
  position: relative; /* 기준점 설정 */
  height: 100vh;
}
.snow-background {
  position: absolute;
  background-image: url('@/img/winter2.png');
  background-size: cover;
  background-position: center;
  z-index: 1110;
  
}

li {
  list-style: none;
}

a {
  text-decoration: none;
}


h1 {
  position: fixed;
  top: 70px;
  left: 150px;
  z-index: 10;
  font-size: 50px;
  font-weight: 700;
  line-height: 1;
  text-align: center;
  font-family: 'SANJUGotgam';
  color: #111;
  text-shadow: 2px 2px #fff;
}

ul#menu {
  font-family: 'SANJUGotgam';
  font-weight: bold;
  position: fixed;
  top: 50%;
  left: 0;
  margin-top: -100px;
  z-index: 13;
}

ul#menu a {
  display: block;
  width: 200px;
  height: 60px;
  font-size: 30px;
  color: #666;
  text-align: right;
  overflow: hidden;
  box-sizing: border-box;
  border-top: 1px solid #111;
  transition: all 0.3s;
  opacity: .9;
  padding-top: 10px;
}

ul#menu li:hover a {
  font-size: 30px;
  opacity: 1;
  text-shadow: none;
}

ul#menu li.on a {
  font-size: 30px;
  opacity: 1;
}

ul#menu li:nth-child(1):hover a {
  color: #ff3366;
  border-color: #ff3366;
  text-shadow: none;
}

ul#menu li:nth-child(2):hover a {
  color: #3366ff;
  border-color: #3366ff;
  text-shadow: none;
}

ul#menu li:nth-child(3):hover a {
  color: #ff6600;
  border-color: #ff6600;
  text-shadow: none;
}

ul#menu li:nth-child(4):hover a {
  color: #006666;
  border-color: #006666;
  text-shadow: none;
}

ul#menu li.on:nth-child(1) a {
  color: #ff3366;
  border-color: #ff3366;
  text-shadow: none;
}

ul#menu li.on:nth-child(2) a {
  color: #3366ff;
  border-color: #3366ff;
  text-shadow: none;
}

ul#menu li.on:nth-child(3) a {
  color: #ff6600;
  border-color: #ff6600;
  text-shadow: none;
}

ul#menu li.on:nth-child(4) a {
  color: #006666;
  border-color: #006666;
}

section {
  width: 100%;
  height: 700px;
  position: relative;
  background-size: cover;
  background-position: right center;
  background-attachment: fixed;
  overflow: hidden;
}

article {
  position: absolute;
  top: 7%;
  left: 450px;
  z-index: 11;
  /* width: 1000px; */
}

article p {
  font-family: 'Spoqa Han Sans Neo', 'sans-serif';
  text-shadow: 2px 2px #fff;
  font-size: 40px;
  font-weight: 900;
  letter-spacing: 2px;
  line-height: 2;
}

article span {
  
  font-size: 24px;
  color: #fff;
  text-align: center;
  font-weight: 800;
  display: block;
}
section:nth-child(1) p {
  color: #474843;
}
section:nth-child(2) p {
  color: #3366ff;
}
section:nth-child(3) p {
  color: #ff6600;
}
section:nth-child(4) p {
  color: #006666;
}
</style>