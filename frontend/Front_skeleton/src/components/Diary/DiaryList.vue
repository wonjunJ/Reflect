<template>
    <div class="container">
      <div class="d1">
        <h3 class="h3-1">My Diary</h3>
      </div>
      
      <el-row class="row rounded-4">
        <el-col class="col1">
          <div class="list" v-for="(item, index) in items" :key="index">
            <p>&#x1F600; {{ item.message }}</p>
            <p>{{ item.tag }}</p>
          </div>
        </el-col>
      </el-row>
    </div>
</template>

<script setup>
import { ref } from "vue"

const items = ref([{ message: '2024.01.24', tag: '#기쁨 #여행 #친구 #휴가'}, { message: '2024.01.25', tag: '#기쁨 #여행 #친구 #휴가' },{ message: '2024.01.26' , tag: '#슬픔 #혼자 #스트레스'}, { message: '2024.01.27', tag: '#놀람 #프로젝트 #최우수' },{ message: '2024.01.28', tag: '#즐거움 #동료 #프로젝트' }, { message: '2024.01.29' , tag: '#화남 #짜증 #직장생활'}])
</script>

<style>
.h3-1 {
  width: 340.53px;
  height: 49.11px;
  flex-shrink: 0;
  color: #000;
  text-align: center;
  font-family: "ZCOOL KuaiLe";
  font-size: 40px;
  font-style: normal;
  font-weight: 400;
  line-height: 110%; /* 44px */
}
.d1 {

  width: 415px;
  height: 74px;
  flex-shrink: 0;
  border-radius: 20px;
  background: #D5F0C1;
}
.col1 {
  width: 415.805px;
  height: 785.767px;
  flex-shrink: 0;
  border-radius: 20px;
  background: #FDFAFA;
  align-content: center;
}
.container {
  padding-bottom: 50px;
  padding-top: 50px;
}
.row {
  border: 1;
  background-color: #F9F7C9;
}
.list {
  width: 415.805px;
  height: 75.233px;
  flex-shrink: 0;
  background: #F9F2E4;
  margin: 10px;
}
</style>