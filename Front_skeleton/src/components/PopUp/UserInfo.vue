<template>
  <div v-if="isModal" class="modal" @click.self="closeModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header d-flex flex-row justify-content-between">
        <p>{{ userData.nickName }} 's INFO</p>
        <button @click="openMessageModal">✉️ 쪽지보내기</button>
        <button @click="closeModal">X</button>
      </div>

      <div>
        <img class="rounded-circle" v-if="userData.profileImg" :src="userData.profileImg" alt="">
        <img class="rounded-circle" v-else src="@/assets/defaultRoom.jpg" alt="">
        <p>{{ userData.nickName }}</p>

        <div>
          <h5>Interests</h5>
          <p v-for="(interest, index) of userData.interests" :key="interest.interestId">
            {{ index + 1 }}. <span :style="{ 'color': interest.preference === 'LIKE' ? 'green' : 'red' }">{{
              interest.interestName }}</span>
          </p>
        </div>
        <button class="btn btn-primary" @click="submitFollow">{{ userData._follow ? 'Unfollow' : 'Follow' }}</button>
      </div>
    </div>
    <SendMessage v-if="isMessageModal" :is-modal="isMessageModal" :user-name="userData.nickName" :user-id="props.userId"
      @close-modal="closeMessageModal">
    </SendMessage>
  </div>
</template>
  
<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user.js'
import axios from 'axios'
import SendMessage from './SendMessage.vue'
const userStore = useUserStore()
const API_URL = userStore.API_URL
const props = defineProps({
  isModal: Boolean,
  userId: Number,
})

const isMessageModal = ref(false)
const openMessageModal = function () {
  isMessageModal.value = true
}
const userData = ref({})


const emit = defineEmits(['closeModal'])
const closeModal = function () {
  emit('closeModal')
}

const closeMessageModal = function () {
  isMessageModal.value = false
}

// 팔로우 언팔로우 중계 함수
const submitFollow = function () {
  if (userData.value._follow) {
    unfollow()
  } else {
    follow()
  }
}

// 팔로우 함수
const follow = function () {
  axios({
    method: 'post',
    url: `${API_URL}/follow`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: {
      id: userData.value.userId
    }
  })
    .then(res => {
      userData.value._follow = true
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        follow()
      }
    })
}

// 언팔로우 함수
const unfollow = function () {
  axios({
    method: 'delete',
    url: `${API_URL}/follow`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: {
      id: userData.value.userId
    }
  })
    .then(res => {
      userData.value._follow = false
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        unfollow()
      }
    })
}

// 해당 유저 정보 get 함수
const getUserInfo = function () {
  axios({
    method: 'get',
    url: `${API_URL}/follow/detail?id=${props.userId}`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      userData.value = res.data
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        getUserInfo()
      }
    })
}

onMounted(() => {
  getUserInfo()
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
  width: 300px;
}

img {
  height: 100px;
  width: 100px;
}

.modal-header {
  background-color: rgb(151, 150, 150, 0.5);
  box-shadow: 5px 5px 5px 5px "black";
  font-family: "YJ_Obang";
  font-size: 25px;
}

.modal-content {
  width: 500px;
}
</style>