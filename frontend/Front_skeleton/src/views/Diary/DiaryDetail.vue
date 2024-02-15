<template>
  <div style="background-color: #80BCBD;">
    <el-row>
      <el-col :span="8" class="sidebar">
        <div class="d2">
          <div class="d1">
          </div>
          <div class="container">
            <div>
              <h3 class="h3-1">My Diary</h3>
            </div>
            <el-row class="row rounded-4">
              <el-col class="col1">
                <div class="list" v-for="entry in responseData" :key="entry.diaryId">
                  <div class="entry1" @click="navigateToDetailPage(entry.diaryId)">
                    <span class="date">{{ entry.createDate }}</span>
                    <div class="keywords">
                      <span v-for="word in entry.keyword" :key="word">{{ word }} </span>
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>

      </el-col>
      <el-col :span="16" class="content">
        <div>
          <h1 style="padding: 10px;">{{ userInfo.createDate }}</h1>
          <h3 style="padding: 10px;">{{ userInfo.keyword }}</h3>
        </div>
        <div class="select-item">
          <el-menu default-active="1" class="el-menu" mode="horizontal">
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
            <el-button @click="uploadImage(userInfo.diaryId)"> 사진 업로드 </el-button>
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
          <div v-if="userInfo.diaryImg && selectMenu === true && imgSelect === true" class="show-item">
            <img :src='userInfo.diaryImg' alt="ai이미지" style="width: 400px; height: 300px;">
          </div>
          <div v-if="selectVideo === true" class="show-item">
            <video controls width="400">
              <source :src="userInfo.video" type="video/webm">
            </video>
          </div>
          <textarea class="textarea1 rounded-4" v-model="userInfo.content" type="textarea" :rows="6"></textarea>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import axios from 'axios';
import { useUserStore } from '@/stores/user.js'
import { ref, watch, onMounted, computed, onUpdated } from "vue";
import { useRoute, useRouter } from 'vue-router'
import { ElLoading } from 'element-plus'

const route = useRoute()
const router = useRouter()
const getimgUrl = ref(null);
const previewUrl = ref(null);
const imageUrl = ref(null);
const videoUrl = ref('');
const selectMenu = ref(false);
const selectVideo = ref(false);
const imgSelect = ref(null);
const userStore = useUserStore()
const API_URL = userStore.API_URL


let responseData = ref([])
const userInfo = ref({})
const userImg = ref({})

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
async function fetchDiaryData() {
  await axios({
    method: 'get',
    url: `${API_URL}/DiaryDetail/${route.params.id}`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(response => {
      console.log(response.data)
      responseData.value = response.data;
      for (const data of responseData.value) {
        if (data.diaryId == route.params.id) {
          userInfo.value = data;
          console.log(userInfo.value);
          break; // 일치하는 데이터를 찾았으면 루프를 중단합니다.
        }
      }
      console.log('userInfo', userInfo);
    })
    .catch(err => {
      console.log(err)
    })
  // 컴포넌트가 마운트되었을 때 초기 텍스트 처리
  userInfo.content = addNewLines(userInfo.content);
}
onMounted(async () => {
  console.log(route.params.id);
  fetchDiaryData();
});

// 파일 업로드 전 호출되는 메서드
const beforeUpload = (file) => {
  const reader = new FileReader();

  reader.onload = (e) => {
    previewUrl.value = e.target.result; // 미리보기 URL 설정
  };
  reader.readAsDataURL(file);
  //return true;
};
const uploadImage = async (diaryId) => {
  if (previewUrl.value) {
    try {
      const Image = previewUrl;
      const json = {
        "id": diaryId,
        "diaryImg": Image.value
      }
      await axios({
        method: 'post',
        url: `${API_URL}/diary/img`,
        headers: {
          Authorization: `Bearer ${userStore.accessToken}`
        },
        data: json
      })
        .then(response => {
          console.log(response)
        })
        .catch(err => {
          console.log(err)
        })
      console.log('서버 응답:', Image);
      console.log('서버 응답:', Image.value);
      console.log('id:', diaryId);
    } catch (error) {
      console.error('에러 발생:', error);
    }
  }
  else if (imageUrl.value) {
    try {
      const Image = imageUrl.value;
      console.log('response', responseData)
      console.log(Image)
      const json = {
        "id": diaryId,
        "diaryImg": Image
      }
      console.log('json', json)
      await axios({
        method: 'post',
        url: `${API_URL}/diary/img`,
        headers: {
          Authorization: `Bearer ${userStore.accessToken}`
        },
        data: json
      })
        .then(response => {
          console.log(response)
        })
        .catch(err => {
          console.log(err)
        })
    } catch (error) {
      console.error('에러 발생:', error);
    }
  }
  else {
    alert('파일을 선택해주세요.');
  }
}

function navigateToDetailPage(diaryId) {
  axios({
    method: 'get',
    url: `${API_URL}/DiaryDetail/${diaryId}`,
    headers: {
      Authorization: `Bearer ${userStore.accessToken}`
    }
  })
    .then(response => {
      console.log(response.data)
      responseData = response.data;
      for (const data of responseData) {
        if (data.diaryId == diaryId) {
          userInfo.value = data;
          location.reload();
          // console.log(userInfo.value);
          break; // 일치하는 데이터를 찾았으면 루프를 중단합니다.
        }
      }
      console.log(userInfo);
    })
    .catch(err => {
      console.log(err)
    })

  router.push({ name: 'DiaryDetail', params: { id: diaryId } });
}

const getImageResponse = async () => {
  try {
    const loadingInstance = ElLoading.service({ fullscreen: true, text: 'AI 이미지 생성 중 입니다.' });
    const prompt = `${userInfo.value.content} 이 일기 내용을 바탕으로 그림을 그려줘.`;
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
    loadingInstance.close();
    console.log('DALL-E 결과: ', imageUrl.value);
    console.log(prompt)
  } catch (error) {

    console.error('DALL-E: 🚨 에러가 발생했습니다.', error.response ? error.response.data : error);

  }
};
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
  height: 860px;
  display: block;
  max-width: 100%;
  background-color: #F9F7C9;
  line-height: 2;
  font-size: large;
  padding: 50px;
}

@media (max-width: 1048px) {
  .textarea1 {
    width: 520px;
    /* 브라우저 크기가 768px 이하일 때 너비를 반으로 줄임 */
  }
}

@media (max-width: 648px) {
  .textarea1 {
    width: 420px;
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

.h3-1 {
  height: 60px;
  flex-shrink: 0;
  color: #000;
  text-align: center;
  font-family: "ZCOOL KuaiLe";
  font-size: 40px;
  font-style: normal;
  font-weight: 400;
  background-color: #D5F0C1;
  border-radius: 20px;
}

.col1 {
  padding: 10px;
  width: 415.805px;
  flex-shrink: 0;
  border-radius: 20px;
  align-content: center;
}

.container {
  flex-shrink: 0;
  padding-bottom: 50px;
  padding-top: 50px;
  /* 일기목록 배경 */
  position: relative;
  width: 90%;
  height: 785.77px;
  left: 30px;
  top: 50px;

  background: #f1f5c3;
  border-radius: 20px;
  margin-right: 100px;
}

.row {
  border: 1;
  background-color: #F9F7C9;
}

.list {
  height: auto;
  flex-shrink: 0;
  background: #F9F2E4;
  margin: 30px;
}

.entry1 {
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.entry1:hover {
  background-color: #e0e0e0;
}

.date {
  font-weight: bold;
  margin-bottom: 5px;
  /* display: block; */
}

.keywords span {
  margin-right: 2px;
  /* display: inline-block; */
}
</style>
