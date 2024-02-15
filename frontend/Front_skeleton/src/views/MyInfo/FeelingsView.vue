<template>
  <div>

    <div class="d-flex">
      <!--aside 화면 -->
      <div class="aside-section">
        <div class="content-section">
          <div class="sub-title-section">
            <p>지난 며칠간의 감정 그래프</p>
          </div>
          <div class="body-section">
            <div v-if="!isPieChartData">일기에서 추출된 감정이 없습니다.</div>
            <div v-if="isPieChart">
              <ChartDoughnut :chart-data="doughnutChartData" :key="selectedPeriod" />
            </div>
            <div class="button-section">
              <button class="btn btn-primary" :disabled="selectedPeriod === 7" @click="selectPeriod(7)">
                7일
              </button>
              <button class="btn btn-primary mx-2" :disabled="selectedPeriod === 14" @click="selectPeriod(14)">
                14일
              </button>
              <button class="btn btn-primary" :disabled="selectedPeriod === 28" @click="selectPeriod(28)">
                28일
              </button>
            </div>
          </div>
        </div>
        <div class="content-section">
          <div class="sub-title-section">
            <p>감정 추이 변화 그래프</p>
          </div>
          <div class="body-section">
            <div v-if="!isLineChartData">일기에서 추출된 감정이 없습니다.</div>
            <div v-if="isLineChart">
              <ChartLine :chart-data="lineChartData" />
            </div>

          </div>
        </div>
      </div>

      <!--main 화면-->
      <div class="main-section">
        <div class="content-section">
          <div class="title-section">
            <p>감정 별 일기</p>
          </div>
          <div class="button-section">
            <button class="option-btn " :disabled="selectedEmotion === 1" @click="selectEmotion(1)">
              Happy
            </button>
            <button class="option-btn  " :disabled="selectedEmotion === 2" @click="selectEmotion(2)">
              Joy
            </button>
            <button class="option-btn " :disabled="selectedEmotion === 3" @click="selectEmotion(3)">
              Sad
            </button>
            <button class="option-btn  " :disabled="selectedEmotion === 4" @click="selectEmotion(4)">
              Anger
            </button>
            <button class="option-btn " :disabled="selectedEmotion === 5" @click="selectEmotion(5)">
              Disgust
            </button>
          </div>
          <div class="body-section">
            <DiaryDetailList :diarys="filteredDiarys" :paging-info="pagingInfo" @change-page="changePage" />
          </div>

        </div>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { onMounted, ref, computed, watch } from 'vue'
import { useUserStore } from '@/stores/user.js'
import axios from 'axios'
import ChartDoughnut from '@/components/charts/ChartDoughnut.vue'
import ChartLine from '@/components/charts/ChartLine.vue'
import DiaryDetailList from './components/DiaryDetailList.vue'

const userStore = useUserStore()
const API_URL = userStore.API_URL

const emotionData = ref({})           // 감정 정보
const selectedPeriod = ref(7)         // 선택한 감정 그래프 기간
const selectedEmotion = ref(1)        // 선택한 감정 코드
const pageNum = ref(1)                // 감정 별 일기 페이지네이션 페이지 번호
const isPieChart = ref(false)         // 파이차트 랜더링 여부 - 데이터 요청 전 랜더링 방지
const isLineChart = ref(false)        // 라인차트 랜더링 여부 - 데이터 요청 전 랜더링 방지
const isPieChartData = ref(true)      // 파이차트 데이터 유무 판단
const isLineChartData = ref(true)     // 라인차트 데이터 유무 판단

// 도넛 그래프 데이터
const doughnutChartData = ref({
  labels: ['Happy', 'Joy', 'Sad', 'Anger', 'Disgust'],
  datasets: [
    {
      label: 'Feelings',
      backgroundColor: ['#81C784', '#f5ed6e', '#64B5F6', '#d52e29', '#7c4ba4'],
      borderColor: '#AAD9BB',
      data: [0, 0, 0,]
    }
  ]
})

// 배열의 합 계산 - 차트용 데이터 유무 판단 용
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
  let data = emotionData.value["7days"]
  if (num === 14) {
    data = emotionData.value["14days"]
  } else if (num === 28) {
    data = emotionData.value["1month"]
  }
  delete data.none
  doughnutChartData.value.datasets[0].data = Object.values(data);

  if (sumArray(Object.values(data)) === 0) {
    isPieChartData.value = false;
  }
}

// 라인차트용 데이터
const lineData = ref([])

// chart js 용 데이터 변환
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
    labels: labels.reverse(),
    datasets: [
      {
        label: 'feelings',
        borderColor: '#f87979',
        fill: false,
        data: data.reverse()
      }
    ]
  }
})

const filteredDiarys = ref([])  // 감정별 일기 목록
const pagingInfo = ref({})      // 일기 목록 페이지네이션 정보

// 페이지가 바뀐 경우 새로운 일기 목록 가져오기
const changePage = function (num) {
  pageNum.value = num
  selectEmotion(selectedEmotion.value)
}

// 선택된 감정이 바뀐 경우 페이지 1로 초기화
watch(selectedEmotion, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    pageNum.value = 1
  }
})

// 일기 목록 감정 선택
const selectEmotion = function (num) {
  selectedEmotion.value = num
  axios({
    method: 'get',
    url: `${API_URL}/emotion/${selectedEmotion.value}?page=${pageNum.value}`,
    headers: {
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
      if (err.response.status === 401) {
        userStore.refresh()
        selectEmotion(num)
      }
    })
}

// 감정 분포 그래프 정보 요청 함수
const getEmotionInfo = function () {
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
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        getEmotionInfo()
      }
    })
}

// 감정 변화 추이 그래프 정보 요청 함수
const getGraphInfo = function () {
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
      if (err.response.status === 401) {
        userStore.refresh()
        getGraphInfo()
      }
    })
}

// selectPeriod(7)이 순서가 꼬이는 문제가 발생해 getEmotionInfo 함수 내용을 일일히 입력함
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
      if (err.response.status === 401) {
        userStore.refresh()
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
      }
    })

  getGraphInfo()
})
</script>
  
<style scoped>
@import '../../css/section.css';
@import '../../css/component.css';
</style>