<template>
  <div>
    <h4>Diary</h4>
    <table>
      <tr>
        <th scope="col">Date</th>
        <th scope="col">content</th>
        <th scope="col">detail</th>
      </tr>
      <tr v-for="diary in diarys" :key="diary.id">
        <td>{{ diary.date }}</td>
        <td>{{ diary.content }}</td>
        <td><button>show</button></td>
      </tr>
    </table>
  </div>
</template>

<script setup>
const props = defineProps({
    diarys: Object
})
</script>

<style scoped>

</style>