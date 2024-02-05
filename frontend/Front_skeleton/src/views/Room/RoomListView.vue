<template>
  <div>
    <el-scrollbar max-height="800px">
    <div class="d-inline-flex gap-1">
      <!-- 페이지 이름 -->
      <div class="title">
          <h1>Community</h1>
      </div>
    </div>



  <div class="row">
    <div class="col-2">

    <!-- 왼쪽 필터 -->

    <div class="card mb-5">
            <div class="card-body" id="filter">
                <h6>FILTER</h6>
                <hr style="width:100%; margin-left: 0;">
                <div class="form-check">
                  <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault">
                  <label class="form-check-label" for="flexCheckDefault">
                     100문100답
                  </label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="checkbox" value="" id="flexCheckChecked">
                  <label class="form-check-label" for="flexCheckChecked">
                    자유 주제
                  </label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="checkbox" value="" id="flexCheckChecked">
                  <label class="form-check-label" for="flexCheckChecked">
                    가면 방
                  </label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="checkbox" value="" id="flexCheckChecked">
                  <label class="form-check-label" for="flexCheckChecked">
                    익명 방
                  </label>
                </div>
            </div>

            <div>
              <h6>MEMBER LIMIT</h6>
              <div class="member-limit">
                <el-slider v-model="limit" range show-stops :min="2" :max="8" />
              </div>
            </div>

            <div>
              <button class="btn" @click="submitFilter">적용</button>
            </div>

          </div>

          </div>

      <div class="col-10">
      <div class="d-inline-flex gap-1">
          <!-- 페이지 navbar? -->
          <div>
              <ul class="nav nav-underline">
                  <!-- 전체 방 목록 -->
                  <li class="nav-item">
                      <a class="nav-link" aria-current="page" @click="allRooms">전체  <span class="badge text-bg-secondary">99</span></a>
                  </li>
                  <!-- 아직 덜 찬 방 목록 -->
                  <li class="nav-item">
                      <a class="nav-link" @click="filteredRooms">사람 구하는중  <span class="badge text-bg-secondary">62</span></a>
                      
                  </li>
                  <!-- 내가 참여중인 방 목록 -->
                  <li class="nav-item">
                      <a class="nav-link" @click="filteredRooms">My Rooms  <span class="badge text-bg-secondary">3</span></a>
                      
                  </li>
              </ul>
          </div>


          <!-- 검색창 -->
          <div>
            <form class="d-flex" role="search">
              <input class="searchbox" type="search" placeholder="Search" aria-label="Search">
              <svg type="submit" xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-search-heart" viewBox="0 0 16 16">
                <path d="M6.5 4.482c1.664-1.673 5.825 1.254 0 5.018-5.825-3.764-1.664-6.69 0-5.018"/>
                <path d="M13 6.5a6.47 6.47 0 0 1-1.258 3.844q.06.044.115.098l3.85 3.85a1 1 0 0 1-1.414 1.415l-3.85-3.85a1 1 0 0 1-.1-.115h.002A6.5 6.5 0 1 1 13 6.5M6.5 12a5.5 5.5 0 1 0 0-11 5.5 5.5 0 0 0 0 11"/>
              </svg>
            </form>
          </div>

          <!-- 버튼 -->
          <div class="d-inline-flex gap-1">

              <!-- 방 추천 드롭다운 -->
              <div class="btn-dropdown">
                  <button type="button" class="btn btn-warning dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                      Recommend
                  </button>
                  <ul class="dropdown-menu">
                      <li><a class="dropdown-item" @click="openHundredModal">100문 100답</a></li>
                      <li><a class="dropdown-item" @click="openFreeModal">자유주제</a></li>
                     
                  </ul>
              </div>

              <!-- 방 추천 모달 내용 -->
              <div>
                <RoomRecommend :isHundredModalOpen="isHundredModalOpen" :isFreeModalOpen="isFreeModalOpen" @closeModal="closeModal" />
              </div>


              <!-- 빠른참여 버튼 -->
              <div class="btn-dropdown">
                  <button type="button" class="btn btn-warning dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                      Quick Join
                  </button>
                  <ul class="dropdown-menu">
                      <li><a class="dropdown-item" @click="openQuickHundredModal">100문 100답</a></li>
                      <li><a class="dropdown-item" @click="openQuickFreeModal">자유주제</a></li>
                     
                  </ul>
              </div>

              <!-- 모달 내용 -->
              <div>
                <QuickJoin :isQuickHundredModalOpen="isQuickHundredModalOpen" :isQuickFreeModalOpen="isQuickFreeModalOpen" @closeModal="closeModal" />
              </div>
              
              <!-- 방 만들기 버튼 -->
              <button type="button" class="btn btn-primary create-btn" @click="openCreateRoomModal">+ Create</button>

                <!-- 모달 내용 -->
                <div>
                  <RoomCreate :isModalOpen="isModalOpen" @closeModal="closeModal" />
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
      <!-- 총 페이지 개수 계산 Math.ceil(전체 컨텐츠 개수 / 한 페이지에 보여주고자 하는 컨텐츠의 개수) -->
      <!-- 화면에 보여질 페이지 그룹 계산 화면에 보여질 페이지 그룹 = Math.ceil(현재 페이지 번호 / 한 화면에 보여질 페이지 개수) -->
      <!-- 화면에 보여질 페이지의 첫/마지막 페이지 번호 계산
      어떤 한 페이지 그룹의 첫번째 페이지 번호 = ((페이지 그룹 - 1) * 한 화면에 보여질 페이지 개수) + 1

      어떤 한 페이지 그룹의 마지막 페이지 번호 = 페이지 그룹 * 한 화면에 보여질 페이지 개수 
      (단, 페이지 그룹 * 한 화면에 보여질 페이지 개수의 값이 전체 페이지보다 크다면 전체 페이지가 마지막 페이지 번호가 됩니다)
      -->
      <nav aria-label="Page navigation example">
        <ul class="pagination">
          <li class="page-item">
            <a class="page-link" href="#" aria-label="Previous">
              <span aria-hidden="true">&laquo;</span>
            </a>
          </li>
          <li class="page-item"><a class="page-link" href="#">1</a></li>
          <li class="page-item"><a class="page-link" href="#">2</a></li>
          <li class="page-item"><a class="page-link" href="#">3</a></li>
          <li class="page-item">
            <a class="page-link" href="#" aria-label="Next">
              <span aria-hidden="true">&raquo;</span>
            </a>
          </li>
        </ul>
      </nav>
    </div>
    </div>
  </el-scrollbar>
  </div>
