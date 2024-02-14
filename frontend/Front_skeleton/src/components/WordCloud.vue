<template>
  <div>
    <canvas ref="wordCloudCanvas" width="300" height="200"></canvas>
  </div>
</template>
  
<script setup>
import { onMounted, ref, watchEffect } from 'vue'
import wordcloud from 'wordcloud'
const props = defineProps({
  data: Object
})
  
const wordCloudCanvas = ref(null)
  
const generateWordCloud = () => {
  if (wordCloudCanvas.value && props.data) {
    const wordList = props.data
  
    wordcloud(wordCloudCanvas.value, {
      list: wordList,
      weightFactor: function (size) {
        return size*10 // 단어 크기 가중치
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
}

onMounted(generateWordCloud)
watchEffect(generateWordCloud)
</script>
  
<style scoped>
  
</style>