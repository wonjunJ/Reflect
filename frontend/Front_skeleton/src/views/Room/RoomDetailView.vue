<template>
  <div class="mainBackground">
    <div class="mainContent">
      <div class="chatTitle">
        <p>웹툰 이야기 할 사람!</p>
      </div>
      <div class="roomSection">
        <div class="cameraSection">

          <user-video :stream-manager="publisher" />
          <user-video v-for="sub in subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub" />


        </div>
        <div class="roomContent">
          <div class="peopleInfo">
            <div class="peopleInfoTitle">
              <div class="peopleTitle">방인원</div>
              <div class="peopleNum">3/4</div>
            </div>
            <div class="peoples">
              내 이름
              <div class="person" v-for="sub in subscribers" :key="sub.stream.connection.connectionId">사람</div>
            </div>
          </div>
          <div class="roomSetting">
            <button class="settingButton" @click="toggleCamera()">
              <font-awesome-icon :icon="['fas', 'camera']" />
            </button>
            <button class="settingButton">
              <font-awesome-icon :icon="['fas', 'microphone']" />
            </button>
            <button class="settingButton">
              <font-awesome-icon :icon="['fas', 'masks-theater']" />
            </button>
            <button class="settingButton">
              <font-awesome-icon :icon="['fas', 'rotate-right']" /></button>
          </div>
          <div class="quitChatting">
            <input class="btn btn-large btn-danger" type="button" id="buttonLeaveSession" @click="leaveSession"
              value="채팅 종료" />
          </div>
          <div class="exitRoom">
            <button>방 나가기</button>
          </div>
          <div class="chattingBtn">
            <button>채팅하기</button>
          </div>
        </div>
      </div>


    </div>
  </div>
</template>

<script>
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import UserVideo from "../../components/RoomVideo/UserVideo.vue"

axios.defaults.headers.post["Content-Type"] = "application/json";
const APPLICATION_SERVER_URL = process.env.NODE_ENV === 'production' ? '' : 'http://localhost:5000/';

export default {
  name: "App",


  created() {
    this.isCameraOn = this.$route.query.isCameraOn;
    this.isMicrophoneOn = this.$route.query.isMicrophoneOn;
    this.roomTitle = this.$route.params.roomTitle;
    this.mySessionId = this.$route.params.roomTitle;
    this.joinSession();
  },

  components: {
    UserVideo,
  },

  data() {
    return {
      // OpenVidu objects
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      isCameraOn: null,
      isMicrophoneOn: null,
      roomTitle: null,
      // Join form
      // mySessionId: "SessionA",
      myUserName: "Participant" + Math.floor(Math.random() * 100),
    };
  },

  methods: {
    joinSession() {

      this.OV = new OpenVidu();
      this.session = this.OV.initSession();

      this.session.on("streamCreated", ({ stream }) => {
        const subscriber = this.session.subscribe(stream);
        this.subscribers.push(subscriber);
      });

      this.session.on("streamDestroyed", ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });

      this.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });


      this.getToken(this.mySessionId).then((token) => {

        this.session.connect(token, { clientData: this.myUserName })
          .then(() => {
            let publisher = this.OV.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: true, // Whether you want to start publishing with your video enabled or not
              resolution: "360x240", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
              mirror: false, // Whether to mirror your local video or not
            });

            // Set the main video in the page to display our webcam and store our Publisher
            this.mainStreamManager = publisher;
            this.publisher = publisher;

            // --- 6) Publish your stream ---

            this.session.publish(this.publisher);
          })
          .catch((error) => {
            console.log("There was an error connecting to the session:", error.code, error.message);
          });
      });

      window.addEventListener("beforeunload", this.leaveSession);
    },

    leaveSession() {
      // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
      if (this.session) this.session.disconnect();

      // Empty all properties...
      this.session = undefined;
      this.mainStreamManager = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OV = undefined;

      // Remove beforeunload listener
      window.removeEventListener("beforeunload", this.leaveSession);
    },

    toggleCamera() {
      this.isCameraOn = !this.isCameraOn;
      if (this.publisher) {
        this.publisher.publishVideo(this.isCameraOn);
      }
    },

    updateMainVideoStreamManager(stream) {
      if (this.mainStreamManager === stream) return;
      this.mainStreamManager = stream;
    },

    /**
     * --------------------------------------------
     * GETTING A TOKEN FROM YOUR APPLICATION SERVER
     * --------------------------------------------
     */
    async getToken(mySessionId) {
      const sessionId = await this.createSession(mySessionId);
      console.log("sessionID is : " + sessionId);
      return await this.createToken(sessionId);
    },

    async createSession(sessionId) {
      const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId }, {
        headers: { 'Content-Type': 'application/json', },
      });
      return response.data; // The sessionId
    },

    async createToken(sessionId) {
      const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
        headers: { 'Content-Type': 'application/json', },
      });
      return response.data; // The token
    },

  },
};
</script>

<style>
.mainBackground {

  background-color: #80BCBD;
  ;
}


.mainContent {
  padding: 50px;
  border-top-left-radius: 100px;
  border-top-right-radius: 100px;
  width: 100%;
  height: 100%;
  background-color: white;
}


.cameraSection {
  margin: 10px;
  flex: 4;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  align-content: stretch;
}


.chatTitle {
  flex-shrink: 0;
  color: #000;
  font-family: Inter;
  font-size: 30px;
  font-style: normal;
  font-weight: 1000;
  line-height: normal;
}

.roomContent {
  flex: 1;
}

.camera {
  margin: 10px;

  background-color: #AAD9BB;
}

.roomSection {
  display: flex;
  flex-direction: row;
}

.roomSetting {
  background-color: #F9F7C9;
  border-radius: 10px;
  margin: 10px;

}

.peopleInfo {
  background-color: #F9F7C9;
  border-radius: 10px;
  margin: 10px;
  padding: 10px;
}

.settingButton {
  background-color: #FFD6B0;
  width: 40px;
  height: 40px;
  border-radius: 100%;
  margin: 10px;
}
</style>