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
            <ChartDoughnut :chart-data="doughnutChartData" :key="selectedPeriod" />
            <div class="button-section">
              <button class="option-btn " :disabled="selectedPeriod === 7" @click="selectPeriod(7)">
                7일
              </button>
              <button class="option-btn" :disabled="selectedPeriod === 14" @click="selectPeriod(14)">
                14일
              </button>
              <button class="option-btn " :disabled="selectedPeriod === 28" @click="selectPeriod(28)">
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
            <ChartLine :chart-data="lineChartData" />
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
            <DiaryDetailList :diarys="filteredDiarys" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { onMounted, ref } from 'vue'
import ChartDoughnut from '@/components/charts/ChartDoughnut.vue'
import ChartLine from '@/components/charts/ChartLine.vue'
import DiaryDetailList from './components/DiaryDetailList.vue'

// 선택된 기간과 감정코드
const selectedPeriod = ref(7)
const selectedEmotion = ref(1)

// 도넛 그래프 데이터 (datasets[0].data 바꿔줘야함)
const doughnutChartData = ref({
  labels: ['Happy', 'Sad', 'Angry'],
  datasets: [
    {
      label: 'Feelings',
      backgroundColor: ['#f87979', '#81C784', '#64B5F6'],
      borderColor: '#AAD9BB',
      data: [4, 2, 1]
    }
  ]
})

// 기간 선택 함수
const selectPeriod = function (num) {
  selectedPeriod.value = num
  // 기간 바뀌면 서버에 요청 보내서 datasets[0].data 바꿔주기
  if (num === 7) {
    doughnutChartData.value.datasets[0].data = [4, 2, 1]
  } else if (num === 14) {
    doughnutChartData.value.datasets[0].data = [9, 3, 2]
  } else {
    doughnutChartData.value.datasets[0].data = [20, 3, 5]
  }
}

// 선형 그래프 데이터 (서버에서 datasets[0].data 받아오기)
const lineChartData = ref({
  labels: ['01-01', '01-02', '01-07', '01-29'],
  datasets: [
    {
      label: 'feelings',
      borderColor: '#f87979',
      fill: false,
      data: [45, -20, 50, 20]
    }
  ]
})

const filteredDiarys = ref([])

// 일기 목록 감정 선택
const selectEmotion = function (num) {
  selectedEmotion.value = num
  // axios 요청으로 일기 불러오고 바꾸기
  filteredDiarys.value = dummyDiarys.filter(diary => diary.emitionCode === num)
}

// 일기 더미데이터
const dummyDiarys = [
  { id: 1, date: '2024-01-01', emitionCode: 1, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 2, date: '2024-01-02', emitionCode: 2, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 3, date: '2024-01-03', emitionCode: 5, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 4, date: '2024-01-05', emitionCode: 4, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 5, date: '2024-01-06', emitionCode: 1, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 6, date: '2024-01-07', emitionCode: 3, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 7, date: '2024-01-10', emitionCode: 2, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
]

onMounted(() => {
  selectEmotion(1)
})
</script>
  
<style scoped>
@import '../../css/section.css';
@import '../../css/component.css';
</style>