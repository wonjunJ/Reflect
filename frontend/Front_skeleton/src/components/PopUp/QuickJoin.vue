<template>
    <div v-if="isQuickModalOpen" class="modal">
    <!-- <div v-if="isQuickHundredModalOpen || isQuickFreeModalOpen" class="modal"> -->
        <div class="modal-content">
            
            <h1>랜덤 방으로 참여하시겠습니까?</h1>


            <div>
                <!-- 백문백답일 경우와 자유주제일 경우 -->
                <div>
                    <button type="button" class="btn btn-secondary" @click="openEnterModalOpen(props.recRooms[0].roomId)">참여하기</button>
                    <button type="button" class="btn btn-primary" @click="closeModal">닫기</button>
                </div>
            </div>
        <RoomEnter :isEnterRoom="isEnterRoom" :id="roomId" @closeModal="closeEnterModal"/>
        </div>
    </div>
</template>

<script setup>
import {ref,onMounted} from 'vue';
import { useRoomStore } from '@/stores/room'
import RoomEnter from '@/components/PopUp/RoomEnter.vue';

const props = defineProps({
    recRooms: Object,
    isQuickModalOpen: Boolean,

});
// const {isQuickHundredModalOpen, isQuickFreeModalOpen} = defineProps(['isQuickHundredModalOpen','isQuickFreeModalOpen'])

const roomStore = useRoomStore()
// const room = ref([])
const isEnterRoom = ref(false)



const emit = defineEmits()

let roomId = null





const openEnterModalOpen = (id) => {
    roomId = id
    isEnterRoom.value =  true
}

const closeEnterModal = () => {
    isEnterRoom.value =  false
}




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