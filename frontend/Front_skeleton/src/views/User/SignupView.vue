<template>
  <div>
    <h1>SignUp Page</h1>
    <el-form :model="state.form" :rules="state.rules" ref="signupForm" :label-position="state.form.align">
      <el-form-item prop="id" label="아이디" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.id" :rules="state.rules.id" autocomplete="off"></el-input>
        <el-button @click="idCheck">아이디 중복 확인</el-button>
        <div v-if="state.idValidationMessage">{{ state.idValidationMessage }}</div>
      </el-form-item>
      <el-form-item prop="password" label="비밀번호" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.password" :rules="state.rules.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item prop="password2" label="비밀번호 확인" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.password2" :rules="state.rules.password2" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item prop="nickname" label="닉네임" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.nickname" :rules="state.rules.nickname" autocomplete="off"></el-input>
        <el-button @click="nicknameCheck">닉네임 중복 확인</el-button>
        <div v-if="state.nicknameValidationMessage">{{ state.nicknameValidationMessage }}</div>
      </el-form-item>
      <el-form-item prop="email" label="이메일" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.email" :rules="state.rules.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="이름" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.name" :rules="state.rules.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="age" label="나이" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.age" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="Activity zone">
        <el-select
          v-model="state.form.gender"
          placeholder="성별"
          clearable
        >
          <el-option label="남성" value="male" />
          <el-option label="여성" value="female" />
          <el-option label="기타" value="etc" />
        </el-select>
      </el-form-item>
      <el-form-item label="생년월일">
        <el-date-picker
          v-model="state.form.birthday"
          type="date"
          placeholder="생년월일을 선택하세요"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :disabled="!isFormValid">signup</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'

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
      { validator: (rule, value, callback) => {
        if (!/(?=.*[a-z])(?=.*\d)(?=.*\W)/.test(value)) {
          callback(new Error('비밀번호는 영문, 숫자, 특수문자가 조합되어야합니다.'))
        } else {
          callback()
        }
      }, trigger: 'change' }
    ],
    password2: [
      { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
      { min: 9, message: '최소 9자를 입력 해야합니다.', trigger: 'change' },
      { max: 16, message: '최대 16자까지 입력 가능합니다.', trigger: 'change' },
      { validator: (rule, value, callback) => {
        if (!/(?=.*[a-z])(?=.*\d)(?=.*\W)/.test(value)) {
          callback(new Error('비밀번호는 영문, 숫자, 특수문자가 조합되어야합니다.'))
        } else {
          callback()
        }
      }, trigger: 'change' },
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
})

// 필수항목이 모두 바르게 입력되었는지 검증
const isFormValid = computed(() => {
  const rules = state.rules
  const form = state.form
  // 다 맞는지 확인하는 변수
  let isValid = true

  // 각 항목의 규칙들 살펴보기
  if (!(state.isIdCheck && state.isNicknameCheck)) {
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
  const idValue = state.form.id // ID 값 가지고 오기

  // API 요청이 들어가야하는 부분
  state.idValidationMessage = 'click'
  state.isIdCheck = true
}

// 닉네임 중복검사 API 요청
const nicknameCheck = function () {
  state.nicknameValidationMessage = ''
  state.isNicknameCheck = false // 일단 초기화
  const nicknameValue = state.form.nickname // 닉네임 값 가져오기

  // API 요청이 들어가야하는 부분
  state.nicknameValidationMessage = 'click'
  state.isNicknameCheck = true
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

</style>