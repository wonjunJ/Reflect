<template>
  <div>
    <button @click="toggleRecording">{{ isRecording ? '종료' : '시작' }}</button>
    <br><br>
    <audio ref="audioEl" controls></audio>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';

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

        mediaRecorder.onstop = ()=>{
          const blob = new Blob(audioArray.value, {"type": "audio/wav"});
          audioArray.value.splice(0);
          const blobURL = window.URL.createObjectURL(blob);
          audioEl.value.src = blobURL;
          audioEl.value.play();
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
