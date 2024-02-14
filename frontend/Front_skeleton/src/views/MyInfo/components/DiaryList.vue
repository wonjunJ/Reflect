<template>
  <div class="diary-section">

    <div class="table-section">

      <table>
        <tr>
          <th scope="col">Date</th>
          <th scope="col">Score</th>
          <th scope="col">keyword</th>
          <th scope="col">Detail</th>
        </tr>
        <tr>
          <td>{{ diary.date }}</td>
          <td>{{ diary.diaryScore }}</td>
          <td>{{ diary.keyword }}</td>
          <td><button @click="router.push({ name: 'DiaryDetail', params: { id: diary.diaryId } })">show</button></td>
        </tr>
      </table>
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const props = defineProps({
  diary: Object
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


table {
  width: 100%;
}


.diary-section {
  padding: 10px
}
</style>