<template>
  <div class="main-section">
    <div class="mainBackground">
      <div class="sub-title-section">
        <div class="sub-title">
          <p>COMMUNITY</p>
        </div>
        <div class="button-section">
          <!-- 방 추천 드롭다운 -->
          <button @click="openRecommendModal" type="button" class="btn btn-recommend">Recommend</button>
          <!-- 빠른참여 버튼 -->
          <button @click="openQuickModal" type="button" class="btn btn-recommend">Quick Join</button>
          <!-- 방 만들기 버튼 -->
          <button class="btn btn-primary btn-create" @click="openCreateRoomModal">
            <div class="inline-flex items-center space-x-2">
              <div>+ Create</div>
            </div>
          </button>
        </div>
      </div>
      <div class="mainContent">
        <div class="section">
          <div class="nav-filter">
            <ul class="nav nav-underline">
              <!-- 전체 방 목록 -->
              <li class="nav-item">
                <a class="nav-link all-rooms" aria-current="page" @click="allRooms">전체 </a>
              </li>
              <!-- 아직 덜 찬 방 목록 -->
              <li class="nav-item waiting-rooms">
                <a class="nav-link" @click="waitingRooms">사람 구하는중 </a>

              </li>
              <!-- 내가 참여중인 방 목록 -->
              <li class="nav-item my-rooms">
                <a class="nav-link" @click="myRooms">My Rooms </a>

              </li>
            </ul>
          </div>
          <div class="search-filter">
            <!-- 검색창 -->
            <div class="room-search">
              <form class="d-flex" role="search" @submit.prevent>
                <input v-model="searchKey" class="searchbox" type="text" placeholder="Search" aria-label="Search"
                  @keyup.enter="search">
              </form>
            </div>

          </div>
          <div class="room-filter">
            <div class="dropdown">
              <img class="dropdown-toggle filter-icon" data-bs-toggle="dropdown" aria-expanded="false"
                data-bs-auto-close="outside" src="@/assets/filter.png" alt="필터 아이콘">
              <form class="dropdown-menu p-4" @submit.prevent>
                <div class="mb-3">
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="hundredFilter"
                      @click="isHundredFilter = !isHundredFilter">
                    <label class="form-check-label" for="hundredFilter">백문백답</label>
                  </div>
                </div>
                <div class="mb-3">
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="freeFilter" @click="isFreeFilter = !isFreeFilter">
                    <label class="form-check-label" for="freeFilter">자유주제</label>
                  </div>
                </div>
                <div class="mb-3">
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="waitingFilter"
                      @click="isWaitingFilter = !isWaitingFilter">
                    <label class="form-check-label" for="waitingFilter">모집중</label>
                  </div>
                </div>
                <div>
                  <h6>인원수</h6>
                  <div class="member-limit">
                    <el-slider v-model="range" range show-stops :min="2" :max="8" />
                  </div>
                </div>
                <button class="btn" @click="submitFilter">적용</button>
              </form>
            </div>
          </div>
        </div>
        <div class="body-section">

          <div class="listsection">
            <div class="roomList">
              <!-- 방 목록 리스트 -->
              <!-- 전체 방 리스트 -->
              <div v-if="isAll">
                <AllRoomList />
              </div>
              <!-- 사람 구하는 방 리스트 -->
              <div v-if="isWaiting">
                <FilteredRoomList :waiting="waiting" :from=2 :to=8 />
              </div>

              <!-- 내가 가입한 방 리스트 -->
              <div v-if="isMyRoom">
                <MyRoomList />
              </div>

              <!-- 필터링된 리스트 불러오기 -->
              <div v-if="isFiltered">
                <FilteredRoomList :type="type" :waiting="waiting" :from="from" :to="to" />
              </div>

              <!-- 검색한 리스트 불러오기 -->
              <div v-if="isSearched">
                <SearchedRoomList :searchKey="searchKey" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>







  </div>



  <div>
    <!-- <RoomRecommend :recRooms="recRooms" :isHundredModalOpen="isHundredModalOpen" :isFreeModalOpen="isFreeModalOpen" @closeModal="closeModal" /> -->
    <RoomRecommend :recRooms="recRooms" :isRecommendModalOpen="isRecommendModalOpen" @closeModal="closeModal" />
  </div>


  <!-- 모달 내용 -->
  <div>
    <QuickJoin :recRooms="recRooms" :isQuickModalOpen="isQuickModalOpen" @closeModal="closeModal" />
    <!-- <QuickJoin :recRooms="recRooms" :isQuickHundredModalOpen="isQuickHundredModalOpen" :isQuickFreeModalOpen="isQuickFreeModalOpen" @closeModal="closeModal" /> -->
  </div>

  <!-- 모달 내용 -->
  <div>
    <RoomCreate :isModalOpen="isModalOpen" @closeModal="closeModal" />
  </div>
