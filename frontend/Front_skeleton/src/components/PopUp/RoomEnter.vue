<template>
    <div v-if="openEnterModalOpen" class="modal">
        <h1>방 참여 확인 팝업</h1>
        
        <h1>방에 참여하시겠습니까?</h1>

        <div>
            <RouterLink :to="{name:'roomdetail'}" class="btn">참여하기</RouterLink>
            <button type="button" class="btn btn-primary" @click="closeModal">닫기</button>
        </div>
    </div>
</template>

<script setup>
import {ref, defineEmits, defineProps} from vue;
import { RouterLink } from 'vue-router';

const { openEnterModalOpen } = defineProps(['openEnterModalOpen'])
const emit = defineEmits()


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

</style>