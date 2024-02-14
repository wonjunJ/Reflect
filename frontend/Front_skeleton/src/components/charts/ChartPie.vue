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

const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false
})
</script>

<style scoped>

</style>