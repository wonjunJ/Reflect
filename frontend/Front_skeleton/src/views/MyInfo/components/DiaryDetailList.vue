<template>
  <div>
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
.table {
  width: 100%;
  border-collapse: collapse;
  margin: 0;
  padding: 0;
  border: 1px solid #ddd;
}

th, td {
  text-align: left;
  padding: 8px;
  border: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
}

tr:nth-child(even) {
  background-color: #f9f9f9;
}

tr:nth-child(odd) {
  background-color: #ffffff;
}

button {
  background-color: #f2f2f2; /* Or any other color you prefer */
  border: none;
  padding: 5px 10px;
  cursor: pointer;
}

button:hover {
  background-color: #e2e2e2; /* Or any other color for hover state */
}

/* This is to style the header row as per the image provided */
thead tr {
  background-color: #e2e2e2; /* Light gray background for header */
  border-bottom: 3px solid #ddd; /* Solid border for header bottom */
}

/* This is to style the header cells */
th {
  border-right: 1px solid #ddd; /* Right border for header cells */
}

/* This is to remove the border from the last header cell as shown in the image */
th:last-child {
  border-right: none;
}

/* This is to give rounded corners to the top of the table */
thead tr:first-child th:first-child {
  border-top-left-radius: 10px;
}

thead tr:first-child th:last-child {
  border-top-right-radius: 10px;
}
</style>