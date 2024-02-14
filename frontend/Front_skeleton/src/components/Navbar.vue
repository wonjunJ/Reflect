<template>
  <nav class="navbar navbar-expand-lg" style="background-color: #80BCBD;">
  <div class="container-fluid">
    <RouterLink class="navbar-brand" :to="{name: 'index'}">Navbar</RouterLink>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav" v-if="userStore.isLogin">
        <li class="nav-item">
          <RouterLink :to="{name: 'mypage'}">mypage</RouterLink>
        </li>
        &nbsp;
        <li class="nav-item">
          <RouterLink :to="{name: 'Diary'}">Diary</RouterLink>
        </li>
        &nbsp;
        <li class="nav-item">
          <RouterLink :to="{name: 'roomlist'}">roomlist</RouterLink>
        </li>
        &nbsp;
        <li class="nav-item">
          <div>{{ userStore.userInfo.point }}</div>
        </li>
        &nbsp;
        <li class="nav-item">
          <div><img src="@/img/bell-solid.svg" alt="">{{ userStore.userInfo.alrams.length }}</div>
        </li>
        &nbsp;
        <li class="nav-item">
          <img class="fortune-icon" src="@/assets/fortune-icon.jpg" alt="포춘쿠키아이콘" @click="openCookieModal">
        </li>
        &nbsp;
        <li class="nav-item" @click="userStore.logout()">
          <button type="button" class="btn">log out</button>
        </li>
      </ul>
      <ul class="navbar-nav" v-else>
        <li class="nav-item">
          <RouterLink :to="{name: 'signup'}">signup</RouterLink>
        </li>
        &nbsp;
        <li class="nav-item">
          <RouterLink :to="{name: 'login'}">login</RouterLink>
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
.fortune-icon{
  width: 45px;
  height: 45px;
  border-radius: 50%;
}
</style>