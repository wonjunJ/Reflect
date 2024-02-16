<template>
  <nav class="navbar navbar-expand-sm navbar-dark" style="background-color: #80BCBD">
    <div class="container-fluid">
      <RouterLink class="logo-section navbar-brand" :to="{ name: 'index' }">
        <img class="logo" src="@/assets/logo6.jpg" alt="로고">
        <p class="logo_text"> REFLECT </p>
      </RouterLink>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
        <ul class="navbar-nav" v-if="userStore.isLogin">
          <li class="nav-item">
            <RouterLink :to="{ name: 'mypage' }">MY PAGE</RouterLink>
          </li>
          &nbsp;
          <li class="nav-item">
            <RouterLink :to="{ name: 'Diary' }">DIARY</RouterLink>
          </li>
          &nbsp;
          <li class="nav-item">
            <RouterLink :to="{ name: 'roomlist' }">COMMUNITY</RouterLink>
          </li>
          &nbsp;
          <li class="nav-item">
            <div>{{ userStore.userInfo.point }} 포인트 </div>
          </li>
          &nbsp;
          <li class="nav-item">
            <div class="alarm"><img src="@/img/bell-solid.svg" alt="">{{ userStore.userInfo.alrams.length }}</div>
          </li>
          &nbsp;
          <li class="nav-item">
            <img class="fortune-icon" src="@/assets/fortune-icon.jpg" alt="포춘쿠키아이콘" @click="openCookieModal">
          </li>
          &nbsp;
          <li class="nav-item" @click="userStore.logout()">
            <button type="button" class="navbar-nav">LOG OUT</button>
          </li>
        </ul>
        <ul class="navbar-nav" v-else>
          <li class="nav-item">
            <RouterLink :to="{ name: 'signup' }">signup</RouterLink>
          </li>
          &nbsp;
          <li class="nav-item">
            <RouterLink :to="{ name: 'login' }">login</RouterLink>
          </li>
        </ul>
      </div>
      <FortuneCookie :isCookie="isCookie" @closeModal="closeModal" />

    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue';
import { RouterLink } from 'vue-router'
import { useUserStore } from '@/stores/user'
import FortuneCookie from './PopUp/FortuneCookie.vue';

const userStore = useUserStore()
const isCookie = ref(false)

const openCookieModal = () => {
  isCookie.value = true
}

const closeModal = () => {
  isCookie.value = false
}

</script>

<style scoped>
a {
  color: black;
  font-weight: bold;
  text-decoration: none;
  font-size: 20px;
}

li {
  padding: 10px;
}

.fortune-icon {
  width: 45px;
  height: 45px;
  border-radius: 50%;
}

.logo-section {
  display: flex;
  align-content: row;
  align-items: center;
}

.nav-item {
  color: black;
  font-weight: bold;
  text-decoration: none;
  font-size: 20px;
}

.logo {
  margin-left: 10px;
  width: 40px;
  height: 50px;
}

.alarm {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.logo_text {
  color: black;
  margin: 10px;
  font-size: 25px;
  font-family: "YJ_Obang";
}
</style>