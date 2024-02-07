<template>
  <div>
    <table>
      <thead>
        <tr>
          <th scope="col">Name</th>
          <th scope="col">Alike</th>
          <th scope="col">Online</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="follow in follows" :key="follow.id">
          <td @click="openModal(follow.username)">{{ follow.username }} <img :src="follow.profileimg" alt="img"></td>
          <td>{{ follow.alike }}</td>
          <td :style="{ 'color' : follow.isonline ? 'green': 'red'}">{{ follow.isonline ? "online" : "offline" }}</td>
        </tr>
      </tbody>
    </table>
    <UserInfo :is-modal="isModal" :username="selectedUser" @close-modal="closeModal"/>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import UserInfo from '@/components/PopUp/UserInfo.vue'

const props = defineProps({
  follows: Object
})

const selectedUser = ref('')
const isModal = ref(false)

const openModal = function (name) {
  selectedUser.value = name
  isModal.value = true
}
const closeModal = function () {
  selectedUser.value = ''
  isModal.value = false
}
</script>

<style scoped>
table {
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