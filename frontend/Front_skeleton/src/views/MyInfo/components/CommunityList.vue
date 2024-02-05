<template>
  <div>
    <h4>Community</h4>
    <table>
      <tr>
        <th scope="col">Title</th>
        <th scope="col">People</th>
        <th scope="col">type</th>
        <th scope="col">#/No.</th>
        <th scope="col">&nbsp;</th>
      </tr>
      <tr v-for="community in communitys" :key="community.id">
        <td>{{ community.title }}</td>
        <td>{{ community.people }}</td>
        <td>자유주제</td>
        <td>{{ community.hashtag }}</td>
        <td><button>show</button></td>
      </tr>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  communitys: Object
})
</script>

<style scoped>

</style>