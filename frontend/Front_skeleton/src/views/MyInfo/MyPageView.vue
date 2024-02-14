<template>
  <div class="d-flex">
    <aside class="aside-container">
      <Profile :user-info="userInfo" @change-character="changeCharacter" />
    </aside>

    <body class="body-section d-flex flex-column body">

      <Calendar />

      <QandA />

      <div class="community-section">
        <div class="subtitle-section ">
          <p>커뮤니티</p>
        </div>
        <div class="contents-section">
          <div class="following-table-section">
            <FollowList :follows="followings" />
          </div>
          <div class="community-table-section">
            <CommunityList :communitys="communitys" />
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

// 사용자 정보 더미데이터
const userInfo = ref({})

const emit = defineEmits(['changeCharacter'])

// 팔로워 목록
const followers = ref([
  { id: 1, profileimg: null, username: 'test01', alike: '75%', isonline: false },
  { id: 2, profileimg: null, username: 'test02', alike: '60%', isonline: true },
  { id: 3, profileimg: null, username: 'test03', alike: '85%', isonline: false }
])

// 팔로잉 목록
const followings = ref([
  { id: 4, profileimg: null, username: 'test04', alike: '73%', isonline: true },
  { id: 5, profileimg: null, username: 'test05', alike: '52%', isonline: true },
  { id: 6, profileimg: null, username: 'test06', alike: '46%', isonline: false }
])

// 가입 커뮤니티 목록
const communitys = ref([
  { id: 1, title: 1, people: 6, hashtag: ['soccer', 'movie'] },
  { id: 2, title: 2, people: 4, hashtag: ['baseball'] },
])

const changeCharacter = (num) => {
  userInfo.value.characterId = num
  emit('changeCharacter', num)
}

onMounted(() => {
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
      // console.log(err)
    })
})
</script>

<style scoped>
.aside-container {
  background-color: #AAD9BB;
  padding: 20px;
  box-shadow: 5px 5px 10px rgb(197, 196, 196);
}

.body-section {
  padding: 35px;
}

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