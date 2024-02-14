<template>
  <div v-if="isModal" class="modal" @click.self="closeModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header d-flex justify-content-between">
        <h3>비밀번호 변경</h3>
        <button @click="closeModal">X</button>
      </div>
      <div class="modal-body">
        <el-form :model="state.form" :rules="state.rules" ref="changePasswordForm" :label-position="state.formAlign">
          <el-form-item prop="originalPassword" label="현재 비밀번호">
            <el-input v-model="state.form.originalPassword" :rules="state.rules.originalPassword" autocomplete="off"
              show-password></el-input>
          </el-form-item>
          <el-form-item prop="password" label="새로운 비밀번호">
            <el-input v-model="state.form.password" :rules="state.rules.password" autocomplete="off"
              show-password></el-input>
          </el-form-item>
          <el-form-item prop="password2" label="비밀번호 확인">
            <el-input v-model="state.form.password2" :rules="state.rules.password2" autocomplete="off"
              show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="!isFormValid" @click="changePassword">비밀번호 변경</el-button>
          </el-form-item>
        </el-form>
      </div>

    </div>
  </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import axios from 'axios'

const userStore = useUserStore()
const API_URL = userStore.API_URL
const props = defineProps({
  isModal: Boolean
})

const emit = defineEmits(['closeModal'])
const closeModal = function () {
  emit('closeModal')
}

const state = reactive({
  form: {
    originalPassword: '',
    password: '',
    password2: '',
  },
  rules: {
    originalPassword: [
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
  },
  formLabelWidth: '120px',
  formAlign: 'left',
})

// 유효성 검사
const isFormValid = computed(() => {
  const rules = state.rules
  const form = state.form
  // 다 맞는지 확인하는 변수
  let isValid = true

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

// 비밀번호 변경 함수
const changePassword = function () {
  axios({
    method: 'put',
    url: `${API_URL}/user/changepw`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: {
      current_pw: state.form.originalPassword,
      new_pw: state.form.password,
    }
  })
    .then(res => {
      window.alert('비밀번호가 변경되었습니다.')
      state.form.originalPassword = ''
      state.form.password = ''
      state.form.password2 = ''
      emit('closeModal')
    })
    .catch(err => {
      if (err.response.status === 400) {
        window.alert('비밀번호가 잘못되었습니다.')
      } else if (err.response.status === 401) {
        userStore.refresh()
        changePassword()
      } else {
        window.alert('비밀번호 변경에 실패하였습니다.')
      }
    })
}
</script>

<style scoped>
.modal-content {
  width: 500px;
}

.modal-header {
  background-color: rgb(151, 150, 150, 0.5);
  box-shadow: 5px 5px 5px 5px "black";
  font-family: "YJ_Obang";
  font-size: 25px;
}
</style>