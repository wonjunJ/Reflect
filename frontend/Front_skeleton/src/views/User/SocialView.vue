<template>
  <div>
    <el-form :model="state.form" :rules="state.rules" ref="signupForm" :label-position="state.form.align">
      <el-form-item prop="nickname" label="닉네임" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.nickname" :rules="state.rules.nickname" autocomplete="off"></el-input>
        <el-button @click="nicknameCheck" :disabled="state.isNicknameCheck">닉네임 중복 확인</el-button>
        <div v-if="state.nicknameValidationMessage" :class="state.isNicknameCheck ? 'green' : 'red'">{{
          state.nicknameValidationMessage }}</div>
      </el-form-item>
      <el-form-item prop="email" label="이메일" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.email" :rules="state.rules.email" autocomplete="off"></el-input>
        <el-button @click="sendEmail" :disabled="!isEmailValid">이메일 인증 요청</el-button>
        <el-form-item v-if="state.isEmailsend" prop="emailCode">
          <el-input v-model="state.emailCode" autocomplete="off"></el-input>
          <el-button @click="validateEmail" :disabled="state.isEmailCheck">이메일 인증</el-button>
        </el-form-item>
        <div v-if="state.emailValidationMessage" :class="state.isEmailCheck ? 'green' : 'red'">{{
          state.emailValidationMessage }}</div>
      </el-form-item>
      <el-form-item prop="name" label="이름" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.name" :rules="state.rules.name" autocomplete="off" disabled></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :disabled="!isFormValid" @click="socialSignup">signup</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const API_URL = userStore.API_URL
const route = useRoute()
const router = useRouter()

const queryParams = JSON.parse(route.query.data)

const state = reactive({
  // 입력 데이터
  form: {
    nickname: '',
    email: '',
    name: queryParams.nickname,
    align: 'left'
  },
  // 규칙
  rules: {
    nickname: [
      { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
      { min: 3, message: '최소 3자를 입력 해야합니다.', trigger: 'change' },
      { max: 16, message: '최대 16자까지 입력 가능합니다.', trigger: 'change' },
    ],
    email: [
      { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
      {
        validator: (rule, value, callback) => {
          if (!/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/.test(value)) {
            callback(new Error('유효한 이메일을 입력해주세요.'))
          } else {
            callback()
          }
        }, trigger: 'change'
      }
    ],
    name: [
      { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
      { max: 30, message: '최대 30자까지 입력 가능합니다.', trigger: 'change' },
    ],
  },
  formLabelWidth: '120px',    // 라벨 너비
  isNicknameCheck: false,         // 닉네임 중복검사 여부
  nicknameValidationMessage: '',  // 닉네임 중복검사 메세지
  isEmailsend: false,            // 이메일 인증 요청을 한 경우
  emailCode: '',
  emailValidationMessage: '',     // 이메일 인증 요청 메세지
  isEmailCheck: false,
})

const isFormValid = computed(() => {
  const rules = state.rules
  const form = state.form
  // 다 맞는지 확인하는 변수
  let isValid = true

  // 각 항목의 규칙들 살펴보기
  if (!(state.isNicknameCheck && state.isEmailCheck)) {
    isValid = false
    return isValid
  }

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

// 닉네임 중복검사 API 요청
const nicknameCheck = function () {
  state.nicknameValidationMessage = ''
  state.isNicknameCheck = false // 일단 초기화
  axios({
    method: 'get',
    url: `${API_URL}/user/nick/${state.form.nickname}`
  })
    .then(res => {
      state.isNicknameCheck = true
      state.nicknameValidationMessage = '사용 가능한 닉네임입니다.'
    })
    .catch(err => {
      if (err.response.status === 406) {
        state.nicknameValidationMessage = '이미 사용 중인 닉네임입니다.'
      } else {
        state.nicknameValidationMessage = '닉네임 유효성 검사 중 오류가 발생했습니다.'
      }
    })
}

let emailValidationTimer = null

const sendEmail = function () {
  state.emailValidationMessage = "이메일을 전송 중입니다. 잠시만 기다려 주세요"
  state.isEmailsend = true


  axios({
    method: 'post',
    url: `${API_URL}/user/send`,
    data: {
      email: state.form.email
    }
  })
    .then((res) => {
      let duration = 120
      emailValidationTimer = setInterval(() => {
        if (duration > 0 && !state.isEmailCheck) {
          state.emailValidationMessage = `${Math.floor(duration / 60)}:${(duration % 60).toString().padStart(2, '0')}`
          duration--
        } else {
          clearInterval(emailValidationTimer)
          state.emailValidationMessage = "시간이 초과되었습니다. 다시 시도해 주세요"
          state.isEmailsend = false
        }
      }, 1000)
    })
    .catch((err) => {
      if (err.response.status === 406) {
        state.emailValidationMessage = '이미 가입된 계정입니다.'
      } else {
        state.emailValidationMessage = '인증번호 전송 중 오류가 발생했습니다.'
      }
    })
}

const validateEmail = function () {
  clearInterval(emailValidationTimer)
  axios({
    method: 'post',
    url: `${API_URL}/user/code`,
    data: {
      email: state.form.email,
      code: state.emailCode
    }
  })
    .then((res) => {
      state.isEmailCheck = true
      state.emailValidationMessage = '이메일 인증이 완료되었습니다.'
    })
    .catch((err) => {
      state.emailValidationMessage = '인증번호가 잘못되었습니다.'
    })
}

const isEmailValid = computed(() => {
  return /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/.test(state.form.email)
})

const socialSignup = function () {
  axios({
    method: 'post',
    url: `${API_URL}/user/social/join`,
    data: {
      id: queryParams.id,
      name: queryParams.nickname,
      nickName: state.form.nickname,
      email: state.form.email,
      profileImg: queryParams.profileImage,
    }
  })
    .then(res => {
      userStore.accessToken = res.data.AccessToken
      userStore.refreshToken = res.data.RefreshToken
      userStore.userId = res.data.userId
      userStore.getUserInfo()
      router.push({ name: 'mypage' })
    })
    .catch(err => {
      window.alert("회원가입에 실패했습니다.")
      console.log(err)
    })
}

onMounted(() => {
  if (queryParams.isLogin === "true") {
    userStore.accessToken = queryParams.AccessToken
    userStore.refreshToken = queryParams.RefreshToken
    userStore.userId = queryParams.userId
    userStore.getUserInfo()
    router.push({ name: 'mypage' })
  }
})
</script>

<style scoped>
.red {
  color: red;
}

.green {
  color: green;
}</style>