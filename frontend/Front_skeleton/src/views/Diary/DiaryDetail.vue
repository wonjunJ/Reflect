<template>
  <div style="background-color: #80BCBD;">
    <el-row>
      <el-col :span="8" class="sidebar">

        <div class="d2">
          <div class="d1">

          </div>
          <DiaryList></DiaryList>
        </div>

      </el-col>

      <el-col :span="16" class="content">
        <div>
          <h1 style="padding: 10px;">2024.01.04 일기</h1>
          <h3 style="padding: 10px;">Keyword #즐거움 #동료 #프로젝트</h3>
        </div>
        <div class="select-item">
          <el-menu default-active="1" class="el-menu" mode="horizontal" @select="handleSelect">
            <el-menu-item index="1" @click="changeMenuFalse(); changeVideoFalse();">일기</el-menu-item>
            <el-menu-item index="2" @click="changeMenuFalse(); changeVideoTrue();">동영상</el-menu-item>
            <el-menu-item index="3" @click="changeMenuTrue(); changeVideoFalse(); imgSelectImg()">사진</el-menu-item>
            <el-menu-item index="4" @click="changeMenuTrue(); changeVideoFalse(); imgSelectAI()">AI사진</el-menu-item>
          </el-menu>
          <div v-if="selectMenu === true" class="select-item">
            <el-upload class="upload-demo" :before-upload="beforeUpload">
              <el-button type="primary" @click="imgSelectImg();">파일 업로드</el-button>

            </el-upload>
            <el-button @click="getImageResponse(); imgSelectAI();">AI 사진생성</el-button>
            <el-button @click="uploadImage"> 사진 업로드 </el-button>
          </div>
        </div>

        <div class="content-detail">
          <div v-if="getimgUrl" class="show-item">
            <img :src="getimgUrl" alt="업로드 이미지" style="width: 300px; height: 300px;" />
          </div>
          <div v-if="previewUrl && selectMenu === true && imgSelect === true" class="show-item">

            <img :src="previewUrl" alt="업로드 이미지" style="width: 300px; height: 300px;" />
          </div>
          <div v-if="imageUrl && selectMenu === true && imgSelect === false" class="show-item">

            <img :src="imageUrl" alt="생성된 이미지" style="width: 300px; height: 300px;">
          </div>
          <div v-if="selectVideo === true" class="show-item">
            <video controls width="250">

              <source src="@/img/my_recording.webm" type="video/webm">
            </video>
          </div>
          <textarea class="textarea1 rounded-4" v-model="transcribedText" type="textarea" :rows="6"></textarea>
        </div>

      </el-col>
    </el-row>
  </div>
</template>

<script setup>
// import {useDiaryStore} from '@/stores/diary.js'
import DiaryList from "@/components/Diary/DiaryList.vue";
import axios from 'axios';
import { useUserStore } from '@/stores/user.js'
import { ref, watch, onMounted, computed } from "vue";
const transcribedText = ref("오늘은 참으로 활기찬 하루였다.햇살이 따스하게 내리쬐는 오후, 나는 오랜만에 마음먹고 집 근처의 공원으로 반려견 웰시코기 루키와 함께 산책을 나섰다.루키는 이미 문밖으로 나서는 순간부터 흥분을 감추지 못하고, 꼬리를 흔들며 기쁨을 표현했다.공원에 도착하자마자, 루키는 마치 오랫동안 기다려온 듯 자유롭게 뛰어다니기 시작했다.나도 그 모습을 보며 자연스레 미소가 지어졌다.")
const getimgUrl = ref(null);
const previewUrl = ref(null);
const imageUrl = ref(null);
const videoUrl = ref('@/img/my_recording.webm');
const selectMenu = ref(false);
const selectVideo = ref(false);
const imgSelect = ref(null);
const userStore = useUserStore()
const API_URL = userStore.API_URL

