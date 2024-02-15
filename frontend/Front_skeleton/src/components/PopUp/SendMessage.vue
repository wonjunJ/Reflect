<template>
  <div v-if="isModal" class="modal" @click.self="closeModal">
    <div class="modal-content" @click.stop>
      <div class="d-flex justify-content-between">
        <div>
          {{ userName }}에게 쪽지 보내기
        </div>
        <button class="btn btn-danger btn-sm" @click="closeModal">X</button>
      </div>
      <form @submit.prevent="send">
        <input v-modal="msg" class="border" type="text">
        <button class="btn btn-primary">전송</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user.js'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

const userStore = useUserStore()
const props = defineProps({
  isModal: Boolean,
  userName: String,
  userId: Number
})

const msg = ref("")

const emit = defineEmits(['closeModal'])

const closeModal = function () {
  emit('closeModal')
}

var stompClient = userStore.stompClient
userStore.connect()

function send() {
  var message = {
    type: 1,
    sender: userStore.userId,
    toId: props.userId,
    msg: msg.value
  }
  const headers = {
    Authorization: `Bearer ${userStore.accessToken}`
  }

  stompClient.send("app/alram", headers, JSON.stringify(message))
}

onMounted(() => {

})
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  width: 300px;
}
</style>