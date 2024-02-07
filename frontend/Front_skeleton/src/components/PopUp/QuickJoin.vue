<template>
    <div v-if="isRandomRoom" class="modal">
        <div class="modal-content">
            
            <h1>랜덤 방으로 참여하시겠습니까?</h1>

            <div>
                <RouterLink :to="{name:'roomdetail'}" class="btn">참여하기</RouterLink>
                <button type="button" class="btn btn-primary" @click="closeModal">닫기</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import {ref, defineEmits, defineProps} from 'vue';
import { RouterLink } from 'vue-router';

const { isRandomRoom } = defineProps(['isRandomRoom'])
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