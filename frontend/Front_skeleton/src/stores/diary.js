import axios from 'axios'
import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'


export const useDiaryStore = defineStore('diary', () => {
  // const BASE_URL = 'https://i10d207.p.ssafy.io:8443/api'
  // const diary = ref([])
  // const userInfo = ref(null)
  
  
  // const getSavings = function() {
  //   axios({
  //     method: 'get',
  //     url: `${BASE_URL}/api/v1/savings/`
  //   })
  //     .then((res) => {
  //       // console.log(res)
  //       savings.value = res.data
  //     })
  //     .then(() => {
  //       getBanks()
  //       console.log('getbank - savings')
  //     })
  //     .catch((err) => {
  //       console.log(err)
  //     })
  // }

  // const signUp = function (payload) {
  //   const {
  //     username, password1, password2,
  //     nickname, email, birthyear, 
  //     money, salary, mainbank, location,
  //   } = payload
  //   axios({
  //     method: 'post',
  //     url: `${BASE_URL}/dj-rest-auth/signup/`,
  //     data: {
  //       username, password1, password2,
  //       nickname, email, birthyear, 
  //       money, salary, mainbank, location,
  //     }
  //   })
  //     .then((res) => {
  //       // console.log(res)
  //       payload = {
  //         username,
  //         password: password1
  //       }
  //       login(payload)
  //     })
  //     .catch((err) => {
  //       console.log(err)
  //     })
  // }

  // const getUserInfo = function () {
  //   axios({
  //     method: 'get',
  //     url: `${BASE_URL}/api/v1/userinfo/`,
  //     headers: {
  //       Authorization: `Token ${token.value}`
  //     }
  //   })
  //     .then((res) => {
  //       console.log(res)
  //       userInfo.value = res.data
  //     })
  //     .catch((err) => {
  //       console.log(err)
  //     })
  // }

  // const updateUserInfo = function (payload) {
  //   const {
  //     nickname, email, birthyear, 
  //     money, salary, mainbank, location,
  //   } = payload
  //   axios({
  //     method: 'put',
  //     url: `${BASE_URL}/api/v1/userinfo/`,
  //     data: {
  //       nickname, email, birthyear, 
  //       money, salary, mainbank, location,
  //     },
  //     headers: {
  //       Authorization: `Token ${token.value}`
  //     }
  //   })
  //     .then((res) => {
  //       console.log(res)
  //       userInfo.value = res.data
  //       alert('정보가 수정되었습니다!')
  //     })
  //     .catch((err) => {
  //       console.log(err)
  //     })
  // }

  // return {
  //   getSavings,
  //   signUp,
  //   getUserInfo,
  //   updateUserInfo,
  // }
}, { persist: true })