<template>
    <div class="all-room-container">
        <div class="row row-cols-1 row-cols-md-5 g-4">
          <div class="col" v-for="room in rooms" :key="room.roomId">
              <div class="card h-100">
                  <div class="room-img" v-if="room.thumnail">
                      <img :src="room.thumnail" alt="방 이미지" class="card-img-top" />
                  </div>
                  <div v-else>
                      <img src="@/assets/defaultRoom.jpg" alt="기본 이미지" class="card-img-top" />
                  </div>
                  <div class="card-body">
                      <h6>{{ room.type }}</h6>
                      <h5 class="card-title">{{ room.title }}</h5>
                      <div class="d-inline-flex gap-1">
                          <span v-for="(tag, index) in room.hashTags" :key="index">
                              #{{ tag }}<span v-if="index < room.hashTags.length - 1"> </span>
                          </span>
                          <p v-if="room.qid">Q : {{ getQuestionById(room.qid) }}</p>

                      </div>
                    <br>
                    <div class="room-member">
                        <div class="member-icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                                <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
                                <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
                            </svg>
                        </div>
                    </div>
  
                </div>
  
                <div class="card-footer text-center">
                  <a class="btn btn-primary" @click="openEnterModalOpen(room.roomId)">Join</a>
                </div>
            </div>
        </div>
        <RoomEnter :isEnterRoom="isEnterRoom" :id="selectedRoomId" @closeModal="closeModal"/>
    </div>
    <v-pagination
              v-model="page"
              :length="pagingInfo.totalPage"
              @update:modelValue="changeRoomPage"
          ></v-pagination>
    </div>
  </template>
  
  <script setup>
  import { useRoomStore } from '@/stores/room';
  import { ref, computed } from 'vue';
  import RoomEnter from '@/components/PopUp/RoomEnter.vue';
  import { onMounted } from 'vue';
  import {useQuestionsStore} from '@/stores/questions'

  
  const props = defineProps({searchKey:String})
  const roomStore = useRoomStore()
  const rooms = computed(() => roomStore.searchedRooms);
  const pagingInfo = computed(() => roomStore.pagingInfo); //페이지네이션 정보
  const page = ref(1)
  const questionStore = useQuestionsStore()

// qid를 사용하여 질문을 찾는 함수
  const getQuestionById = (qid) => {
    const question = questionStore.questions.find(question => question.id === qid);
    return question ? question.question : '질문을 찾을 수 없음';
  };


  const changeRoomPage = (num) => {
        page.value = num
        roomStore.getSearchRooms(props.searchKey, page.value) // 새로운 커뮤니티 목록을 가져오는 함수
      }
  
  
  onMounted(() =>{
      console.log(props.searchKey)
      roomStore.getSearchRooms(props.searchKey, page.value)
  })
  
  
  const isEnterRoom = ref(false)
  const selectedRoomId = ref(null); // 선택된 방의 roomId를 저장할 ref
  
  const openEnterModalOpen = (roomId) => {
      selectedRoomId.value = roomId; // 선택된 방의 roomId 저장
      isEnterRoom.value =  true
  }
  
  const closeModal = () => {
  isEnterRoom.value = false
  }
  
  
  
  
  </script>
  
  <style scoped>
  
  </style>