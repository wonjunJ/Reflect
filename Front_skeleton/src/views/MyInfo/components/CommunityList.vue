<template>
  <div class="community-section">
    <div class="sub-section">
      <p>Community</p>
    </div>
    <div class="table-section">
      <table v-if="communitys.length">
        <thead>
          <tr>
            <th scope="col">Title</th>
            <th scope="col">People</th>
            <th scope="col">Type</th>
            <th scope="col">HashTag</th>
            <th scope="col">Detail</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="community in communitys" :key="community.id">
            <td>{{ community.title }}</td>
            <td>{{ community.curNum }}/{{ community.totalNum }}</td>
            <td>{{ community.type }}</td>
            <td>{{ community.hashTags.join(", ") }}</td>
            <td><button @click="router.push({ name: 'waiting', params: { id: community.roomId } })">show</button></td>
          </tr>
        </tbody>
      </table>
      <div v-else>커뮤니티 활동 내역이 없습니다.</div>
      <v-pagination v-model="page" :length="pagingInfo.totalPage"></v-pagination>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const emit = defineEmits(['changePage'])
const props = defineProps({
  communitys: Object,
  pagingInfo: Object
})

const page = ref(1)

// 페이지 바뀌면 emit 보내기
watch(page, (newVal, oldVal) => {
  emit('changePage', newVal)
})
</script>

<style scoped>
@import '../../../css/table.css';
@import '../../../css/component.css';


.community-section {
  padding: 10px
}
</style>