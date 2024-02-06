<template>
    <div v-if="isModal" class="modal" @click.self="closeModal">
      <div class="modal-content" @click.stop>
        <div class="d-flex justify-content-between">
          <h3>캐릭터 설정</h3>
          <button class="btn btn-danger" @click="closeModal">X</button>
        </div>
        <div v-for="option in options" :key="option.id">
          <label :for="option.id">{{ option.name }}</label>
          <input v-if="option.ishave" type="checkbox" :id="option.id" :checked="option.id === selectedCharacter" @click="selectCharacter(option.id)">
          <button v-else class="btn btn-sm btn-success">구매하기</button>
        </div>
        <button class="btn btn-primary" @click="changeCharacter">저장</button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from "vue"
  
  const props = defineProps({
    isModal: Boolean,
    characterId: Number,
  })
  
  const emit = defineEmits(['closeModal', 'changeCharacter'])
  const closeModal = function () {
    emit('closeModal')
  }
  
  const options = ref([
    { id: 1, name: '슬라임', ishave: true },
    { id: 2, name: '코기', ishave: false },
    { id: 3, name: '해골', ishave: true },
  ])
  
  const selectedCharacter = ref(props.characterId)
  const selectCharacter = function (num) {
    selectedCharacter.value = num
  }
  
  const changeCharacter = function () {
    emit('changeCharacter', selectedCharacter.value)
  }
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
    width : 500px;
  }
  </style>