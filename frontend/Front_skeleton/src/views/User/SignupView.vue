<template>
  <div class="background-div">
    <div class="signup">
      <div class="title-section">
        <p class="title">SIGN UP</p>
      </div>
      <div class="section">
        <el-form :model="state.form" :rules="state.rules" ref="signupForm" :label-position="state.form.align">
          <el-form-item prop="id" label="아이디" :label-width="state.formLabelWidth">
            <el-input v-model="state.form.id" :rules="state.rules.id" autocomplete="off"></el-input>
            <el-button @click="idCheck" :disabled="state.isIdCheck">아이디 중복 확인</el-button>
            <div v-if="state.idValidationMessage" :class="state.isIdCheck ? 'green' : 'red'">
              {{ state.idValidationMessage }}
            </div>
          </el-form-item>
          <el-form-item prop="password" label="비밀번호" :label-width="state.formLabelWidth">
            <el-input v-model="state.form.password" :rules="state.rules.password" autocomplete="off"
              show-password></el-input>
          </el-form-item>
          <el-form-item prop="password2" label="비밀번호 확인" :label-width="state.formLabelWidth">
            <el-input v-model="state.form.password2" :rules="state.rules.password2" autocomplete="off"
              show-password></el-input>
          </el-form-item>
          <el-form-item prop="nickname" label="닉네임" :label-width="state.formLabelWidth">
            <el-input v-model="state.form.nickname" :rules="state.rules.nickname" autocomplete="off"></el-input>
            <el-button @click="nicknameCheck" :disabled="state.isNicknameCheck">닉네임 중복 확인</el-button>
            <div v-if="state.nicknameValidationMessage" :class="state.isNicknameCheck ? 'green' : 'red'">
              {{ state.nicknameValidationMessage }}
            </div>
          </el-form-item>
          <el-form-item prop="email" label="이메일" :label-width="state.formLabelWidth">
            <el-input v-model="state.form.email" :rules="state.rules.email" autocomplete="off"></el-input>
            <el-button @click="sendEmail" :disabled="!isEmailValid">이메일 인증 요청</el-button>
            <el-form-item v-if="state.isEmailsend" prop="emailCode">
              <el-input v-model="state.emailCode" autocomplete="off"></el-input>
              <el-button @click="validateEmail" :disabled="state.isEmailCheck">이메일 인증</el-button>
            </el-form-item>
            <div v-if="state.emailValidationMessage" :class="state.isEmailCheck ? 'green' : 'red'">
              {{ state.emailValidationMessage }}
            </div>
          </el-form-item>
          <el-form-item prop="name" label="이름" :label-width="state.formLabelWidth">
            <el-input v-model="state.form.name" :rules="state.rules.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="age" label="나이" :label-width="state.formLabelWidth">
            <el-input v-model="state.form.age" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="성별">
            <el-select v-model="state.form.gender" placeholder="성별" clearable>
              <el-option label="남성" value="MALE" />
              <el-option label="여성" value="FEMALE" />
              <el-option label="기타" value="" />
            </el-select>
          </el-form-item>
          <el-form-item label="생년월일">
            <el-date-picker v-model="state.form.birthday" type="date" placeholder="생년월일을 선택하세요" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="!isFormValid" @click="signup">signup</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import axios from 'axios'
import { useUserStore } from '@/stores/user.js'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()
const API_URL = userStore.API_URL

// form 관련 설정
// 아이디, 중복확인, 비밀번호, 비밀번호확인, 닉네임, 닉네임 중복확인, 이메일, 이름
// 선택사항 나이, 성별, 생년월일
const state = reactive({
  // 입력 데이터
  form: {
    id: '',
    password: '',
    password2: '',
    nickname: '',
    email: '',
    name: '',
    age: '',
    gender: '',
    birthday: '',
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
    password2: [
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
      },
      {
        validator: (rule, value, callback) => {
          if (value !== state.form.password) {
            callback(new Error('입력한 비밀번호와 일치하지 않습니다.'))
          } else {
            callback()
          }
        }, trigger: 'change'
      }
    ],
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
    age: [
      {
        validator: (rule, value, callback) => {
          if (isNaN(value)) {
            callback(new Error('숫자를 입력해주세요'))
          } else {
            callback()
          }
        }, trigger: 'change'
      }
    ]
  },
  formLabelWidth: '120px',    // 라벨 너비
  isIdCheck: false,           // 아이디 검사 여부
  idValidationMessage: '',    // 아이디 중복검사 메세지
  isNicknameCheck: false,         // 닉네임 중복검사 여부
  nicknameValidationMessage: '',  // 닉네임 중복검사 메세지
  isEmailsend: false,            // 이메일 인증 요청을 한 경우
  emailCode: '',
  emailValidationMessage: '',     // 이메일 인증 요청 메세지
  isEmailCheck: false,
})

// 필수항목이 모두 바르게 입력되었는지 검증
const isFormValid = computed(() => {
  const rules = state.rules
  const form = state.form
  // 다 맞는지 확인하는 변수
  let isValid = true

  // 각 항목의 규칙들 살펴보기
  if (!(state.isIdCheck && state.isNicknameCheck && state.isEmailCheck)) {
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

// 아이디 중복검사 API 요청
const idCheck = function () {
  state.idValidationMessage = ''
  state.isIdCheck = false // 일단 초기화
  axios({
    method: 'get',
    url: `${API_URL}/user/id/${state.form.id}`
  })
    .then(res => {
      state.isIdCheck = true
      state.idValidationMessage = '사용 가능한 아이디입니다.'
    })
    .catch(err => {
      if (err.response.status === 406) {
        state.idValidationMessage = '이미 사용 중인 아이디입니다.'
      } else {
        state.idValidationMessage = '아이디 유효성 검사 중 오류가 발생했습니다.'
      }
    })
}

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

const signup = function () {
  axios({
    method: 'post',
    url: `${API_URL}/user/join`,
    data: {
      siteId: state.form.id,
      pw: state.form.password,
      nickName: state.form.nickname,
      email: state.form.email,
      name: state.form.name,
      // 나이, 성별, 생년월일
    }
  })
    .then(res => {
      window.alert('회원가입이 완료되었습니다. \n로그인 페이지로 이동합니다.')
      router.push({ name: 'login' })
    })
    .catch(err => {
      window.alert('회원가입 중 오류가 발생했습니다.')
    })
}

// 중복확인 이후 id 값이 변한 경우 검사여부 다시 false로 만들기
watch(() => state.form.id, (newId) => {
  state.idValidationMessage = ''
  state.isIdCheck = false
})

// 중복확인 이후 닉네임 값이 변한 경우 검사여부 다시 false로 만들기
watch(() => state.form.nickname, (newId) => {
  state.nicknameValidationMessage = ''
  state.isNicknameCheck = false
})
</script>

<style scoped>
.background-div {
  display: flex;
  justify-content: center;
  align-items: center;
  color: #80BCBD;
}

.section {
  margin: 50px;
  width: 70%;
}

.signup {
  margin: 50px;
  padding: 50px;
  width: 70%;
  height: 80%;
  border-radius: 50px;
  background-color: #F9F7C9;
}

.title {
  font-size: 50px;
  color: black;
  font-weight: bold;
}

.el-form-item {
  display: flex;
  align-items: center;
  /* 세로 중앙 정렬 */
  width: 70%;
  margin-bottom: 20px;
  /* 각 항목 사이의 간격 조절 */
}

.el-form-item>.el-input,
.el-form-item>.el-button {
  margin-right: 10px;
  /* 입력 요소 간의 간격 조절 */
}

.red {
  color: red;
}

.green {
  color: green;
}
</style>