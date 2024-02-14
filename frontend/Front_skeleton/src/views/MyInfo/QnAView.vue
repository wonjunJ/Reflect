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

const answerList = ref(createArray())

const submitAnswer = function (id) {
  if (answerList.value[id - 1].initAnswer) {
    putAnswer(answerList.value[id - 1])
  } else {
    postAnswer(answerList.value[id - 1])
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
}, { deep: true })

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
</style>