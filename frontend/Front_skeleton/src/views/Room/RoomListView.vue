<template>
  <div class="room-list-container">
    <el-scrollbar max-height="800px">
      <div class="room-top-nav">
        <!-- 페이지 이름 -->
        <div class="title">
          <p>방 목록</p>
        </div>


        <!-- 버튼 -->
        <div class="d-inline-flex gap-1 room-btn-group">

          <!-- 방 추천 드롭다운 -->
          <div class="btn-dropdown">
            <button type="button" class="btn dropdown-toggle btn-recommend" data-bs-toggle="dropdown"
              aria-expanded="false">
              Recommend
            </button>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" @click="openHundredModal">100문 100답</a></li>
              <li><a class="dropdown-item" @click="openFreeModal">자유주제</a></li>

            </ul>
          </div>

          <!-- 방 추천 모달 내용 -->
          <div>
            <RoomRecommend :isHundredModalOpen="isHundredModalOpen" :isFreeModalOpen="isFreeModalOpen"
              @closeModal="closeModal" />
          </div>


          <!-- 빠른참여 버튼 -->
          <div class="btn-dropdown">
            <button type="button" class="btn dropdown-toggle btn-quick" data-bs-toggle="dropdown" aria-expanded="false">
              Quick Join
            </button>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" @click="openQuickHundredModal">100문 100답</a></li>
              <li><a class="dropdown-item" @click="openQuickFreeModal">자유주제</a></li>
            </ul>
          </div>

          <!-- 모달 내용 -->
          <div>
            <QuickJoin :isQuickHundredModalOpen="isQuickHundredModalOpen" :isQuickFreeModalOpen="isQuickFreeModalOpen"
              @closeModal="closeModal" />
          </div>

          <!-- 방 만들기 버튼 -->
          <!-- <button type="button" class="btn btn-primary create-btn" @click="openCreateRoomModal">+ Create</button> -->
          <button class="btn btn-primary btn-create" @click="openCreateRoomModal">
            <div class="inline-flex items-center space-x-2">
              <div>+ Create</div>
            </div>
          </button>
          <!-- 모달 내용 -->
          <div>
            <RoomCreate :isModalOpen="isModalOpen" @closeModal="closeModal" />
          </div>

        </div>
      </div>

      <div class="room-list-back">
        <div class="d-inline-flex gap-1 room-list-nav">
          <!-- 페이지 navbar? -->
          <div class="nav-filter">
            <ul class="nav nav-underline">
              <!-- 전체 방 목록 -->
              <li class="nav-item">
                <a class="nav-link all-rooms" aria-current="page" @click="allRooms">전체 <span class="nav-link-badge"
                    :value="allRoomNum">0</span></a>
              </li>
              <!-- 아직 덜 찬 방 목록 -->
              <li class="nav-item waiting-rooms">
                <a class="nav-link" @click="filteredRooms">사람 구하는중 <span class="nav-link-badge"
                    :value="waitingRoomNum">0</span></a>

              </li>
              <!-- 내가 참여중인 방 목록 -->
              <li class="nav-item my-rooms">
                <a class="nav-link" @click="filteredRooms">My Rooms <span class="nav-link-badge"
                    :value="myRoomNum">0</span></a>

              </li>
            </ul>
          </div>


          <div class="search-filter">
            <!-- 검색창 -->
            <div class="room-search">
              <form class="d-flex" role="search">
                <input class="searchbox" type="search" placeholder="Search" aria-label="Search">
              </form>
            </div>

            <!-- 필터 드롭다운 -->
            <div class="room-filter">
              <div class="dropdown">
                <img class="dropdown-toggle filter-icon" data-bs-toggle="dropdown" aria-expanded="false"
                  data-bs-auto-close="outside" src="@/assets/filter.png" alt="필터 아이콘">
                <form class="dropdown-menu p-4">
                  <div class="mb-3">
                    <div class="form-check">
                      <input type="checkbox" class="form-check-input" id="hundredFilter">
                      <label class="form-check-label" for="hundredFilter">백문백답</label>
                    </div>
                  </div>
                  <div class="mb-3">
                    <div class="form-check">
                      <input type="checkbox" class="form-check-input" id="freeFilter">
                      <label class="form-check-label" for="freeFilter">자유주제</label>
                    </div>
                  </div>
                  <div class="mb-3">
                    <div class="form-check">
                      <input type="checkbox" class="form-check-input" id="waitingFilter">
                      <label class="form-check-label" for="waitingFilter">모집중</label>
                    </div>
                  </div>
                  <div>
                    <h6>인원수</h6>
                    <div class="member-limit">
                      <el-slider v-model="limit" range show-stops :min="2" :max="8" />
                    </div>
                  </div>
                  <button class="btn" @click="submitFilter">적용</button>
                </form>
              </div>

            </div>
          </div>

        </div>


        <hr>
        <div class="roomList">
          <!-- 방 목록 리스트 -->
          <!-- 전체 방 리스트 -->
          <div v-if="isAll">
            <AllRoomList />
          </div>

          <!-- 사람 구하는 방 리스트 -->
          <div v-if="isWaiting">

            <FilteredRoomList />
          </div>

          <!-- 내가 가입한 방 리스트 -->
          <div v-if="isFiltered">

            <FilteredRoomList />
          </div>

          <div>
            <!-- 필터링된 리스트 불러오기 -->
          </div>
        </div>

        <hr>

        <!-- 페이지네이션 -->
        <RoomPagenation />
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup>
// import { RouterLink, RouterView, useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';
import AllRoomList from '@/components/Room/AllRoomList.vue'
import FilteredRoomList from '@/components/Room/FilteredRoomList.vue'
import RoomCreate from '@/components/PopUp/RoomCreate.vue'
import RoomRecommend from '@/components/PopUp/RoomRecommend.vue'
import QuickJoin from '@/components/PopUp/QuickJoin.vue'
import RoomPagenation from '@/components/Room/RoomPagenation.vue'
import { useRoomStore } from '@/stores/room'
// import Slider from '@vueform/slider'


