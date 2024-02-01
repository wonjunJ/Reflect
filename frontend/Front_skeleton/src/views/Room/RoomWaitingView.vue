<template>
    <div>
      <!-- 배경 컬러 설정 -->
      <div>
        <!-- 배경 아래 -->
        <div class="box3"></div>
        <!-- 배경 위1 -->
        <div class="box1"></div>
        <!-- 배경 위2 -->
        <!-- <div class="box2"></div> -->
      </div>
  
      <!-- 방 info -->
      <div v-if="roomInfo" class="roominfo">
        <!-- 방 타입 -->
        <div class="topic">
          <p v-if="roomInfo.isHundred">
            백문백답
            <hr>
            {{ roomInfo.questions }}
          </p>
          <p v-if="roomInfo.isFree">Free Topic</p>
        </div>
  
        <!-- 방 제목 -->
        <div class="roomtitle">
          <p>{{ roomInfo.roomTitle }}</p>
        </div>
  
        <!-- 방 해시태그 -->
        <div class="hashtag">
          <p>해시태그</p>
        </div>
  
        <!-- 방 상세설명 -->
        <div class="roomdetail">
          <p>{{ roomInfo.roomDescribe }}</p>
        </div>
  
        <!-- 방 설정 아이콘 -->
        <div class="roomicon">
          <!-- 비밀방 아이콘 -->
          <div class="icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="105" height="89" viewBox="0 0 105 89" fill="none">
              <!-- 아이콘 내용 -->
            </svg>
          </div>
  
          <!-- 가면모드 아이콘 -->
          <div class="icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="123" height="105" viewBox="0 0 123 105" fill="none">
              <!-- 아이콘 내용 -->
            </svg>
          </div>
  
          <!-- 익명모드 아이콘 -->
          <div class="icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="107" height="89" viewBox="0 0 107 89" fill="none">
              <!-- 아이콘 내용 -->
            </svg>
          </div>
        </div>
  
        <!-- 들어가기 버튼 -->
        <div>
          <router-link :to="{ name: 'roomdetail' }" class="btn">ENTER</router-link>
        </div>
      </div>
  
      <!-- 채팅화면 설정 -->
      <div class="box2">
        <!-- 채팅 화면 -->
        <div class="chatview">
          <!-- 내 정보 -->
          <div class="myinfo">
            <!-- 프로필사진 -->
            <div class="profileimg">
              <img src="~@/assets/cmh (1).jpg" alt="profileimg">
            </div>
  
            <!-- 닉네임 -->
            <div class="nickname">
              <a href="#">닉네임</a>
            </div>
          </div>
  
          <!-- 내 모습 비디오 -->
          <div class="video">
            <img src="~@/assets/defaultRoom.jpg" alt="일단 임시이미지">
          </div>
        </div>
  
        <!-- 설정 버튼 -->
        <div class="setting">
          <!-- 카메라 온오프 -->
          <button class="setbtn">
            카메라
          </button>
  
          <!-- 마이크 온오프 -->
          <button class="setbtn">
            마이크
          </button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRoute } from 'vue-router';
  
  const route = useRoute();
  const roomInfo = ref(null);
  
  onMounted(() => {
    // 라우터로부터 전달받은 roomInfo 파라미터를 가져와서 변수에 저장
    const roomInfoParam = route.query.roomInfo;
    if (roomInfoParam) {
      // JSON 형태의 문자열을 객체로 파싱
      roomInfo.value = JSON.parse(roomInfoParam);
    }
  });
  
  </script>

<style scoped>
html {
    background: #fff;
}
.box1 {
    position: relative;
    width: 678px;
    height: 115px;
    position: absolute;
    left: -7px;
    top: 50px;
    background: #80BCBD;
}

.box2 {
    position: fixed;
    top:55px;
    left:567px;
    width: 873px;
    height: 923px;
    flex-shrink: 0;
    border-radius: 70px 0px 0px 0px;
    background: #FFF;
    box-shadow: insert 4px 7px 0px 0px rgba(0, 0, 0, 0.25);
}

.box3 {
    width: 567px;
    height: 550px;
    flex-shrink: 0;
    position: absolute;
    border-radius: 0px 0px 70px 0px;
    background: #80BCBD;
    box-shadow: 0px 4px 7px 0px rgba(0, 0, 0, 0.25);
}

.topic {
    width: 175px;
    height: 38px;
    flex-shrink: 0;
    color: #000;
    font-family: Inter;
    font-size: 32px;
    font-style: normal;
    font-weight: 700;
    line-height: normal;
}

.roomtitle{
    width: 384px;
    height: 46px;
    flex-shrink: 0;
    color: #000;
    font-family: Inter;
    font-size: 40px;
    font-style: normal;
    font-weight: 500;
    line-height: normal;
}

.roominfo{
    position: absolute;
}

.hashtag{
    width: 378px;
    height: 29px;
    flex-shrink: 0;
    color: #000;
    font-family: Inter;
    font-size: 24px;
    font-style: normal;
    font-weight: 200;
    line-height: normal;
}

.roomdetail{
    width: 429px;
    height: 130px;
    flex-shrink: 0;
    color: #000;
    font-family: Inter;
    font-size: 24px;
    font-style: normal;
    font-weight: 200;
    line-height: normal;
}

div.icon {
    width: 107px;
    height: 97px;
    flex-shrink: 0;
    float: left;
}

.btn{
    width: 299px;
    height: 94px;
    flex-shrink: 0;
    border-radius: 50px;
    background: #D5F0C1;
    box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
    color: #000;
    font-family: Inter;
    font-size: 40px;
    font-style: normal;
    font-weight: 700;
    line-height: normal;
    display:flex;
    justify-content: center;
    align-items: center;

}

.chatview {
    width: 734px;
    height: 370px;
    flex-shrink: 542;
    border-radius: 20px 20px 20px 20px;
    background: #AAD9BB;
    position: relative;
    top:65px;
    left: 60px;
}

.profileimg img{
    width: 51px;
    height: 51px;
    flex-shrink: 0;
    border-radius: 100px;
    border: 2px solid #000;
    background: lightgray 50% / cover no-repeat;
    float: left;
}

.nickname {
    width: 185px;
    height: 50px;
    flex-shrink: 0;
    color: #000;
    font-family: Inter;
    font-size: 32px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    float: left;
}

.video img {
    width: 600px;
    height: 300px;
    position: static;
}

.setting button {

    float: left;
}

.setbtn {
    width: 150px;
    height: 47px;
    flex-shrink: 0;
    border-radius: 50px;
    background: #D5F0C1;
    box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
    color: #000;
    font-family: Inter;
    font-size: 40px;
    font-style: normal;
    font-weight: 700;
    line-height: normal;
    display:flex;
    position: relative;
    top: 100px;
    left: 20%;
    margin: 0px 50px 0px 50px;
    justify-content: center;
    align-items: center;
}

</style>



