import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import axios from 'axios'

export const useUserStore = defineStore('user', () => {
  const router = useRouter()

  const token = ref(null)
  const isLogin = ref(false)

  function login() {
    isLogin.value = true
    router.push({name: 'mypage'})
  }

  // const isLogin = computed(() => {
  //   if (token.value === null) {
  //     return false
  //   } else {
  //     return true
  //   }
  // })

  // function login(id, password) {
  //   axios({
  //     method: 'post',
  //     url: `http://i10d207.p.ssafy.io:8080/user/login/`,
  //     data: {
  //       siteId: id,
  //       pw: password
  //     }
  //   })
  //     .then(res => {
  //       console.log(res.data)
  //       router.push({name: 'mypage'})
  //     })
  //     .catch(err => {
  //       console.log(err)
  //     })
  // }

  function logout() {
    isLogin.value = false
    router.push({name: 'index'})
  }

  return { token, isLogin, login, logout }
})