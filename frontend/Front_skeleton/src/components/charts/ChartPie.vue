<template>
  <Pie :data="chartData" :options="chartOptions" />
</template>

<script setup>
import { Pie } from 'vue-chartjs'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import { ref } from 'vue'
ChartJS.register(ArcElement, Tooltip, Legend)

const props = defineProps({
  chartData: Object
})

// const chartData = ref({
//   labels: ['January', 'February', 'March'],
//   datasets: [
//     {
//       label: 'Data One',
//       backgroundColor: ['#f87979', '#81C784', '#64B5F6'],
//       data: [40, 20, 12]
//     }
//   ]
// })
const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false
})
</script>

<style scoped>

</style>