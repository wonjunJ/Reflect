<template>
  <div>
    <div class="d-flex flew-row">

      <div class="aside-section ">
        <div class="content-section">
          <div class="sub-title-section">
            <p>진행률 : {{ completedQuestions.length }} / 100</p>
            <div class=" progress" role="progressbar" aria-label="Basic example"
              :aria-valuenow="completedQuestions.length" aria-valuemin="0" aria-valuemax="100">
              <div class="progress-bar" :style="{ width: (completedQuestions.length) + '%' }"></div>
            </div>
          </div>

          <div class="box-section">
            <div v-for="row in rows" :key="row" class="row-section ">
              <div v-for="column in columns" :key="row * 20 + column">
                <div class="box" :class="{ 'box': true }">
                  <p class="tip"> {{ row * 20 + column }} </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="main-section " style="overflow:scroll; height:800px">
        <div class="title-section">
          <p>100문 100답</p>
        </div>

        <div v-for="(answer, index) of answerList" :key="answer.id" :class="'content' + (answer.id)">
          <p>{{ answer.id }}. {{ questionlist[index].question }}</p>
          <form @submit.prevent="submitAnswer(answer.id)">
            <input type="text" v-model="answer.answer">
            <button class="btn btn-success"
              :disabled="(answer.answer.trim() === answer.initAnswer) || (answer.answer.trim().length === 0)">{{
                answer.initAnswer
                ? '수정' : '작성' }}</button>
            <span v-if="answer.initAnswer && answer.answer.length === 0" style="color: red;">내용이 없으면 저장할 수 없습니다.</span>
          </form>
          <button v-if="answer.initAnswer" class="btn btn-danger" @click="deleteAnswer(answer.id)">삭제</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import { useQuestionsStore } from '@/stores/questions'
import QandA from './components/QandA.vue'
import axios from 'axios'



const userStore = useUserStore()
const API_URL = userStore.API_URL
const questionsStore = useQuestionsStore()

// 답변한 질문 목록
const completedQuestions = ref([])

// const contents = document.querySelectorAll(".content")
// const boxs = document.querySelectorAll(".box")

const rows = [0, 1, 2, 3, 4]
const columns = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,]

// const scrollToDiv = (divNumber) => {
//   console.log("============================" + (i + 1));
//   const refName = `div${divNumber}`;
//   const targetDiv = ref(refName);
//   if (targetDiv.value) {
//     targetDiv.value.scrollIntoView({ behavior: 'smooth' });
//   }
// }

// const moveQuestion = function (i) {
//   console.log("============================" + (i + 1));
//   $('.main-section').scroll({ top: contents[i + 1], behavior: 'smooth' });

// }

// 답 저장용 배열 만들기
const createArray = function () {
  const emptyArray = []
  for (let id = 1; id <= 100; id++) {
    emptyArray.push({ id, answer: '', initAnswer: '' })
  }
  return emptyArray
}

// 전체 답변 배열
const answerList = ref(createArray())

// 전체 답변 배열과 답변한 질문 동기화 
const refreshAnswerList = function () {
  completedQuestions.value.forEach((element) => {
    answerList.value[element.questionId - 1].answer = element.answer
    answerList.value[element.questionId - 1].initAnswer = element.answer
  })
}

// 답변 수정 및 작성 중계 함수
const submitAnswer = function (id) {
  if (answerList.value[id - 1].initAnswer) {
    putAnswer(answerList.value[id - 1])
  } else {
    postAnswer(answerList.value[id - 1])
  }
}

// 답변 수정 함수
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
      const id = res.data.questionId
      completedQuestions.value.find(item => item.questionId === res.data.questionId).answer = res.data.answer
      answerList.value[id - 1].answer = res.data.answer
      answerList.value[id - 1].initAnswer = res.data.answer
    })
    .catch(err => {
      console.log(err)
      if (err.response.status == 401) {
        userStore.refresh()
        putAnswer(obj)
      }
    })
}

// 답변 작성 함수
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
      const id = res.data.questionId
      completedQuestions.value.find(item => item.questionId === res.data.questionId).answer = res.data.answer
      answerList.value[id - 1].answer = res.data.answer
      answerList.value[id - 1].initAnswer = res.data.answer
    })
    .catch(err => {
      console.log(err)
      if (err.response.status == 401) {
        userStore.refresh()
        postAnswer(obj)
      }
    })
}

// 답변 삭제 함수
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
      answerList.value[id - 1].answer = ''
      answerList.value[id - 1].initAnswer = ''
    })
    .catch(err => {
      console.log(err)
      if (err.response.status == 401) {
        userStore.refresh()
        deleteAnswer(id)
      }
    })
}

// 답변한 질문 목록 요청 함수
const getAnswers = function () {
  axios({
    method: 'get',
    url: `${API_URL}/question/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      completedQuestions.value = res.data
      refreshAnswerList()
    })
    .catch(err => {
      console.log(err)
      userStore.refresh()
      getAnswers()
    })
}

//여기 이상할 수 있음
watch(completedQuestions, (newCompletedQuestions) => {
  newCompletedQuestions.forEach((element) => {
    answerList.value[element.questionId - 1].answer = element.answer
    answerList.value[element.questionId - 1].initAnswer = element.answer
  })
}, { deep: true })

//여기 까지 

onMounted(() => {
  getAnswers()
})

const questionlist = questionsStore.questions
</script>

<style scoped>
@import '../../css/section.css';

.progress-bar {
  background-color: red;
}

.box {
  height: 20px;
  width: 20px;
  margin: 2px;
  border-radius: 20%;
  background-color: white;
}

.row-section {
  display: flex;
  flex-direction: row;
  justify-content: center;
}

.sticky {
  position: sticky;
  top: 0;
  z-index: 1;
  background-color: #80bcbd;
}

.question {
  width: 80%;
}

input[type="text"] {
  width: 80%;
  border: 1px solid #aad9bb;
  background-color: #d5f0c1;
}
</style>