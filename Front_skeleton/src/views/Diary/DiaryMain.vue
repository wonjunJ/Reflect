<template>
  <div v-if="displayMessage" class="message-container" :style="bubbleStyle">
    <div id="displayText" class="speech-bubble">{{ displayText }}</div>
  </div>
  <div class="snow-background">
    <div class="camera-box">
      <div id="videoContainer">
        <video ref="videoElement" width="640" height="480" autoplay muted @play="onPlay"></video>
        <canvas id="videoCanvas" width="720" height="560"></canvas>
      </div>
    </div>
    <div class="talk-section">
      <div class="talk">
        <p>{{ talk }}</p>
      </div>
      <div class="buttons">
        <button @click="startListening(); startRecording()">일기 쓰기</button>
        <button @click="stopListening(); stopRecording(); toggleVideoStream();">일기 종료</button>
        <!-- 비디오 스트림 켜기/끄기 버튼 -->
        <button @click="toggleVideoStream">{{ isVideoStreamOn ? '비디오 켜기' : '비디오 끄기' }}</button>
      </div>
    </div>
    <!-- 모달 창 -->
    <div v-if="isModalVisible" class="modal">
      <div class="modal-content">
        <h1> 오늘의 일기 확인 </h1>
        <textarea v-model="transcribedText" style="width: 900px; height: 500px;"></textarea>
        <div style="width: 150px; justify-content: center;">
          <el-button type="success" @click="closeModal" size="large">완료</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { usePositionStore } from '@/stores/pet.js';
import { ElLoading } from 'element-plus'
import { onMounted, ref, nextTick } from 'vue';
import { useUserStore } from '@/stores/user.js'
import axios from 'axios'
import OpenAI from 'openai';
import AWS from 'aws-sdk';

const detectionOptions = {
  withLandmarks: true,
  withDescriptors: true,
  minConfidence: 0.7,
  MODEL_URLS: {
    Mobilenetv1Model:
      "https://raw.githubusercontent.com/ml5js/ml5-data-and-models/main/models/faceapi/ssd_mobilenetv1_model-weights_manifest.json",
    FaceExpressionModel:
      "https://raw.githubusercontent.com/justadudewhohacks/face-api.js/master/weights/face_expression_model-weights_manifest.json",
    TinyFaceLandmarkModel:
      // "https://raw.githubusercontent.com/justadudewhohacks/face-api.js/master/weights/face_landmark_68_model-weights_manifest.json",
      "https://raw.githubusercontent.com/justadudewhohacks/face-api.js/master/weights/tiny_face_detector_model-weights_manifest.json",
    FaceLandmark68TinyNet:
      "https://raw.githubusercontent.com/justadudewhohacks/face-api.js/master/weights/face_landmark_68_model-weights_manifest.json",
    FaceRecognitionModel:
      "https://raw.githubusercontent.com/justadudewhohacks/face-api.js/master/weights/face_recognition_model-weights_manifest.json",
  },
};

