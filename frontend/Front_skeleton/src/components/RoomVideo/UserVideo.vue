<template>
    <div class="myVideo" v-if="streamManager">
        <div class="detailCamera">
            <div class="detailCameraTitle">
                <img class="myInfoImg" src="@\assets\cmh (1).jpg" alt="profileimg">
                <p class="nickname">{{ clientData }}</p>

                <button @click=followFriend>친구 추가 하기 </button>
                <font-awesome-icon icon="fa-solid fa-star fa-5x" />
            </div>
            <ov-video :stream-manager="streamManager" />
        </div>

    </div>
</template>
    
<script>
import OvVideo from './OvVideo.vue';
import axios from "axios";
import { useUserStore } from '@/stores/user.js';

export default {
    name: 'UserVideo',

    data() {
        return {
            userNickname: null,
            userImg: null,
            thisdata: null
        };
    },

    components: {
        OvVideo,
    },

    props: {
        streamManager: Object,
    },

    computed: {
        clientData() {
            const { clientData } = this.getConnectionData();
            this.thisdata = clientData;
            console.log(this.thisdata)
            return clientData;
        },
    },

    methods: {
        getConnectionData() {
            const { connection } = this.streamManager.stream;
            console.log(connection.session.sessionId);
            console.log("sessionID는 이것입니다. " + connection.session.sessionId)
            return JSON.parse(connection.data);
        },

        followFriend() {
            console.log("=========================" + this.thisdata)
            axios({
                method: 'post',
                url: `${useUserStore().API_URL}/follow`,
                headers: {
                    Authorization: `Bearer ${useUserStore().accessToken}`
                },
                data: {
                    nickName: this.thisdata
                }
            })
                .then(res => {
                    console.log("성공?")
                })
        },


    },
};
</script>

<style scoped>
.myVideo {
    width: auto;
    padding: 5px;
    /* padding을 줄여서 그리드 아이템 사이의 간격을 조절합니다. */
    background-color: #AAD9BB;
    border-radius: 10px;
}


.detailCameraTitle {
    display: flex;
}

.myInfoImg {
    width: 30px;
    height: 30px;
    border-radius: 100%;
    margin: 5px;
}

.nickname {
    font-size: 20px;
}

.icon {
    margin: 5px;
}
</style>