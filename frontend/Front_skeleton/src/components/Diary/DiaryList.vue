<template>
  <div class="container">
    <div class="d2">
      <h3 class="h3-1">My Diary</h3>
    </div>

    <el-row class="row rounded-4">
      <el-col class="col1">
        <div class="list" v-for="entry in responseData.data" :key="entry.diaryId" @click="selectDate(entry.diaryId)">
          <div class="entry">
            <span class="date"> {{ formatDate(entry.createDate) }}</span>
            <div class="keywords">
              <span v-for="word in entry.keyword" :key="word">{{ word }} </span>
            </div>
          </div>

        </div>
      </el-col>
    </el-row>
  </div>
</template>
  
<script setup>
import { ref, computed, onMounted, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { defineProps } from 'vue'; // defineProps import
const router = useRouter();
// props를 정의

// props를 정의
const props = defineProps({
  responseData: Array // responseData prop은 Array 형식으로 정의
});

// responseData를 reactive로 선언
const responseData = reactive({
  data: props.responseData
});


const selectDate = (date) => {
  console.log(date)
  router.push({ name: 'DiaryDetail', params: { id: date } });
  this.$forceUpdate();
  //window.location.reload();
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
  font-family: "YJ_Obang";
  font-size: 40px;
  font-style: normal;
  font-weight: 400;
  border-radius: 20px;
}

.d1 {

  width: 415px;
  height: 74px;
  flex-shrink: 0;
  border-radius: 20px;
  background: #F9F7C9;
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

  position: relative;
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