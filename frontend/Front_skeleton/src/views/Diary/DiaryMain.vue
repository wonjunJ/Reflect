<template>
    <div>
      <button @click="startListening">음성 인식 시작</button>
      <button @click="stopListening">음성 인식 종료</button>
      <p>{{ transcribedText }}</p>
    </div>
</template>

<script>
export default {
data() {
    return {
    recognition: null,
    transcribedText: '',
    };
},
methods: {
    startListening() {
    // 브라우저 지원 여부 확인
    if ('webkitSpeechRecognition' in window) {
        this.recognition = new webkitSpeechRecognition();
        this.recognition.continuous = true;
        this.recognition.interimResults = true;

        this.recognition.onstart = () => {
        console.log('음성 인식이 시작되었습니다.');
        };

        this.recognition.onresult = (event) => {
        let interimTranscript = '';
        for (let i = event.resultIndex; i < event.results.length; i++) {
            const transcript = event.results[i][0].transcript;
            if (event.results[i].isFinal) {
            this.transcribedText += transcript + ' ';
            } else {
            interimTranscript += transcript;
            }
        }
        console.log('중간 결과:', interimTranscript);
        };

        this.recognition.onerror = (event) => {
        console.error('음성 인식 오류:', event.error);
        };

        this.recognition.onend = () => {
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
    }
    },
},
};
</script>