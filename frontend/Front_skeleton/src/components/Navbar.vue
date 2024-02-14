<template>
  <nav class="navbar navbar-expand-sm navbar-dark" style="background-color: #80BCBD">
    <div class="container-fluid">
      <RouterLink class="logo-section navbar-brand" :to="{ name: 'index' }">
        <img class="logo" src="@/assets/logo6.jpg" alt="로고">
        <p class="logo_text"> REFLECT </p>
      </RouterLink>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse justify-content-end" id="collapsibleNavbar">
        <ul class="navbar-nav">
          <li v-if="userStore.isLogin" class="nav-item">
            <RouterLink :to="{ name: 'mypage' }">MY PAGE</RouterLink>
          </li>
          &nbsp;
          <li v-if="userStore.isLogin" class="nav-item">
            <RouterLink :to="{ name: 'Diary' }">DIARY</RouterLink>
          </li>
          &nbsp;
          <li v-if="userStore.isLogin" class="nav-item">
            <RouterLink :to="{ name: 'roomlist' }">Room</RouterLink>
          </li>
          &nbsp;
          <li v-if="userStore.isLogin" class="nav-item">
            <img class="fortune-icon" src="@/assets/fortune-icon.jpg" alt="포춘쿠키아이콘" @click="onpenCookieModal">
          </li>
          <li v-if="!userStore.isLogin" class="nav-item">
            <RouterLink :to="{ name: 'signup' }">SIGN UP</RouterLink>
          </li>
          &nbsp;
          <li v-if="!userStore.isLogin" class="nav-item">
            <RouterLink :to="{ name: 'login' }">LOGIN</RouterLink>
          </li>
          <li v-if="userStore.isLogin" class="nav-item" @click="userStore.logout()">
            <button type="button" class="btn">LOGOUT</button>
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

const onpenCookieModal = () => {
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

.logo {
  margin-left: 10px;
  width: 40px;
  height: 50px;
}

.logo_text {
  color: black;
  margin: 10px;
  font-size: 25px;
  font-family: "YJ_Obang";
}
</style>