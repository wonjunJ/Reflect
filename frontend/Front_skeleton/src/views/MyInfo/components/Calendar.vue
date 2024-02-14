<template>
  <div class="calendar-section">
    <p class="subtitle-section"> CALENDAR</p>
    <div class="calendar-container">

      <div class="calendar">
        <el-calendar v-model="selectedDate">
          <template #date-cell="{ data }">
            <p :class="data.isSelected ? 'is-selected' : ''">
              {{ data.day.split('-').slice(2).join('') }}
            </p>
            <!-- 이 날짜에 작성한 일기가 있으면 감정 따라 이모지 넣기 -->
            <span v-if="getDateText(data.day) && getDateText(data.day).diarys.length">
              {{ getDateText(data.day).diarys[0].feeling > 0 ? '😊' : '😔' }}
            </span>
            <!-- 이 날짜에 커뮤니티 활동 내역이 있으면 체크표시 -->
            <span v-if="getDateText(data.day) && getDateText(data.day).communitys.length">
              ✔️
            </span>
          </template>
        </el-calendar>
      </div>
      <div class="calendar-contents">

        <p class="selected-date">{{ selectedDate.toISOString().split('T')[0] }}</p>
        <div>
          <p class="sub-section">Diary</p>
          <div v-if="isDiary" class="isDiary_contents d-flex">
            <DiaryList v-if="sampleData[selectedDate.toISOString().split('T')[0]].diarys.length"
              :diarys="sampleData[selectedDate.toISOString().split('T')[0]].diarys" />
          </div>
          <div v-else>
            <p>작성한 일기가 없습니다. </p>
          </div>

        </div>
        <div>
          <p class="sub-section">Community</p>
          <div v-if="isDiary" class="isDiary_contents d-flex">
            <CommunityList v-if="sampleData[selectedDate.toISOString().split('T')[0]].communitys.length"
              :communitys="sampleData[selectedDate.toISOString().split('T')[0]].communitys" />
          </div>
          <div v-else>
            <p>활동한 커뮤니티 내역이 없습니다. </p>
          </div>
        </div>



      </div>
    </div>


    <!-- 선택된 날에 활동 내역이 있는지 없는지 판별, 있으면 props로 데이터 전달-->

  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import DiaryList from './DiaryList.vue'
import CommunityList from './CommunityList.vue'

const userStore = useUserStore()
const API_URL = userStore.API_URL

// 선택된 날짜
const selectedDate = ref(new Date())

// 샘플 데이터
const sampleData = ref({
  '2024-01-01': {
    diarys: [
      { id: 1, title: 'New Year', date: '2024-01-01', length: '5m', feeling: 40 },
    ],
    communitys: [
      { id: 1, type: 1, title: 1, people: 6, hashtag: ['soccer', 'movie'] },
      { id: 2, type: 1, title: 2, people: 4, hashtag: ['baseball'] },
    ],
  },
  '2024-01-02': {
    diarys: [
      { id: 2, title: 'Hangover', date: '2024-01-02', length: '5m', feeling: -20 },
    ],
    communitys: [],
  },
  '2024-01-07': {
    diarys: [
      { id: 3, title: 'Birthday', date: '2024-01-07', length: '5m', feeling: 50 },
    ],
    communitys: [],
  },
  '2024-01-29': {
    diarys: [],
    communitys: [
      { id: 2, type: 1, title: 2, people: 4, hashtag: ['baseball'] },

    ],
  },
})

// 각 날짜에 데이터 뿌리는 함수
const getDateText = (date) => {
  if (sampleData.value[date]) {
    return sampleData.value[date]
  } else {
    return ''
  }
}

// 선택된 날짜에 작성된 일기가 있는지 판별
const isDiary = computed(() => {
  return sampleData.value[selectedDate.value.toISOString().split('T')[0]] !== undefined
})

// 달이 바뀌면 일기 데이터 다시 불러와야하니까 watch로 지켜보기
watch(() => selectedDate.value.getMonth(), (newMonth, oldMonth) => {
  if (newMonth !== oldMonth) {
    const date = selectedDate.value.toISOString().split('T')[0].split('-')
    const year = date[0]
    const month = date[1]
    // axios 요청 뒤에 넣기 /2024-02 식으로
  }
  getDateText()
})
</script>

<style scoped>
.is-selected {
  color: #1989fa;
}

.subtitle-section {
  font-size: 30px;
  font-family: "YJ_Obang";
}

.calendar-container {
  display: flex;
  justify-content: center;

  flex-direction: row;

}

.calendar-container {
  display: flex;
}

.calendar {
  flex: 3;
}

.calendar-contents {
  margin: 15px;
  flex: 2;
  display: flex;
  flex-direction: column;
}

.isDiary_contents {
  display: flex;
  justify-content: center;
  flex-direction: column;
}

.selected-date {
  font-size: 30px;
  font-weight: bold;
  display: flex;
  height: 40px;
  justify-content: space-between;


}

.sub-section {
  font-size: 20px;
  font-weight: bold;
  padding-top: 10px;
}
</style>