import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'

export const useUserStore = defineStore('user', () => {
  const router = useRouter()

  const isLogin = ref(false)

  function login() {
    isLogin.value = true
  }

  function logout() {
    isLogin.value = false
    router.push({name: 'index'})
  }

  return { isLogin, login, logout }
})