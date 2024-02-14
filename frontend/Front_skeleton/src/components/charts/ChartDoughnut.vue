<template>
  <div>
    <Doughnut :data="internalChartData" :options="chartOptions" />
  </div>
</template>

<script setup>
import { Doughnut } from 'vue-chartjs'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import { ref, watch } from 'vue'
ChartJS.register(ArcElement, Tooltip, Legend)

const props = defineProps({
  chartData: Object
})

const internalChartData = ref(props.chartData)

const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false
})



watch(() => props.chartData, (newChartData) => {
  internalChartData.value = newChartData
}, { deep: true })
</script>


<style scoped></style>