<template>
  <div class="d-flex">
    <!--aside 화면 -->
    <div class=" aside-section">
      <!--관심사 section-->
      <div class="content-section">
        <div class="sub-title-section">
          <p>관심사</p>
        </div>

        <div class="body-section">
          <WordCloud v-if="isWordCloud" :data="wordCloudData" />
        </div>
      </div>
      <!--mbti section-->
      <div class="content-section">
        <div class="sub-title-section">
          <p>MBTI</p>
        </div>
        <div class="body-section">
          <h5>당신의 MBTI는 {{ mbti }}입니다.</h5>
          <div>
            <ChartRadar :chart-data="radarChartData" />
          </div>
        </div>
      </div>
      <!--남bti section-->
      <div class="content-section">
        <div class="sub-title-section">
          <p>다른 사람이 본 나의 MBTI</p>
        </div>
        <div class="body-section">
          <ChartBar :chart-data="barChartData" />
        </div>
      </div>
    </div>

    <!--main 화면-->
    <div class="main-section">
      <!--관심사 목록-->
      <div class="content-section">
        <div class="title-section">
          <p>관심사 목록</p>
        </div>

        <div class="body-section">
          <div class="form-section">
            <form @submit.prevent="addInterest">
              <div>
                <input type="text" v-model="interestForm.name">
              </div>

              <select name="emotion" v-model="interestForm.emotion">
                <option value="LIKE">LIKE</option>
                <option value="UNLIKE">UNLIKE</option>
              </select>
              <button class="option-btn">submit</button>
            </form>
          </div>

          <div class="d-flex flex-row justify-content-around">
            <div class="positive-section">
              <div class="sub-title-section">
                <p>긍정</p>
              </div>
              <div v-for="(interest, index) in interestsPositive" :key="interest.interestId">
                <p>
                  {{ index + 1 }}. {{ interest.name }}
                  <button @click="deleteInterests(interest.interestId)">🗑</button>
                </p>
              </div>
            </div>
            <div class="negative-section">
              <div class="sub-title-section">
                <p>부정</p>
              </div>
              <div v-for="(interest, index) in interestsNegative" :key="interest.interestId">
                <p>
                  {{ index + 1 }}. {{ interest.name }}
                  <button @click="deleteInterests(interest.interestId)">🗑</button>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!--MBTI 목록-->
      <div class="content-section">
        <div class="title-section">
          <p>MBTI Detail</p>
        </div>
        <div class="body-section">
          <div class="button-section">
            <button class="option-btn" v-for="(label, code) in mbtiCodes" :key="code"
              :disabled="selectedMbti === Number(code)" @click="selectMbti(Number(code))">
              {{ label }}
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
import { computed, onMounted, reactive, ref } from 'vue'
import axios from 'axios'
import { useUserStore } from '@/stores/user.js'
import ChartRadar from '@/components/charts/ChartRadar.vue'
import WordCloud from '@/components/WordCloud.vue'
import ChartBar from '@/components/charts/ChartBar.vue'
import DiaryDetailList from './components/DiaryDetailList.vue'

const userStore = useUserStore()
const API_URL = userStore.API_URL

// 워드 클라우드용 더미 데이터 (axios 요청 받아서 이 형태로 처리 필요)
// const wordCloudData = ref([
//   ['Word1', 30],
//   ['Word2', 20],
//   ['Word3', 10],
//   ['Word4', 7],
//   ['Word5', 5],
//   ['Word6', 3],
//   ['Word7', 2],
// ])
const isWordCloud = ref(false)

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
  1: 'I', 2: 'N', 3: 'T', 4: 'P', 5: 'E', 6: 'S', 7: 'F', 8: 'J'
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
    .map(key => ({ label: key, data: mbtiOthers.value[key] }))
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
  { id: 1, weight: 20, emotion: 1, name: '축구', preference: 'UNLIKE' },
  { id: 2, weight: 17, emotion: 0, name: '농구', preference: 'LIKE' },
  { id: 3, weight: 15, emotion: 0, name: '수영', preference: 'LIKE' },
  { id: 4, weight: 13, emotion: 1, name: '야구', preference: 'LIKE' },
  { id: 5, weight: 11, emotion: 0, name: '노래방', preference: 'LIKE' },
  { id: 6, weight: 11, emotion: 1, name: '개발', preference: 'UNLIKE' },
  { id: 7, weight: 9, emotion: 0, name: '영화보기', preference: 'LIKE' },
  { id: 8, weight: 8, emotion: 1, name: '산책하기', preference: 'UNLIKE' },
  { id: 9, weight: 6, emotion: 1, name: '잠자기', preference: 'LIKE' },
  { id: 10, weight: 4, emotion: 0, name: '맛집 탐방', preference: 'LIKE' },
  { id: 11, weight: 3, emotion: 1, name: '카페 투어', preference: 'LIKE' },
])

// 긍정 관심사 걸러내기
const interestsPositive = computed(() => {
  const filteredInterests = interests.value.filter(interest => interest.preference === 'LIKE')
  filteredInterests.sort((a, b) => b.weight - a.weight)
  return filteredInterests
})

// 부정 관심사 걸러내기
const interestsNegative = computed(() => {
  const filteredInterests = interests.value.filter(interest => interest.preference === 'UNLIKE')
  filteredInterests.sort((a, b) => b.weight - a.weight)
  return filteredInterests
})

const wordCloudData = computed(() => {
  let result = []
  interests.value.forEach(element => {
    result.push([element.interestName, element.cnt])
  })
  return result
})

// 새로운 관심사 항목 만들기용 입력 데이터
const interestForm = ref({
  name: '',
  emotion: 'LIKE',
})

// 새로운 관심사 항목 만들기
const addInterest = function () {
  if (interestForm.value.name) {
    axios({
      method: 'post',
      url: `${API_URL}/interest/`,
      headers: {
        Authorization: `Bearer ${userStore.accessToken}`
      },
      data: {
        name: interestForm.value.name,
        preference: interestForm.value.emotion
      }
    })
      .then(res => {
        interests.value.push({ ...res.data })
        interestForm.value.name = ''
        interestForm.value.emotion = 'LIKE'
      })
      .catch(err => {
        console.log(err)
      })
  } else {
    window.alert('관심사를 입력해주세요')
  }
}

const deleteInterests = function (num) {
  axios({
    method: 'delete',
    url: `${API_URL}/interest/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: {
      id: num
    }
  })
    .then(res => {
      const idx = interests.value.findIndex(item => item.interestId === num)
      if (idx !== -1) {
        interests.value.splice(idx, 1)
      }
    })
    .catch(err => {
      console.log(err)
    })
}

// api 저기말고 다른데서 받아와야 함 지금 더미임
onMounted(() => {
  selectMbti(1)
  axios({
    method: 'get',
    url: `${API_URL}/user/simple`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      interests.value = res.data.interests
      isWordCloud.value = true
    })
    .catch(err => {
      console.log(err)
    })
})
</script>

<style scoped>
@import '../../css/section.css';
@import "../../css/component.css";
</style>