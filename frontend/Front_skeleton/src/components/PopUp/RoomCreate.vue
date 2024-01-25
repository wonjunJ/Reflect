<template>

  <div v-if="isModalOpen" class="modal">
    <div class="modal-content">

        <h3>방 만들기</h3>
        <!-- 모달 내용 -->
        <div>
            <!-- 썸네일 사진-->
            <div>
                <img :src="roomImage" @error="replaceImg" width="300px"/>
                <!-- 사진 수정 아이콘 -->
                <button @click="imageChange">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
                        <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>
                        <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"/>
                    </svg>
                </button>
            </div>

            <!-- 대화 타입 선택 -->
            <div>
                <h3>대화 타입</h3>
                <div>
                    <button>100문 100답</button>
                    <button>자유 주제</button>
                </div>
            </div>


            <div>
                <!-- 방 제목 -->
                <div>
                    <label for="title-label">Title</label>
                    <input type="text" v-model.trim="roomTitle" class="room-input">
                </div>
            

                <!-- 해시태그  -->
                <div>
                    <label for="hashtag-label">Hashtag</label>
                    <input type="text" v-model.trim="hashtag" class="room-input">
                </div>


                <!-- 방 설명 -->
                <div>
                    <label for="describe-label">Title</label>
                    <input type="text" v-model.trim="roomDescribe" class="room-input">
                </div>

            </div>


            <!-- 방 설정 토글 -->
            <div>
                <!-- 비밀의 방 -->
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" role="switch" id="secretRoomOnOff" checked>
                    <label class="form-check-label" for="secretRoomOnOff">비밀의 방으로</label>
                </div>

                <!-- 가면모드 -->
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" role="switch" id="maskOnOff" checked>
                    <label class="form-check-label" for="maskOnOff">가면모드</label>
                </div>

                <!-- 익명모드 -->
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" role="switch" id="anonymousOnOff" checked>
                    <label class="form-check-label" for="anonymousOnOff">비밀의 방으로</label>
                </div>

            </div>

            <!-- 인원수 설정 슬라이더-->
            <div>
                <label for="limit" class="form-label">인원수</label>
                <input type="range" class="form-range" min="2" max="8" step="1" id="limit">

            </div>
            <button @click="closeModal">창 닫기</button>
        </div>
    </div>



  

</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue';

const { isModalOpen } = defineProps(['isModalOpen']);
const emit = defineEmits();

const roomImage = ref('');

const replaceImg = (e) => {
  e.target.src = require(`~/assets/images/defaultRoom.jpg`);
};

const imageChange = () => {
  if (roomImage.value === '') {
    roomImage.value = require(`~/assets/images/image.jpg`);
  } else {
    roomImage.value = '';
  }
};

const closeModal = () => {
  emit('closeModal');
};





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
  width: 400px;
  background: white;
  padding: 20px;
  border-radius: 8px;
}

</style>