<template>
    <div>
  
      <!-- 방 세팅 토글 -->
        <div class="room-setting">
            <div class="form-check form-switch">
              <input class="form-check-input" type="checkbox" role="switch" id="cameraOnOff" checked>
              <label class="form-check-label" for="cameraOnOff">카메라</label>
            </div>
            
            <div class="form-check form-switch">
              <input class="form-check-input" type="checkbox" role="switch" id="voiceOnOff" checked>
              <label class="form-check-label" for="voiceOnOff">음성</label>
            </div>
            
            <div class="form-check form-switch">
              <input class="form-check-input" type="checkbox" role="switch" id="maskOnOff" checked>
              <label class="form-check-label" for="maskOnOff">가면쓰기</label>
            </div>
            
            <div class="form-check form-switch">
              <input class="form-check-input" type="checkbox" role="switch" id="changeVoiceOnOff" checked>
              <label class="form-check-label" for="changeVoiceOnOff">목소리 바꾸기</label>
            </div>
            
            <div class="form-check form-switch">
              <input class="form-check-input" type="checkbox" role="switch" id="secretOnOff" checked>
              <label class="form-check-label" for="secretOnOff">비밀방</label>
            </div>
        </div>
  
  
  
        <!-- 접속인원 창 -->
        <div class="accordion" id="total-member-box">
          <div class="accordion-item">
            <h2 class="accordion-header">
              <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                방 인원    
                <div>
                    1/4
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                    <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
                    <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
                  </svg>
                </div>
              </button>
            </h2>
            <div id="room-members" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
              <div class="accordion-body">
                <div id="room-member">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                    <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
                    <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
                  </svg>
                  <a href="#">닉네임1  </a>
  
                  <!-- 온라인 여부 아이콘. 만약 온라인이면 초록색, 오프라인이면 회색 -->
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="green" class="bi bi-circle-fill" viewBox="0 0 16 16">
                    <circle cx="8" cy="8" r="8"/>
                  </svg>
  
  
                  <!-- 팔로우 아이콘. 이미 팔로우가 된 상대라면 뜨지 않는다 -->
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="blue" class="bi bi-plus-lg" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M8 2a.5.5 0 0 1 .5.5v5h5a.5.5 0 0 1 0 1h-5v5a.5.5 0 0 1-1 0v-5h-5a.5.5 0 0 1 0-1h5v-5A.5.5 0 0 1 8 2"/>
                  </svg>
  
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 방 나가기 아이콘 -->
        <div>
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-box-arrow-right" viewBox="0 0 16 16">
                <path fill-rule="evenodd" d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0z"/>
                <path fill-rule="evenodd" d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708z"/>
            </svg>
        </div>
  
  
  
        <!-- 멤버별 채팅 화면 -->
        <div class="chatting-video">
  
          <div class="d-flex mb-3" style="width: 18rem;">
            <!-- 멤버 닉네임 -->
            <div class="p-2">
              <span class="badge rounded-pill text-bg-info">닉네임1</span>
            </div>
            
            <!-- v-if로 만약 해당 닉네임1이 말을 하고 있으면 말하는중 아이콘 넣기  -->
            <div class="p-2">
              <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-megaphone" viewBox="0 0 16 16">
                <path d="M13 2.5a1.5 1.5 0 0 1 3 0v11a1.5 1.5 0 0 1-3 0v-.214c-2.162-1.241-4.49-1.843-6.912-2.083l.405 2.712A1 1 0 0 1 5.51 15.1h-.548a1 1 0 0 1-.916-.599l-1.85-3.49-.202-.003A2.014 2.014 0 0 1 0 9V7a2.02 2.02 0 0 1 1.992-2.013 75 75 0 0 0 2.483-.075c3.043-.154 6.148-.849 8.525-2.199zm1 0v11a.5.5 0 0 0 1 0v-11a.5.5 0 0 0-1 0m-1 1.35c-2.344 1.205-5.209 1.842-8 2.033v4.233q.27.015.537.036c2.568.189 5.093.744 7.463 1.993zm-9 6.215v-4.13a95 95 0 0 1-1.992.052A1.02 1.02 0 0 0 1 7v2c0 .55.448 1.002 1.006 1.009A61 61 0 0 1 4 10.065m-.657.975 1.609 3.037.01.024h.548l-.002-.014-.443-2.966a68 68 0 0 0-1.722-.082z"/>
              </svg>
            </div>
  
            
            <!-- 방장 양도 버튼 --><!-- 방장에게만 보이는 -->
            <div class="ms-auto p-2" @click="">
              <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-egg-fried" viewBox="0 0 16 16">
                <path d="M8 11a3 3 0 1 0 0-6 3 3 0 0 0 0 6"/>
                <path d="M13.997 5.17a5 5 0 0 0-8.101-4.09A5 5 0 0 0 1.28 9.342a5 5 0 0 0 8.336 5.109 3.5 3.5 0 0 0 5.201-4.065 3.001 3.001 0 0 0-.822-5.216zm-1-.034a1 1 0 0 0 .668.977 2.001 2.001 0 0 1 .547 3.478 1 1 0 0 0-.341 1.113 2.5 2.5 0 0 1-3.715 2.905 1 1 0 0 0-1.262.152 4 4 0 0 1-6.67-4.087 1 1 0 0 0-.2-1 4 4 0 0 1 3.693-6.61 1 1 0 0 0 .8-.2 4 4 0 0 1 6.48 3.273z"/>
              </svg>
            </div>
  
          </div>
  
          <!-- 비디오 화면 -->
          <div class="card text-bg-primary mb-3" style="width: 18rem; height: 14rem;">
            <div class="card-body">
              <h5 class="card-title">이것은 멤버별 비디오...</h5>
            </div>
          </div>
  
          
  
          <!-- 강퇴 버튼 --><!-- 방장에게만 보이는 -->
          <button type="button" class="btn btn-secondary">Kick</button>
  
      </div>
  
  
      <div class="d-flex justify-content-between">
  
        <!-- 방 폭파 버튼 -->
        <div>
            <!-- 방장에게만 보이는 버튼 -->
            <div>
                <button type="button" class="btn btn-danger">방 폭파</button>
            </div>

            <!-- 일반 유저에게 보이는 버튼 -->
            <div>
                <button type="button" class="btn btn-danger">방 탈퇴</button>
            </div>
        
        </div>
        
        
        <!-- 채팅 시작 전--><!-- 방장이면 -->

        <div>
            <button type="button" class="btn btn-success">방 폭파</button>
        </div>


        <!-- 채팅 시작 후 -->
        <div>
            <button type="button" class="btn btn-success">채팅 종료</button>
        </div>

  
  
  
        <!-- 채팅 아이콘 -->
        <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-chat-text" viewBox="0 0 16 16">
          <path d="M2.678 11.894a1 1 0 0 1 .287.801 11 11 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8 8 0 0 0 8 14c3.996 0 7-2.807 7-6s-3.004-6-7-6-7 2.808-7 6c0 1.468.617 2.83 1.678 3.894m-.493 3.905a22 22 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a10 10 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9 9 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105"/>
          <path d="M4 5.5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5M4 8a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7A.5.5 0 0 1 4 8m0 2.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5"/>
        </svg>
    
      </div>
  
  
    </div>
  </template>
  
  <script setup>
  
  </script>
  
  <style scoped>
  
  </style>