export default {
  created() {
    const petStore = usePositionStore();

    // 스토어의 position 상태를 반응형으로 불러오기
    this.$watch(
      () => petStore.position,
      (newPosition) => {
        this.position = { ...newPosition };
      },
      { deep: true } // 객체 내부의 변화도 감지하기 위해 deep 옵션 사용
    );
  },
  // props: {
  //   petPosition: Object, // 상위 컴포넌트에서 전달받는 펫의 위치 데이터
  // },
  computed: {
    bubbleStyle() {
      return {
        left: `${this.position.x - 40}px`,
        top: `${this.position.y - 70}px`, // 펫의 위에 위치하도록 조정
        position: 'fixed', // 필요에 따라 추가

        // 기타 스타일 설정
      };
    },
  },
  setup() {
    const userInfo = ref({})



    const API_URL = ref('https://i10d207.p.ssafy.io:8443/api')

    onMounted(() => {


      const userStore = useUserStore()
      API_URL.value = userStore.API_URL
      return { userInfo, API_URL };

    });

  },
  mounted() {
    this.videoElement = this.$refs.videoElement; // DOM이 준비된 후 videoElement를 참조
    // this.setupVideoRecorder();
    this.loadModels();
    // if (this.videoElement) {
    //   this.setupVideoRecorder();
    // } else {
    //   console.error('비디오 요소를 찾을 수 없습니다.');
    // }
  },
  data() {
    return {
      position: { x: 850, y: 380 },
      messageTimeout: null,
      messageCooldown: null,
      displayText: ref(''),
      displayMessage: false,
      textDisplayed: false,
      videoElement: null,
      detectionOptions,
      recognition: null,
      transcribedText: '',
      interimTranscript: '',
      talk: "안녕! 오늘의 일기를 쓸래?",
      isModalVisible: false,
      videoMediaRecorder: null,
      isVideoRecording: false,
      isVideoStreamOn: false,
      videoChunks: [],
      mediaRecorder: null,
      stream: null,
      fileUrl: ref(''),
      diaryNum: ref('')
    };
  },
  methods: {
    async loadModels() {
      await Promise.all([
        faceapi.nets.ssdMobilenetv1.loadFromUri(detectionOptions.MODEL_URLS.Mobilenetv1Model),
        faceapi.nets.tinyFaceDetector.loadFromUri(
          detectionOptions.MODEL_URLS.TinyFaceLandmarkModel
        ),
        faceapi.nets.faceLandmark68Net.loadFromUri(
          detectionOptions.MODEL_URLS.FaceLandmark68TinyNet
        ),
        faceapi.nets.faceRecognitionNet.loadFromUri(
          detectionOptions.MODEL_URLS.FaceRecognitionModel
        ),
        faceapi.nets.faceExpressionNet.loadFromUri(
          detectionOptions.MODEL_URLS.FaceExpressionModel
        ),
      ]);
      this.setupVideoRecorder();
    },

    async setupVideoRecorder() {
      try {
        const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
        this.videoElement.srcObject = stream;
        this.videoMediaRecorder = new MediaRecorder(stream);
        this.videoMediaRecorder.ondataavailable = event => {
          if (event.data.size > 0) {
            this.videoChunks.push(event.data);
          }
        };
        this.videoMediaRecorder.onstop = async () => {
          const blob = new Blob(this.videoChunks, { type: 'video/webm' });
          await this.uploadToS3(blob); // S3에 업로드
          this.videoChunks.length = 0; // 다음 녹화를 위해 videoChunks 배열 초기화
        };
      } catch (error) {
        console.error('비디오 접근 에러:', error);
      }
    },
    async onPlay() {
      const video = this.videoElement;
      const canvas = document.getElementById('videoCanvas');

      // 비디오 요소의 실제 크기를 사용하여 displaySize 설정
      const displaySize = { width: video.videoWidth, height: video.videoHeight };
      faceapi.matchDimensions(canvas, displaySize);

      // 이전에 감지된 표정을 저장할 변수
      let previousExpression = "";

      setInterval(async () => {
        const detections = await faceapi.detectAllFaces(video, new faceapi.TinyFaceDetectorOptions()).withFaceLandmarks().withFaceExpressions();
        const resizedDetections = faceapi.resizeResults(detections, displaySize);
        canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height);
        // faceapi.draw.drawDetections(canvas, resizedDetections);
        // faceapi.draw.drawFaceLandmarks(canvas, resizedDetections);
        // faceapi.draw.drawFaceExpressions(canvas, resizedDetections);

        // 각 얼굴의 표정에 대한 정보를 가져오고 가장 높은 확률의 표정을 출력합니다.
        resizedDetections.forEach((face) => {
          const expressions = face.expressions;
          const dominantExpression = this.getDominantExpression(expressions);

          // 이전에 감지된 표정과 다를 경우에만 출력
          if (dominantExpression !== previousExpression) {
            console.log(`Dominant Expression: ${dominantExpression}`);
            previousExpression = dominantExpression;
            if (this.displayMessage === false) {
              this.displayTextBasedOnExpression(dominantExpression);
            }

          }

        });
      }, 333);
    },
    postAnswer() {
      const userStore = useUserStore();
      axios({
        method: 'post',
        url: `${userStore.API_URL}/diary/video`,
        headers: {
          Authorization: `Bearer ${userStore.accessToken}`
        },
        data: {
          videoUrl: this.fileUrl // 업로드된 파일의 URL을 전송합니다.
        }
      })

        .then(res => {
          console.log('video send complete', res)
        })
        .catch(err => {
          console.log(err)
        })

    },
    // 주어진 표정 객체에서 가장 높은 확률을 가진 표정의 이름을 반환하는 메서드
    getDominantExpression(expressions) {
      let dominantExpression = "";
      let maxProbability = 0;

      Object.keys(expressions).forEach((expression) => {
        const probability = expressions[expression];
        if (probability > maxProbability) {
          maxProbability = probability;
          dominantExpression = expression;
        }
      });

      return dominantExpression;
    },
    // Display different texts based on dominantExpression
    displayTextBasedOnExpression(dominantExpression) {
      if (this.displayMessage || this.messageCooldown) {
        return;
      }
      switch (dominantExpression) {
        case 'happy':
          this.displayText = '오늘은 어떤 좋은 일 있었니?';
          this.displayMessage = true;
          console.log(this.displayText)
          break;
        case 'sad':
          this.displayText = '오늘 안 좋은 일이 있었니?';
          this.displayMessage = true;
          console.log(this.displayText)
          break;
        case 'angry':
          this.displayText = '오늘 안 좋은 일이 있었니?';
          this.displayMessage = true;
          console.log(this.displayText)
          break;
        case 'surprised':
          this.displayText = '오늘 많이 피곤 했나요?';
          this.displayMessage = true;
          console.log(this.displayText)
          break;
        default:
          this.displayText = ''
          this.displayMessage = false;
          console.log('noText')
          break
      }
      if (this.displayMessage) {
        // 5초 후 메시지 숨기기
        this.messageTimeout = setTimeout(() => {
          this.displayMessage = false;
          this.displayText = '';
          console.log('Hide message');

          // 메시지 숨김 후 1분 쿨다운
          this.messageCooldown = setTimeout(() => {
            this.messageCooldown = null;
            console.log('Cooldown ended');
          }, 6000);  // 1분 쿨다운
        }, 10000);  // 5초 후 메시지 숨김
      }
    },

    // 메모리 누수 방지를 위해 컴포넌트 제거 시 타이머 제거
    beforeDestroy() {
      if (this.messageTimeout) {
        clearTimeout(this.messageTimeout);
      }
      if (this.messageCooldown) {
        clearTimeout(this.messageCooldown);
      }
    },

    toggleVideoStream() {
      if (!this.isVideoStreamOn) {
        // 비디오 스트림을 끕니다.
        this.videoElement.srcObject.getTracks().forEach(track => track.stop());
        this.videoElement.srcObject = null;
      } else {
        // 비디오 스트림을 다시 켭니다.
        this.setupVideoRecorder();
      }
      this.isVideoStreamOn = !this.isVideoStreamOn;
    },
    startRecording() {
      if (this.videoMediaRecorder && this.videoMediaRecorder.state === 'inactive') {
        this.videoMediaRecorder.start();
        this.isVideoRecording = true;
      }
    },
    stopRecording() {
      if (this.videoMediaRecorder && this.videoMediaRecorder.state === 'recording') {
        this.videoMediaRecorder.stop();
        this.isVideoRecording = false;
      } else {
        console.log('비디오 녹화가 이미 중지되었거나 시작되지 않았습니다.');
      }
    },
    async uploadToS3(blob) {
      try {
        // AWS SDK 설정
        AWS.config.update({
          accessKeyId: 'AKIAYS2NVQEAPWSNKXES',
          secretAccessKey: import.meta.env.VITE_S3_ACCESS_KEY,
          region: 'ap-southeast-2'
        });

        // S3 객체를 생성합니다.
        const s3 = new AWS.S3();
        const today = new Date().toISOString().slice(0, 19);
        // 파일 업로드를 위한 설정을 정의합니다.
        const params = {
          Bucket: 'mademe',
          Key: `${today}recording.webm`, // 업로드된 파일의 이름
          Body: blob,
          ContentType: 'video/webm' // 파일의 컨텐츠 타입
        };

        // S3로 파일을 업로드합니다.
        const uploadResult = await s3.upload(params).promise();

        // 업로드된 파일의 URL을 저장합니다.
        this.fileUrl = uploadResult.Location;
        console.log(this.fileUrl)
      } catch (error) {
        console.error('S3 업로드 오류:', error);
      }
    },
    closeModal() {
      try {
        this.isModalVisible = false;
        this.videoElement.srcObject = null;
        this.sendToChatGPT(this.transcribedText);
        this.isVideoStreamOn = false;
        this.videoMediaRecorder.stop();
        this.isVideoRecording = false;

      } catch (err) {
        console.log('오류발생', err)
      }
    },
    async navigateToAnotherPage(value) {
      // 기다렸다가 받은 값(value)을 사용하여 페이지 이동
    },
    startListening() {
      if ('webkitSpeechRecognition' in window) {
        this.recognition = new webkitSpeechRecognition();
        this.recognition.continuous = true;
        this.recognition.interimResults = true;
        this.recognition.onstart = () => {
          this.talk = '오늘 하루는 어땠어?';
          console.log('음성 인식이 시작되었습니다.');
        };

        this.recognition.onresult = (event) => {
          this.interimTranscript = '';
          for (let i = event.resultIndex; i < event.results.length; i++) {
            const transcript = event.results[i][0].transcript;
            if (event.results[i].isFinal) {
              this.transcribedText += transcript + ' ';
            } else {
              this.interimTranscript += transcript;
            }
          }
          console.log('중간 결과:', this.interimTranscript);
        };

        this.recognition.onerror = (event) => {
          console.error('음성 인식 오류:', event.error);
        };

        this.recognition.onend = () => {
          this.isModalVisible = true; // 모달 창 표시
          console.log('음성 인식이 종료되었습니다.');
        };

        this.recognition.start();
      } else {
        console.error('이 브라우저에서는 음성 인식이 지원되지 않습니다.');
      }
    },
    stopListening() {
      if (this.recognition) {
        this.recognition.stop();
        console.log('음성 인식이 중지되었습니다.');
      }
    },
    async sendToChatGPT(text) { // ChatGPT API 요청을 위한 메소드
      const vm = this;
      const loadingInstance = ElLoading.service({ fullscreen: true, text: 'AI가 일기를 분석하고 있습니다.' });
      try {
        const openai = new OpenAI({
          apiKey: import.meta.env.VITE_API_URL,
          dangerouslyAllowBrowser: true
        });

        async function main() {
          try {
            const assistantId = "asst_aFJZlObiinQ8gKlbOgVnnZ8i";
            const assistantFilePath = "./assistant.json";
            const assistant = await openai.beta.assistants.retrieve(assistantId);
            console.log(
              `Hello there, I'm your personal assistant.Id:\n${assistantId}\n`
            );

            const thread = await openai.beta.threads.create();

            let continueAskingQuestion = true;

            const userQuestion = text;

            await openai.beta.threads.messages.create(thread.id, {
              role: "user",
              content: userQuestion,
            });

            const run = await openai.beta.threads.runs.create(thread.id, {
              assistant_id: assistantId,
            });

            // Imediately fetch run-status, which will be "in_progress"
            let runStatus = await openai.beta.threads.runs.retrieve(
              thread.id,
              run.id
            );

            while (runStatus.status !== "completed") {
              await new Promise((resolve) => setTimeout(resolve, 1000));
              runStatus = await openai.beta.threads.runs.retrieve(
                thread.id,
                run.id
              );

              if (runStatus.status === "requires_action") {
                const toolCalls =
                  runStatus.required_action.submit_tool_outputs.tool_calls;
                const toolOutputs = [];
                for (const toolCall of toolCalls) {
                  const functionName = toolCall.function.name;

                  console.log(
                    `This question requires us to call a function: ${functionName}`
                  );

                  const args = JSON.parse(toolCall.function.arguments);

                  const argsArray = Object.keys(args).map((key) => args[key]);

                  // Dynamically call the function with arguments
                  const output = await global[functionName].apply(null, [args]);

                  toolOutputs.push({
                    tool_call_id: toolCall.id,
                    output: output,
                  });
                }
                // Submit tool outputs
                await openai.beta.threads.runs.submitToolOutputs(
                  thread.id,
                  run.id,
                  { tool_outputs: toolOutputs }
                );
                continue; // Continue polling for the final response
              }

              // Check for failed, cancelled, or expired status
              if (["failed", "cancelled", "expired"].includes(runStatus.status)) {
                console.log(
                  `Run status is '${runStatus.status}'. Unable to complete the request.`
                );
                break;
              }
            }

            // Get the last assistant message from the messages array
            const messages = await openai.beta.threads.messages.list(thread.id);
            const lastMessageForRun = messages.data
              .filter(
                (message) =>
                  message.run_id === run.id && message.role === "assistant"
              )
              .pop();
            console.log(messages)
            if (lastMessageForRun) {
              const userStore = useUserStore();
              const gptjson = lastMessageForRun.content[0].text.value;
              const fixedJson = gptjson.replace(/,(\s*[}\]])/g, '$1');
              const result = fixedJson.replace(/\n|\r/g, "");
              console.log(userStore.accessToken)
              console.log(`${lastMessageForRun.content[0].text.value} \n`);
              console.log((JSON.parse(result)));
              console.log('data', (JSON.parse(result).data));
              const json = {
                "data": JSON.parse(result).data,
                "video": vm.fileUrl
              }
              console.log('json', json)
              axios({
                method: 'post',
                url: `${userStore.API_URL}/diary/gpt`,
                headers: {
                  Authorization: `Bearer ${userStore.accessToken}`
                },
                data: json
              })
                .then(res => {
                  const diaryId = res.data.diaryId;
                  loadingInstance.close();
                  vm.$router.push({ name: 'DiaryDetail', params: { id: diaryId } });
                  console.log('gptAnswer')
                })
                .catch(err => {
                  console.log(userStore.accessToken)
                  console.log(err)
                })
            } else if (
              !["failed", "cancelled", "expired"].includes(runStatus.status)
            ) {
              console.log("No response received from the assistant.");
            }

          } catch (error) {
            console.error(error);
            loadingInstance.close();
          }

        }
        main();
      } catch (error) {
        console.error('ChatGPT 요청 오류:', error);
      }
    },
    startVideoRecording() {
      if (this.videoMediaRecorder && this.videoMediaRecorder.state === 'inactive') {
        this.videoMediaRecorder.start();
        this.isVideoRecording = true;
      }
    },
    stopVideoRecording() {
      if (this.videoMediaRecorder && this.videoMediaRecorder.state === 'recording') {
        // 녹화 중지
        this.videoMediaRecorder.stop();
        this.isVideoRecording = false;

        // 비디오 스트림의 모든 트랙을 중지
        if (this.videoElement && this.videoElement.srcObject) {
          const tracks = this.videoElement.srcObject.getTracks();
          tracks.forEach(track => track.stop());
        }
      } else {
        console.log('비디오 녹화가 이미 중지되었거나 시작되지 않았습니다.');
      }
    },
  },
};
</script>
<style scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  /* 다른 요소 위에 표시되도록 z-index 설정 */
}

