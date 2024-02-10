<template>
    <div>
        <!-- <RoomList 
            v-for="room in rooms"
            :key="room.id"
            :room="room"
        /> -->
    </div>
</template>

<script setup>
import { useRoomStore } from '@/stores/room';
import RoomList from '@/components/PopUp/RoomList.vue'

// const roomStore = useRoomStore()
// onMounted(() =>{
//     roomStore.getFilteredRooms()
// })
// const rooms = roomStore.filteredRooms




</script>

<style scoped>

</style>