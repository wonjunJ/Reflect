<template>
    <div class="main">
        <div class="roomInfoBack">
            <div class="roomInfo">
                <div class="roomInfoContent">
                    <div class="roomSubTitle">
                        <div class="topic">
                            <p v-if="this.roomType == 'FREE'">Free Topic</p>
                            <p v-else>100문 100답</p>
                        </div>
                        <div class="people">
                            <p>3/4</p>
                        </div>
                    </div>
                    <div class="roomTitle">
                        <p>{{ roomTitle }}</p>
                    </div>
                    <div class="roomHashtag">
                        <p v-for="hashtag in hashtags" :key="hashtag"><a href="#">{{ hashtag }}</a></p>
                    </div>
                    <div class="roomDetail">
                        <p>{{ intro }}</p>
                    </div>
                    <div class="roomIcon">
                        <!-- 비밀방 아이콘 -->
                        <div class="icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="105" height="89" viewBox="0 0 105 89"
                                fill="none">
                                <path
                                    d="M46.1703 6.58231L46.9436 9.64134C49.2044 9.29924 51.6077 9.11638 53.7317 9.2092C53.8547 8.17149 53.955 7.13193 54.0326 6.09107C51.4041 6.04014 48.5149 6.19189 46.1703 6.58231ZM57.8638 6.46393L57.2958 9.55512C59.5512 9.92068 61.8725 10.4785 63.8171 11.2087C64.3696 10.277 64.9017 9.33667 65.4132 8.38821C62.9077 7.47474 60.2577 6.8062 57.8638 6.46393ZM42.4272 7.43494C39.8416 8.17006 37.3404 9.16992 35.287 10.2676L37.2953 12.8924C39.2641 11.8903 41.4326 10.9942 43.4561 10.4396C43.1292 9.41536 42.7582 8.33189 42.4272 7.43494ZM68.9832 9.83376L66.9256 12.4311C68.7867 13.5714 70.6117 14.909 72.0089 16.2682C72.9747 15.6221 73.9263 14.9608 74.8632 14.2848C73.0491 12.5587 70.9813 11.0026 68.9832 9.83376ZM32.1069 12.4266C30.2351 14.108 28.5855 15.9888 27.3847 17.7774L30.6155 19.2902C31.7947 17.6197 33.2087 15.9629 34.6851 14.6653C33.8421 13.9056 32.9825 13.1593 32.1069 12.4266ZM77.182 17.1448L73.974 18.6915C75.04 20.4156 75.9696 22.3028 76.5054 24.0472C77.7306 23.8299 79.0279 23.5791 80.1035 23.351C79.3878 21.1203 78.3557 18.9462 77.182 17.1448ZM25.8066 20.9038C25.0546 23.0949 24.5667 25.2852 24.4304 27.4834L28.1185 27.602C28.2871 25.6949 28.6743 23.6481 29.2977 21.9195C28.1415 21.5627 26.9776 21.2241 25.8066 20.9038ZM80.5215 26.6961L76.8383 26.8952C76.9613 28.9196 76.9047 30.9492 76.9047 33.0515H80.5961C80.5641 30.9617 80.6693 28.6809 80.5217 26.6963L80.5215 26.6961ZM24.4047 30.6707V36.9282H28.0961V30.6707H24.4047ZM76.9047 36.18V42.4371H80.5961V36.18H76.9047ZM24.4047 40.0567V46.314H28.0961V40.0567H24.4047ZM76.9047 45.5657V51.8228H80.5961V45.5657H76.9047ZM24.4047 49.4426V55.6997H28.0961V49.4426H24.4047ZM62.3442 51.977V55.1059H72.1879V51.977H62.3442ZM76.9047 54.9514V61.2085H80.5961V54.9514H76.9047ZM24.4047 58.8283V65.0854H28.0961V58.8283H24.4047ZM76.9047 64.3374V70.5945H80.5961V64.3374H76.9047ZM24.4047 68.2138V74.4711H28.0961V68.2139L24.4047 68.2138ZM76.9047 73.7231V79.9802H80.5961V73.7231H76.9047ZM24.4047 77.5996C24.4004 79.3834 24.3908 81.1346 24.4047 82.9184C27.0609 82.8604 29.731 82.9184 32.388 82.9184V79.7895H28.0959V77.5996H24.4047ZM36.079 79.7895V82.9184H43.461V79.7895H36.079ZM47.152 79.7895V82.9184H54.534V79.7895H47.152ZM58.225 79.7895V82.9184H65.607V79.7895H58.225ZM69.298 79.7895V82.9184H76.68V79.7895H69.298Z"
                                    fill="#0F62FE" />
                            </svg>
                        </div>

                        <!-- 가면모드 아이콘 -->
                        <div class="icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="123" height="105" viewBox="0 0 123 105"
                                fill="none">
                                <path
                                    d="M81.1287 42.9188C75.3375 42.4375 70.1613 45.4125 68.9825 50.3125C68.9825 51.8 75.9012 53.7687 82.2562 53.7687C88.6113 53.7687 94.3512 50.3125 94.3512 49.35C94.3512 48.3437 90.3537 43.4438 81.1287 42.9188ZM41.9225 42.9188C32.6975 43.4438 28.6488 47.8625 28.6488 49.3063C28.6488 50.3125 34.9525 53.7687 40.7437 53.7687C46.535 53.7687 54.0175 51.8 54.0175 50.3125C52.8387 45.4125 47.0988 42.4375 41.9225 42.9188ZM86.8688 70C77.08 70 70.725 60.1562 61.5 60.1562C52.275 60.1562 45.3563 70 36.1313 70C24.0363 70 15.375 60.6375 15.375 43.925C15.375 33.6 18.86 30.625 34.3888 30.625C49.9175 30.625 54.0175 36.05 61.5 36.05C68.9825 36.05 73.595 30.625 88.6113 30.625C103.628 30.625 107.625 34.0813 107.625 43.925C107.625 60.6375 98.9638 70 86.8688 70Z"
                                    fill="#0F62FE" />
                            </svg>
                        </div>

                        <!-- 익명모드 아이콘 -->
                        <div class="icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="107" height="89" viewBox="0 0 107 89"
                                fill="none">
                                <path
                                    d="M86.8988 73.4508C87.2095 73.7325 87.4559 74.0668 87.6241 74.4348C87.7922 74.8028 87.8787 75.1971 87.8787 75.5954C87.8787 75.9937 87.7922 76.3881 87.6241 76.7561C87.4559 77.1241 87.2095 77.4584 86.8988 77.74C86.5882 78.0217 86.2193 78.2451 85.8134 78.3975C85.4075 78.5499 84.9725 78.6284 84.5331 78.6284C84.0938 78.6284 83.6587 78.5499 83.2528 78.3975C82.8469 78.2451 82.4781 78.0217 82.1674 77.74L54.4394 52.5996L26.7113 77.74C26.0839 78.3088 25.2329 78.6284 24.3456 78.6284C23.4583 78.6284 22.6073 78.3088 21.9799 77.74C21.3525 77.1713 21 76.3998 21 75.5954C21 74.7911 21.3525 74.0196 21.9799 73.4508L49.7121 48.3142L21.9799 23.1775C21.3525 22.6088 21 21.8373 21 21.0329C21 20.2286 21.3525 19.4571 21.9799 18.8883C22.6073 18.3195 23.4583 18 24.3456 18C25.2329 18 26.0839 18.3195 26.7113 18.8883L54.4394 44.0288L82.1674 18.8883C82.7948 18.3195 83.6458 18 84.5331 18C85.4204 18 86.2714 18.3195 86.8988 18.8883C87.5262 19.4571 87.8787 20.2286 87.8787 21.0329C87.8787 21.8373 87.5262 22.6088 86.8988 23.1775L59.1666 48.3142L86.8988 73.4508Z"
                                    fill="#A7A5A5" />
                            </svg>
                        </div>
                    </div>
                    <div class="roomEnterBtn">
                        <!-- <input v-model="mySessionId" class="form-control" type="text" required /> -->
                        <button class="setbtn" @click="enterRoom">Enter</button>

                    </div>
                </div>
            </div>
        </div>

        <div class=chatInfoBack>
            <div class="chatInfo">
                <div class="chatContent">
                    <div class="myInfo">
                        <div class="myInfoTitle">
                            <div class="myInfoImg">
                                <img v-if="profileImg" :src="profileImg" alt="Profile image">
                                <img v-else src="@/assets/defaultRoom.jpg" alt="Default image">
                            </div>
                            <div class="myInfoNickname">{{ userNickname }}</div>
                        </div>
                        <!--사용자 비디오  Session 연결해야 나타남-->
                        <div ref="previewContainer" class="preview-container">
                            <video ref="previewVideo" autoplay playsinline></video>
                        </div>
                    </div>
                    <div class="chatSetting">
                        <button class="setbtn" @click="toggleCamera">{{ isCameraOn ? '카메라 끄기' : '카메라 켜기' }}</button>
                        <button class="setbtn" @click="toggleMicrophone">{{ isMicrophoneOn ? '마이크 끄기' : '마이크 켜기' }}</button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import UserVideo from "../../components/RoomVideo/UserVideo.vue";
