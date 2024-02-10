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
          <FollowList :follows="followings" :paging-info="followPagingInfo"/>
        </div>
        <CommunityList :communitys="communitys" :paging-info="roomPagingInfo"/>
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
const followings = ref([
  { id: 4, profileimg: null, username: 'test04', alike: '73%', isonline: true},
  { id: 5, profileimg: null, username: 'test05', alike: '52%', isonline: true},
  { id: 6, profileimg: null, username: 'test06', alike: '46%', isonline: false}
])
const followPageNum = ref(1)
const followPagingInfo = ref({})

// 가입 커뮤니티 목록
const communitys = ref([
  { id: 1, title: 1, people: 6, hashtag: ['soccer', 'movie']},
  { id: 2, title: 2, people: 4, hashtag: ['baseball']},
])
const roomPageNum = ref(1)
const roomPagingInfo = ref({})

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