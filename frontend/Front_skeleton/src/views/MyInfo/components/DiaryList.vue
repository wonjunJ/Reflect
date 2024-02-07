<template>
  <div>
    <h4>Diary</h4>
    <table>
      <tr>
        <th scope="col">Title</th>
        <th scope="col">Date</th>
        <th scope="col">Length</th>
        <th scope="col">Feeling</th>
        <th scope="col">&nbsp;</th>
      </tr>
      <tr v-for="diary in diarys" :key="diary.id">
        <td>{{ diary.title }}</td>
        <td>{{ diary.date }}</td>
        <td>{{ diary.length }}</td>
        <td>{{ diary.feeling }}</td>
        <td><button>show</button></td>
      </tr>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  diarys: Object
})
</script>

<style scoped>

</style>