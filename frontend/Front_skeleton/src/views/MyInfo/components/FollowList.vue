<template>
  <div>
    <table>
      <tr>
        <th scope="col">Name</th>
        <th scope="col">&nbsp;</th>
        <th scope="col">Alike</th>
        <th scope="col">Online</th>
      </tr>
      <tr v-for="follow in follows" :key="follow.id">
        <td>{{ follow.username }}</td>
        <td>{{ follow.profileimg }}</td>
        <td>{{ follow.alike }}</td>
        <td :style="{ 'color' : follow.isonline ? 'green': 'red'}">{{ follow.isonline ? "online" : "offline" }}</td>
    </tr>
</table>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  follows: Object
})

</script>

<style scoped>

</style>