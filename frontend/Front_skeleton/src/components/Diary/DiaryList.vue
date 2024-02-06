<template>
    <div class="container">
      <div class="d2">
        <h3 class="h3-1">My Diary</h3>
      </div>
      
      <el-row class="row rounded-4">
        <el-col class="col1">
        <div  class="list" v-for="entry in filteredDiaryEntries" :key="entry.date" @click="selectDate(entry.date)">
          <div class="entry">
            <span class="date">{{ entry.emotion }} {{ formatDate(entry.date) }}</span>
             <div class="keywords">
              <span v-for="keyword in entry.keywords" :key="keyword">#{{ keyword }} </span>
              </div>
           </div>
        </div>
       </el-col>
      </el-row>
    </div>
</template>
  
<script setup>
import { ref, computed } from 'vue';
// import {useDiaryStore} from '@/stores/diary.js'
// const store = useDiaryStore();

const diaryEntries = ref([
  { date: '2024-01-01', emotion: '😄', keywords: ['행복', '가족', '기쁨'] },
  { date: '2024-01-02', emotion: '😄', keywords: ['사랑', '친구', '즐거움'] },
  { date: '2024-01-03', emotion: '😃', keywords: ['운동', '건강', '활동'] },
  { date: '2024-01-04', emotion: '😀', keywords: ['공부', '성취', '목표'] },
  { date: '2024-01-05', emotion: '😀', keywords: ['여행', '경험', '새로움'] },
  { date: '2024-01-06', emotion: '😀', keywords: ['미술', '창의성', '아름다움'] },
  { date: '2024-01-07', emotion: '😀', keywords: ['음악', '휴식', '평화'] },
]);

const filteredDiaryEntries = computed(() => {
  return diaryEntries.value.filter(entry => entry.date);
});

const selectDate = (date) => {
  store.selectedDate(date);
  console.log(date)
};

const formatDate = (date) => {
  const options = { year: 'numeric', month: 'short', day: 'numeric' };
  return new Date(date).toLocaleDateString('en-US', options);
};
</script>

<style scoped>
.h3-1 {
  
  height: 60px;
  flex-shrink: 0;
  color: #000;
  text-align: center;
  font-family: "ZCOOL KuaiLe";
  font-size: 40px;
  font-style: normal;
  font-weight: 400;
  background-color: #D5F0C1;
  border-radius: 20px;
}
.d1 {

  width: 415px;
  height: 74px;
  flex-shrink: 0;
  border-radius: 20px;
  background: #D5F0C1;
}
.col1 {
  padding: 10px;
  width: 415.805px;
  
  flex-shrink: 0;
  border-radius: 20px;
  align-content: center;
}
.container {
  flex-shrink: 0;
  padding-bottom: 50px;
  padding-top: 50px;
  /* 일기목록 배경 */

  position:relative;
  width: 90%;
  height: 785.77px;
  left: 30px;
  top: 50px;

  background: #f1f5c3;
  border-radius: 20px;
  margin-right: 100px;
}
.row {
  border: 1;
  background-color: #F9F7C9;
}
.list {
  
  
  height: 75.233px;
  flex-shrink: 0;
  background: #F9F2E4;
  margin: 20px;
}

.sidebar {
    width: 300px;
    background-color: #f0f0f0;
    padding: 20px;
    cursor: pointer;
  }
  
  .entry {
    margin-bottom: 15px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .entry:hover {
    background-color: #e0e0e0;
  }
  
  .date {
    font-weight: bold;
    margin-bottom: 5px;
    display: block;
  }
  
  .keywords span {
    margin-right: 5px;
    display: inline-block;
  }
</style>