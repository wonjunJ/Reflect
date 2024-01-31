<template>
  <h1>Forgot Something?</h1>
  <el-button @click="isId=true" :disabled="isId ? true :false">아이디 찾기</el-button>
  <el-button @click="isId=false" :disabled="isId ? false :true">비밀번호 찾기</el-button>
  <div v-if="isId">
    <h3>아이디 찾기</h3>
    <el-form :model="state.idForm" :rules="state.idRules" :label-position="state.align">
      <el-form-item prop="email" label="이메일" :label-width="formLabelWidth">
        <el-input v-model="state.idForm.email" :rules="state.idRules.email"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :disabled="!isIdFormValid">아이디 찾기</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div v-else>
    <h3>비밀번호 찾기</h3>
    <el-form :model="state.passwordForm" :rules="state.passwordRules" :label-position="state.align">
      <el-form-item prop="email" label="이메일" :label-width="formLabelWidth">
        <el-input v-model="state.passwordForm.email" :rules="state.passwordForm.email"></el-input>
      </el-form-item>
      <el-form-item prop="id" label="아이디" :label-width="formLabelWidth">
        <el-input v-model=state.passwordForm.id :rules="state.passwordRules.id"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :disabled="!isPasswordFormValid">비밀번호 찾기</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
  
<script>
import { ref, reactive, computed } from 'vue'
  
const isId = ref(true)
  
const state = reactive({
  idForm: {
    email: '',
    },
    passwordForm: {
      email: '',
      id: '',
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
    align: 'left',
    formLabelWidth: '80px'
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
  
export default {
  setup() {
    return { isId, state, isIdFormValid, isPasswordFormValid }
  }
}
</script>
  
<style>
  
</style>