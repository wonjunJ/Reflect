<template>
  <div v-if="isModalOpen" class="modal">
    <div class="create-container">
      <div class="room-back"></div>
      <div class="create-top">
        <p>방 만들기</p>  
      </div>

        <!-- 썸네일 사진 -->
        <div class="room-img">
          <div class="preview-img" v-if="previewUrl">
            <img :src="previewUrl" alt="업로드 이미지" @error="replaceImg" style="width:250px;" />
          </div>
          <div v-else>
            <img src="@/assets/defaultRoom.jpg" alt="기본 이미지" style="width:15rem;"/>
          </div>
          <div class="up-img">
            <el-upload class="upload-demo" :before-upload="beforeUpload">
              <div class="btn-container">
                <a class="img-btn cyan" :disabled="uploading">사진 업로드</a>
              </div>
            </el-upload>
          </div>
        </div>

        <!-- 대화타입 백문백답 -->
        <div class="type-hundred">
          <button type="button" class="btn btn-primary" @click="writeQuestion">백문백답</button>
        </div>

        <!-- 대화타입 자유주제 -->
        <div class="type-free">
          <button type="button" class="btn btn-primary" @click="writeHashtag">자유주제</button>
        </div>


        <!-- 방 설정 버튼 -->
        <div class="room-setting">
          <button type="button" class="btn btn-primary" @click="Secret">비밀의 방</button>
          <button type="button" class="btn btn-primary" @click="Mask">가면쓰기</button>
          <button type="button" class="btn btn-primary" @click="Anonymous">익명모드</button>
        </div>

          <!-- 방 제목 -->
          <div class="room-title">
              <label for="title-label">방 제목</label>
              <input type="text" v-model.trim="roomTitle" class="room-input">
          </div>

          <!-- 방 한줄 설명 -->
          <div class="room-describe">
              <label for="describe-label">한 줄 설명</label>
              <input type="text" v-model.trim="roomDescribe" class="room-input">
          </div>


          <!-- 자유주제/100문100답에 따라 다르게 나옴 -->
          
          <!-- 자유주제 --><!-- 해시태그  -->
          <div v-if="isFree" class="room-hashtag">
            <Tagify />
          </div>

          
          <!-- 백문백답 --><!-- 질문선택-->
          <div v-if="isHundred" class="select-hundred" >
              <label for="questions" class="qestions-label">질문  </label>
              <select class="question-select" id="questions" required="required">
                <option value="" selected disabled hidden>질문을 선택해주세요.</option>
                <option v-for="question in questionlist" :key="question.pk" :value="question.id">
                  {{ question.id }}
                </option>
              </select>
          </div>  
          
          <!-- 인원수 설정 칸 -->
          <div class="member-limit">
            <p>인원수</p>
            <MemberLimitSlider />
          </div>


      <div class="create-bottom">
        <!-- 버튼 -->
        <button type="button" class="btn btn-primary" @click="goRoom">방 만들기</button>
        <button type="button" class="btn btn-secondary" @click="closeModal">닫기</button>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref} from 'vue'
import {useRouter} from 'vue-router'


const router = useRouter()

const { isModalOpen } = defineProps(['isModalOpen'])
const emit = defineEmits()
const isHundred = ref(true)
const isFree = ref(false)
const questions = ref(null)
const previewUrl = ref(null);
let uploading = false;
const roomTitle = ref(null)
const roomDescribe = ref(null)
const hashtag = ref(null)
const isAnonymous = ref(false)
const isMask = ref(false)
const isSecret = ref(false)
const memberNum = ref(null)

  


// 파일 업로드 전 호출되는 메서드
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/'); // 이미지 파일 여부 확인

  if (!isImage) {
    // 이미지 파일이 아닌 경우
    alert('이미지 파일만 업로드할 수 있습니다.');
    return false; // 업로드 중지
  }

  const reader = new FileReader();

  reader.onload = (e) => {
    previewUrl.value = e.target.result; // 미리보기 URL 설정
  };
  reader.readAsDataURL(file);
  // uploading = true;
  return true;
};

const replaceImg = (e) => {
  import('@/assets/defaultRoom.jpg').then((image) => {
    e.target.src = image.default;
  });
};

const writeQuestion = () => {
  isHundred.value = true
  isFree.value = false
}

const writeHashtag= () => {
  isHundred.value = false
  isFree.value = true
}