</template>



<script setup>
// import { RouterLink, RouterView, useRouter } from 'vue-router';
import { ref } from 'vue';
import AllRoomList from '@/components/Room/AllRoomList.vue'
import FilteredRoomList from '@/components/Room/FilteredRoomList.vue'
import RoomCreate from '@/components/PopUp/RoomCreate.vue'
import RoomRecommend from '@/components/PopUp/RoomRecommend.vue'
import QuickJoin from '@/components/PopUp/QuickJoin.vue';
import { useRoomStore} from '@/stores/room'
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
const limit = ref([2,8])


// const value = ref([2,8])

const submitFilter = () => {
  // 필터 선택한 정보 보내주고 불러오기
}

const allRooms = () => {
  
  isAll.value = true
  isWaiting.value = false
  isFiltered.value = false
  roomStore.getRooms()
}

const waitingRooms = () => {
  isAll.value = false
  isWaiting.value = true
  isFiltered.value = false
  roomStore.getFilteredRooms()
  
}

const filteredRooms = () => {
  isAll.value = false
  isWaiting.value = false
  isFiltered.value = true
  roomStore.getFilteredRooms()
  
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

const openFreeModal = ()=> {
  isFreeModalOpen.value = true
}

const openQuickHundredModal = () => {
  isQuickHundredModalOpen.value = true
}

const openQuickFreeModal = ()=> {
  isQuickFreeModalOpen.value = true
}




</script>


<style>
body {
  margin : 0;
}
div {
  box-sizing: border-box;
}
.modal {
  width: 100%; height:100%;
  background: rgba(0,0,0,0.5);
  position: fixed; padding: 20px;
}
.modal-content {
  width: 100%; background: white;
  border-radius: 8px;
  padding: 20px;
} 

#filter label {
  background-color: none;
  
}

.searchbox {
  padding: 3px 5px;
  border-bottom: 1px solid black;
}

.searchbox:hover {
  border-bottom: 2px solid rgb(152, 117, 241);

}
.searchbox:hover::placeholder {
  color: rgb(203, 188, 243) ;
}

.searchbox:focus {
  outline:none;
  border-bottom: 2px solid rgb(152, 117, 241);
}


.bi-search-heart {
  margin-top: 5px;
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