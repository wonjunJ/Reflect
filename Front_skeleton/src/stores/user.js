import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
// import SockJS from 'sockjs-client/dist/sockjs.min.js'
import SockJS from "sockjs-client/dist/sockjs";
import Stomp from 'webstomp-client'
import axios from 'axios'

export const useUserStore = defineStore('user', () => {
  const API_URL = 'https://i10d207.p.ssafy.io:8443/api'
  const router = useRouter()
  const accessToken = ref(null)
  const refreshToken = ref(null)
  const userId = ref(null)

  const userInfo = ref({})

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
    stompClient.connect({}, function (frame){
      console.log('Connected: ' + frame)

      stompClient.subscribe(`/alram/msg-to/${userId.value}`, function (greeting) {
        console.log(greeting)
        const message = JSON.parse(greeting.body).msg
      })
    })
  }

  function getUserInfo() {
    axios({
      method: 'get',
      url: `${API_URL}/user/`,
      headers: {
        Authorization: `Bearer ${accessToken.value}`
      }
    })
      .then(res => {
        const { point, titles, alrams, charName, charId } = res.data
        userInfo.value = { point, titles, alrams, charName, charId }
      })
      .catch(err => {
        if (err.response.status === 401) {
          refresh()
          getUserInfo()
        } 
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
        getUserInfo()
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
    router.push({name: 'index'})
    accessToken.value = null
    refreshToken.value = null
    userId.value = null
    userInfo.value = {}
  }

  return { accessToken, userId, refreshToken, isLogin, login, logout, API_URL, refresh, stompClient, connect, getUserInfo, userInfo }
}, { persist : {storage : sessionStorage}})