#videoContainer {
  position: relative;
}

#video,
#videoCanvas {
  position: absolute;
  top: 0px;
  left: 0px;
}

.message-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
  /* 필요에 따라 조정 */
  z-index: 1000;
}

.speech-bubble {
  position: relative;
  background: #f0f0f0;
  border-radius: .4em;
  padding: 10px;
  max-width: 300px;
  /* 필요에 따라 조정 */
  text-align: center;
}

.speech-bubble:after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 0;
  border: 15px solid transparent;
  border-top-color: #f0f0f0;
  border-bottom: 0;
  margin-left: -15px;
  margin-bottom: -15px;
}

.snow-background {
  height: 90vh;
  background-image: url('@/img/back6.png');
  background-size: cover;
  background-position: center;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.camera-box {

  background-color: rgba(255, 255, 255, 0.5);
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  margin: 0 auto;
  /* 가운데 정렬을 위한 margin 추가 */
}

.greeting-box p {
  margin: 0;
  padding: 0;
  color: #333;
  font-size: 1.5rem;
}

.greeting-box button {
  margin-top: 10px;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #f9f9f9;
  cursor: pointer;
}

.greeting-box button:hover {
  background-color: #e0e0e0;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  padding: 20px;
  border-radius: 50px;
  width: 1000px;
  height: 700px;
  text-align: center;
  background-color: #F9F7C9;
  align-items: center;
}

.talk-section {
  width: 70%;
  height: 23%;
  margin-top: 10px;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  margin: 0 auto;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.talk {
  font-family: "YJ_Obang";
  font-size: 40px;
}

.buttons {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
}

.buttons button {
  font-weight: bold;
  height: 50px;
  margin: 2px;
  padding: 10px;
  border-radius: 10px;
  background-color: #F9F7C9;
}
</style>