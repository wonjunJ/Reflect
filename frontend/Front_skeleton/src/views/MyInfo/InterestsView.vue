<template>
  <h1>Interest</h1>
  <div class="d-flex">
    <div class="d-flex flex-column">
      <div>
        <h2>관심사</h2>
        <WordCloud :data="wordCloudData"/>
      </div>
      <div>
        <h2>MBTI</h2>
        <h5>당신의 MBTI는 {{ mbti }}입니다.</h5>
        <div>
          <ChartRadar :chart-data="radarChartData"/>
        </div>
      </div>
      <div>
        <h3>다른 사람이 본 나의 MBTI</h3>
        <div>
          <ChartBar :chart-data="barChartData"/>
        </div>
      </div>
    </div>
    <div class="d-flex flex-column">
      <div>
        <h2>관심사 목록</h2>
        <div class="border">
          <form @submit.prevent="addInterest">
            <input type="text" v-model="interestForm.name">
            <select name="emotion" v-model="interestForm.emotion">
              <option value="1">Positive</option>
              <option value="0">Negative</option>
            </select>
            <button>submit</button>
          </form>
        </div>
        <div class="d-flex">
          <div class="d-flex flex-column">
            <h3>긍정</h3>
            <div 
              v-for="interest in interestsPositive" 
              :key="interest.id"
            >
              <p>{{ interest.name }} <button @click="deleteInterests(interest.id)">🗑</button></p>
            </div>
          </div>
          <div class="d-flex flex-column">
            <h3>부정</h3>
            <div 
              v-for="interest in interestsNegative" 
              :key="interest.id"
            >
              <p>{{ interest.name }} <button @click="deleteInterests(interest.id)">🗑</button></p>
            </div>
          </div>
        </div>
      </div>
      <div>
        <h2>MBTI Detail</h2>
        <div class="d-flex">
          <button 
            class="btn btn-primary"
            v-for="(label, code) in mbtiCodes"
            :key="code"
            :disabled="selectedMbti === Number(code)"
            @click="selectMbti(Number(code))"
          >
            {{ label }}
          </button>
        </div>
        <div>
          <!-- 일기 목록 -->
          <div v-for="diary in filteredDiarys" :key="diary.id">
            <p>{{ diary }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import ChartRadar from '@/components/charts/ChartRadar.vue'
import WordCloud from '@/components/WordCloud.vue'
import ChartBar from '@/components/charts/ChartBar.vue'

// 워드 클라우드용 더미 데이터 (axios 요청 받아서 이 형태로 처리 필요)
const wordCloudData = ref([
  ['Word1', 30],
  ['Word2', 20],
  ['Word3', 10],
  ['Word4', 7],
  ['Word5', 5],
  ['Word6', 3],
  ['Word7', 2],
])

// 레이더 차트 데이터 전달 사항 (datassets.data 부분을 받아와야함)
const radarChartData = ref({
  labels: ['I', 'N', 'T', 'P', 'E', 'S', 'F', 'J'],
  datasets: [
    {
    label: 'MBTI',
    backgroundColor: 'rgba(255, 99, 132, 0.2)',
    borderColor: 'rgba(255, 99, 132, 1)',
    pointBackgroundColor: 'rgba(255, 99, 132, 1)',
    data: [80, 75, 50, 70, 20, 25, 50, 30]
    }
  ]
})

const mbtiCodes = ref({   // 버튼 용 MBTI 속성 목록
  1:'I', 2:'N', 3:'T', 4:'P', 5:'E', 6:'S', 7:'F', 8:'J'
})

const filteredDiarys = ref([])  // props로 일기 목록에 전해줄 일기
const selectedMbti = ref(1)     // 선택된 MBTI 코드
const selectMbti = function (num) {   // MBTI 선택 함수
  selectedMbti.value = num
  // 선택되면 해당 MBTI 일기 요청받아오기 (지금 있는건 그냥 자리 채우기)
  filteredDiarys.value = dummyDiarys.filter(diary => diary.mbtiCode === num)
}

// 화면에 출력되는 사용자의 MBTI
const calculateMbti = function () {
  const data = radarChartData.value.datasets[0].data
  let temp = ['?', '?', '?', '?']
  if (data[0] > 50) {
    temp[0] = 'I'
  } else if (data[0] < 50) {
    temp[0] = 'E'
  }
  if (data[1] > 50) {
    temp[1] = 'N'
  } else if (data[1] < 50) {
    temp[1] = 'S'
  }
  if (data[2] > 50) {
    temp[2] = 'T'
  } else if (data[2] < 50) {
    temp[2] = 'F'
  }
  if (data[3] > 50) {
    temp[3] = 'P'
  } else if (data[3] < 50) {
    temp[3] = 'J'
  }
  return temp.join('')
}
const mbti = calculateMbti()

// 남이 보는 나의 MBTI 더미데이터
const mbtiOthers = ref({
  'INTP': 5, 'INTJ': 4, 'INFP': 1, 'INFJ': 3,
  'ISTP': 1, 'ISTJ': 0, 'ISFP': 0, 'ISFJ': 0,
  'ENTP': 2, 'ENTJ': 0, 'ENFP': 2, 'ENFJ': 0,
  'ESTP': 0, 'ESTJ': 0, 'ESFP': 0, 'ESFJ': 0,
})

const barChartData = computed(() => {
  const filteredData = Object.keys(mbtiOthers.value)
    .filter(key => mbtiOthers.value[key] > 0)
    .map(key => ({ label: key, data: mbtiOthers.value[key]}))
  filteredData.sort((a, b) => b.data - a.data)

  const labels = filteredData.map(item => item.label)
  const data = filteredData.map(item => item.data)

  return {
    labels,
    datasets: [
      {
        label: 'MBTI',
        backgroundColor: '#f87979',
        data,
      }
    ]
  }
})

// 더미데이터 
const dummyDiarys = [
  { id: 1, date: '2024-01-01', mbtiCode: 1, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 2, date: '2024-01-02', mbtiCode: 8, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 3, date: '2024-01-03', mbtiCode: 7, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 4, date: '2024-01-05', mbtiCode: 6, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 5, date: '2024-01-06', mbtiCode: 4, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 6, date: '2024-01-07', mbtiCode: 5, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 7, date: '2024-01-10', mbtiCode: 3, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 8, date: '2024-01-10', mbtiCode: 3, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 9, date: '2024-01-10', mbtiCode: 2, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
  { id: 10, date: '2024-01-10', mbtiCode: 1, content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', },
]

// 관심사 목록 더미데이터
const interests = ref([
  { id: 1, weight: 20, emotion: 1, name: '1', },
  { id: 2, weight: 17, emotion: 0, name: '2', },
  { id: 3, weight: 15, emotion: 0, name: '3', },
  { id: 4, weight: 13, emotion: 1, name: '4', },
  { id: 5, weight: 11, emotion: 0, name: '5', },
  { id: 6, weight: 11, emotion: 1, name: '6', },
  { id: 7, weight: 9, emotion: 0, name: '7', },
  { id: 8, weight: 8, emotion: 1, name: '8', },
  { id: 9, weight: 6, emotion: 1, name: '9', },
  { id: 10, weight: 4, emotion: 0, name: '10', },
  { id: 11, weight: 3, emotion: 1, name: '11' },
])

// 긍정 관심사 걸러내기
const interestsPositive = computed(() => {
  const filteredInterests = interests.value.filter(interest => interest.emotion === 1)
  filteredInterests.sort((a, b) => b.weight - a.weight)
  return filteredInterests
})

// 부정 관심사 걸러내기
const interestsNegative = computed(() => {
  const filteredInterests = interests.value.filter(interest => interest.emotion === 0)
  filteredInterests.sort((a, b) => b.weight - a.weight)
  return filteredInterests
})

// 새로운 관심사 항목 만들기용 입력 데이터
const interestForm = ref({
  name: '',
  emotion: '',
  id: 11
})

// 새로운 관심사 항목 만들기
const addInterest = function () {
  let weight = 0
  if (interestForm.value.emotion === '1') {
    if (interestsPositive.value.length > 3) {
      weight = interestsPositive.value[3].weight - 1
    } else if (interestsPositive.value.length !== 0) {
      weight = interestsPositive.value[interestsPositive.value.length-1] -1
    } else {
      weight = 10
    }
  } else {
    if (interestsNegative.value.length > 3) {
      weight = interestsNegative.value[3].weight - 1
    } else if (interestsNegative.value.length !== 0) {
      weight = interestsNegative.value[interestsNegative.value.length-1] -1
    } else {
      weight = 10
    }
  }
  interests.value.push({
    id: interestForm.value.id,
    name: interestForm.value.name,
    emotion: Number(interestForm.value.emotion),
    weight: weight
  })
  // axios 요청으로 백에 새로운 관심사 저장하고 적힌 값 초기화
  interestForm.value.id += 1
  interestForm.value.name = ''
  interestForm.value.emotion = ''
}

const deleteInterests = function (num) {
  const idx = interests.value.findIndex(item => item.id === num)
  if (idx !== -1) {
    interests.value.splice(idx, 1)
  }
  // 그 관심사 삭제하는 요청 back으로 보내기
}

onMounted(() => {
  selectMbti(1)
})
</script>

<style scoped>
.box {
  height: 500px;
  width: 500px;
}
</style>