<template>
  <div v-if="isModal" class="modal" @click.self="closeModal">
    <div class="modal-content" @click.stop>
      <div class="d-flex justify-content-between">
        <h3>문제 {{ question.id }}번</h3>
        <button class="btn btn-danger" @click="closeModal">X</button>
      </div>
      <div>
        <p>Q. {{ question.question }}</p>
        <form @submit.prevent="onSubmit">
          <label for="ans">A.</label>
          <input type="text" id="ans" v-model="ans">
          <button class="btn btn-success" :disabled="answer===ans">{{ answer.length > 0 ? '수정' : '작성' }}</button>
        </form>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { ref, watch } from 'vue'
const props = defineProps({
  isModal: Boolean,
  question: Object,
  answer: String,
})

const emit = defineEmits(['closeModal', 'addAnswer'])

const ans = ref('')

const closeModal = function () {
  if (props.answer !== ans.value) {
    if(window.confirm('답변에 변경사항이 있습니다. 저장하지 않고 나가시겠습니까?')) {
      emit('closeModal')
    }
  } else {
    emit('closeModal')
  }
}

watch([() => props.isModal, () => props.answer], ([newIsModal, newAnswer], [oldIsModal, oldAnswer]) => {
  if (newIsModal && newIsModal !== oldIsModal) {
    ans.value = newAnswer // Reset ans to the new answer when the modal is opened
  }
}, {
  immediate: true, // This ensures the watcher is triggered immediately upon component mount
})

const onSubmit = function () {
  emit('addAnswer', props.question.id, ans.value)
  // axios 요청
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
.modal-content {
width : 800px;
}
</style>