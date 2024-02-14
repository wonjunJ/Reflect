<template>
  <div class="following-section">
    <div class="sub-section">
      <p>Following</p>
    </div>
    <div class="table-section">
      <table>
        <thead>
          <tr>
            <th scope="col">Name</th>
            <th scope="col">Alike</th>
            <th scope="col">Online</th>
            <th scope="col">Detail</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="follow in follows" :key="follow.id">
            <td @click="openModal(follow.username)">{{ follow.username }} <img :src="follow.profileimg" alt="img"></td>
            <td>{{ follow.alike }}</td>
            <td :style="{ 'color': follow.isonline ? 'green' : 'red' }">{{ follow.isonline ? "online" : "offline" }}</td>
            <td><button @click="openModal(follow.username)">show</button></td>
          </tr>
        </tbody>
      </table>
    </div>

    <UserInfo :is-modal="isModal" :username="selectedUser" @close-modal="closeModal" />
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
@import '../../../css/table.css';

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

.sub-section {
  font-size: 20px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
}

.following-section {
  padding: 10px;
}
</style>