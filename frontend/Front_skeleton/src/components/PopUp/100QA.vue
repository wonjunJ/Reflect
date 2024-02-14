<template>
  <div v-if="isModal" class="modal" @click.self="closeModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header d-flex flex-row justify-content-between">
        <p>문제 {{ question.id }}번</p>
        <button @click="closeModal">X</button>
      </div>

      <div>
        <p>Q. {{ question.question }}</p>
        <form @submit.prevent="submitAnswer">
          <label for="ans">A.</label>
          <input type="text" id="ans" v-model="ans">
          <button class="btn btn-success" :disabled="answer === ans.trim()">{{ answer.length > 0 ? '수정' : '작성' }}</button>
        </form>
        <button class="btn btn-danger" v-if="answer" @click="deleteAnswer">삭제</button>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { ref, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import axios from 'axios'

const userStore = useUserStore()
const API_URL = userStore.API_URL
const props = defineProps({
  isModal: Boolean,
  question: Object,
  answer: String,
})

const emit = defineEmits(['closeModal', 'addAnswer', 'editAnswer', 'deleteAnswer'])

const ans = ref('')

const closeModal = function () {
  if (props.answer !== ans.value) {
    if (window.confirm('답변에 변경사항이 있습니다. 저장하지 않고 나가시겠습니까?')) {
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

const submitAnswer = function () {
  if (props.answer.length > 0) {
    putAnswer()
  } else {
    postAnswer()
  }
}

const postAnswer = function () {
  if (ans.value.trim().length) {
    axios({
      method: 'post',
      url: `${API_URL}/question/`,
      headers: {
        Authorization: `Bearer ${userStore.accessToken}`
      },
      data: {
        questionId: props.question.id,
        answer: ans.value.trim()
      }
    })
      .then(res => {
        emit('addAnswer', res.data)
        emit('closeModal')
      })
      .catch(err => {
        console.log(err)
      })
  } else {
    window.alert('내용을 입력해야합니다.')
  }
}

const putAnswer = function () {
  if (ans.value.trim().length) {
    axios({
      method: 'put',
      url: `${API_URL}/question/`,
      headers: {
        Authorization: `Bearer ${userStore.accessToken}`
      },
      data: {
        questionId: props.question.id,
        answer: ans.value.trim()
      }
    })
      .then(res => {
        emit('editAnswer', props.question.id, ans.value.trim())
        emit('closeModal')
      })
      .catch(err => {
        console.log(err)
      })
  } else {
    window.alert('내용을 입력해야합니다.')
  }
}

const deleteAnswer = function () {
  axios({
    method: 'delete',
    url: `${API_URL}/question/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: {
      questionId: props.question.id
    }
  })
    .then(res => {
      emit('deleteAnswer', props.question.id)
      emit('closeModal')
    })
    .catch(err => {
      console.log(err)
    })
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
  width: 800px;
}

.modal-header {
  background-color: rgb(151, 150, 150, 0.5);
  box-shadow: 5px 5px 5px 5px "black";
  font-family: "YJ_Obang";
  font-size: 25px;
}
</style>