const goRoom = () => {
  const roomInfoData = {
    isHundred: isHundred.value,
    isFree: isFree.value,
    questions: questions.value,
    previewUrl: previewUrl.value,
    roomTitle: roomTitle.value,
    roomDescribe: roomDescribe.value,
    hashtag: hashtag.value,
    isAnonymous: isAnonymous.value,
    isMask: isMask.value,
    isSecret: isSecret.value,
    memberNum: memberNum.value,
  };

  // RoomWaitingView.vue로 이동
  // route.push()를 사용하여 라우터를 변경하고 roomInfo를 query parameter로 전달
  router.push({ name: 'waiting', query: { roomInfo: JSON.stringify(roomInfoData) } });
}; 




const closeModal = () => {
  previewUrl.value = null;
  questions.value = ''
  emit('closeModal')
};



const questionlist = [
  { pk: 1, id: '내 이름의 뜻은?' },
  { pk: 2, id: '내 생일은?' },
  { pk: 3, id: '내 키는?' },
  { pk: 4, id: '내 최근 관심사는?' },
  { pk: 5, id: '집에서 심심할 때 하는 일은?' },
  { pk: 6, id: '아침에 일어나서 제일 먼저 하는 일은?' },
  { pk: 7, id: '내가 좋아하는 가수는?' },
  { pk: 8, id: '내가 좋아하는 배우는?' },
  { pk: 9, id: '요즘 자주 듣는 노래는?' },
  { pk: 10, id: '내 인생 노래는?' },
  { pk: 11, id: '내 인생 드라마는?' },
  { pk: 12, id: '넷플릭스에서 추천하는 것은?' },
  { pk: 13, id: '내가 좋아하는 음식은?' },
  { pk: 14, id: '내가 싫어하는 음식은?' },
  { pk: 15, id: '내 이상형은?' },
  { pk: 16, id: '나랑 친해지는 방법은?' },
  { pk: 17, id: '요즘 가장 하고 싶은 것은?' },
  { pk: 18, id: '연상 vs 동갑 vs 연하 중 선호하는 것은?' },
  { pk: 19, id: '내 목표 또는 꿈은?' },
  { pk: 20, id: '자기 전에 마지막으로 하는 일은?' },
  { pk: 21, id: '내 MBTI는?' },
  { pk: 22, id: '내 습관이나 버릇은?' },
  { pk: 23, id: '내 취미는?' },
  { pk: 24, id: '내 특기는?' },
  { pk: 25, id: '내가 좋아하는 계절은?' },
  { pk: 26, id: '낮과 밤 중 내가 가장 활발한 시간은?' },
  { pk: 27, id: '즐겨보는 유튜버는?' },
  { pk: 28, id: '내가 좋아하는 색깔은?' },
  { pk: 29, id: '내가 좋아하는 과일은?' },
  { pk: 30, id: '내가 좋아하는 반찬은?' },
  { pk: 31, id: '지금 카톡 프로필 사진은?' },
  { pk: 32, id: '내 성격을 한 마디로 표현하면?' },
  { pk: 33, id: '가장 오래 연애한 기간은?' },
  { pk: 34, id: '가장 짧게 연애한 기간은?' },
  { pk: 35, id: '잠이 안 올 때 하는 것은?' },
  { pk: 36, id: '내가 좋아하는 라면은?' },
  { pk: 37, id: '답장 평균 시간은?' },
  { pk: 38, id: '아침식사 하는지 여부는?' },
  { pk: 39, id: '낯가림이 있는지?' },
  { pk: 40, id: '단 음식의 호, 불호는?' },
  { pk: 41, id: '매운 음식의 호, 불호는?' },
  { pk: 42, id: '노래방에서 항상 부르는 노래는?' },
  { pk: 43, id: '가장 무서운 것은?' },
  { pk: 44, id: '민트초코의 호, 불호는?' },
  { pk: 45, id: '가장 좋아하는 배라 맛은?' },
  { pk: 46, id: '전화 vs 문자는?' },
  { pk: 47, id: '스트레스 해소법은?' },
  { pk: 48, id: '인생 좌우명은?' },
  { pk: 49, id: '가장 많이 사용하는 카메라 어플은?' },
  { pk: 50, id: '최근 웃긴 말이나 짤은?' },
  { pk: 51, id: '내가 가진 것 중 가장 비싼 물건은?' },
  { pk: 52, id: '내가 가진 것 중 가장 오래된 물건은?' },
  { pk: 53, id: '내가 가진 것 중 가장 예쁜 물건은?' },
  { pk: 54, id: '내가 가진 것 중 가장 쓸모 없는 물건은?' },
  { pk: 55, id: '가장 아끼는 물건은?' },
  { pk: 56, id: '넷플릭스에서 추천하는 프로그램은?' },
  { pk: 57, id: '내가 좋아하는 옷 브랜드는?' },
  { pk: 58, id: '내 추억이 담긴 물건은?' },
  { pk: 59, id: '내가 사용하는 SNS는?' },
  { pk: 60, id: '내 게임 닉네임은?' },
  { pk: 61, id: '내 게임 닉네임의 뜻은?' },
  { pk: 62, id: '돈을 얻으면 가장 많이 쓰고 싶은 곳은?' },
  { pk: 63, id: '죽을 때 남기고 싶은 유언은?' },
  { pk: 64, id: '내 묘비명은?' },
  { pk: 65, id: '친구가 죽으면 내가 가장 먼저 할 행동은?' },
  { pk: 66, id: '물냉 vs 비냉은?' },
  { pk: 67, id: '내가 잘하는 음식은?' },
  { pk: 68, id: '가장 좋아하는 영화는?' },
  { pk: 69, id: '내 별명은?' },
  { pk: 70, id: '싫어하는 색깔은?' },
  { pk: 71, id: '가족 관계는?' },
  { pk: 72, id: '내 혈액형은?' },
  { pk: 73, id: '내 발사이즈는?' },
  { pk: 74, id: '요즘 갖고 싶은 물건은?' },
  { pk: 75, id: '내가 결혼하고 싶은 나이는?' },
  { pk: 76, id: '내가 좋아하는 꽃은?' },
  { pk: 77, id: '요즘 가장 심각한 고민은?' },
  { pk: 78, id: '내가 가장 아팠을 때는?' },
  { pk: 79, id: '친구가 약속 시간에 1시간 늦었을 때 나는?' },
  { pk: 80, id: '가장 좋아하는 동물은?' },
  { pk: 81, id: '내가 가진 기억 중 가장 어릴 때의 기억은?' },
  { pk: 82, id: '내가 가진 기억 중 가장 무서웠던 기억은?' },
  { pk: 83, id: '내가 가진 기억 중 가장 신기한 기억은?' },
  { pk: 84, id: '내가 가진 기억 중 가장 웃긴 기억은?' },
  { pk: 85, id: '남들에 비해 잘하는 것은?' },
  { pk: 86, id: '남들에 비해 못하는 것은?' },
  { pk: 87, id: '주량은?' },
  { pk: 88, id: '나의 TMI는?' },
  { pk: 89, id: '백만원 생기면 하고 싶은 것은?' },
  { pk: 90, id: '하루 식사 횟수는?' },
  { pk: 91, id: '나의 종교는?' },
  { pk: 92, id: '좋아하는 단어는?' },
  { pk: 93, id: '내가 자주 쓰는 말은?' },
  { pk: 94, id: '내가 좋아하는 공간은?' },
  { pk: 95, id: '여행 간다면 가고 싶은 지역 또는 나라는?' },
  { pk: 96, id: '무인도에 가져갈 3가지는?' },
  { pk: 97, id: '내가 갖고 싶은 초능력은?' },
  { pk: 98, id: '죽어서 영혼이 되면 가장 먼저 하고 싶은 일은?' },
  { pk: 99, id: '바라는 인간상은?' },
  { pk: 100, id: '항상 갖고 다니는 소지품은?' },
];