import { useRoomStore } from '../../stores/room';
import { useUserStore } from '@/stores/user.js';


axios.defaults.headers.post["Content-Type"] = "application/json";

const APPLICATION_SERVER_URL = process.env.NODE_ENV === 'production' ? '' : 'https://i10d207.p.ssafy.io:8443/api/';


export default {

    components: {
        UserVideo,
    },

    data() {
        return {
            isCameraOn: false,
            isMicrophoneOn: false,
            previewVideo: null,
            openviduSession: null,
            previewContainer: null,
            roomId: null,
            roomTitle: null,
            userNickname: null,
            userId: null,
            profileImg: null,
            intro: null,
            roomType: null,
            hashtags: null,
            myUserName: "Participant" + Math.floor(Math.random() * 100),
            // OpenVidu objects
        };
    },

    mounted() {
        this.setupOpenViduSession();
        this.previewContainer = this.$refs.previewContainer;
        this.setupCameraPreview();
        this.roomId = this.$route.params.id;

        console.log(this.userNickname);

        this.getRoomInfo()
        this.getUserInfo()


        // console.log("room title : " + room.title);
        console.log("roomId : " + this.roomId);
        // console.log(userInfo.nickName);
    },

    methods: {

        getRoomInfo() {

            axios({
                method: 'get',
                url: `${useRoomStore().API_URL}/room/detail/${this.roomId}`,
                headers: {
                    Authorization: `Bearer ${useUserStore().accessToken}`
                }
            }).then(res => {
                this.roomTitle = res.data.title
                this.intro = res.data.intro
                this.hashtags = res.data.hashTags
                this.roomType = res.data.type
                console.log(res.data)
                console.log(this.roomTitle)
            })
                .catch(err => {
                    console.log(err)
                    if (err.response.status === 401) {
                        useRoomStore().refresh()
                    }
                })
        },


        getUserInfo() {
            axios({
                method: 'get',
                url: `${useUserStore().API_URL}/user/`,
                headers: {
                    Authorization: `Bearer ${useUserStore().accessToken}`
                }
            })
                .then(res => {
                    this.userId = res.data.id
                    this.userNickname = res.data.nickName
                    this.profileImg = res.data.profileImg
                    this.roomType = res.data.type
                    console.log(this.userId)
                    console.log(res.data)
                    console.log(this.userNickname)
                })
                .catch(err => {
                    console.log(err)
                    if (err.response.status === 401) {
                        useUserStore().refresh()
                    }
                })
        },
        enterRoom() {
            console.log('룸 아이디는 ', this.roomId);
            this.$router.push({ name: 'roomdetail', params: { roomId: this.roomId }, query: { userId: this.userId, userNickname: this.userNickname, isCameraOn: this.isCameraOn, isMicrophoneOn: this.isMicrophoneOn, roomTitle: this.roomTitle } });
        },
        setupOpenViduSession() {
            this.openviduSession = new OpenVidu();
        },

        async setupCameraPreview() {
            const previewVideo = this.$refs.previewVideo;

            try {
                const stream = await navigator.mediaDevices.getUserMedia({
                    video: { width: 1000, height: 1000 }, // 원하는 해상도로 조절
                    audio: true,
                });

                previewVideo.srcObject = stream;
                this.isCameraOn = true;
                this.isMicrophoneOn = true;

                // 동적으로 카메라 프리뷰 크기 조절
                const { offsetWidth, offsetHeight } = this.previewContainer;
                previewVideo.width = offsetWidth;
                previewVideo.height = offsetHeight;
            } catch (error) {
                console.error('카메라 미리보기 설정 오류:', error);
            }
        },
        toggleCamera() {
            if (this.isCameraOn) {
                this.stopCamera();
            } else {
                this.startCamera();
            }
        },
        toggleMicrophone() {
            if (this.isMicrophoneOn) {
                this.stopMicrophone();
            } else {
                this.startMicrophone();
            }
        },
        startCamera() {
            this.setupCameraPreview();
        },
        stopCamera() {
            const previewVideo = this.$refs.previewVideo;
            const stream = previewVideo.srcObject;

            if (stream) {
                const tracks = stream.getTracks();
                tracks.forEach((track) => track.stop());
            }

            previewVideo.srcObject = null;
            this.isCameraOn = false;
        },
        startMicrophone() {
            const previewVideo = this.$refs.previewVideo;
            const stream = previewVideo.srcObject;

            if (stream) {
                const audioTracks = stream.getAudioTracks();
                audioTracks.forEach((track) => (track.enabled = true));
                this.isMicrophoneOn = true;
            }
        },
        stopMicrophone() {
            const previewVideo = this.$refs.previewVideo;
            const stream = previewVideo.srcObject;

            if (stream) {
                const audioTracks = stream.getAudioTracks();
                audioTracks.forEach((track) => (track.enabled = false));
                this.isMicrophoneOn = false;
            }
        },

    },

    beforeDestroy() {
        this.stopCamera();
        this.stopMicrophone();
    },

};
</script>

