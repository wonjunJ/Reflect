<template>
  <Bar :data="chartData" :options="chartOptions" />
</template>

<script setup>
import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'
import { ref } from 'vue'
ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

const chartData = ref({
  labels: [ 'January', 'February', 'March'],
  datasets: [
    {
      label: 'Data One',
      backgroundColor: '#f87979',
      data: [40, 20, 12]
    }
  ]
})
const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false
})
</script>

<style scoped>

</style>