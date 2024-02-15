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
          <div v-if="!isWordCloudData">추출된 관심사가 없습니다.</div>
          <WordCloud v-if="isWordCloud" :data="wordCloudData" />
        </div>
      </div>
      <!--mbti section-->
      <div class="content-section">
        <div class="sub-title-section">
          <p>MBTI</p>
        </div>
        <div class="body-section">
          <h5 v-if="mbti">당신의 MBTI는 {{ mbti }}입니다.</h5>
          <h5 v-else>MBTI 정보가 없습니다.</h5>
          <div>
            <div v-if="!isRaderChart">측정된 MBTI 데이터가 없습니다.</div>
            <div v-if="isChart">
              <ChartRadar :chart-data="radarChartData" />
            </div>
          </div>
        </div>
      </div>
      <!--남bti section-->
      <div class="content-section">
        <div class="sub-title-section">
          <p>다른 사람이 본 나의 MBTI</p>
        </div>
        <div class="body-section">
          <div v-if="!isBarChart">다른 사람이 남긴 MBTI 평가가 없습니다.</div>
          <div v-if="isChart">
            <ChartBar :chart-data="barChartData" />
          </div>
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
              <button>submit</button>
            </form>
          </div>

          <div class="d-flex flex-row justify-content-around">
            <div class="positive-section">
              <div class="sub-title-section">
                <p>긍정</p>
              </div>
              <div v-for="(interest, index) in interestsPositive" :key="interest.interestId">
                <p>
                  {{ index + 1 }}. {{ interest.interestName }}
                </p>
                <button @click="deleteInterests(interest.interestId, 'LIKE')">🗑</button>
              </div>
            </div>
            <div class="negative-section">
              <div class="sub-title-section">
                <p>부정</p>
              </div>
              <div v-for="(interest, index) in interestsNegative" :key="interest.interestId">
                <p>
                  {{ index + 1 }}. {{ interest.interestName }}
                </p>
                <button @click="deleteInterests(interest.interestId, 'UNLIKE')">🗑</button>
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
            <DiaryDetailList :diarys="filteredDiarys" :pagingInfo="pagingInfo" @change-page="changePage"
              :key="selectedMbti"></DiaryDetailList>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import axios from 'axios'
import { useUserStore } from '@/stores/user.js'
import ChartRadar from '@/components/charts/ChartRadar.vue'
import WordCloud from '@/components/WordCloud.vue'
import ChartBar from '@/components/charts/ChartBar.vue'
import DiaryDetailList from './components/DiaryDetailList.vue'

const userStore = useUserStore()
const API_URL = userStore.API_URL

// 차트 렌더링 결정 변수
const isWordCloud = ref(false)
const isChart = ref(false)
// 차트에 데이터 들어있는지 판별하는 변수
const isWordCloudData = ref(true)
const isRaderChart = ref(true)
const isBarChart = ref(true)

const mbtiCodes = ref({   // 버튼 용 MBTI 속성 목록
  1: 'I', 2: 'N', 3: 'T', 4: 'P', 5: 'E', 6: 'S', 7: 'F', 8: 'J'
})

const filteredDiarys = ref([])  // props로 일기 목록에 전해줄 일기
const pagingInfo = ref({})      // 페이지네이션 정보
const selectedMbti = ref(1)     // 선택된 MBTI 코드
const pageNum = ref(1)          // 선택된 페이지
// 페이지 변경 시 새 일기 목록 요청 함수
const changePage = function (num) {
  pageNum.value = num
  selectMbti(selectedMbti.value)
}

// 선택된 mbti가 바뀌면 페이지 1로 초기화
watch(selectedMbti, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    pageNum.value = 1
  }
})

const selectMbti = function (num) {   // MBTI 선택 함수
  axios({
    method: 'get',
    url: `${API_URL}/interest/mbti/${mbtiCodes.value[num]}?page=${pageNum.value}`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      selectedMbti.value = num              // 선택된 mbti 바꾸기
      filteredDiarys.value = res.data.data  // 일기 목록 저장
      pagingInfo.value = res.data.pagingInfo  // 페이지네이션 정보 저장
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        selectMbti(num)
      }
    })
}

const mbti = ref("")    // 사용자의 mbti
const mbtiInfo = ref([])  // 레이더 차트 mbti 정보 저장

