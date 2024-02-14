<template>
  <div v-if="isModal" class="modal" @click.self="closeModal">
    <div class="modal-content" @click.stop>
      <div class="d-flex justify-content-between">
        <h3>캐릭터 설정</h3>
        <button class="btn btn-danger" @click="closeModal">X</button>
      </div>
      <div v-for="option in options" :key="option.id">
        <img :src="getPetImage(option.id)" alt="img" height="50px" width="50px">
        <label :for="option.id">{{ option.name }}</label>
        <input v-if="option.ishave" type="checkbox" :id="option.id" :checked="option.id === selectedCharacter" @click="selectCharacter(option.id)">
        <button v-else class="btn btn-sm btn-success">구매하기</button>
      </div>
      <button class="btn btn-primary" @click="changeCharacter">저장</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { useUserStore } from "@/stores/user"
import axios from "axios"
import corgi from '@/img/corgi.gif'
import slime from '@/img/slime.gif'
import rabbit from '@/img/rabbit.gif'

const userStore = useUserStore()
const API_URL = userStore.API_URL

const props = defineProps({
  isModal: Boolean,
  characterId: Number,
})

const emit = defineEmits(['closeModal', 'changeCharacter'])
const closeModal = function () {
  emit('closeModal')
}

// 캐릭터 선택지
const options = ref([
  { id: 1, name: '슬라임', ishave: false },
  { id: 2, name: '코기', ishave: false },
  { id: 3, name: '토끼', ishave: false },
])

const myPets = ref([])

const selectedCharacter = ref(userStore.userInfo.charName ? options.value.find(item => item.name === userStore.userInfo.charName).id : 1)  // 선택된 캐릭터 아이디
// 캐릭터 선택 함수
const selectCharacter = function (num) {
  selectedCharacter.value = num
}

// 캐릭터 변경 emit
const changeCharacter = function () {
  axios({
    method: 'put',
    url: `${API_URL}/pet/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: {
      petId : selectedCharacter.value
    }
  })
    .then(res => {
      userStore.getUserInfo()
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        changeCharacter()
      }
    })
  emit('changeCharacter')
}

const getPetImage = function (id) {
  if (id === 1) {
    return slime
  } else if (id === 2) {
    return corgi
  } else if (id === 3) {
    return rabbit
  }
}

const getPetInfo = function () {
  axios({
    method: 'get',
    url: `${API_URL}/pet/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      myPets.value = res.data
      options.value.forEach(option => {
        const petInfo = myPets.value.find(pet => pet.petId === option.id)
        if (petInfo) {
          option.ishave = true
        }
      })
    })
    .catch(err => {
      if (err.response.status === 401) {
        userStore.refresh()
        getPetInfo()
      }
    })
}

onMounted(() => {
  getPetInfo()
})
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
  width : 500px;
}
</style>