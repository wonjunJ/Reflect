<template>
    <div class="modal" v-if="isOpen">
      <div class="modal-content">
        <h3>방 만들기</h3>
        <!-- 모달 내용 -->
        <div>


        </div>

        <!-- 모달 닫기 버튼 -->
        <button @click="closeModal" class="btn btn-secondary">닫기</button>
      </div>
    </div>
  

</template>

<script setup>
import { ref } from 'vue';


const isOpen = ref(true)

const closeModal = () => {
  isOpen.value = false
}


</script>

<style scoped>
body {
  margin : 0;
}
div {
  box-sizing: border-box;
}
.modal {
  width: 100%; height:100%;
  background: rgba(0,0,0,0.5);
  position: fixed; padding: 20px;
}
.modal-content {
  width: 100%; background: white;
  border-radius: 8px;
  padding: 20px;
} 

</style>