<style>
.main {
    height: 86vh;
    display: flex;
    flex-direction: row;
}

.roomInfoBack {
    flex: 2;
    background-color: white;
}

.roomInfo {
    height: 80%;
    border-bottom-right-radius: 50px;
    background-color: #80BCBD;
}

.roomInfoContent {
    padding: 50px;
}

.roomSubTitle {
    display: flex;
    justify-content: flex-start;
    flex-shrink: 0;
    color: #000;
    font-family: Inter;
    font-size: 32px;
    font-style: normal;
    font-weight: 700;
    line-height: normal;

}

.roomTitle {
    flex-shrink: 0;
    color: #000;
    font-family: Inter;
    font-size: 40px;
    font-style: normal;
    font-weight: 500;
    line-height: normal;
}

.roomDetail {
    flex-shrink: 0;
    color: #000;
    font-family: Inter;
    font-size: 24px;
    font-style: normal;
    font-weight: 200;
    line-height: normal;
}

.roomHashtag {
    flex-shrink: 0;
    color: #000;
    font-family: Inter;
    font-size: 24px;
    font-style: normal;
    font-weight: 200;
    line-height: normal;
}

.chatInfoBack {
    flex: 3;
    background-color: #80BCBD;
}

.chatInfo {
    height: 100%;
    border-top-left-radius: 50px;
    background-color: white;
}

