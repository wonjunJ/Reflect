<template>
  <div>
    <h1>100문 100답 페이지</h1>
    <h3>진행률 : {{ completedQuestions.length }} / 100</h3>
    <div class="progress" role="progressbar" aria-label="Basic example" :aria-valuenow="completedQuestions.length" aria-valuemin="0" aria-valuemax="100">
      <div class="progress-bar" :style="{width: (completedQuestions.length)+ '%'}"></div>
    </div>
    <div v-for="(answer, index) of answerList" :key="answer.id">
      <p>{{ answer.id }}. {{ questionlist[index].question }}</p>
      <form @submit.prevent="submitAnswer(answer.id)">
        <input type="text" v-model="answer.answer">
        <button class="btn btn-success" :disabled="(answer.answer.trim() === answer.initAnswer) || (answer.answer.trim().length === 0)">{{answer.initAnswer ? '수정' : '작성'}}</button>
        <span v-if="answer.initAnswer && answer.answer.length===0" style="color: red;">내용이 없으면 저장할 수 없습니다.</span>
      </form>
      <button v-if="answer.initAnswer" class="btn btn-danger" @click="deleteAnswer(answer.id)">삭제</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import { useQuestionsStore } from '@/stores/questions'
import axios from 'axios'

const userStore = useUserStore()
const API_URL = userStore.API_URL
const questionsStore = useQuestionsStore()

const completedQuestions = ref([])

// 답 저장용 배열 만들기
const createArray = function () {
  const emptyArray = []
  for (let id = 1; id <= 100; id++) {
    emptyArray.push({ id, answer: '', initAnswer: '' })
  }
  return emptyArray
}

const answerList = ref(createArray())

const submitAnswer = function (id) {
  if (answerList.value[id-1].initAnswer) {
    putAnswer(answerList.value[id-1])
  } else {
    postAnswer(answerList.value[id-1])
  }
}

const putAnswer = function (obj) {
  axios({
    method: 'put',
    url: `${API_URL}/question/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: {
      questionId: obj.id,
      answer: obj.answer
    }
  })
    .then(res => {
      completedQuestions.value.find(item => item.questionId === res.data.questionId).answer = res.data.answer
    })
    .catch(err => {
      console.log(err)
    })
}

const postAnswer = function (obj) {
  axios({
    method: 'post',
    url: `${API_URL}/question/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: {
      questionId: obj.id,
      answer: obj.answer
    }
  })
    .then(res => {
      completedQuestions.value.push(res.data)
    })
    .catch(err => {
      console.log(err)
    })
}

const deleteAnswer = function (id) {
  axios({
    method: 'delete',
    url: `${API_URL}/question/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: {
      questionId: id
    }
  })
    .then(res => {
      const idx = completedQuestions.value.findIndex(item => item.questionId === id)
      if (idx !== -1) {
        completedQuestions.value.splice(idx, 1)
      }
    })
    .catch(err => {
      console.log(err)
    })
}


onMounted(() => {
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
    })
})

watch(completedQuestions, (newCompletedQuestions) => {
  newCompletedQuestions.forEach((element) => {
    answerList.value[element.questionId - 1].answer = element.answer
    answerList.value[element.questionId - 1].initAnswer = element.answer
  })
}, {deep: true})

const questionlist = questionsStore.questions
</script>

<style>

</style>