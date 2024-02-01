<template>

  <div v-if="isModalOpen" class="modal">
    <div class="modal-content">

        <h3>방 만들기</h3>
        <!-- 모달 내용 -->
        <div>

            <!-- 썸네일 사진-->
            <div>
              
              <div class="preview-img" v-if="previewUrl">
                <img :src="previewUrl" alt="업로드 이미지" @error="replaceImg" style="width: 350x;" />
              </div>
              <div v-else>
                <img src="@/assets/defaultRoom.jpg" alt="기본 이미지" style="width: 350px;"/>
              </div>
                <el-upload class="upload-demo" :before-upload="beforeUpload">
                  <a class="img-icon" :disabled="uploading">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
                        <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>
                        <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"/>
                    </svg>
                  </a>
                </el-upload>
            
              </div>


            <!-- 대화 타입 선택 -->
            <div>
                <h3>대화 타입</h3>
                <div>
                  <button type="button" class="btn btn-primary" @click="writeQuestion">백문백답</button>
                  <button type="button" class="btn btn-primary" @click="writeHashtag">자유주제</button>
                </div>
            </div>


            <div>
                <!-- 방 제목 -->
                <div>
                    <label for="title-label">Title</label>
                    <input type="text" v-model.trim="roomTitle" class="room-input">
                </div>
                <!-- 방 한줄 설명 -->
                <div>
                    <label for="describe-label">Describe</label>
                    <input type="text" v-model.trim="roomDescribe" class="room-input">
                </div>
            

                <!-- 자유주제/100문100답에 따라 다르게 나옴 -->
                
                <!-- 자유주제 --><!-- 해시태그  -->
                <div v-if="isHundred">
                    <label for="hashtag-label">Hashtag  </label>
                    <input type="text" v-model.trim="hashtag" class="room-input">
                </div>

                
                <!-- 백문백답 --><!-- 질문선택-->
                <div v-if="isFree">
                    <label for="questions" class="qestions-label">Question  </label>
                    <select v-model="questions" class="question-select" id="questions">
                      <option value="" disabled selected>질문 선택</option>
                      <option v-for="question in questionlist" :key="question.pk" :value="question.id">
                        {{ question.id }}
                      </option>
                    </select>
                </div>

            </div>


            <!-- 방 설정 토글 -->
            <div>
                <!-- 비밀의 방 -->
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" role="switch" id="secretRoomOnOff" v-model="isSecret">  
                    <label class="form-check-label" for="secretRoomOnOff">비밀의 방으로</label>
                </div>

                <!-- 가면모드 -->
                <div class="form-check form-switch">
                  <input class="form-check-input" type="checkbox" role="switch" id="maskOnOff" v-model="isMask">
                  <label class="form-check-label" for="maskOnOff">가면모드</label>
                </div>

                <!-- 익명모드 -->
                <div class="form-check form-switch">
                  <input class="form-check-input" type="checkbox" role="switch" id="anonymousOnOff" v-model="isAnonymous">
                  <label class="form-check-label" for="anonymousOnOff">비밀의 방으로</label>
                </div>

            </div>

            <!-- 인원수 설정 슬라이더-->
            <div>
                <label for="limit" class="form-label">인원수</label>
                <input type="range" v-model="memberNum" class="form-range" min="2" max="8" step="1" id="limit">

            </div>
            <div>
              <button type="button" class="btn btn-primary" @click="goRoom">방 만들기</button>
              <button type="button" class="btn btn-secondary" @click="closeModal">닫기</button>
            </div>
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

const writeHashtag = () => {
  isHundred.value = true
  isFree.value = false
}

const writeQuestion = () => {
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

.modal-content {
  width: 800px;
  background: white;
  padding: 20px;
  border-radius: 8px;
}

.upload-demo {
  display: flex;
  align-items: center;
}

.preview-img img {
  max-width: 350px;
  max-height: 300px;
}



#hashtag {
  height: 4vh;
  place-items: center;
  font: 16px Arial;
}

</style>

