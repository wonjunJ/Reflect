<template>
    <div>
        <h1>모든 방 목록</h1>
        <div class="row row-cols-1 row-cols-md-3 g-4">
            
            <div class="col">
                <div class="card h-100">
                <img src="..." class="card-img-top" alt="...">
                <div class="card-body">
                    <h5 class="card-title">Card title</h5>
                    <div class="d-inline-flex gap-1"><a href="#">#웹툰</a><a href="#">#해시태그</a></div>
                    <br>
                    <div class="room-member">
                        <div class="member-icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                                <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
                                <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
                            </svg>
                        </div>
                        <!-- <div class="member-icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                                <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
                                <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
                            </svg>
                        </div> -->
                    </div>

                </div>

                <div class="card-footer text-center">
                    <a href="#" class="btn btn-primary" @click="openEnterModalOpen">Join</a>
                </div>
                
            </div>
        </div>
        <RoomEnter :openEnterModalOpen="openEnterModalOpen" @closeModal="closeModal"/>
        </div>
    </div>
</template>

<script setup>
import {ref} from vue;
import RoomEnter from '../PopUp/RoomEnter.vue';

const isEnterRoom = ref(false)

const openEnterModalOpen = () => {
    isEnterRoom.value =  true
}

const closeModal = () => {
    isEnterRoom.value = false
}



</script>

<style scoped>

</style>