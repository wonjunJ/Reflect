<template>
    <div>
        <RoomList 
            v-for="room in rooms"
            :key="room.id"
            :room="room"
        />
    </div>
</template>

<script setup>
import { useRoomStore } from '@/stores/room';
import RoomList from '@/components/PopUp/RoomList.vue'

const roomStore = useRoomStore()
const rooms = roomStore.value



</script>

<style scoped>

</style>