<template>
  <div class="community-section">
    <div class="sub-section">
      <p>Community</p>
    </div>
    <div class="table-section">
      <table>
        <thead>
          <tr>
            <th scope="col">Title</th>
            <th scope="col">People</th>
            <th scope="col">Hashtag</th>
            <th scope="col">Detail</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="community in communitys" :key="community.id">
            <td>{{ community.title }}</td>
            <td>{{ community.people }}</td>
            <td>{{ community.hashtag.join(', ') }}</td>
            <td><button>show</button></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  communitys: Object
})
</script>

<style scoped>
@import '../../../css/table.css';
@import '../../../css/component.css';


.community-section {
  padding: 10px
}
</style>