<template>
  <div>
    <button @click="toggleRecording">{{ isRecording ? '종료' : '시작' }}</button>
    <br><br>
    <audio ref="audioEl" controls></audio>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';

import axios from 'axios';

export default {
  setup() {
    const isRecording = ref(false);
    const audioEl = ref(null);
    const audioArray = ref([]);
    let mediaRecorder = null;

    const toggleRecording = async () => {
      if(!isRecording.value){
        const mediaStream = await navigator.mediaDevices.getUserMedia({audio: true});
        mediaRecorder = new MediaRecorder(mediaStream);

        mediaRecorder.ondataavailable = (event)=>{
          audioArray.value.push(event.data);
        }

        mediaRecorder.onstop = async ()=>{
        const blob = new Blob(audioArray.value, {"type": "audio/wav"});
        audioArray.value.splice(0);
        const blobURL = window.URL.createObjectURL(blob);
        audioEl.value.src = blobURL;
        audioEl.value.play();

        // Clova Speech API 요청 코드 추가
        let formData = new FormData();
        formData.append('media', blob);
        const res = await axios.post('https://clovaspeech-gw.ncloud.com/external/v1/6820/7881eddfe386c9831f86d5865409a071362181f34e97e0d9c0a6c574dd25de3e', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'X-CLOVASPEECH-API-KEY': '1377d300f8254766b464a13d95f63af0'
          }
        });

        console.log(res.data);
      }

        mediaRecorder.start();
        isRecording.value = true;

      }else{
        mediaRecorder.stop();
        isRecording.value = false;
      }
    }
    
    return {
      isRecording,
      audioEl,
      toggleRecording
    }
  }
}
</script>

