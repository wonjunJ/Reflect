<template>
  <div v-if="isEnterRoom" class="modal">
    <div class="modal-content">
      <h1>채팅을 종료하시겠습니까?</h1>
      <button type="button" class="btn btn-secondary" @click="leaveSession">채팅 종료</button>
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
  id: Number
});
const emit = defineEmits();

const leaveSession = () => {
  // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
  if (this.session) this.session.disconnect();

  // Empty all properties...
  this.session = undefined;
  this.mainStreamManager = undefined;
  this.publisher = undefined;
  this.subscribers = [];
  this.OV = undefined;

  // Remove beforeunload listener
  window.removeEventListener("beforeunload", this.leaveSession);
  this.$router.push({ name: 'mypage' });
};



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
