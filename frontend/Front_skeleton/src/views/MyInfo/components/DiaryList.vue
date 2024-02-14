<template>
  <div class="diary-section">

    <div class="table-section">

      <table>
        <tr>
          <th scope="col">Title</th>
          <th scope="col">Date</th>
          <th scope="col">Length</th>
          <th scope="col">Feeling</th>
          <th scope="col">Detail</th>
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

  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  diarys: Object
})
</script>

<style scoped>
@import '../../../css/table.css';
@import '../../../css/component.css';




button {
  background-color: #f2f2f2;
  /* Or any other color you prefer */
  border: none;
  padding: 5px 10px;
  cursor: pointer;
}

button:hover {
  background-color: #e2e2e2;
  /* Or any other color for hover state */
}

.diary-section {}

table {
  width: 100%;
}


.diary-section {
  padding: 10px
}
</style>