// 받은 정보를 가공해서 레이더 차트에 전달
const radarChartData = computed(() => {
  let data = [0, 0, 0, 0, 0, 0, 0, 0]
  let sum = 0
  mbtiInfo.value.forEach(element => {
    sum += element.percent
    if (element.mbtiCode === "I") {
      data[0] = element.percent
    } else if (element.mbtiCode === "N") {
      data[1] = element.percent
    } else if (element.mbtiCode === "T") {
      data[2] = element.percent
    } else if (element.mbtiCode === "P") {
      data[3] = element.percent
    } else if (element.mbtiCode === "E") {
      data[4] = element.percent
    } else if (element.mbtiCode === "S") {
      data[5] = element.percent
    } else if (element.mbtiCode === "F") {
      data[6] = element.percent
    } else if (element.mbtiCode === "J") {
      data[7] = element.percent
    }
  })
  if (sum === 0) {
    isRaderChart.value = false
  }
  return {
    labels: ['I', 'N', 'T', 'P', 'E', 'S', 'F', 'J'],
    datasets: [
      {
        label: 'MBTI',
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderColor: 'rgba(255, 99, 132, 1)',
        pointBackgroundColor: 'rgba(255, 99, 132, 1)',
        data
      }
    ]
  }
})

// 남이 보는 나의 MBTI 더미데이터
const mbtiOthers = ref([])

// 남이보는 mbti 가공해서 바 차트에 전달
const barChartData = computed(() => {
  const result = {}
  let sum = 0
  mbtiOthers.value.forEach(item => {
    result[item.mbti] = item.cnt
    sum += item.cnt
  })
  const filteredData = Object.keys(result)
    .filter(key => result[key] > 0)
    .map(key => ({ label: key, data: result[key] }))
  filteredData.sort((a, b) => b.data - a.data)
  if (sum === 0) {
    isBarChart.value = false
  }

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

const interestsPositive = ref([])   // 긍정 관심사 목록 
const interestsNegative = ref([])   // 부정 관심사 목록

// 긍정 부정 관심사 합친거
const interests = computed(() => {
  return [...interestsNegative.value, ...interestsPositive.value]
})

// 관심사 목록 가공해서 워드클라우드에 전달
const wordCloudData = computed(() => {
  let result = []
  let sum = 0
  interests.value.forEach(element => {
    sum += element.cnt
    result.push([element.interestName, element.cnt])
  })
  if (sum === 0) {
    isWordCloudData.value = false
  }
  return result
})

// 새로운 관심사 항목 만들기용 입력 데이터
const interestForm = ref({
  name: '',
  emotion: 'LIKE',
})

// 새로운 관심사 항목 추가 함수
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
        if (res.data.preference === "LIKE") {
          interestsPositive.value.push(res.data)
        } else {
          interestsNegative.value.push(res.data)
        }
        interestForm.value.name = ''
        interestForm.value.emotion = 'LIKE'
      })
      .catch(err => {
        console.log(err)
        if (err.response.status === 401) {
          userStore.refresh()
          addInterest()
        }
      })
  } else {
    window.alert('관심사를 입력해주세요')
  }
}

// 관심사 삭제 함수
const deleteInterests = function (num, preference) {
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
      if (preference === "LIKE") {
        const idx = interestsPositive.value.findIndex(item => item.interestId === num)
        if (idx !== -1) {
          interestsPositive.value.splice(idx, 1)
        }
      } else {
        const idx = interestsNegative.value.findIndex(item => item.interestId === num)
        if (idx !== -1) {
          interestsNegative.value.splice(idx, 1)
        }
      }
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        deleteInterests()
      }
    })
}

// 관심사 목록 정보 요청 함수
const getInterestInfos = function () {
  axios({
    method: 'get',
    url: `${API_URL}/interest?top=15`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      interestsPositive.value = res.data.like
      interestsNegative.value = res.data.unlike
      isWordCloud.value = true
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        getInterestInfos()
      }
    })
}

// MBTI 정보 요청 함수
const getMbtiInfos = function () {
  axios({
    method: 'get',
    url: `${API_URL}/interest/mbti`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      mbtiOthers.value = res.data.Others
      mbti.value = res.data.Mine.mbti
      mbtiInfo.value = res.data.Mine.mbtiInfo
      isChart.value = true
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        getMbtiInfos()
      }
    })
}

onMounted(() => {
  selectMbti(1)
  getInterestInfos()
  getMbtiInfos()
})
</script>

<style scoped>
@import '../../css/section.css';
@import "../../css/component.css";

.interest {
  background-color: #f9f7c9;
  margin: 2px;
  padding: 3px;
  padding-left: 20px;
  padding-right: 20px;
  border-radius: 30px;
}

.positive-section {
  padding: 40px;
}

.negative-section {
  padding: 40px;
}
</style>