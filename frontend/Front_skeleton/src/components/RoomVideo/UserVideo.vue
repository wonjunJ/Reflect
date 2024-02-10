<template>
    <div class="myVideo" v-if="streamManager">
        <div class="detailCamera">
            <div class="detailCameraTitle">
                <img class="myInfoImg" src="@\assets\cmh (1).jpg" alt="profileimg">
                <p class="nickname">{{ clientData }}</p>
                <font-awesome-icon icon="fa-solid fa-star fa-5x" />
            </div>
            <ov-video :stream-manager="streamManager" />
        </div>

    </div>
</template>
    
<script>
import OvVideo from './OvVideo.vue';

export default {
    name: 'UserVideo',

    components: {
        OvVideo,
    },

    props: {
        streamManager: Object,
    },

    computed: {
        clientData() {
            const { clientData } = this.getConnectionData();
            return clientData;
        },
    },

    methods: {
        getConnectionData() {
            const { connection } = this.streamManager.stream;
            console.log(connection.session.sessionId);
            return JSON.parse(connection.data);
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