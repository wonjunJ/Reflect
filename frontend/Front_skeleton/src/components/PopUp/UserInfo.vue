<template>
    <div v-if="isModal" class="modal" @click.self="closeModal">
      <div class="modal-content" @click.stop>
        <div class="d-flex justify-content-between">
          <h3>{{ username }}</h3>
          <button class="btn btn-danger" @click="closeModal">X</button>
        </div>
        <div>
          <img class="rounded-circle" v-if="userdata.profileimg" src="" alt="">
          <img class="rounded-circle" v-else src="@/assets/defaultRoom.jpg" alt="">
          <p>{{ username }}</p>
          <div>
            <h5>Interests</h5>
            <p v-for="(interest, index) of userdata.interests" :key="interest.id">
              {{ index + 1 }}. {{ interest.name }}
            </p>
          </div>
          <button class="btn btn-primary" @click="follow">{{ userdata.isFollow ? 'Unfollow' : 'Follow' }}</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  
  const props = defineProps({
    isModal: Boolean,
    username: String,
  })
  
  const emit = defineEmits(['closeModal'])
  const closeModal = function () {
    emit('closeModal')
  }
  
  // onMounted하면서 불러올 더미데이터
  const userdata = ref({
    username: 'test',
    profileimg: null,
    mbti: 'ISTJ',
    interests: [
      { id: 1, weight: 20, emotion: 1, name: '1', },
      { id: 2, weight: 17, emotion: 0, name: '2', },
      { id: 3, weight: 15, emotion: 0, name: '3', },
      { id: 4, weight: 13, emotion: 1, name: '4', },
      { id: 5, weight: 11, emotion: 0, name: '5', }, 
    ],
    isFollow: true,
  })
  
  const follow = function () {
      userdata.value.isFollow = !userdata.value.isFollow
    // axios 요청
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
    width : 300px;
  }
  
  img {
    height: 100px;
    width: 100px;
  }
  </style>