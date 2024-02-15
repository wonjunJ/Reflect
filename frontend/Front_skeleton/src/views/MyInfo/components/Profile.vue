<template>
  <div class="d-flex flex-column">

    <p class="title-section">ABOUT ME</p>
    <div class="sub-title-section">
      <p class="profile-title">MY INFO</p>
      <button @click="openProfileModal">Edit</button>
    </div>
    <div class="info-content">
      <img style="width: 100px" v-if="userInfo.profileImg" :src="userInfo.profileImg" alt="Profile image">
      <img v-else src="@/assets/defaultRoom.jpg" alt="Default image">
      <p>닉네임 : {{ userInfo.nickName }}</p>
      <p>이메일 : {{ userInfo.email }}</p>
      <p>{{ userInfo.myIntro }}</p>
      <p>{{ userInfo.gender }}</p>
      <p>{{ userInfo.birthday }}</p>
    </div>

    <div class="sub-title-section">
      <p class="profile-title">MBTI</p>
      <p>{{ userInfo.mbti }}</p>
    </div>
    <div class="info-content">
      <h5>{{ userInfo.mbti }}</h5>
    </div>

    <div class="sub-title-section">
      <p class="profile-title">최근 감정 </p>
      <button @click="router.push({ name: 'feelings' })">Detail</button>
    </div>
    <div class="info-content">
      <div v-if="isGraph">
        <PieChart :chart-data="chartData" />
      </div>
      <div v-else>
        작성된 일기에서 추출된 감정이 아직 없습니다.
      </div>
    </div>

    <div class="sub-title-section">
      <p class="profile-title">관심사 </p>
      <button @click="router.push({ name: 'interests' })">Detail</button>
    </div>
    <div class="info-content">
      <p v-for="(interest, index) of interests" :key="interest.interestId">
        {{ index + 1 }}. <span :style="{ 'color': interest.preference === 'LIKE' ? 'green' : 'red' }">{{
          interest.interestName }}</span>
      </p>
      <p v-if="!interests.length">작성된 일기에서 추출된 관심사가 아직 없습니다.</p>
    </div>
  </div>
  <ProfileEdit :is-modal="isProfileModal" :user-info="userInfo" @change-character="changeCharacter"
    @reload-profile="reloadProfile" @closeModal="closeProfileModal" />
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

const emit = defineEmits(['changeCharacter', 'reloadProfile'])

const isProfileModal = ref(false)

const openProfileModal = function () {
  isProfileModal.value = true
}

const closeProfileModal = function () {
  isProfileModal.value = false
}

const changeCharacter = () => {
  emit('changeCharacter')
}

const reloadProfile = () => {
  emit('reloadProfile')
}

// 그래프 랜더링 여부 - onMounted로 데이터 불러오기 전에 먼저 랜더링 하는거 방지
const isGraph = ref(false)

// 그래프용 데이터
const chartData = ref({
  labels: ['Happy', 'Joy', 'Sad', 'Anger', 'Disgust'],
  datasets: [
    {
      label: 'Feelings',
      backgroundColor: ['#81C784', '#f5ed6e', '#64B5F6', '#d52e29', '#7c4ba4'],
      data: [],
      // data: [4, 2, 1]
    }
  ]
})

// 관심사 목록
const interests = ref([])

// 배열 합 구하는 함수
const sumArray = function (array) {
  let sum = 0
  array.forEach(element => {
    if (element !== null) {
      sum += element
    }
  })
  return sum
}

// 프로필 정보 요청 함수
const getProfileInfo = function () {
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
      chartData.value.datasets[0].data = Object.values(emotion)
      if (sumArray(Object.values(emotion))) {
        isGraph.value = true  // 추출된 데이터가 있을 때만 그래프 출력
      }
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        getProfileInfo()
      }
    })
}

onMounted(() => {
  getProfileInfo()
})
</script>

<style scoped>
.title-section {
  font-size: 30px;
  font-family: "YJ_Obang";
}

.sub-title-section {
  font-size: 20px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;

}

p {
  margin: 10px;
}

button {
  font-size: 20px;
  width: 100px;
  border-radius: 10px;
  margin: 10px;
  background-color: #F9F7C9;
}
</style>