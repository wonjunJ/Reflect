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

const props = defineProps({
  isEnterRoom: Boolean,
  waitingId: Number
});
const emit = defineEmits();

const participate = () => {
  router.push({path: `/waiting/${props.waitingId}`, params:{id:waitingId}})
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
