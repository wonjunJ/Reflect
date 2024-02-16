<template>
  <Radar :data="chartData" :options="chartOptions" />
</template>

<script setup>
import { Radar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend
} from 'chart.js'
import { ref } from 'vue'
ChartJS.register(
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend
)

const props = defineProps({
  chartData: Object
})

const chartOptions = ref({
  responsive: false,
  maintainAspectRatio: false,
  scale: {
      ticks: {
        beginAtZero: true,
        max: 100,
        min: 0,
      },
      r: {
        suggestedMin: 0,
        suggestedMax: 100,
      }
  }
})
</script>

<style scoped>

</style>