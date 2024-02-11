<template>
    <div v-if="isModal" class="modal" @click.self="closeModal">
      <div class="modal-content" @click.stop>
        <div class="d-flex justify-content-between">
          <h3>{{ userData.nickName }}</h3>
          <button class="btn btn-danger" @click="closeModal">X</button>
        </div>
        <div>
          <img class="rounded-circle" v-if="userData.profileImg" :src="userData.profileImg" alt="">
          <img class="rounded-circle" v-else src="@/assets/defaultRoom.jpg" alt="">
          <p>{{ userData.nickName }}</p>
          <div>
            <h5>Interests</h5>
            <p v-for="(interest, index) of userData.interests" :key="interest.interestId">
              {{ index + 1 }}. <span :style="{ 'color' : interest.preference === 'LIKE' ? 'green': 'red'}">{{ interest.interestName }}</span>
            </p>
          </div>
          <button class="btn btn-primary" @click="submitFollow">{{ userData._follow ? 'Unfollow' : 'Follow' }}</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useUserStore } from '@/stores/user.js'
  import axios from 'axios'

  const userStore = useUserStore()
  const API_URL = userStore.API_URL
  
  const props = defineProps({
    isModal: Boolean,
    userId: Number,
  })

  const userData = ref({})
  
  const emit = defineEmits(['closeModal'])
  const closeModal = function () {
    emit('closeModal')
  }

  const submitFollow = function () {
    if (userData.value._follow) {
      unfollow()
    } else {
      follow()
    }
  }
  
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
        console.log(res.data)
        userData.value._follow = true
      })
      .catch(err => {
        console.log(err)
      })
  }

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
        console.log(res.data)
        userData.value._follow = false
      })
      .catch(err => {
        console.log(err)
      })
  }

  onMounted(() => {
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
      })
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
    width : 300px;
  }
  
  img {
    height: 100px;
    width: 100px;
  }
  </style>