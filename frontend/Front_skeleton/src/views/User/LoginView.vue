<template>
  <h1>Login Page</h1>
  <!-- 입력 폼 -->
  <el-form :model="state.form" :rules="state.rules" ref="loginForm" :label-position="state.form.align">
    <el-form-item prop="id" label="아이디" :label-width="state.formLabelWidth">
      <el-input v-model="state.form.id" :rules="state.rules.id" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item prop="password" label="비밀번호" :label-width="state.formLabelWidth">
      <el-input v-model="state.form.password" :rules="state.rules.password" autocomplete="off" show-password></el-input>
    </el-form-item>
    <RouterLink :to="{name: 'forgot'}">forgot ID or password ?</RouterLink>
    <el-form-item>
      <el-button type="primary" @click="login" :disabled="!isFormValid">login</el-button>
    </el-form-item>
  </el-form>
  <button class="btn btn-warning"><a href="http://localhost:8080/oauth2/authorization/kakao">카카오로 로그인</a></button>
  <button class="btn btn-warning" @click="socialLogin">카카오 로그인</button>
  <button @click="test">test</button>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { useRouter, RouterLink } from 'vue-router'
import axios from 'axios'
const userStore = useUserStore()
const API_URL = userStore.API_URL
const router = useRouter()

import { ref, reactive, computed } from 'vue'

// form 관련 설정
const state = reactive({
  // 입력 데이터
  form: {
    id: '',
    password: '',
    align: 'left'
  },
  // 규칙
  rules: {
    id: [
      { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
      { max: 16, message: '최대 16자까지 입력 가능합니다.', trigger: 'change' },
    ],
    password: [
    { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
    { min: 9, message: '최소 9자를 입력 해야합니다.', trigger: 'change' },
    { max: 16, message: '최대 16자까지 입력 가능합니다.', trigger: 'change' },
    { validator: (rule, value, callback) => {
      if (!/(?=.*[a-z])(?=.*\d)(?=.*\W)/.test(value)) {
        callback(new Error('비밀번호는 영문, 숫자, 특수문자가 조합되어야합니다.'))
      } else {
        callback()
      }
    }, trigger: 'change' }
    ]
  },
  // 라벨 너비
  formLabelWidth: '80px'
})

// 필수항목이 모두 입력되었는지 검증
const isFormValid = computed(() => {
  const rules = state.rules
  const form = state.form
  // 다 맞는지 확인하는 변수
  let isValid = true

  // 각 항목의 규칙들 살펴보기
  for (const key in rules) {
    if (rules[key]) {
      const itemRules = rules[key]
      for (const rule of itemRules) {
        if (rule.required && !form[key]) {
          isValid = false   // 필수사항인데 적힌게 없는 경우
          break
        }
        if (rule.validator) {
          const validatorCallback = (error) => {
            if (error) {
              isValid = false // 유효성 검사를 통과하지 못한 경우
            }
          }
          rule.validator({}, form[key], validatorCallback)
        }
      }
    }
  }
  return isValid
})

// async function socialLogin() {
//   axios({
//     method: 'get',
//     url: `${API_URL}/oauth2/authorization/kakao`,
//     params : {
//       redirect_url : 'http://localhost:5173',
//     }
//   })
//     .then(res => {
//       const urlParams = new URLSearchParams(window.location.search)
//       const data = JSON.parsee(urlParams.get('data'))
//     })
//     .catch(err => {
//       console.log(err)
//     })
// }

// async function socialLogin() {
  
// }

const socialLogin = function () {
  axios({
    method: 'get',
    url: `${API_URL}/oauth2/authorization/kakao`,
    params : {
      redirect_url : 'http://localhost:5173',
    }
  })
    .then(res => {
      console.log(res.data)
      const urlParams = new URLSearchParams(window.location.search)
      const data = JSON.parsee(urlParams.get('data'))
    })
    .catch(err => {
      console.log(err)
    })
}



function login() {
  userStore.login(state.form.id, state.form.password)
}
</script>

<style scoped>

</style>