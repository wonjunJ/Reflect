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
            <th scope="col">mbti</th>
            <th scope="col">interests</th>
            <th scope="col">Online</th>
            <th scope="col">Detail</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="follow in follows" :key="follow.userId">
            <td class="follow-name" @click="openModal(follow.userId)">{{ follow.nickName }}</td>
            <td>{{ follow.mbti }}</td>
            <td>{{ follow.interests }}</td>
            <td :style="{ 'color': follow._online ? 'green' : 'red' }">{{ follow._online ? "online" : "offline" }}</td>
            <td><button @click="openModal(follow.userId)">show</button></td>
          </tr>
        </tbody>
      </table>
    </div>
    <v-pagination v-model="page" :length="pagingInfo.totalPage"></v-pagination>
    <UserInfo v-if="isModal" :is-modal="isModal" :user-id="selectedUserId" @close-modal="closeModal" />

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
  selectedUserId.value = id
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