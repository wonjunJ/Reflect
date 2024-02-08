<template>
    <div>
        <RoomList 
            v-for="room in rooms"
            :key="room.room_id"
            :room="room"
        />
    </div>
</template>

<script setup>
import { useRoomStore } from '@/stores/room';
import RoomList from '@/components/PopUp/RoomList.vue'

const roomStore = useRoomStore()
onMounted(() =>{
    roomStore.getMyRooms()
    console.log('내 방 목록 가져오기 성공')
})
const rooms = roomStore.myRooms



</script>

<style scoped>

</style>