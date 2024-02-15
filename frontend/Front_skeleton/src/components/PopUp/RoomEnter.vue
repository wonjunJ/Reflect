<template>
  <div v-if="isEnterRoom" class="modal">
    <div class="modal-content">
      <div class="modal-header">
        <p>방에 참여하시겠습니까?</p>
      </div>
      <div class="button-section">
        <button class="option-btn" type="button" @click="participate">참여하기</button>
        <button class="option-btn" type="button" @click="closeModal">닫기</button>
      </div>

    </div>
  </div>
</template>
<script setup>
import { ref, defineProps, defineEmits, onMounted } from 'vue';
import router from '@/router';
import { useRoomStore } from '@/stores/room';
import { useUserStore } from '@/stores/user.js';

import axios from 'axios';

const roomStore = useRoomStore()
const props = defineProps({
  isEnterRoom: Boolean,
  id: Number,
  roomTitle: String,
});
const emit = defineEmits();


const getRoomInfo = () => {
  axios({
    method: 'get',
    url: `${useRoomStore().API_URL}/room/detail/${this.roomId}`,
    headers: {
      Authorization: `Bearer ${useUserStore().accessToken}`
    }
  }).then(res => {
    console.log(roomTitle)
    roomTitle = res.data.roomTitle
    console.log(res.data)
    console.log(this.roomTitle)
  })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        useRoomStore().refresh()
      }
    })
}

const participateRoom = (id) => {
  const roomId = id
  axios({
    method: 'post',
    url: `${roomStore.API_URL}/room/attend`,
    headers: {
      Authorization: `Bearer ${roomStore.token}`
    },
    data: { id: roomId }
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
  participateRoom(props.id)
  router.push({ path: `/waiting/${props.id}`, params: { id: props.id } })
}

const closeModal = () => {
  emit('closeModal');
};
</script>

<style scoped>
@import '../../css/modal';
@import '../../css/section.css';
@import '../../css/component.css';
</style>
