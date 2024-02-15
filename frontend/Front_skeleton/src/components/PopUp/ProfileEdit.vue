<template>
  <div v-if="isModal" class="modal" @click.self="closeModal">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content" @click.stop style="max-height: 700px; overflow-y: auto;">
        <div class="d-flex justify-content-between">
          <h3>프로필 수정</h3>
          <button class="btn btn-danger" @click="closeModal">X</button>
        </div>
        <div>
          <!-- 프로필 사진 수정 -->
          <div class="d-flex justify-content-between">
            <h5>프로필 사진</h5>
            <input type="file" @change="handleImageUpload">
          </div>
          <img v-if="userInfo.profileImg" :src="userInfo.profileImg" alt="Profile image" style="width : 300px;">
          <img v-else src="@/assets/defaultRoom.jpg" alt="Default image" style="width : 300px;">
          <!-- 캐릭터 수정 -->
          <div class="d-flex justify-content-between">
            <h5>캐릭터 설정</h5>
            <button class="btn btn-primary" @click="openCharacterModal">수정</button>
          </div>
          <el-form :model="state.form" :rules="state.rules" ref="editProfile" label-position="left">
            <el-form-item prop="email" label="이메일" :label-width="state.labelWidth">
              <el-input v-model="state.form.email" :rules="state.rules.email" autocomplete="off" disabled></el-input>
            </el-form-item>
            <el-form-item prop="intro" label="자기소개" :label-width="state.labelWidth">
              <el-input v-model="state.form.myIntro" :rules="state.rules.intro" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="성별">
              <el-select
                v-model="state.form.gender"
                placeholder="성별"
                clearable
              >
                <el-option label="남성" value="MALE" />
                <el-option label="여성" value="FEMALE" />
                <el-option label="기타" value="" />
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
            <!-- 다른 항목 더 넣기 -->
          </el-form>
          <button class="btn btn-primary" :disabled="!isFormValid" @click="editProfile">저장</button>
        </div>
        <div>
          <button class="btn btn-primary" @click="openPasswordModal">비밀번호 변경</button>
          <button class="btn btn-danger" @click="deleteUser">탈퇴하기</button>
        </div>
      </div>
    </div>
    <CharacterSetting :is-modal="isCharacterModal" 
      :character-id="userInfo.characterId" 
      @change-character="changeCharacter"
      @close-modal="closeCharacterModal"/>
    <PasswordChange :is-modal="isPasswordModal" @close-modal="closePasswordModal"/>
  </div>
</template>

<script setup>
import { reactive, ref, computed, watchEffect } from 'vue'
import { useUserStore } from '@/stores/user'
import axios from 'axios'
import CharacterSetting from './CharacterSetting.vue'
import PasswordChange from './PasswordChange.vue'

const userStore = useUserStore()
const API_URL = userStore.API_URL
const props = defineProps({
  isModal: Boolean,
  userInfo: Object,
})

const emit = defineEmits(['closeModal', 'changeCharacter', 'reloadProfile'])
const closeModal = () => {
    emit('closeModal')
}

const state = reactive({
  form: {},
  rules: {
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
    intro: [
      { max: 50, message: '최대 50자까지 입력 가능합니다.', trigger: 'change' },
    ]
  },
  labelWidth: '80px'
})

watchEffect(() => {
  if (props.isModal) {
    state.form = {... props.userInfo}
  }
})

const handleImageUpload = (event) => {
  const file = event.target.files[0]
  const reader = new FileReader()
  reader.onload = () => {
    state.form.profileImg = reader.result
  }
  reader.readAsDataURL(file)
}

// 유효성 검사
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

const editProfile = function () {
  axios({
    method: 'put',
    url: `${API_URL}/user/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: state.form
  })
    .then(res => {
      window.alert('변경 사항이 저장 되었습니다.')
      emit('reloadProfile')
    })
    .catch(err => {
      console.log(err)
    })
}

// 사용자 탈퇴
const deleteUser = function () {
  if (window.confirm('정말로 탈퇴하시겠습니까?')) {
    if (window.confirm('탈퇴하시게 될 경우 기록된 모든 데이터들이 삭제되며, 다시 되돌이킬 수 없습니다. 정말로 탈퇴하시겠습니까?')) {
      // 탈퇴 논리
      window.alert('탈퇴가 완료되었습니다.')
    }
  }
}

// 모달 관련
const isCharacterModal = ref(false)
const openCharacterModal = function () {
    isCharacterModal.value = true
}
const closeCharacterModal = function () {
    isCharacterModal.value = false
}

const isPasswordModal = ref(false)
const openPasswordModal = function () {
    isPasswordModal.value = true
}
const closePasswordModal = function () {
    isPasswordModal.value = false
}

const changeCharacter = () => {
  isCharacterModal.value = false
  emit('changeCharacter')
}
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-content {
  width : 350px;
}
</style>