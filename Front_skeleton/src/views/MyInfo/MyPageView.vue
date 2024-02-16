<template>
  <div class="d-flex">
    <aside class="aside-section">
      <div class="content-section">
        <Profile :user-info="userInfo" @change-character="changeCharacter" @reload-profile="getUserInfo" />
      </div>

    </aside>

    <body class="main-section d-flex flex-column body">
      <div class="content-section">
        <Calendar />
      </div>
      <div class="content-section">
        <QandA />
      </div>
      <div class="content-section">
        <div class="community-section">
          <div class="subtitle-section ">
            <p>커뮤니티</p>
          </div>
          <div class="contents-section">
            <div class="following-table-section">
              <FollowList :follows="followings" :paging-info="followPagingInfo" @change-page="changeFollowPage" />
            </div>
            <div class="community-table-section">
              <CommunityList class="mx-3 text-center" :communitys="communitys" :paging-info="roomPagingInfo"
                @change-page="changeCommunityPage" />
            </div>
          </div>
        </div>
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

// 사용자 정보
const userInfo = ref({})

const emit = defineEmits(['changeCharacter',])

const followings = ref([])    // 팔로잉 목록
const followPageNum = ref(1)  // 팔로잉 페이지네이션 번호
const followPagingInfo = ref({})  // 팔로잉 페이지네이션 정보
// 페이지 변화 시 새로운 목록 요청
const changeFollowPage = function (num) {
  followPageNum.value = num
  getFollowInfo()
}

const communitys = ref([])  // 커뮤니티 목록
const roomPageNum = ref(1)  // 커뮤니티 페이지네이션 번호
const roomPagingInfo = ref({})  // 커뮤니티 페이지네이션 정보
// 페이지 변화 시 새로운 목록 요청
const changeCommunityPage = function (num) {
  roomPageNum.value = num
  getCommunitysInfo()
}

const changeCharacter = () => {
  emit('changeCharacter')
}

// 사용자 정보 요청 함수
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
      if (err.response.status === 401) {
        userStore.refresh()
        getUserInfo()
      }
    })
}

// 커뮤니티 목록 요청 함수
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
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        getCommunitysInfo()
      }
    })
}

// 팔로워 목록 요청 함수
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
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        getFollowInfo()
      }
    })
}

onMounted(() => {
  getUserInfo()
  getCommunitysInfo()
  getFollowInfo()
})
</script>

<style scoped>
@import '../../css/section.css';




.subtitle-section {
  font-size: 30px;
  font-family: "YJ_Obang";
}

.community-section {
  width: 100%;
  display: flex;
  flex-direction: column;
  margin-top: 20px;

}

.contents-section {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
}

.following-table-section {
  flex: 1;
}

.community-table-section {
  flex: 1;
}
</style>