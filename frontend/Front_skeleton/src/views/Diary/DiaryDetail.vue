<template>
  <div>
  <el-row>
    <el-col :span="6" class="sidebar">
      <div class="d1">
        <p>아오상수시치</p>
      </div>
      <div class="d2">
        <DiaryList></DiaryList>
      </div>
      
    </el-col>

    <el-col :span="18" class="content">
      <div style="align-items: center;">
        <h1>2024.01.28 일기</h1>
        <h3>Keyword  #즐거움 #동료 #프로젝트</h3>
        <el-upload
        class="upload-demo"
        :before-upload="beforeUpload"
        >
        <el-button type="primary" :disabled="uploading">파일 업로드</el-button>
        
        </el-upload>
        <el-button @click="getImageResponse">AI 사진생성</el-button>
        <div v-if="previewUrl">
          <h3>업로드 이미지 미리보기</h3>
          <img :src="previewUrl" alt="업로드 이미지" style="width: 500px; height: 500px;" />
        </div>
        
        <div v-if="imageUrl">
          <h3>생성된 이미지 미리보기</h3>
          <img :src="imageUrl" alt="생성된 이미지" style="width: 500px; height: 500px;">
        </div>
      </div>
      
      <textarea class="textarea1 rounded-4" v-model="transcribedText" type="textarea" :rows="6"></textarea>
    </el-col>
  </el-row>
  </div>
</template>

<script setup>
import DiaryList from "@/components/Diary/DiaryList.vue";
import axios from 'axios';
import { ref, watch, onMounted } from "vue";
const transcribedText = ref("오늘은 새로운 도전이 기대되는 하루였다.업무에서는 전날의 성과를 바탕으로 협업과 아이디어 공유를 통해 더 나은 결과물을 이끌어내고자 노력했다.개인 시간에는 독서와 기술 학습에 집중하며 더 의미 있는 하루를 만들기 위한 계획을 세웠다.저녁에는 오랜만에 친구들과의 만남으로 즐거운 시간을 보내고, 감사한 마음으로 하루를 마무리했다.내일은 더 나은 성장을 위한 새로운 기회일 것이다.")
const previewUrl = ref(null);
const imageUrl = ref(null); 
let uploading = false;

// 텍스트에서 마침표 뒤에 줄바꿈을 추가하는 함수
const addNewLines = (text) => {
  return text.replace(/\.(\S)/g, '.\n$1');
};

onMounted(() => {
  // 컴포넌트가 마운트되었을 때 초기 텍스트 처리
  transcribedText.value = addNewLines(transcribedText.value);
});

watch(transcribedText, (newValue, oldValue) => {
  // 마침표('.') 뒤에 바로 공백이 오지 않는 경우에 줄바꿈 문자('\n')을 추가합니다.
  // newValue와 oldValue가 같지 않을 때만 처리하도록 함으로써 무한 루프를 방지합니다.
  if (newValue !== oldValue) {
    transcribedText.value = newValue.replace(/\.(\S)/g, '.\n$1');
  }
});
// 파일 업로드 성공 시 호출되는 메서드
// const handleSuccess = (response) => {
//   console.log('파일 업로드 성공:', response);
//   // 업로드 완료 후 필요한 로직을 추가할 수 있습니다.
//   uploading = false;
// };

// 파일 업로드 전 호출되는 메서드
const beforeUpload = (file) => {
  const reader = new FileReader();

  reader.onload = (e) => {
    previewUrl.value = e.target.result; // 미리보기 URL 설정
  };
  reader.readAsDataURL(file);
  // uploading = true;
  return true;
};

let generatedImage = null;

const getImageResponse = async () => {
  try {
    const prompt = `${transcribedText.value} 이 일기 내용을 바탕으로 그림을 그려줘.`;
    const response = await axios.post('https://api.openai.com/v1/images/generations', {
      model: "dall-e-3",
      prompt: prompt,
      n: 1, // 생성할 이미지의 수
      size: '1024x1024', // 이미지의 크기
    }, {
      headers: {
        'Authorization': `Bearer ${import.meta.env.VITE_DALLE_API_KEY}`, // API 키를 사용하여 인증
      }
    });
    
    imageUrl.value = response.data.data[0].url; // 응답에서 이미지 URL을 추출하고 저장
    console.log('DALL-E 결과: ', imageUrl.value);
    console.log(prompt)
  } catch (error) {
    
    console.error('DALL-E: 🚨 에러가 발생했습니다.', error.response ? error.response.data : error);

  }
};
</script>
  
<style>
.d1 {
  width: 492px;
  height: 108px;
  flex-shrink: 0;
  border-radius: 0px 0px 70px 0px;
  background: #80BCBD;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
}
.d2 {
  width: 528px;
  height: 1196px;
  flex-shrink: 0;
  background: #FFF;
}
.upload-demo {
  display: flex;
  align-items: center;
}
.el-col {
  
}
.sidebar {
  background-color: #D5F0C1;
  text-align: center;
}
.content {
  display: flex; /* Flexbox 레이아웃 사용 */
  flex-direction: column; /* 요소들을 세로로 정렬 */
  align-items: center; /* 수평 가운데 정렬 */
  justify-content: center; /* 수직 가운데 정렬 */
  padding: 50px;
  background-color: #AAD9BB;
}
.textarea1 {
  margin: auto;
  width: 683px;
  height: 560px;
  display: block;
  max-width: 100%;
  background-color: #F9F7C9;
  line-height: 2;
  font-size: large;
  padding: 50px;
}
</style>
