<template>
  <div>
    <h4>Diary</h4>
    <table>
      <tr>
        <th scope="col">Date</th>
        <th scope="col">Score</th>
        <th scope="col">keyword</th>
        <th scope="col">&nbsp;</th>
      </tr>
      <tr>
        <td>{{ diary.date }}</td>
        <td>{{ diary.diaryScore }}</td>
        <td>{{ diary.keyword }}</td>
        <td><button>show</button></td>
      </tr>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  diary: Object
})
</script>

<style scoped>

</style>