</template>

<script setup>
// import { RouterLink, RouterView, useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';
import AllRoomList from '@/components/Room/AllRoomList.vue'
import FilteredRoomList from '@/components/Room/FilteredRoomList.vue'
import MyRoomList from '@/components/Room/MyRoomList.vue'
import SearchedRoomList from '@/components/Room/SearchedRoomList.vue'
import RoomCreate from '@/components/PopUp/RoomCreate.vue'
import RoomRecommend from '@/components/PopUp/RoomRecommend.vue'
import QuickJoin from '@/components/PopUp/QuickJoin.vue'

import { useRoomStore } from '@/stores/room'
import { useRouter } from 'vue-router'
import axios from 'axios';

// import Slider from '@vueform/slider'

const router = useRouter()
const roomStore = useRoomStore()
const isAll = ref(true)
const isWaiting = ref(false)
const isFiltered = ref(false)
const isMyRoom = ref(false)
const isSearched = ref(false)
const isModalOpen = ref(false)
const isRecommendModalOpen = ref(false)
const isQuickModalOpen = ref(false)
const isHundredModalOpen = ref(false)
const isFreeModalOpen = ref(false)
const isQuickHundredModalOpen = ref(false)
const isQuickFreeModalOpen = ref(false)
const isHundredFilter = ref(false)
const isFreeFilter = ref(false)
const isWaitingFilter = ref(false)
const waiting = ref(null)
const range = ref([2, 8])
const from = ref(2)
const to = ref(8)
const searchKey = ref(null)
const type = ref(null)

const search = () => {
  // const searchKey = document.getElementsByClassName('searchbox')
  // console.log('검색 시작')
  // console.log(searchKey.value)
  isSearched.value = true
  isAll.value = false
  isWaiting.value = false
  isFiltered.value = false
  isMyRoom.value = false
}

const submitFilter = () => {

  from.value = range.value[0]
  to.value = range.value[1]
  console.log(from.value)
  console.log(to.value)
  if (isHundredFilter.value && !isFreeFilter.value) {
    type.value = 'BACKMOON'
  }
  else if (!isHundredFilter.value && isFreeFilter.value) {
    type.value = 'FREE'
  }
  else {
    type.value = null
  }
  if (isWaitingFilter.value) {
    waiting.value = 'Waiting'
  }
  else {
    waiting.value = null
  }

  isAll.value = false
  isWaiting.value = false
  isFiltered.value = true
  isMyRoom.value = false
  isSearched.value = false

}

const allRooms = () => {
  isAll.value = true
  isWaiting.value = false
  isFiltered.value = false
  isMyRoom.value = false
  isSearched.value = false
}

const waitingRooms = () => {
  isAll.value = false
  isWaiting.value = true
  isFiltered.value = false
  isMyRoom.value = false
  isSearched.value = false
  to.value = 8
  from.value = 2

}
const myRooms = () => {
  isAll.value = false
  isWaiting.value = false
  isFiltered.value = false
  isMyRoom.value = true
  isSearched.value = false //
}

const recRooms = ref([])

// 방 추천 axios로 가져오기
const roomRecommend = () => {
  return axios({
    method: 'get',
    url: `${roomStore.API_URL}/room/recommend`,
    headers: {
      Authorization: `Bearer ${roomStore.token}`
    }
  })
    .then((res) => {
      console.log(res.data);
      recRooms.value = res.data;
      console.log(recRooms.value);
    })
    .catch((err) => {
      console.log(err);
    });
}


const openCreateRoomModal = () => {
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
  isRecommendModalOpen.value = false
  isQuickModalOpen.value = false
  // isHundredModalOpen.value = false
  // isFreeModalOpen.value = false
  // isQuickFreeModalOpen.value = false
  // isQuickHundredModalOpen.value = false
}



