<template>
  <div>
    <h1>Login Page</h1>

    <button @click="login">login</button>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
const userStore = useUserStore()
const router = useRouter()

function login() {
    userStore.login()
    router.push({name: 'mypage'})
}
</script>

<style scoped>

</style>