</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.create-container {
  width: 800px;
  height: 500px;
  padding: 10px 20px 30px 20px;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-template-rows: repeat(8, 1fr);
  background-color: #D5F0C1;
  border-radius: 20px;
}

.room-back {
  grid-column: 1/6;
  grid-row: 2/8;
  background-color: #AAD9BB;
  border-radius: 5px;
}
.btn-container{
  background-color: white;
  border-radius: 4px;
  text-align: center;
  margin-bottom: 40px;
}

.create-top {
  grid-column: 1 / 3;
  grid-row: 1 / 2;
}

.create-top p {
  font-size: 2rem;
  font-weight: bold;
}

.room-img {
  grid-column: 1 / 3;
  grid-row: 2 / 5;
  padding: 10px;
}

.preview-img {
  margin-top: 10px;
}

.up-img {
  margin-top: 3px;
  width: 100%;
  margin-left: 0.7rem;
  max-width: 250px;
}


.img-btn.cyan {
  position: relative;
  display: inline-block;
  font-size: 1rem;
  padding: 3px 4rem;
  height: 28px;
  border-radius: 6px;
  text-align: center;
  transition: top .1s linear;
  text-shadow: 0 1px 0 rgba(0,0,0,0.15);
  background-color: #7fccde;
  box-shadow: 0 0 0 1px #7fccde inset,
        0 0 0 2px rgba(255,255,255,0.15) inset,
        0 2px 0 0 rgba(102, 164, 178, .6),
        0 2px 0 1px rgba(0,0,0,.4),
        0 2px 2px 1px rgba(0,0,0,0.5);
}
.img-btn.cyan:active {
  top: 5px;
  box-shadow: 0 0 0 1px #7fccde inset,
  0 0 0 2px rgba(255,255,255,0.15) inset,
  0 0 0 1px rgba(0,0,0,0.4);
}
.img-btn.cyan:hover {
  background-color: #afe8f5;
}

