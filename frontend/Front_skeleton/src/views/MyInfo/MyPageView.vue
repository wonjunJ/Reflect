<template>
  <h1>This is MyPage</h1>
  <div class="d-flex">
    <aside class="aside-container">
      <Profile :user-info="userInfo" @change-character="changeCharacter" @reload-profile="getUserInfo"/>
    </aside>
    <body class="d-flex flex-column body">
      <Calendar/>
      <QandA/>
      <div class="d-flex">
        <div>
          <h4>Following</h4>
          <FollowList :follows="followings" :paging-info="followPagingInfo" @change-page="changeFollowPage"/>
        </div>
        <CommunityList :communitys="communitys" :paging-info="roomPagingInfo" @change-page="changeCommunityPage"/>
      </div>
    </body>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useUserStore } from '@/stores/user.js'
import axios from 'axios'
import Profile from './components/Profile.vue'
import Calendar from './components/Calendar.vue'
import QandA from './components/QandA.vue'
import FollowList from './components/FollowList.vue'
import CommunityList from './components/CommunityList.vue'

const userStore = useUserStore()
const API_URL = userStore.API_URL

// 사용자 정보 더미데이터
const userInfo = ref({})

const emit = defineEmits(['changeCharacter',])

// 팔로잉 목록
const followings = ref([])
const followPageNum = ref(1)
const followPagingInfo = ref({})
const changeFollowPage = function (num) {
  followPageNum.value = num
  getFollowInfo()
}

// 가입 커뮤니티 목록
const communitys = ref([])
const roomPageNum = ref(1)
const roomPagingInfo = ref({})
const changeCommunityPage = function (num) {
  roomPageNum.value = num
  getCommunitysInfo()
}

const changeCharacter = (num) => {
  userInfo.value.characterId = num
  emit('changeCharacter', num)
}

const getUserInfo = function () {
  axios({
    method: 'get',
    url: `${API_URL}/user/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      userInfo.value = res.data
    })
    .catch(err => {
      console.log(err)
    })
}

const getCommunitysInfo = function () {
  axios({
    method: 'get',
    url: `${API_URL}/room/mine?page=${roomPageNum.value}`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      communitys.value = res.data.data
      roomPagingInfo.value = res.data.pagingInfo
    })
}

const getFollowInfo = function () {
  axios({
    method: 'get',
    url: `${API_URL}/follow?page=${followPageNum.value}`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      followings.value = res.data.data
      followPagingInfo.value = res.data.pagingInfo
    })
}

onMounted(() => {
  getUserInfo()
  getCommunitysInfo()
  getFollowInfo()
})
</script>

<style scoped>
.aside-container {
  background-color: #AAD9BB;
  padding: 20px;
}

.body {
  background-color: #F9F7C9;
} 
</style>