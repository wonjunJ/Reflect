<template>
    <div v-if="isModal" class="modal" @click.self="closeModal">
        <div class="modal-content" @click.stop>
        <!-- 방 info -->
            <div class="d-flex justify-content-between">

                <!-- 방 타입 -->
                <div class="topic">
                    <p v-if="room.type === 'BACKMOON'">
                        백문백답
                    </p>
                    <p v-if="room.type === 'FREE'">
                        자유주제
                    </p>
                </div>
                <button class="btn btn-danger" @click="closeModal">X</button>
            </div>

            <!-- 방 제목 -->
            <div class="roomtitle">
                <p>방 이름 : </p>
                <p>{{ room.title }}</p>
            </div>

            <div v-if="room.type === 'BACKMOON'">
                <p>질문 : </p>
               <p> {{ getQuestionById(room.qid)}}</p>

            </div>
            <div v-if="room.type === 'FREE'">
                <p>해시태그 : </p>
                {{ room.hashName.join(' #') }}
            </div>



            <!-- 방 상세설명 -->
            <div class="roomdetail">
                <p>한 줄 설명 :</p>
                <p>{{ room.intro }}</p>
            </div>


            <!-- 방 설정 아이콘 -->
            <div class="roomicon">
                <!-- 비밀방 아이콘 -->
                <div class="icon">
                    <img class="secret-icon" :class="{'secret-icon-blue': isSecret, 'secret-icon-gray': !isSecret}" src="@/assets/secret.png" alt="비밀모드아이콘">
                </div>

                <!-- 가면모드 아이콘 -->
                <div class="icon">
                    <img class="mask-icon" :class="{'mask-icon-blue': isMask, 'mask-icon-gray': !isMask}"  src="@/assets/mask.png" alt="가면모드아이콘">
                </div>

                <!-- 익명모드 아이콘 -->
                <div class="icon">
                    <img class="anonymous-icon" :class="{'anonymous-icon-blue': isAnonymous, 'anonymous-icon-gray': !isAnonymous}"  src="@/assets/anonymous.png" alt="익명아이콘">
                </div>
            </div>

        
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRoomStore} from '@/stores/room'
import {useQuestionsStore} from '@/stores/questions'


const isSecret = ref(false)
const isMask = ref(false)
const isAnonymous = ref(false)
const roomStore = useRoomStore();
const roomId = defineProps({
    isModal:Boolean,
    roomId:String
})

const questionStore = useQuestionsStore()

// qid를 사용하여 질문을 찾는 함수
  const getQuestionById = (qid) => {
    const question = questionStore.questions.find(question => question.id === qid);
    return question ? question.question : '질문을 찾을 수 없음';
  };

const emit = defineEmits(['closeModal'])
  const closeModal = function () {
    emit('closeModal')
  }
const room = roomStore.roomDetail


onMounted(() => {
    roomStore.getRoomDetail(roomId)
    if (room.mode === 'SECRET') {
        isSecret.value = true
    }
    if (room.mode === 'MASK') {
        isMask.value = true
    }
    if (room.mode === 'ANONYMOUS') {
        isAnonymous.value = true
    }
});





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
    width : 300px;
  }

  .secret-icon-blue {
  filter: hue-rotate(180deg); /* 예시로 파란색으로 변경하는 필터 */
}

    .secret-icon-gray {
    filter: grayscale(100%); /* 회색으로 변경하는 필터 */
}

  .mask-icon-blue {
  filter: hue-rotate(180deg); /* 예시로 파란색으로 변경하는 필터 */
}

    .mask-icon-gray {
    filter: grayscale(100%); /* 회색으로 변경하는 필터 */
}
  .anonymous-icon-blue {
  filter: hue-rotate(180deg); /* 예시로 파란색으로 변경하는 필터 */
}

    .anonymous-icon-gray {
    filter: grayscale(100%); /* 회색으로 변경하는 필터 */
}

</style>



