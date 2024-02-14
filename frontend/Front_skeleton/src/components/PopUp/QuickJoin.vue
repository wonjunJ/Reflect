<template>
    <div v-if="isQuickHundredModalOpen || isQuickFreeModalOpen" class="modal">
        <div class="modal-content">
            
            <h1>랜덤 방으로 참여하시겠습니까?</h1>

            <div>
                <!-- 백문백답일 경우와 자유주제일 경우 -->
                <div>
                    <button type="button" class="btn btn-secondary" @click="participate">참여하기</button>
                    <button type="button" class="btn btn-primary" @click="closeModal">닫기</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import {ref,onMounted} from 'vue';
import { useRoomStore } from '@/stores/room'
import router from '@/router';


const {isQuickHundredModalOpen, isQuickFreeModalOpen} = defineProps(['isQuickHundredModalOpen','isQuickFreeModalOpen'])

const roomStore = useRoomStore()
const rooms = ref([])
const from =2
const to = 8
const page = 0
const emit = defineEmits()
const participate =() => {
    if (isQuickFreeModalOpen){
        rooms.value = roomStore.getFilteredRooms(['FREE','Waiting'], from, to, page)
    }
    else if (isQuickHundredModalOpen){
        rooms.value = roomStore.getFilteredRooms(['BACKMOON','Waiting'], from, to, page)
    }
    // 방 목록이 비어있지 않은지 확인
    if (rooms.value && rooms.value.length > 0) {
        // 랜덤 방 선택
        const randomRoom = rooms.value[Math.floor(Math.random() * rooms.value.length)];

        // 선택된 방의 ID를 사용하여 라우트 변경
         router.push({path: `/waiting/${randomRoom.roomId}`})
    } else {
        // 적절한 처리 또는 경고 메시지 표시
        console.log("사용 가능한 방이 없습니다.");
        window.alert("사용 가능한 방이 없습니다.")
    }
}
// const rooms = ref(null)
// onMounted(() =>{
//     const chooseType = () => {
//             if (isQuickFreeModalOpen){
//                 rooms.value = roomStore.getFilteredRooms('FREE')
//             }
//             else if (isQuickHundredModalOpen){
//                 rooms.value = roomStore.getFilteredRooms('BACKMOON')
//             }
//         }

//     const randomRoom = rooms[Math.floor(Math.random() * rooms.length)];
//     const roomId = ref(randomRoom.room_id)
// })




const closeModal = () => {
    emit('closeModal')
}



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