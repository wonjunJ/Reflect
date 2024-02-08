import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import axios from 'axios'

export const useUserStore = defineStore('user', () => {
  const API_URL = 'http://localhost:8080'
  //const API_URL = 'https://i10d207.p.ssafy.io:8443/api'
  const router = useRouter()
  const accessToken = ref(null)
  const refreshToken = ref(null)
  const userId = ref(null)

  // const isLogin = ref(false)

  // function login() {
  //   isLogin.value = true
  //   router.push({name: 'mypage'})
  // }

  const isLogin = computed(() => {
    if (accessToken.value) {
      return true
    } else {
      return false
    }
  })

  function login(id, password) {
    axios({
      method: 'post',
      url: `${API_URL}/user/login`,
      data: {
        siteId: id,
        pw: password
      }
    })
      .then(res => {
        accessToken.value = res.data.AccessToken
        refreshToken.value = res.data.RefreshToken
        userId.value = res.data.userId
        router.push({name: 'mypage'})
      })
      .catch(err => {
        window.alert('아이디 또는 비밀번호가 잘못되었습니다.')
      })
  }

  function logout() {
    accessToken.value = null
    refreshToken.value = null
    userId.value = null
    router.push({name: 'index'})
  }

  return { accessToken, userId, refreshToken, isLogin, login, logout, API_URL }
}, { persist : {storage : sessionStorage}})