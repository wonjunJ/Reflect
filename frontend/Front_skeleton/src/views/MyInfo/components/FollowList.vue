<template>
  <div class="test">
    <table>
      <thead>
        <tr>
          <th scope="col">Name</th>
          <th scope="col">mbti</th>
          <th scope="col">interests</th>
          <th scope="col">Online</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="follow in follows" :key="follow.userId">
          <td @click="openModal(follow.userId)">{{ follow.nickName }}</td>
          <td>{{ follow.mbti }}</td>
          <td>{{ follow.interests }}</td>
          <td :style="{ 'color' : follow._online ? 'green': 'red'}">{{ follow._online ? "online" : "offline" }}</td>
        </tr>
      </tbody>
    </table>
    <v-pagination v-model="page" :length="pagingInfo.totalPage"></v-pagination>
    <UserInfo v-if="isModal" :is-modal="isModal" :user-id="selectedUserId" @close-modal="closeModal"/>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import UserInfo from '@/components/PopUp/UserInfo.vue'

const emit = defineEmits(['changePage'])
const props = defineProps({
  follows: Object,
  pagingInfo: Object
})

const page = ref(1)
const selectedUserId = ref(0)
const isModal = ref(false)

const openModal = function (id) {
  selectedUserId.value = props.follows.find(item => item.userId === id).userId
  isModal.value = true
}
const closeModal = function () {
  isModal.value = false
}

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