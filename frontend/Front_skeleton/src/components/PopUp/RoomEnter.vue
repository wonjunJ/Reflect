<template>
  <div v-if="isEnterRoom" class="modal">
    <div class="modal-content">
      <h1>방에 참여하시겠습니까?</h1>      
      <button type="button" class="btn btn-secondary" @click="participate">참여하기</button>
        <button type="button" class="btn btn-primary" @click="closeModal">닫기</button>
      </div>
    </div>
</template>
<script setup>
import { ref, defineProps, defineEmits } from 'vue';
import router from '@/router';
import { useRoomStore } from '@/stores/room';
import axios from 'axios';

const roomStore = useRoomStore()
const props = defineProps({
  isEnterRoom: Boolean,
  waitingId: Number
});
const emit = defineEmits();

const participateRoom = (id) => {
  const roomId = id
    axios({
      method: 'post',
      url: `${roomStore.API_URL}/room/attend`,
      headers : {
        Authorization: `Bearer ${roomStore.token}`
      },
      data : {id:roomId}
    })
    .then((res) => {
        console.log(res.data)
        console.log('방 참가 완료')
      })
    .catch((err) => {
      console.log(err)
      console.log('방 참가 실패')
    })
}

const participate = () => {
  participateRoom(props.waitingId)
  router.push({ name: 'waiting', params:{id:props.waitingId}})
}

const closeModal = () => {
  emit('closeModal');
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
</style>
