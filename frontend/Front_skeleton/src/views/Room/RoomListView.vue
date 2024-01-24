<template>
    <div>
      <div class="d-inline-flex gap-1">
        <!-- 페이지 이름 -->
        <div class="title">
            <h1>Community</h1>
        </div>
  
        <!-- 검색창 -->
        <form class="d-flex" role="search">
          <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
          <button class="btn btn-outline-success" type="submit">Search</button>
        </form>
      </div>
  
  
  
    <div class="row">
      <div class="col-2">
  
  
  
      <!-- 왼쪽 필터 -->
  
      <div class="card mb-5">
              <div class="card-body">
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
                <!-- <Slider v-model="value"/> -->
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
                        <RouterLink :to="{}">전체  </RouterLink>
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
  
            <!-- 버튼 -->
            <div class="d-inline-flex gap-1">
  
                <!-- 방 추천 드롭다운 -->
                <div class="btn-dropdown">
                    <button type="button" class="btn btn-warning dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                        Recommend
                    </button>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="#">100문 100답</a></li>
                        <li><a class="dropdown-item" href="#">자유주제</a></li>
                    </ul>
                </div>
  
                <!-- 빠른참여 버튼 -->
                <button type="button" class="btn btn-danger">Quick Join</button>
                
                <!-- 방 만들기 버튼 -->
                <!-- <RouterLink :to="{name: 'CreateRoom'}" @click="openCreateRoomModal" class="btn">+ Create</RouterLink> -->
                <button @click="openCreateRoomModal" type="button" class="btn btn-primary">+ Create</button>
                <div class="modal" v-if="isModalOpen">
                  <div class="modal-content">
                    <h3>방 만들기</h3>
                    <!-- 모달 내용 -->
                    <div>
  
  
                    </div>
  
                    <!-- 모달 닫기 버튼 -->
                    <button @click="closeModal" class="btn btn-secondary">닫기</button>
                  </div>
                </div>
            
            </div>
        </div>
  
  
        <hr>
        <div class="roomList">
          <!-- 방 목록 리스트 -->
          <!-- 전체 방 리스트 -->
          <div v-if="isAll"> 
            <RoomList />
          </div>
  
          <!-- 사람 구하는 방 리스트 -->
          <div v-if="isFiltered"> 
  
            <FilteredRoomList />
          </div>
          
          <!-- 내가 가입한 방 리스트 -->
          <div v-if="isFiltered"> 
  
            <FilteredRoomList />  
          </div>
        </div>
  
        <hr>
  
        <!-- 페이지네이션 -->
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
  
    </div>
  </template>
  
  
  
  <script setup>
  import { RouterLink, RouterView, useRouter } from 'vue-router';
  import { ref } from 'vue';
  import RoomList from '@/components/rooms/RoomList.vue'
  import FilteredRoomList from '@/components/rooms/FilteredRoomList.vue'
  import { useRoomStore} from '@/stores/room'
  // import Slider from '@vueform/slider'
  
  const isModalOpen = ref(false)
  
  const roomStore = useRoomStore()
  const isAll = ref(false)
  const isFiltered = ref(false)
  // const value = ref([2,8])
  
  const allRooms = () => {
    
    isAll.value = true
    isFiltered.value = false
    roomStore.getRooms()
  }
  
  const filteredRooms = () => {
    isAll.value = false
    isFiltered.value = true
    roomStore.getFilteredRooms()
    
  }
  
  const openCreateRoomModal = () => {
    isModalOpen.value = true
  }
  
  const closeModal = () => {
    isModalOpen.value = false
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
  
  
  
  </style>