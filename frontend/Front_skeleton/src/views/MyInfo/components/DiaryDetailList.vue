<template>
  <div>
    <h4>Diary</h4>
    <div v-if="diarys.length">
      <table>
        <tr>
          <th scope="col">Date</th>
          <th scope="col">content</th>
          <th scope="col">detail</th>
        </tr>
        <tr v-for="diary in diarys" :key="diary.diaryId">
          <td>{{ diary.createDate }}</td>
          <td>{{ diary.sentence }}</td>
          <td><button>test</button></td>
        </tr>
      </table>
    </div>
    <div v-else>조건을 만족하는 작성된 일기가 없습니다.</div>
    <v-pagination v-model="page" :length="pagingInfo.totalPage"></v-pagination>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
const props = defineProps({
    diarys: Object,
    pagingInfo: Object,
})
const emit = defineEmits(['changePage'])

const page = ref(1)

watch(page, (newVal, oldVal) => {
  emit('changePage', newVal)
})
</script>

<style scoped>

</style>