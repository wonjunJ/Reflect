<template>
  <div v-if="isModal" class="modal" @click.self="closeModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header d-flex flex-row justify-content-between">
        <p>캐릭터 설정</p>
        <button @click="closeModal">X</button>
      </div>

      <div class="modal-body" style="max-height: 700px; overflow-y: auto;">
        <div class="character-select-section d-flex flex-row ">
          <div class="character" v-for="option in options" :key="option.id">
            <label :for="option.id">{{ option.petName }}</label>
            <!-- <input v-if="option.ishave" type="checkbox" :id="option.id" :checked="option.id === selectedCharacter"
              @click="selectCharacter(option.id)"> -->
            <input v-if="option.ishave" type="checkbox" :id="option.id" :checked="option.id === selectedCharacter"
              @click="selectCharacter(option.id)">
            <button v-else class="btn btn-sm btn-success" 
              @click="buyPet(option.id)" 
              :disabled="(option.price > userStore.userInfo.point)">구매</button>
            <img :src="getPetImage(option.id)" alt="img" height="50px" width="50px">
          </div>
        </div>

      <button class="btn btn-primary" @click="changeCharacter">저장</button>
    </div>
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
  characterImg: String,

})

const emit = defineEmits(['closeModal', 'changeCharacter'])
const closeModal = function () {
  emit('closeModal')
}


const options = ref([
  { id: 1, name: '슬라임', ishave: false, img: "src/img/slime.gif" },
  { id: 2, name: '코기', ishave: false, img: 'src/img/corgi.gif' },
  { id: 3, name: '토끼', ishave: false, img: 'src/img/rabbit.gif' },
])


// 캐릭터 선택지

const myPets = ref([])

const selectedCharacter = ref(userStore.userInfo.charId)  // 선택된 캐릭터 아이디
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
      petId: selectedCharacter.value
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

const buyPet = function (id) {
  console.log(id)
  axios({
    method: 'post',
    url: `${API_URL}/pet/buy`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: {
      petId: id
    }
  })
    .then(res => {
      options.value.find(item => item.id === id).ishave = true
      userStore.getUserInfo()
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        buyPet(id)
      }
    })
}

const getPetInfo = function () {
  axios({
    method: 'get',
    url: `${API_URL}/pet/total`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      options.value = res.data
    })
    .catch(res => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        getPetInfo()
      }
    })
}

const getMyPetInfo = function () {
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
        getMyPetInfo()
      }
    })
}

onMounted(() => {
  getPetInfo()
  getMyPetInfo()
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
  width: 500px;
}

.modal-header {
  background-color: rgb(151, 150, 150, 0.5);
  box-shadow: 5px 5px 5px 5px "black";
  font-family: "YJ_Obang";
  font-size: 25px;
}

.modal-body {
  padding: 30px;
}


.profile-edit-button {
  position: absolute;
  bottom: 0;
  right: 0;
  /* transform: translate(50%, 50%); */
}

.profile-img-section {
  position: relative;
  width: 100%;
}

.character {
  padding: 20px;
  border: 1px black;
}
</style>