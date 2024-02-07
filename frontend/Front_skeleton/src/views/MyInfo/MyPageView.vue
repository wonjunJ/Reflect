<template>
  <h1>This is MyPage</h1>
  <div class="d-flex">
    <aside class="aside-container">
      <Profile :user-info="userInfo" @change-character="changeCharacter"/>
    </aside>
    <body class="d-flex flex-column body">
      <Calendar/>
      <QandA/>
      <div class="d-flex">
        <div>
          <h4>Follower</h4>
          <FollowList :follows="followers"/>
        </div>
        <div>
          <h4>Following</h4>
          <FollowList :follows="followings"/>
        </div>
        <CommunityList :communitys="communitys"/>
      </div>
    </body>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Profile from './components/Profile.vue'
import Calendar from './components/Calendar.vue'
import QandA from './components/QandA.vue'
import FollowList from './components/FollowList.vue'
import CommunityList from './components/CommunityList.vue'

// 사용자 정보 더미데이터
const userInfo = ref({
  username: 'test',
  profileimg: null,
  email: 'asdf@asdf.com',
  introduction: 'Hello, my name is Elder Price and I will like to share with you the most amazing book',
  mbti: 'INTP',
  characterId: 1,
})

const emit = defineEmits(['changeCharacter'])

// 팔로워 목록
const followers = ref([
  { id: 1, profileimg: null, username: 'test01', alike: '75%', isonline: false},
  { id: 2, profileimg: null, username: 'test02', alike: '60%', isonline: true},
  { id: 3, profileimg: null, username: 'test03', alike: '85%', isonline: false}
])

// 팔로잉 목록
const followings = ref([
  { id: 4, profileimg: null, username: 'test04', alike: '73%', isonline: true},
  { id: 5, profileimg: null, username: 'test05', alike: '52%', isonline: true},
  { id: 6, profileimg: null, username: 'test06', alike: '46%', isonline: false}
])

// 가입 커뮤니티 목록
const communitys = ref([
  { id: 1, title: 1, people: 6, hashtag: ['soccer', 'movie']},
  { id: 2, title: 2, people: 4, hashtag: ['baseball']},
])

const changeCharacter = (num) => {
  userInfo.value.characterId = num
  emit('changeCharacter', num)
}
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