const userInfo = ref({})
const userImg = ref({})
console.log(videoUrl.value)
// const diaryStore = useDiaryStore();
// 텍스트에서 마침표 뒤에 줄바꿈을 추가하는 함수
const addNewLines = (text) => {
  return text.replace(/\.(\S)/g, '.\n$1');
};
const changeMenuTrue = () => {
  return selectMenu.value = true;
}
const changeMenuFalse = () => {
  return selectMenu.value = false;
}
const changeVideoTrue = () => {
  console.log(videoUrl)
  return selectVideo.value = true;
}
const changeVideoFalse = () => {
  return selectVideo.value = false;
}
const imgSelectImg = () => {
  return imgSelect.value = true;
}
const imgSelectAI = () => {
  return imgSelect.value = false;
}
onMounted(() => {
  axios({
    method: 'get',
    url: `${API_URL}/diaryDetail/`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(res => {
      userInfo.value = res.data
    })
    .catch(err => {
      // console.log(err)
    })
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

// 파일 업로드 전 호출되는 메서드
const beforeUpload = (file) => {
  const reader = new FileReader();

  reader.onload = (e) => {
    previewUrl.value = e.target.result; // 미리보기 URL 설정
  };
  reader.readAsDataURL(file);
  return true;
};
const uploadImage = async () => {
  if (previewUrl.value) {
    try {
      const Image = previewUrl.value;
      const reader = new FileReader();
      const base64Image = reader.readAsDataURL(Image);

      const response = await axios.post(`${API_URL}/diaryDetail/img`, {
        image: base64Image,
      });
      console.log('서버 응답:', response.data);
    } catch (error) {
      console.error('에러 발생:', error);
    }
  }
  else if (imageUrl.value) {
    try {
      const Image = imageUrl.value;
      const reader = new FileReader();
      const base64Image = reader.readAsDataURL(Image);

      const response = await axios.post(`${API_URL}/diaryDetail/img`, {
        image: base64Image,
      });
      console.log('서버 응답:', response.data);
    } catch (error) {
      console.error('에러 발생:', error);
    }
  }
  else {
    alert('파일을 선택해주세요.');
  }
}


const decodeBase64ToImage = function (base64String) {
  // Base64 문자열에서 데이터 부분을 추출합니다.
  const base64Data = base64String.split(',')[1];

  // Base64를 디코딩하여 이진 데이터를 얻습니다.
  const binaryString = atob(base64Data);

  // 이진 데이터를 ArrayBuffer로 변환합니다.
  const arrayBuffer = new ArrayBuffer(binaryString.length);
  const uint8Array = new Uint8Array(arrayBuffer);
  for (let i = 0; i < binaryString.length; i++) {
    uint8Array[i] = binaryString.charCodeAt(i);
  }

  // ArrayBuffer를 Blob으로 변환합니다.
  const blob = new Blob([uint8Array], { type: 'image/jpeg' }); // 이미지 유형에 따라 변경 가능

  return blob;
}


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

const putImg = function (obj) {
  axios({
    method: 'put',
    url: `${API_URL}/diaryDetail/img`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    },
    data: {
      image: obj.image,
    }
  })
    .then(res => {
      //userImg.value.find 밑에꺼 참고해서 수정해야됨
      //(item => item.questionId === res.data.questionId).answer = res.data.answer
    })
    .catch(err => {
      console.log(err)
    })
}
</script>
  
<style scoped>
.d1 {
  height: 120px;
  flex-shrink: 0;
  border-radius: 0px 0px 70px 0px;
  background: #80BCBD;
  box-shadow: 0px 4px 0px 0px rgba(0, 0, 0, 0.25);
  align-content: center;
  justify-content: center;

}

.d2 {
  height: 100vh;
  align-content: center;
  justify-content: center;
  top: 58px;

  flex-shrink: 0;
  background: #FFF;
}

.p1 {
  font-size: x-large;
  margin-top: 70px;
  margin-bottom: 0px;
}

.button-group {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  /* 버튼 위쪽 여백 */
}

.upload-demo {
  display: flex;
  align-items: center;
}

.el-col {}

.sidebar {
  text-align: center;

}

.content {
  /* Rectangle 690 */
  width: 949px;

  left: 490px;
  top: 80px;

  background: #FFFFFF;

  border-radius: 70px 0px 0px 0px;
  box-shadow: 0px 4px 0px 0px rgba(0, 0, 0, 0.25);

  flex-shrink: 0;
  display: flex;
  /* Flexbox 레이아웃 사용 */
  flex-direction: column;
  /* 요소들을 세로로 정렬 */
  align-items: center;
  /* 수평 가운데 정렬 */

  padding: 50px;
}

.content-detail {
  background-color: #F9F7C9;
  border-radius: 30px;
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

@media (max-width: 1048px) {
  .textarea1 {
    width: 400px;
    /* 브라우저 크기가 768px 이하일 때 너비를 반으로 줄임 */
  }
}

@media (max-width: 648px) {
  .textarea1 {
    width: 300px;
    /* 브라우저 크기가 768px 이하일 때 너비를 반으로 줄임 */
  }
}

.el-menu-demo {
  /* 스타일링 예시 */
  background-color: #ffffff;
  border-bottom: 1px solid #ebeef5;
}

.el-menu-item {
  /* 스타일링 예시 */
  font-size: 16px;
  color: #303133;
  white-space: nowrap;
  /* 텍스트가 줄바꿈 되지 않도록 설정 */
  overflow: visible;
  /* 컨테이너 넘치는 내용도 보이도록 설정 */
  text-overflow: clip;
  /* 텍스트가 넘칠 때 잘리게 설정 */
}

.select-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.show-item {
  margin-top: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
