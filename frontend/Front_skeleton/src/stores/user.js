import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import axios from 'axios'

export const useUserStore = defineStore('user', () => {
  const API_URL = 'http://localhost:8080'
  // const API_URL = "https://i10d207.p.ssafy.io:8443/api"
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

  var stompClient = null

  function connect() {
    // SockJS 연결 설정
    var socket = new SockJS(`${API_URL}/reflect-socket`)
    // STOMP 클라이언트 생성
    stompClient = Stomp.over(socket)
    let headers = {
      Authorization: `Bearer ${accessToken.value}`
    }
    // 웹소켓 서버 연결
    stompClient.connect(headers, function (frame){
      console.log('Connected: ' + frame)

      stompClient.subscribe(`/alram/msg-to/${userId.value}`, function (greeting) {
        console.log(greeting)
        const message = JSON.parse(greeting.body).msg
      })
    })

  }

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
        connect()
        
      })
      .catch(err => {
        console.log(err)
        window.alert('아이디 또는 비밀번호가 잘못되었습니다.')
      })
  }

  function refresh() {
    axios({
      method: "post",
      url: `${API_URL}/user/refresh`,
      headers: {
        Authorization: `Bearer ${refreshToken.value}`
      }
    })
      .then(res => {
        accessToken.value = res.data.AccessToken
        refreshToken.value = res.data.RefreshToken
      })
      .catch(err => {
        console.log(err)
      })
  }

  function logout() {
    accessToken.value = null
    refreshToken.value = null
    userId.value = null
    router.push({name: 'index'})
  }

  return { accessToken, userId, refreshToken, isLogin, login, logout, API_URL, refresh }
}, { persist : {storage : sessionStorage}})