<template>
  <div class="d-flex flex-column">
    <h2>About Me</h2>
    <div>
      <p>username : {{ userInfo.username }}</p>
      <p>email : {{ userInfo.email }}</p>
      <p>{{ userInfo.introduction }}</p>
      <h3>MBTI</h3>
      <h5>{{ userInfo.mbti }}</h5>
    </div>
    <div>
      <h3>감정그래프</h3>
      <div>
        <PieChart :chart-data="chartData"/>
      </div>
      <button class="btn" @click="router.push({name:'feelings'})">Detail</button>
    </div>
    <div>
      <h3>관심사</h3>
      <p>asdf</p>
      <p>asdf</p>
      <p>asdf</p>
      <button class="btn" @click="router.push({name: 'interests'})">Detail</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import PieChart from '@/components/charts/ChartPie.vue'

const router = useRouter()
const props = defineProps({
  userInfo: Object,
})

// 그래프용 데이터 (서버에서 datasets[0].data 데이터 받아와야함)
const chartData = ref({
  labels: ['Happy', 'Sad', 'Angry'],
  datasets: [
    {
      label: 'Feelings',
      backgroundColor: ['#f87979', '#81C784', '#64B5F6'],
      data: [4, 2, 1]
    }
  ]
})
</script>

<style scoped>

</style>