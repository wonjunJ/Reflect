<template>
  <div v-if="isModal" class="modal" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header d-flex flex-row justify-content-between">
        <p>캐릭터 설정</p>
        <button @click="closeModal">X</button>
      </div>

      <div class="modal-body" @click.stop style="max-height: 700px; overflow-y: auto;">
        <div class="character-select-section d-flex flex-row ">
          <div class="character" v-for="option in options" :key="option.id">
            <label :for="option.id">{{ option.name }}</label>
            <!-- <input v-if="option.ishave" type="checkbox" :id="option.id" :checked="option.id === selectedCharacter"
              @click="selectCharacter(option.id)"> -->
            <button v-if="option.ishave" @click="selectCharacter(option.img)">선택</button>
            <button v-else class="btn btn-sm btn-success">구매</button>
            <img :src=option.img style="width: 150px" />
          </div>
        </div>
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
  characterImg: String,
})

const emit = defineEmits(['closeModal', 'changeCharacter'])
const closeModal = function () {
  emit('closeModal')
}

const options = ref([
  { id: 1, name: '슬라임', ishave: true, img: "src/img/slime.gif" },
  { id: 2, name: '코기', ishave: false, img: 'src/img/corgi.gif' },
  { id: 3, name: '토끼', ishave: true, img: 'src/img/rabbit.gif' },
])

const selectedCharacter = ref(props.characterImg)
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
  width: 500px;
}

.modal-header {
  background-color: rgb(151, 150, 150, 0.5);
  box-shadow: 5px 5px 5px 5px "black";
  font-family: "YJ_Obang";
  font-size: 25px;
}

.modal-body {
  padding: 30px;
}


.profile-edit-button {
  position: absolute;
  bottom: 0;
  right: 0;
  /* transform: translate(50%, 50%); */
}

.profile-img-section {
  position: relative;
  width: 100%;
}

.character {
  padding: 20px;
  border: 1px black;
}
</style>