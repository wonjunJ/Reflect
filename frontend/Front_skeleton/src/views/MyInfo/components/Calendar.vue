<template>
  <div>
    <el-calendar v-model="selectedDate">
      <template #date-cell="{ data }">
        <p :class="data.isSelected ? 'is-selected' : ''">
        {{ data.day.split('-').slice(2).join('') }}
        </p>
        <span v-if="getDateDiary(data.day)">
          {{ getEmotionEmoji(getDateDiary(data.day).emotion) }}
        </span>
        <!-- 커뮤니티 활동 내역 있으면 체크표시 -->
        <span v-if="getDateDiary(data.day) && getDateDiary(data.day).doCommunity">
          ✔️
        </span>
      </template>
    </el-calendar>
    <!-- 선택된 날에 활동 내역이 있는지 없는지 판별, 있으면 props로 데이터 전달-->
    <div v-if="isDiary" class="d-flex justify-content-evenly mt-3">
      <div class="text-center">
        <h4>Diary</h4>
        <DiaryList class="mx-3" v-if="getDateDiary(modifyDate(selectedDate))" :diary="diaryDetail"/>
      </div>
      <div class="text-center">
        <h4>Community</h4>
        <CommunityList class="mx-3" v-if="getDateDiary(modifyDate(selectedDate))" :communitys="communitys" :pagingInfo="pagingInfo" @change-page="changePage"/>
      </div>
    </div>
    <div v-if="!isDiary" class="text-center">
      <p class="m-3">이 날 활동 내역이 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watch, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import axios from 'axios'
import DiaryList from './DiaryList.vue'
import CommunityList from './CommunityList.vue'

const userStore = useUserStore()
const API_URL = userStore.API_URL

// 선택된 날짜
const selectedDate = ref(new Date())
const modifyDate = function (date) {
  const year = date.getFullYear().toString()
  let month = (date.getMonth() + 1).toString()
  let day = date.getDate().toString()
  if (month.length === 1) {
    month = "0" + month
  }
  if (day.length === 1) {
    day = "0" + day
  }
  return `${year}-${month}-${day}`
}

const monthlyDiarys = ref([])
const diaryDetail = ref({})
const communitys = ref([])
const pageNum = ref(1)
const pagingInfo = ref({})
const changePage = function (num) {
  pageNum.value = num
}

const getDateDiary = function (date) {
  return monthlyDiarys.value.find(diary => diary.date === date)
}

const getDailyDiary = function (id, num) {
  axios({
    method: 'get',
    url: `${API_URL}/diary/simple?diaryId=${id}&page=${num}`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      diaryDetail.value = res.data.diary
      communitys.value = res.data.community
      pagingInfo.value = res.data.communityPagingInfo
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        getDailyDiary()
      }
    })
}

// 선택된 날짜에 작성된 일기가 있는지 판별
const isDiary = computed(() => {
  if (monthlyDiarys.value.find(diary => diary.date === modifyDate(selectedDate.value))) {
    const diaryId = monthlyDiarys.value.find(diary => diary.date === modifyDate(selectedDate.value)).diaryId
    getDailyDiary(diaryId, pageNum.value)
  }
  return monthlyDiarys.value.find(diary => diary.date === modifyDate(selectedDate.value)) !== undefined
})

const getMonthlyDiary = function (year, month) {
  axios({
    method: 'get',
    url: `${API_URL}/diary?date=${year}-${month}-01`,
    headers: {
      Authorization : `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      monthlyDiarys.value = res.data
    })
    .catch(err => {
      console.log(err)
      if (err.response.status === 401) {
        userStore.refresh()
        getMonthlyDiary()
      }
    })
}

const getEmotionEmoji = function (num) {
  if (num === 1) {
    return "😊"
  } else if (num === 2) {
    return "😆"
  } else if (num === 3) {
    return "😢"
  } else if (num === 4) {
    return "😠"
  } else if (num === 5) {
    return "😫"
  } else {
    return ""
  }
}

// 달이 바뀌면 일기 데이터 다시 불러와야하니까 watch로 지켜보기
watch(() => selectedDate.value.getMonth(), (newMonth, oldMonth) => {
  const date = modifyDate(selectedDate.value).split('T')[0].split('-')
  const year = date[0]
  const month = date[1]
  // axios 요청 뒤에 넣기 /2024-02 식으로
  getMonthlyDiary(year, month)
})

watch(selectedDate, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    pageNum.value = 1
  }
})

onMounted(() => {
  const today = new Date()
  const year = today.getFullYear().toString()
  let month = (today.getMonth() + 1).toString()
  if (month.length === 1) {
    month = "0" + month
  }
  getMonthlyDiary(year, month)
})
</script>

<style scoped>
.is-selected {
color: #1989fa;
}

</style>