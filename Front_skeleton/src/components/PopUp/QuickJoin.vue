<template>
    <div v-if="isQuickModalOpen" class="modal">
        <!-- <div v-if="isQuickHundredModalOpen || isQuickFreeModalOpen" class="modal"> -->
        <div class="modal-content">
            <div class="modal-header">
                <p>랜덤 방 참여</p>
                <button @click="closeModal">X</button>
            </div>
            <div class="modal-body">
                <p>랜덤 방으로 참여하시겠습니까?</p>
                <div class="button-section">
                    <!-- 백문백답일 경우와 자유주제일 경우 -->

                    <button type="button" class="option-btn"
                        @click="openEnterModalOpen(props.recRooms[0].roomId)">참여하기</button>
                    <button type="button" class="option-btn" @click="closeModal">닫기</button>
                </div>

            </div>
        </div>
        <RoomEnter :isEnterRoom="isEnterRoom" :id="roomId" @closeModal="closeEnterModal" />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
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
    isEnterRoom.value = true
}

const closeEnterModal = () => {
    isEnterRoom.value = false
}




const closeModal = () => {
    emit('closeModal')
}



</script>

<style scoped>
@import '../../css/section.css';
@import '../../css/modal.css';
@import '../../css/component.css';
</style>