a.img-btn {
  text-decoration: none;
  color: white;
}

.type-hundred {
  grid-column: 3 / 4;
  grid-row: 2 / 5;
}

.type-free {
  grid-column: 4 / 5;
  grid-row: 2 / 5;
}

.room-setting {
  grid-column: 5 / 6;
  grid-row: 2 / 5;
}

.room-setting button {
  grid-column: 5 / 6;
  grid-row: 1fr;
}

.create-container label {
  font-family: 'Heebo', sans-serif;
  font-size: 0.75rem;
  font-weight: 500;
  letter-spacing: 0.025rem;
  font-style: normal;
  color: #FFFFFF;
  background-color: #7fccde;
  border-radius: 5px;
  -webkit-border-radius: 0rem;
  -moz-border-radius: 0rem;
  padding: 0.35rem 0.5rem;
  border-style: none;
  border-width: 0rem;
  border-color: #7fccde;
  width: 7rem;
}


.room-title {
  grid-column: 1 / 4;
  grid-row: 5 / 6;
}

.room-title label {
  margin-top: 30px;
  margin-left: 10px;
  position: relative;
  display: inline-block;
  height: 50%;
  width: 7rem;
  padding: 5px;
  text-align: center;
  border-radius: 5px;
  font-size: medium;
}
.room-title input {
  margin-left: 5px;
  width: 20rem;
  height: 32px;
  font-size: 15px;
  border: 0;
  border-radius: 5px;
  outline: none;
  padding-left: 10px;
  background-color: rgb(233, 233, 233);
}
.room-describe input {
  margin-left: 5px;
  width: 20rem;
  height: 32px;
  font-size: 15px;
  border: 0;
  border-radius: 5px;
  outline: none;
  padding-left: 10px;
  background-color: rgb(233, 233, 233);
}

.room-describe {
  grid-column: 1 / 4;
  grid-row: 6 / 7;
}

.room-describe label {
  margin-top: 12.5px;
  margin-left: 10px;
  position: relative;
  display: inline-block;
  height: 50%;
  width: 7rem;
  padding: 5px;
  text-align: center;
  border-radius: 5px;
  font-size: medium;
  line-height: 110%;
}
.room-hashtag {
  grid-column: 1 / 4;
  grid-row: 7 / 8;
}

.room-hashtag label {
  margin-left: 10px;
  margin-top: -10px;
  position: relative;
  display: inline-block;
  height: 50%;
  width: 7rem;
  padding: 5px;
  text-align: center;
  border-radius: 5px;
  font-size: medium;
}
.select-hundred {
  grid-column: 1 / 4;
  grid-row: 7 / 8;
  
}

.select-hundred select {
  margin-left: 5px;
}

.type-hundred {
  padding-top: 10px;
}
.type-hundred button {
  height: 100%;
  width: 100%;
}

.type-free{
  padding-top: 10px;
}
.type-free button {
  height: 100%;
  width: 100%;
}

.room-setting {
  padding-top: 10px;
  width: 100%;
  height: 100%;

}

.room-setting button {
  width: 100%;


}   

.select-hundred label {
  line-height: 100%;
  margin-left: 10px;
  margin-top: -10px;
  position: relative;
  display: inline-block;
  flex: left;
  height: 50%;
  width: 7rem;
  padding: 5px;
  text-align: center;
  border-radius: 5px;
  font-size: medium;
}

.member-limit {
  grid-column: 4 / 6;
  grid-row: 5 / 8;
  width: 100%;
  height: 100%;
  background-color: aqua;
  border-radius: 5px;
}

.create-bottom {
  grid-column: 2/ 5;
  grid-row: 8 / 9;
  align-items: center;
  justify-items: center;
}


.upload-demo {
  display: flex;
  align-items: center;
}

</style>