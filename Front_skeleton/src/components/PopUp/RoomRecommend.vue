<template>
    <!-- <div v-if="isHundredModalOpen || isFreeModalOpen" class="modal"> -->
    <div v-if="isRecommendModalOpen" class="modal" @click="closeModal">
        <div class="modal-content" @click.stop>
            <div class="modal-header">
                <p>이러한 방들은 어떠신가요?</p>
                <button @click="closeModal">X</button>
            </div>
            <div class="modal-body">
                <div>
                    <!-- 추천방 목록 -->
                    <div>
                        <!-- 백문백답 추천일 경우와 자유주제 추천의 경우를 나눠서 데이터를 가져올 것 -->
                        <div>
                            <div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="carousel">
                                <div class="carousel-inner">
                                    <!-- 방1 -->
                                    <div class="carousel-item active">
                                        <div class="col" v-for="room in props.recRooms" :key="room.roomId">
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
                                                    <div class="d-inline-flex gap-1" v-if="room.hashTags">
                                                        <span v-for="(tag, index) in room.hashTags" :key="index">
                                                            #{{ tag }}<span v-if="index < room.hashTags.length - 1"> </span>
                                                        </span>
                                                    </div>
                                                    <div v-if="room.qid">Q : {{ getQuestionById(room.qid) }}</div>
                                                    <br>
                                                    <div class=" text-center">
                                                        <a class="btn btn-primary"
                                                            @click="openEnterModalOpen(room.roomId)">Join</a>
                                                    </div>


                                                </div>


                                            </div>
                                        </div>
                                    </div>

                                </div>
                                <button class="carousel-control-prev" type="button"
                                    data-bs-target="#carouselExampleAutoplaying" data-bs-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Previous</span>
                                </button>
                                <button class="carousel-control-next" type="button"
                                    data-bs-target="#carouselExampleAutoplaying" data-bs-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Next</span>
                                </button>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div>
            <RoomEnter :isEnterRoom="isEnterRoom" :id="roomId" @closeModal="closeEnterModal" />
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import RoomEnter from '@/components/PopUp/RoomEnter.vue';
import { useRoomStore } from '@/stores/room'
import { useQuestionsStore } from '@/stores/questions'

const roomStore = useRoomStore()
const isEnterRoom = ref(false)

const props = defineProps({
    recRooms: Object,
    isRecommendModalOpen: Boolean,

});




const questionStore = useQuestionsStore()

// qid를 사용하여 질문을 찾는 함수
const getQuestionById = (qid) => {
    const question = questionStore.questions.find(question => question.id === qid);
    return question ? question.question : '질문을 찾을 수 없음';
};
// const props = defineProps({
//     recRooms: Object,
//     isHundredModalOpen: Boolean,
//     isFreeModalOpen: Boolean
// });


const emit = defineEmits()

let roomId = null


const openEnterModalOpen = (id) => {
    roomId = id
    isEnterRoom.value = true
}

const closeEnterModal = () => {
    isEnterRoom.value = false
}




const closeModal = () => {
    emit('closeModal')
};


</script>

<style scoped>
@import '../../css/modal.css';
</style>