const roomStore = useRoomStore()
const isAll = ref(true)
const isWaiting = ref(false)
const isFiltered = ref(false)
const isModalOpen = ref(false)
const isHundredModalOpen = ref(false)
const isFreeModalOpen = ref(false)
const isQuickHundredModalOpen = ref(false)
const isQuickFreeModalOpen = ref(false)
const limit = ref([2, 8])
const allRoomNum = ref(0) // 전체 방 개수
const waitingRoomNum = ref(0) // 모집중인 방 개수
const myRoomNum = ref(0) //내 방 개수

onMounted(() => {
  roomStore.getRooms()
})


const submitFilter = () => {
  // 필터 선택한 정보 보내주고 불러오기
}

const allRooms = () => {

  isAll.value = true
  isWaiting.value = false
  isFiltered.value = false
}

const waitingRooms = () => {
  isAll.value = false
  isWaiting.value = true
  isFiltered.value = false

}

const filteredRooms = () => {
  isAll.value = false
  isWaiting.value = false
  isFiltered.value = true

}


const openCreateRoomModal = () => {
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
  isHundredModalOpen.value = false
  isFreeModalOpen.value = false
  isQuickFreeModalOpen.value = false
  isQuickHundredModalOpen.value = false
}

const openHundredModal = () => {
  isHundredModalOpen.value = true
}

const openFreeModal = () => {
  isFreeModalOpen.value = true
}

const openQuickHundredModal = () => {
  isQuickHundredModalOpen.value = true
}

const openQuickFreeModal = () => {
  isQuickFreeModalOpen.value = true
}




</script>


<style scoped>
html body {
  width: 100%;
  height: 100%;
  margin: 0;
  background-color: #80BCBD;

}

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
}</style>