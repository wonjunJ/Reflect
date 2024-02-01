<template>
  <div class = "mainBackground">
    <div class = "mainContent">
        <div class = "chatTitle">
          <p>웹툰 이야기 할 사람!</p>
        </div>
      <div class = "roomSection">
        <div class = "cameraSection">
          <div class = "camera"> 
            <div class = "cameraHeading">
              <div class="profileImg">프로필 이미지</div>
              <div class = "etc">별표</div>
            </div>
            <img src="@\assets\defaultRoom.jpg" alt="일단 임시이미지">
          </div>
          <div class = "camera"> 
            <div class = "cameraHeading">
              <div class="profileImg">프로필 이미지</div>
              <div class = "etc">별표</div>
            </div>
            <img src="@\assets\defaultRoom.jpg" alt="일단 임시이미지">
          </div>
          <div class = "camera"> 
            <div class = "cameraHeading">
              <div class="profileImg">프로필 이미지</div>
              <div class = "etc">별표</div>
            </div>
            <img src="@\assets\defaultRoom.jpg" alt="일단 임시이미지">
          </div>
          <div class = "camera"> 
            <div class = "cameraHeading">
              <div class="profileImg">프로필 이미지</div>
              <div class = "etc">별표</div>
            </div>
            <img src="@\assets\defaultRoom.jpg" alt="일단 임시이미지">
          </div>
         
        </div>
        <div class = "roomContent">
          <div class = "peopleInfo">
            <div class = "peopleInfoTitle">
              <div class = "peopleTitle">방인원</div>
              <div class = "peopleNum">3/4</div>
            </div>
            <div class = "peoples">
              <div class = "person">사람</div>
              <div class = "person">사람</div>
              <div class = "person">사람</div>
            </div>
          </div>
          <div class = "roomSetting">
            <div >카메라</div>
            <div >마이크</div>
            <div >가면모드</div>
            <div >목소리 변조</div>
          </div>
          <div class = "quitChatting">
            <button>채팅 종료</button>
          </div>
          <div class = "exitRoom">
            <button>방 나가기</button>
          </div>
          <div class = "chattingBtn">
            <button>채팅하기</button>
          </div>
        </div>
      </div>

      
    </div>
  </div>
</template>

<script>

</script>

<style>
.mainBackground{
  height: 86vh;
  background-color: #80BCBD;;
}


.mainContent{
  padding: 50px;
  border-top-left-radius: 100px;
  border-top-right-radius: 100px;
  width: 100%;
  height: 100%;
  background-color:white;
}


.cameraSection{
  flex:4;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  align-content: stretch;
}

.chatTitle{
  flex-shrink: 0;
    color: #000;
    font-family: Inter;
    font-size: 30px;
    font-style: normal;
    font-weight: 1000;
    line-height: normal;
}

.roomContent{
  flex:1;
}
.camera{
  margin: 10px;
 
  background-color: #AAD9BB;
}

.roomSection{
  display:flex;
  flex-direction: row;
}

.roomSetting{
  background-color: #F9F7C9;
  border-radius: 10px;
}

.peopleInfo{
  background-color:#F9F7C9;
  border-radius: 10px;
}

</style>