.chatContent {
    height: 100%;
    padding: 5% 10% 5% 10%;
}

.myInfo {
    padding: 30px;
    border-radius: 10px;
    background-color: #AAD9BB;
    display: flex;
    flex-direction: column;
}

.myInfoTitle {
    padding: 10px;
}

.myInfoImg img {
    width: 50px;
    height: 50px;
    flex-shrink: 0;
    border-radius: 100px;
    background: lightgray 50% / cover no-repeat;
    float: left;
}

.myInfoNickname {
    margin: 5px;
    flex-shrink: 0;
    color: #000;
    font-family: Inter;
    font-size: 32px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    float: left;
}

.myInfoVideo {
    width: 100%;
    height: auto;

}

.chatSetting {
    display: flex;
    flex-direction: row;
    margin: 50px;
    justify-content: space-evenly
}


.setbtn {
    width: 200px;
    height: 60px;
    flex-shrink: 0;
    border-radius: 50px;
    background: #D5F0C1;
    box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
    color: #000;
    font-family: Inter;
    font-size: 30px;
    font-style: normal;
    font-weight: 700;
    line-height: normal;
}



.icon {
    width: 107px;
    height: 97px;
    flex-shrink: 0;
    float: left;
}


.preview-container {
    height: 42vh;
    width: auto;
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
}


.preview-container video {
    height: 100%;
    width: auto;
    object-fit: cover;
}
</style>