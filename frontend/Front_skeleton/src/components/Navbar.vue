<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
  <div class="container-fluid">
    <RouterLink class="navbar-brand" :to="{name: 'index'}">Navbar</RouterLink>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav">
        <div class="d-flex flex-row" v-if="userStore.isLogin">
          <li class="nav-item">
            <RouterLink :to="{name: 'Diary' }">Diary</RouterLink> &nbsp;
          </li>
        </div>
        <li v-if="!userStore.isLogin" class="nav-item">
            <RouterLink :to="{name: 'signup'}">signup</RouterLink>
        </li>
        &nbsp;
        <li v-if="!userStore.isLogin" class="nav-item">
          <RouterLink :to="{name: 'login'}">login</RouterLink>
        </li>
        <li v-if="userStore.isLogin" class="nav-item" @click="userStore.logout()">
          <button type="button" class="btn">log out</button>
        </li>
      </ul>
    </div>
  </div>
</nav>
</template>

<script setup>
import { RouterLink } from 'vue-router'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

</script>

<style scoped>

</style>