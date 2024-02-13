<template>
    <div v-if="isHundredModalOpen || isFreeModalOpen" class="modal">
        <div class="modal-content">
        <!-- 방 추천 팝업 -->

        <h2>이러한 방들은 어떠신가요?</h2>

        
        <div>
            <!-- 추천방 목록 -->
            <div>
                <!-- 백문백답 추천일 경우와 자유주제 추천의 경우를 나눠서 데이터를 가져올 것 -->
                <div>
                    <div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="carousel">


                        <div class="carousel-inner">
                            
                            <!-- 방1 -->
                            <div class="carousel-item active">
                                <div class="col" v-for="room in randomRooms" :key="room.room_id">
                                    <div class="card h-100">
                                        <img :src="room.thumbnail" class="card-img-top" alt="방 썸네일">
                                        <div class="card-body">
                                            <h6>{{ room.type }}</h6>
                                            <h5 class="card-title">{{ room.title }}</h5>
                                            <div class="d-inline-flex gap-1">{{ room.hashtag }}</div>
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
                                            <a class="btn btn-primary" @click="openEnterModalOpen">Join</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                        </div>
                        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="prev">
                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Previous</span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Next</span>
                        </button>
                        </div>
                        
                </div>
            </div>
        </div>
       
        <div>
            <button type="button" class="btn btn-secondary" @click="closeModal">닫기</button>
        </div>

    </div>
    <div>
        <RoomEnter :isEnterRoom="isEnterRoom" :id="roomId" @closeModal="closeEnterModal"/>
    </div>
    </div>
</template>

<script setup>
import { ref} from 'vue'
import RoomEnter from '@/components/PopUp/RoomEnter.vue';
import { useRoomStore } from '@/stores/room'

const roomStore = useRoomStore()
const isEnterRoom = ref(false)
const rooms = ref([])
const randomRooms = ref([])
const from =2
const to = 8
const page = 0
const roomId = ref(null)

const { isHundredModalOpen, isFreeModalOpen } = defineProps(['isHundredModalOpen', 'isFreeModalOpen']);
const emit = defineEmits()

const shuffleArray = (array) => {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]]; // ES6 구조 분해 할당을 사용한 요소 교환
    }
};

const participate =() => {
    if (isFreeModalOpen){
        rooms.value = roomStore.getFilteredRooms('FREE', from, to, page)
    }
    else if (isHundredModalOpen){
        rooms.value = roomStore.getFilteredRooms('BACKMOON', from, to, page)
    }
    // 방 목록이 비어있지 않은지 확인
    if (rooms.value && rooms.value.length > 0) {
        // 방 목록 셔플
        shuffleArray(rooms.value);

        // 셔플된 배열에서 최대 5개의 방 선택
        randomRooms.value = rooms.value.slice(0, Math.min(5, rooms.value.length));

        // 선택된 방의 ID를 사용하여 처리
        // 예: 선택된 방을 콘솔에 출력
        console.log("선택된 랜덤 방:", randomRooms.value);
    } else {
        // 적절한 처리 또는 경고 메시지 표시
        console.log("사용 가능한 방이 없습니다.");
        window.alert("사용 가능한 방이 없습니다.");
    }
};


const openEnterModalOpen = () => {
    isEnterRoom.value =  true
}

const closeEnterModal = () => {
    isEnterRoom.value =  false
}




const closeModal = () => {
    emit('closeModal')
};


</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  width: 400px;
  background: white;
  padding: 20px;
  border-radius: 8px;
}
</style>