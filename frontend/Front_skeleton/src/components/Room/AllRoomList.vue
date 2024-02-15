<template>
    <div>
        <div class="all-room-container">
            <div class="row row-cols-1 row-cols-md-5 g-4">
                <div class="col" v-for="room in rooms" :key="room.roomId">
                    <div class="card h-100">
                        <div class="room-img" v-if="room.thumnail">
                            <img :src="room.thumnail" alt="방 이미지" class="card-img-top" />
                        </div>
                        <div v-else>
                            <img src="@/assets/defaultRoom.jpg" alt="기본 이미지" class="card-img-top" />
                        </div>
                        <div class="card-body">
                            <h6>{{ room.type }}</h6>
                            <h5 class="card-title">{{ room.title }}</h5>
                            <div class="d-inline-flex gap-1">
                                <span v-for="(tag, index) in room.hashTags" :key="index">
                                    #{{ tag }}<span v-if="index < room.hashTags.length - 1"> </span>
                                </span>
                                <p v-if="room.qid">Q : {{ getQuestionById(room.qid) }}</p>
                            </div>
                            <br>
                            <div class="text-center">
                                <a class="btn btn-primary" @click="openEnterModalOpen(room.roomId)">Join</a>
                            </div>


                        </div>


                    </div>
                </div>
            </div>
            <RoomEnter :isEnterRoom="isEnterRoom" :id="selectedRoomId" @closeModal="closeModal" />
        </div>
        <!-- 페이지네이션 -->
        <v-pagination v-model="page" :length="pagingInfo.totalPage" @update:modelValue="changeRoomPage"></v-pagination>
    </div>
</template>

<script setup>
import { useRoomStore } from '@/stores/room';
import { ref, computed, onMounted } from 'vue';
import RoomEnter from '@/components/PopUp/RoomEnter.vue';
import { useQuestionsStore } from '@/stores/questions'


const roomStore = useRoomStore()
const questionStore = useQuestionsStore()
const selectedRoomId = ref(null); // 선택된 방의 roomId를 저장할 ref
const page = ref(1) // 현재 페이지 번호
// computed 속성을 사용하여 rooms와 pagingInfo 정의
const rooms = computed(() => roomStore.rooms); // 방 목록
const pagingInfo = computed(() => roomStore.pagingInfo); //페이지네이션 정보


const changeRoomPage = (num) => {
    page.value = num
    roomStore.getRooms(page.value) // 새로운 커뮤니티 목록을 가져오는 함수
}


// qid를 사용하여 질문을 찾는 함수
const getQuestionById = (qid) => {
    const question = questionStore.questions.find(question => question.id === qid);
    return question ? question.question : '질문을 찾을 수 없음';
};





const isEnterRoom = ref(false)

const openEnterModalOpen = (roomId) => {
    selectedRoomId.value = roomId; // 선택된 방의 roomId 저장
    isEnterRoom.value = true
}

const closeModal = () => {
    isEnterRoom.value = false
}

onMounted(() => {
    roomStore.getRooms(page.value)
})




</script>

<style scoped></style>