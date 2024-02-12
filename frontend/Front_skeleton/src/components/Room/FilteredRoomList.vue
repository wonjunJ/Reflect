<template>
    <div class="all-room-container">
        <div class="row row-cols-1 row-cols-md-5 g-4">
        <div class="col" v-for="room in rooms" :key="room.room_id">
            <div class="card h-100">
                <img src="data:image/;base64,+{{ room.thumbnail }}" class="card-img-top" alt="방 썸네일">
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
    <RoomEnter :isEnterRoom="isEnterRoom" @closeModal="closeModal"/>
    </div>
</template>

<script setup>
import { useRoomStore } from '@/stores/room';
import { ref } from 'vue';
import RoomEnter from '@/components/PopUp/RoomEnter.vue';
import { onMounted } from 'vue';
const props = defineProps({
  type: String,
  waiting: String,
  from: Number,
  to: Number
});





const roomStore = useRoomStore()
onMounted(() =>{
    // roomStore.num1 = props.limit[0]
    // roomStore.num2 = props.limit[1]
    const from = props.from
    const to = props.to
    const type = props.type
    const waiting = props.waiting
    const filter = {type, waiting}
    roomStore.getFilteredRooms(filter, from, to, pageable)
})
const rooms = roomStore.filteredRooms

const isEnterRoom = ref(false)

const openEnterModalOpen = () => {
isEnterRoom.value =  true
}

const closeModal = () => {
isEnterRoom.value = false
}




</script>

<style scoped>

</style>