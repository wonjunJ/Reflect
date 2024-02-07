<template>
  <div>
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
    <!-- 선택된 날에 활동 내역이 있는지 없는지 판별, 있으면 props로 데이터 전달-->
    <div v-if="isDiary" class="d-flex">
      <DiaryList v-if="sampleData[selectedDate.toISOString().split('T')[0]].diarys.length" :diarys="sampleData[selectedDate.toISOString().split('T')[0]].diarys"/>
      <CommunityList v-if="sampleData[selectedDate.toISOString().split('T')[0]].communitys.length" :communitys="sampleData[selectedDate.toISOString().split('T')[0]].communitys"/>
    </div>
    <div v-else>
      <p>이 날 활동 내역이 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import DiaryList from './DiaryList.vue'
import CommunityList from './CommunityList.vue'

// 선택된 날짜
const selectedDate = ref(new Date())

// 샘플 데이터
const sampleData = ref({
  '2024-01-01': { 
    diarys: [
      { id: 1, title: 'New Year', date: '2024-01-01', length: '5m', feeling: 40},
    ],
    communitys: [
      { id: 1, type: 1, title: 1, people: 6, hashtag: ['soccer', 'movie']}
    ],
  },
  '2024-01-02': { 
    diarys: [
      { id: 2, title: 'Hangover', date: '2024-01-02', length: '5m', feeling: -20},
    ],
    communitys: [],
  },
  '2024-01-07': { 
    diarys: [
      { id: 3, title: 'Birthday', date: '2024-01-07', length: '5m', feeling: 50},
    ],
    communitys: [],
  },
  '2024-01-29': { 
    diarys: [],
    communitys: [
      { id: 2, type: 1, title: 2, people: 4, hashtag: ['baseball']},
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
    console.log('month changed')
    // axios 요청 뒤에 넣기
  }
  getDateText()
})
</script>

<style scoped>
.is-selected {
color: #1989fa;
}
</style>