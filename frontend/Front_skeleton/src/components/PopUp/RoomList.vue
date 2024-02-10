<template>

    <div class="row row-cols-1 row-cols-md-3 g-4">
        
        <div class="col">
            <div class="card h-100">
                <img :src="imageURL" class="card-img-top" alt="방 썸네일">
                <img src="@/assets/defaultRoom.jpg" class="card-img-top" alt="방 임시 이미지">
                <div class="card-body">
                    <h6>{{ room.type }}</h6>
                    <h5 class="card-title">{{ room.title }}</h5>
                    <div class="d-inline-flex gap-1"><a href="#">#웹툰</a><a href="#">#해시태그</a></div>
                    <br>
                    <div class="room-member">
                        <div class="member-icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                                <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
                                <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
                            </svg>
                        </div>
                    </div>

                </div>

                <div class="card-footer text-center">
                    <a class="btn btn-primary" @click="openEnterModalOpen">Join</a>
                </div>
            </div>
        </div>
    </div>
    <RoomEnter :isEnterRoom="isEnterRoom" @closeModal="closeModal"/>
</template>

<script setup>

import { ref } from 'vue';
import RoomEnter from '@/components/PopUp/RoomEnter.vue';
defineProps({ room: Object })


const isEnterRoom = ref(false)

const openEnterModalOpen = () => {
isEnterRoom.value =  true
}

const closeModal = () => {
isEnterRoom.value = false
}

// 이 메서드를 추가하여 base64 이미지를 디코딩합니다.
// const decodeBase64Image = (base64String) => {
//     const matches = base64String.match(/^data:(.+);base64,(.+)$/);
//     if (matches.length !== 3) {
//         throw new Error('잘못된 base64 문자열입니다');
//     }

//     return {
//         type: matches[1],
//         data: Buffer.from(matches[2], 'base64'),
//     };
// }

// // room.image가 데이터에서 base64로 인코딩된 이미지를 가정합니다.
// const decodedImage = decodeBase64Image(room.thumnail);
// const imageURL = URL.createObjectURL(new Blob([decodedImage.data]));




</script>

<style scoped>

</style>