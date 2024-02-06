<template>
  <div>
    <canvas ref="wordCloudCanvas" width="300" height="200"></canvas>
  </div>
</template>
  
<script setup>
import { onMounted, ref } from 'vue'
import wordcloud from 'wordcloud'
const props = defineProps({
  data: Object
})
  
const wordCloudCanvas = ref(null)
  
onMounted(() => {
  if (wordCloudCanvas.value) {
    const wordList = props.data
  
    wordcloud(wordCloudCanvas.value, {
      list: wordList,
      weightFactor: function (size) {
        return size*2.5 // Adjusted weight factor
      },
      fontFamily: 'Times, serif',
      color: function (word, weight) {
        if (weight >= 30) {
          return '#ff5733'
        } else if (weight >= 20) {
          return '#79ff33'
        } else if (weight >= 10) {
          return '#33a8ff'
        } else {
          return '#ff33d3'
        }
      },
      rotateRatio: 0,
    })
  }
})
</script>
  
<style scoped>
  
</style>