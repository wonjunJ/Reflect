<template>
  <div ref="snowContainer" class="snow-background">
    <div class="greeting-box">
      <p>{{ talk }}</p>
      <button @click="startListening(); startRecording()">일기 쓰기</button>
      <button @click="stopListening(); stopRecording(); toggleVideoStream();">일기 종료</button>
      <br>
      <video ref="videoElement" autoplay muted></video>
      <!-- 비디오 스트림 켜기/끄기 버튼 -->
      <button @click="toggleVideoStream">{{ isVideoStreamOn ? '비디오 켜기' : '비디오 끄기' }}</button>
    </div>
    <!-- 모달 창 -->
    <div v-if="isModalVisible" class="modal">
      <div class="modal-content">
        <h1> 오늘의 일기 확인 </h1>
        <textarea v-model="transcribedText" style="width: 900px; height: 500px;"></textarea>
        <div style="width: 150px; justify-content: center;">
        <el-button type="success" @click="closeModal" size="large" >완료</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue';
import OpenAI from 'openai';
import AWS from 'aws-sdk';

export default {
  setup() {
    const snowContainer = ref(null);
    
    onMounted(() => {
      const Snow = new SnowCanvas();
      Snow.createSnowFlakes(1000);

      const canvas = Snow.getCanvas(); // return HTMLCanvasElement
      snowContainer.value.appendChild(canvas);
    });
    
    return { snowContainer };
  },
  mounted() {
    this.videoElement = this.$refs.videoElement; // DOM이 준비된 후 videoElement를 참조
    if (this.videoElement) {
      this.setupVideoRecorder();
    } else {
      console.error('비디오 요소를 찾을 수 없습니다.');
    }
  },
  data() {
    return {
      recognition: null,
      transcribedText: '',
      interimTranscript: '',
      talk: "안녕! 오늘의 일기를 쓸래?",
      isModalVisible: false,
      videoElement: null,
      videoMediaRecorder: null,
      isVideoRecording: false,
      isVideoStreamOn: false,
      videoChunks: [],
      mediaRecorder: null,
      stream: null,
      fileUrl: null,
    };
  },
  methods: {
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

        // 파일 업로드를 위한 설정을 정의합니다.
        const params = {
          Bucket: 'mademe',
          Key: 'recording.webm', // 업로드된 파일의 이름
          Body: blob,
          ContentType: 'video/webm' // 파일의 컨텐츠 타입
        };

        // S3로 파일을 업로드합니다.
        const uploadResult = await s3.upload(params).promise();

        // 업로드된 파일의 URL을 저장합니다.
        this.fileUrl = uploadResult.Location;
      } catch (error) {
        console.error('S3 업로드 오류:', error);
      }
    },
    closeModal() {
      try {
        this.isModalVisible = false;
        this.videoElement.srcObject = null;
        //this.sendToChatGPT(this.transcribedText);
        this.isVideoStreamOn = false;
        this.videoMediaRecorder.stop();
        this.isVideoRecording = false;
        this.$router.push({ name: 'DiaryDetail' });
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
        try {
        const openai = new OpenAI({
            apiKey: import.meta.env.VITE_API_URL,
            dangerouslyAllowBrowser: true
        });

        async function main() {
        try {
            const assistantId = "asst_oodV12lRGyeVQ5Qk0Jd9AxI6";
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
            console.log(`${lastMessageForRun.content[0].text.value} \n`);
          } else if (
            !["failed", "cancelled", "expired"].includes(runStatus.status)
          ) {
            console.log("No response received from the assistant.");
          }
          
        } catch (error) {
            console.error(error);
        }
        }
        main();
        } catch(error) {
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
.snow-background {
  background-image: url('@/img/winter2.png');
  background-size: cover;
  background-position: center;
}

.greeting-box {
  position: absolute;
  top: 10%;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(255, 255, 255, 0.5);
  padding: 20px;
  border-radius: 10px;
  text-align: center;
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
  transition: background-color 0.3s;
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
</style>