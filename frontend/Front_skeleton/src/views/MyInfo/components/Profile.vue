<template>
  <div class="d-flex flex-column">
    <h2>About Me</h2>
    <div>
      <img v-if="userInfo.profileImg" :src="'data:image/jpeg;base64,' + userInfo.profileImg" alt="Profile image">
      <img v-else src="@/assets/defaultRoom.jpg" alt="Default image">
      <p>username : {{ userInfo.nickName }}</p>
      <p>email : {{ userInfo.email }}</p>
      <p>{{ userInfo.introduction }}</p>
      <p>{{ userInfo.charName }}</p>
      <button @click="openProfileModal">Edit</button>
      <h3>MBTI</h3>
      <h5>{{ userInfo.mbti }}</h5>
    </div>
    <div>
      <h3>감정그래프</h3>
      <div v-if="isGraph">
        <PieChart :chart-data="chartData"/>
      </div>
      <button class="btn" @click="router.push({name:'feelings'})">Detail</button>
    </div>
    <div>
      <h3>관심사</h3>
      <p v-for="(interest, index) of interests" :key="interest.id">
        {{ index + 1 }}. <span :style="{ 'color' : interest.emotion ? 'green': 'red'}">{{ interest.name }}</span>
      </p>
      <p v-if="!interests.length">추출된 관심사가 아직 없습니다.</p>
      <button class="btn" @click="router.push({name: 'interests'})">Detail</button>
    </div>
    <ProfileEdit :is-modal="isProfileModal" :user-info="userInfo" @change-character="changeCharacter" @closeModal="closeProfileModal"/>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import axios from 'axios'
import PieChart from '@/components/charts/ChartPie.vue'
import ProfileEdit from '@/components/PopUp/ProfileEdit.vue'

const userStore = useUserStore()
const API_URL = userStore.API_URL
const router = useRouter()
const props = defineProps({
  userInfo: Object,
})

const emit =defineEmits(['changeCharacter'])

const isProfileModal = ref(false)

const openProfileModal = function () {
  isProfileModal.value = true
}

const closeProfileModal = function () {
  isProfileModal.value = false
}

const changeCharacter = (num) => {
  emit('changeCharacter', num)
}

const isGraph = ref(false)

// 그래프용 데이터 (서버에서 datasets[0].data 데이터 받아와야함)
const chartData = ref({
  labels: [],
  // labels: ['Happy', 'Sad', 'Angry'],
  datasets: [
    {
      label: 'Feelings',
      backgroundColor: ['#81C784', '#f5ed6e', '#64B5F6', '#d52e29', '#7c4ba4'],
      data : [],
      // data: [4, 2, 1]
    }
  ]
})

const interests = ref([
  { id: 1, name: 'test1', emotion: 1},
  { id: 2, name: 'test2', emotion: 0},
  { id: 3, name: 'test3', emotion: 0},
  { id: 4, name: 'test4', emotion: 1},
])

onMounted(() => {
  axios({
    method: 'get',
    url: `${API_URL}/user/simple`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      const emotion = res.data.emotion
      delete emotion.none
      interests.value = res.data.interests
      chartData.value.labels = Object.keys(emotion)
      chartData.value.datasets[0].data = Object.values(emotion)
      isGraph.value = true
    })
    .catch(err => {
      // console.log(err)
    })
})
</script>

<style scoped>

</style>