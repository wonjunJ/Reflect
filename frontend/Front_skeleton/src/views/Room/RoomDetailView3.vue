<template>
    <div class = "main">
      <div class = "content">
        <div class = "roomTitle">
          <p>웹툰 이야기 할 사람!</p>
        </div>
        <div class = "roomSection">
          <div class = "camera">
           카메라
          </div>
          <div class = "roomContent">
          방정보
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
            <div class = "roomSetting"></div>
            <div class = "quitChatting"></div>
            <div class = "exitRoom"></div>
            <div class = "chattingBtn"></div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  
  </script>
  
  <style>
  .main{
    background-color: #80BCBD;;
  }
  
  .content{
    border-top-left-radius: 100px;
    border-top-right-radius: 100px;
    width: 100%;
    background-color:white;
  }
  
  .roomSection{
    display:flex;
    flex-direction: row;
  }
  
  .peopleInfo{
    background-color:#F9F7C9;
    border-radius: 10px;
  }
  
  
  
  
  
  
  </style>