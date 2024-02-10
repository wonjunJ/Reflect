<template>
    <div v-if="isQuickHundredModalOpen || isQuickFreeModalOpen" class="modal">
        <div class="modal-content">
            
            <h1>랜덤 방으로 참여하시겠습니까?</h1>

            <div>
                <!-- 백문백답일 경우와 자유주제일 경우 -->
                <div>
                    <!-- <RouterLink :to="{name:'waiting', params:{id:roomId}}" class="btn">참여하기</RouterLink> -->
                    <button type="button" class="btn btn-primary" @click="closeModal">닫기</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import {ref,onMounted} from 'vue';
import { RouterLink } from 'vue-router';
import { useRoomStore } from '@/stores/room'


const {isQuickHundredModalOpen, isQuickFreeModalOpen} = defineProps(['isQuickHundredModalOpen','isQuickFreeModalOpen'])

const roomStore = useRoomStore()
const emit = defineEmits()
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