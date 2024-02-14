<template>
  <div class="background-div">
    <div class="login">
      <div class="title-section">
        <p class="title">Forgot Something?</p>
      </div>
      <div class="section">
        <div class="select-section">
          <el-button @click="isId = true" :disabled="isId ? true : false">아이디 찾기</el-button>
          <el-button @click="isId = false" :disabled="isId ? false : true">비밀번호 찾기</el-button>
        </div>

        <div v-if="isId">
          <h3>아이디 찾기</h3>
          <el-form :model="state.idForm" :rules="state.idRules" :label-position="state.align">
            <el-form-item prop="email" label="이메일" :label-width="state.formLabelWidth">
              <el-input v-model="state.idForm.email" :rules="state.idRules.email"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="!isIdFormValid" @click="findId">아이디 찾기</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-else>
          <h3>비밀번호 찾기</h3>
          <el-form :model="state.passwordForm" :rules="state.passwordRules" :label-position="state.align">
            <el-form-item prop="email" label="이메일" :label-width="state.formLabelWidth">
              <el-input v-model="state.passwordForm.email" :rules="state.passwordForm.email"></el-input>
            </el-form-item>
            <el-form-item prop="id" label="아이디" :label-width="state.formLabelWidth">
              <el-input v-model=state.passwordForm.id :rules="state.passwordRules.id"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="!isPasswordFormValid" @click="sendEmail">비밀번호 찾기</el-button>
            </el-form-item>
          </el-form>
          <div v-if="state.isEmailSend">
            <el-input v-model="state.emailCode" autocomplete="off"></el-input>
            <el-button @click="validateEmail">이메일 인증</el-button>
          </div>
          <div v-if="state.isEmailCheck">
            <el-form :model="state.newPwForm" :rules="state.newPwRules" ref="newpassword" :label-position="state.align">
              <el-form-item prop="password" label="새로운 비밀번호" :label-width="state.formLabelWidth">
                <el-input v-model="state.newPwForm.password" :rules="state.newPwRules.password" autocomplete="off"
                  show-password></el-input>
              </el-form-item>
            </el-form>
            <button class="btn btn-success" @click="changePassword" :disabled="!isNewPwValid">비밀번호 변경</button>
          </div>
          <div v-if="state.emailValidationMessage">{{ state.emailValidationMessage }}</div>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { ref, reactive, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const userStore = useUserStore()
const API_URL = userStore.API_URL
const isId = ref(true)

const state = reactive({
  idForm: {
    email: '',
  },
  passwordForm: {
    email: '',
    id: '',
  },
  newPwForm: {
    password: ''
  },
  idRules: {
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
  },
  passwordRules: {
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
    id: [
      { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
      { max: 16, message: '최대 16자까지 입력 가능합니다.', trigger: 'change' },
    ],
  },
  newPwRules: {
    password: [
      { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
      { min: 9, message: '최소 9자를 입력 해야합니다.', trigger: 'change' },
      { max: 16, message: '최대 16자까지 입력 가능합니다.', trigger: 'change' },
      {
        validator: (rule, value, callback) => {
          if (!/(?=.*[a-z])(?=.*\d)(?=.*\W)/.test(value)) {
            callback(new Error('비밀번호는 영문, 숫자, 특수문자가 조합되어야합니다.'))
          } else {
            callback()
          }
        }, trigger: 'change'
      }
    ],
  },
  align: 'left',
  formLabelWidth: '120px',
  emailCode: '',
  serverCode: '',
  isEmailSend: false,
  emailValidationMessage: '',
  isEmailCheck: false,
})

const isIdFormValid = computed(() => {
  const rules = state.idRules
  const form = state.idForm
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

const isPasswordFormValid = computed(() => {
  const rules = state.passwordRules
  const form = state.passwordForm
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

const isNewPwValid = computed(() => {
  const rules = state.newPwRules
  const form = state.newPwForm
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

const findId = function () {
  axios({
    method: 'get',
    url: `${API_URL}/user/findid`,
    params: {
      email: state.idForm.email
    }
  })
    .then(res => {
      window.alert(`회원님의 아이디는 ${res.data.user_id}입니다.`)
    })
    .catch(err => {
      window.alert('가입하지 않은 이메일입니다.')
    })
}

let emailValidationTimer = null

const sendEmail = function () {
  state.emailValidationMessage = ""
  state.isEmailSend = true

  axios({
    method: 'post',
    url: `${API_URL}/user/send`,
    data: {
      email: state.passwordForm.email,
      siteId: state.passwordForm.id,
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
          state.isEmailSend = false
        }
      }, 1000)
    })
    .catch((err) => {
      state.emailValidationMessage = '인증번호 전송 중 오류가 발생했습니다.'
    })
}

const validateEmail = function () {
  clearInterval(emailValidationTimer)
  axios({
    method: 'post',
    url: `${API_URL}/user/code`,
    data: {
      email: state.passwordForm.email,
      code: state.emailCode,
      siteId: state.passwordForm.id
    }
  })
    .then((res) => {
      state.isEmailCheck = true
      state.emailValidationMessage = '이메일 인증이 완료되었습니다.'
      state.serverCode = res.data.code
    })
    .catch((err) => {
      state.emailValidationMessage = '인증번호가 잘못되었습니다.'
    })
}

const changePassword = function () {
  axios({
    method: 'post',
    url: `${API_URL}/user/findpw`,
    data: {
      user_id: state.passwordForm.id,
      email: state.passwordForm.email,
      code: state.serverCode,
      pw: state.newPwForm.password,
    }
  })
    .then(res => {
      window.alert('비밀번호가 변경되었습니다. \n로그인 페이지로 이동합니다.')
      router.push({ name: 'login' })
    })
    .catch(err => {
      window.alert('비밀번호 변경에 실패했습니다.')
    })
}
</script>
  
<style scoped>
.background-div {
  display: flex;
  justify-content: center;
  align-items: center;
}

.section {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin: 30px;
  width: 70%;
}


.login {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin: 50px;
  padding: 50px;
  width: 70%;
  height: 80%;
  border-radius: 50px;
  background-color: #F9F7C9;
  box-shadow: 5px 5px 10px rgb(197, 196, 196);
}

.title {
  font-size: 50px;
  color: black;
  font-weight: bold;
}

.social-login {
  background-color: #F7E600;
}

button {

  font-size: 15px;
  width: 300px;
  height: 50px;
  border-radius: 10px;
}

a {
  color: black;
  font-weight: bold;

  font-size: 15px;
}

/* .el-button {

  background-color: aqua;
} */
</style>