const openRecommendModal = () => {
  // 방 추천 요청을 먼저 보내고, 응답을 받은 후에 recRooms를 업데이트합니다.
  roomRecommend().then(() => {
    isRecommendModalOpen.value = true;
    console.log(recRooms.value);
  });
}
// const openHundredModal = () => {
//   // 방 추천 요청을 먼저 보내고, 응답을 받은 후에 recRooms를 업데이트합니다.
//   roomRecommend().then(() => {
//     isHundredModalOpen.value = true;
//     console.log(recRooms.value);
//   });
// }

// const openFreeModal = () => {
//   isFreeModalOpen.value = true
//   roomRecommend()
//   console.log(recRooms.value)

// }

const openQuickModal = () => {
  roomRecommend().then(() => {
    isQuickModalOpen.value = true
    console.log(recRooms.value);
  });
}
// const openQuickHundredModal = () => {
//   isQuickHundredModalOpen.value = true
// }

// const openQuickFreeModal = () => {
//   isQuickFreeModalOpen.value = true
// }




</script>


<style scoped>
@import '../../css/section.css';

.mainBackground {
  background-color: #80BCBD;
  ;
}

.mainContent {
  padding: 50px;
  border-top-left-radius: 100px;
  border-top-right-radius: 100px;
  width: 100%;
  height: 100%;
  background-color: white;
}


.main-section {
  margin: 0px;
}

.sub-title-section {
  background-color: #80BCBD;
  margin: 0px;
}

.body-section {
  border-radius: 100%;
}

.section {
  display: flex;
  flex-direction: row;
  padding: 20px;
}

/* @import "../../css/component.css"; */


.room-list-back {
  width: 100%;
  height: 100%;
  background-color: white;
  border-radius: 70px 70px 0 0;
  padding-top: 30px;
}

div {
  box-sizing: border-box;

}

.modal {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;
}

.modal-content {
  width: 100%;
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.room-top-nav {
  display: flex;
  justify-content: space-between;
  flex-direction: row;
  flex-wrap: nowrap;
}

.title {
  margin: 40px 50px;
}

.title p {
  font-size: x-large;
  font-weight: bold;
}

.nav-link {
  color: grey;

  &:hover {
    color: #80BCBD;

    span {
      background-color: #80BCBD;
      color: white;
    }
  }

  &:active {
    color: #80BCBD;

    span {
      background-color: #80BCBD;
      color: white;
    }
  }

  &:target {
    color: #80BCBD;

    span {
      background-color: #80BCBD;
      color: white;
    }
  }
}

.nav-link-badge {
  background-color: darkgray;
  color: white;
  padding: 0 4px 1px 4px;
  border-radius: 5px;
}




.room-btn-group {
  margin-right: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.btn-dropdown button {
  background-color: #F9F7C9;

  &:hover {
    background-color: #f7f499;
  }

  &:active {
    background-color: #f7f499;
  }

  &:focus {
    background-color: #f7f499;

  }
}


.btn-create {
  background-color: #D5F0C1;
  color: black;
  font-weight: 500;
  border: none;
}

.btn-create:hover {
  animation: jelly 0.5s;
  background-color: #D5F0C1;
  color: black;
}

@keyframes jelly {
  25% {
    transform: scale(0.9, 1.1);
  }

  50% {
    transform: scale(1.1, 0.9);
  }

  75% {
    transform: scale(0.95, 1.05);
  }
}

.room-list-nav {
  display: flex;
  justify-content: space-between;
  flex-direction: row;
  flex-wrap: nowrap;
  margin-left: 50px;
  margin-bottom: -20px;
}

.search-filter {
  display: flex;
}

.room-search {
  flex: right;
}

.room-filter {
  margin-top: 4px;
  margin-left: 2px;
  flex: right;
}


.searchbox {
  padding: 3px 5px;
  border-bottom: 1px solid black;
}

.searchbox:hover {
  border-bottom: 2px solid rgb(152, 117, 241);

}

.searchbox:hover::placeholder {
  color: rgb(203, 188, 243);
}

.searchbox:focus {
  outline: none;
  border-bottom: 2px solid rgb(152, 117, 241);
}

.filter-icon {
  width: 25px;
}

.filter-icon:hover {
  width: 25px;
  filter: invert(53%) sepia(57%) saturate(3871%) hue-rotate(226deg) brightness(101%) contrast(89%);
}

.member-limit {
  display: flex;
  align-items: center;
}

.member-limit .el-slider {
  margin-top: 0;
  margin-left: 12px;
}
</style>