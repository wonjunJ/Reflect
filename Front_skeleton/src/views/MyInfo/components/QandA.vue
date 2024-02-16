<template>
  <div class="qna-section">
    <div class="subtitle-section">
      <p class="subtitle"> 100문 100답 </p>
      <button @click="router.push({ name: '100QnA' })">Detail</button>
    </div>

    <div class="box-section">
      <div v-for="row in rows" :key="row" class="row-section ">
        <div v-for="column in columns" :key="row * 25 + column">
          <div :class="{ 'box': true, 'green': isCompleted(row * 25 + column + 1) }"
            @click="openModal(row * 25 + column + 1)">
          </div>
        </div>
      </div>
    </div>
  </div>

  <QnA :is-modal="isModal" :question="questionlist[selectedNumber - 1]"
    :answer="isCompleted(selectedNumber) ? selectedAnswer.answer : ''" @add-answer="addAnswer" @edit-answer="editAnswer"
    @delete-answer="deleteAnswer" @close-modal="closeModal" />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useQuestionsStore } from '@/stores/questions'
import axios from 'axios'
import QnA from '@/components/PopUp/100QA.vue'

const userStore = useUserStore()
const API_URL = userStore.API_URL
const questionsStore = useQuestionsStore()
const router = useRouter()

// 답변 끝낸 질문 목록
const completedQuestions = ref([])

const rows = [0, 1, 2, 3]
const columns = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]

const isModal = ref(false)
const selectedNumber = ref(0) // 선택된 질문 번호

// 답변이 있는지 없는지 확인하는 함수
const isCompleted = function (key) {
  return completedQuestions.value.some(item => item.questionId === key) && completedQuestions.value.find(item => item.questionId === key).answer
}
// 고른 숫자의 답변 찾기
const selectedAnswer = computed(() => {
  return completedQuestions.value.find(item => item.questionId === selectedNumber.value)
})

// 팝업에서 새로 답변을 한 경우
const addAnswer = function (object) {
  completedQuestions.value.push(object)
}

// 팝업에서 답변을 수정한 경우
const editAnswer = function (id, answer) {
  completedQuestions.value.find(item => item.questionId === id).answer = answer
}

// 팝업에서 답변을 삭제한 경우
const deleteAnswer = function (id) {
  const idx = completedQuestions.value.findIndex(item => item.questionId === id)
  if (idx !== -1) {
    completedQuestions.value.splice(idx, 1)
  }
}

const openModal = function (num) {
  selectedNumber.value = num
  isModal.value = true
}

const closeModal = function () {
  isModal.value = false
}

// 질문 목록
const questionlist = questionsStore.questions

// 답변한 질문 목록 요청 함수
const getQuestions = function () {
  axios({
    method: 'get',
    url: `${API_URL}/question/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      completedQuestions.value = res.data
    })
    .catch(err => {
      console.log(err)
      if (err.response.statuss === 401) {
        userStore.refresh()
        getQuestions()
      }
    })
}

onMounted(() => {
  getQuestions()
})
</script>

<style scoped>
.subtitle-section {
  font-weight: bold;
  display: flex;
  align-content: center;

}

.subtitle {
  font-size: 30px;
  font-family: "YJ_Obang";
}

.box {
  height: 45px;
  width: 45px;
  margin: 5px;
  border-radius: 20%;
  background-color: rgb(210, 210, 210);
  cursor: pointer;
}

.box:hover {
  transform: scale(1.1);
}

.green {
  background-color: greenyellow;
}

button {
  font-size: 20px;
  width: 100px;
  border-radius: 10px;
  margin: 10px;
  background-color: #F9F7C9;
}

.box-section {
  margin-top: 30px;
  margin-bottom: 30px;
}

.row-section {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
</style>