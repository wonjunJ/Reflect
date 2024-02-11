<template>
  <div>
    <h1>Feeling</h1>
    <div class="d-flex">
      <div class="d-flex flex-column">
        <div>
          <h2>지난 며칠간의 감정 그래프</h2>
          <div>
            <button class="btn btn-primary"
              :disabled="selectedPeriod === 7"
              @click="selectPeriod(7)">
              7일
            </button>
            <button class="btn btn-primary"
              :disabled="selectedPeriod === 14"
              @click="selectPeriod(14)">
              14일
            </button>
            <button class="btn btn-primary"
              :disabled="selectedPeriod === 28"
              @click="selectPeriod(28)">
              28일
            </button>
          </div>
          <div v-if="!isPieChartData">일기에서 추출된 감정이 없습니다.</div>
          <div v-if="isPieChart">
            <ChartDoughnut :chart-data="doughnutChartData" :key="selectedPeriod"/>
          </div>
        </div>
        <div>
          <h2>감정 추이 변화 그래프</h2>
          <div v-if="!isLineChartData">일기에서 추출된 감정이 없습니다.</div>
          <div v-if="isLineChart">
            <ChartLine :chart-data="lineChartData"/>
          </div>
        </div>
      </div>
      <div>
        <h2>감정 별 일기</h2>
        <div>
        <!-- 버튼 목록 -->
          <button class="btn btn-primary"
            :disabled="selectedEmotion===1"
            @click="selectEmotion(1)">
            Happy
          </button>
          <button class="btn btn-primary"
            :disabled="selectedEmotion===2"
            @click="selectEmotion(2)">
            Joy
          </button>
          <button class="btn btn-primary"
            :disabled="selectedEmotion===3"
            @click="selectEmotion(3)">
            Sad
          </button>
          <button class="btn btn-primary"
            :disabled="selectedEmotion===4"
            @click="selectEmotion(4)">
            Anger
          </button>
          <button class="btn btn-primary"
            :disabled="selectedEmotion===5"
            @click="selectEmotion(5)">
            Disgust
          </button>
        </div>
        <div>
          <!-- 일기 목록 컴포넌트 따로 만들고 MBTI 일기랑 같이쓸까함 -->
          <DiaryDetailList :diarys="filteredDiarys" :paging-info="pagingInfo" @change-page="changePage"/>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { onMounted, ref, computed } from 'vue'
import { useUserStore } from '@/stores/user.js'
import axios from 'axios'
import ChartDoughnut from '@/components/charts/ChartDoughnut.vue'
import ChartLine from '@/components/charts/ChartLine.vue'
import DiaryDetailList from './components/DiaryDetailList.vue'

const userStore = useUserStore()
const API_URL = userStore.API_URL
  
const emotionData = ref({})
// 선택된 기간과 감정코드
const selectedPeriod = ref(7)
const selectedEmotion = ref(1)
const pageNum = ref(1)
const isPieChart = ref(false)
const isLineChart = ref(false)
const isPieChartData = ref(true)
const isLineChartData = ref(true)
  
// 도넛 그래프 데이터
const doughnutChartData = ref({
  labels: ['Happy', 'Joy', 'Sad', 'Anger', 'Disgust'],
  datasets: [
    {
      label: 'Feelings',
      backgroundColor: ['#81C784', '#f5ed6e', '#64B5F6', '#d52e29', '#7c4ba4'],
      data: [0, 0, 0,]
    }
  ]
})

const sumArray = function (array) {
  let sum = 0
  array.forEach(element => {
    if (element !== null) {
      sum += element
    }
  })
  return sum
}

// 기간 선택 함수
const selectPeriod = function (num) {
  selectedPeriod.value = num
  // 기간 바뀌면 서버에 요청 보내서 datasets[0].data 바꿔주기
  if (num === 7) {
    const data = emotionData.value["7days"]
    delete data.none
    doughnutChartData.value.datasets[0].data = Object.values(emotionData.value["7days"])
    if (sumArray(Object.values(emotionData.value["7days"])) === 0) {
      isPieChartData.value = false
    }
  } else if (num === 14) {
    const data = emotionData.value["14days"]
    delete data.none
    doughnutChartData.value.datasets[0].data = Object.values(emotionData.value["14days"])
    if (sumArray(Object.values(emotionData.value["14days"])) === 0) {
      isPieChartData.value = false
    }
  } else {
    const data = emotionData.value["1month"]
    delete data.none
    doughnutChartData.value.datasets[0].data = Object.values(emotionData.value["1month"])
    if (sumArray(Object.values(emotionData.value["1month"])) === 0) {
      isPieChartData.value = false
    }
  }
}

const lineData = ref([
  { date: "2024-02-01", score: 50 },
  { date: "2024-02-02", score: 100 },
  { date: "2024-02-03", score: 0 },
  { date: "2024-02-04", score: 25 },
  { date: "2024-02-05", score: 75 },
])

const lineChartData = computed(() => {
  let labels = []
  let data = []
  let sum = 0
  lineData.value.forEach(item => {
    sum += item.score
    labels.push(item.date)
    data.push(2 * item.score - 100)
  })
  if (sum === 0) {
    isLineChartData.value = false
  }
  return {
    labels,
    datasets: [
      {
        label: 'feelings',
        borderColor: '#f87979',
        fill: false,
        data
      }
    ]
  }
})
  
const filteredDiarys = ref([])
const pagingInfo = ref({})

const changePage = function (num) {
  pageNum.value = num
  selectedEmotion(selectedEmotion.value)
}
  
// 일기 목록 감정 선택
const selectEmotion = function (num) {
  selectedEmotion.value = num
  axios({
    method: 'get',
    url: `${API_URL}/emotion/${selectedEmotion.value}?page=${pageNum.value}`,
    headers:{
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      filteredDiarys.value = res.data.data
      pagingInfo.value = res.data.pagingInfo
      selectedEmotion.value = num
    })
    .catch(err => {
      console.log(err)
    })
}
  
onMounted(() => {
  selectEmotion(1)
  axios({
    method: 'get',
    url: `${API_URL}/emotion/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      emotionData.value = res.data
      isPieChart.value = true
      selectPeriod(7)
    })
    .catch(err => {
      console.log(err)
    })
  
  axios({
    method: 'get',
    url: `${API_URL}/emotion/graph?recent=15`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      lineData.value = res.data
      isLineChart.value = true
    })
    .catch(err => {
      console.log(err)
    })
})
</script>
  
<style scoped>
  
</style>