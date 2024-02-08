<template>
  <div v-if="isCookie" class="modal">
      <div class="fortune-cookie" >
          <div ref="screen1" class="screen1">
              <h1>행운 가득한 하루 되세요</h1>
              <p>포춘 쿠키 뽑기</p>
              <img id="cookie1" src="@/assets/cookie1.png" alt="포춘쿠키" @click="openCookie">
          </div>

          <div class="screen2 hide" ref="screen2">
              <h2>결과</h2>
          <div class="fortune">
              <p class="lucky-text"></p>
              <p class="lucky-numbers">행운의 숫자 
                  <span>?, ?, ?, ?, ?, ?, ?</span>
              </p>
          </div>
              <img src="@/assets/cookie_open1.png" alt="열린 포춘쿠키">
              <button @click="closeModal">닫기</button>
          </div>

      </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const { isCookie } = defineProps(['isCookie'])
const emit = defineEmits()
const fortunePhrases = [
"행복은 간단한 것에서 시작됩니다.",
"오늘은 어제보다 나은 날이 될 것입니다.",
"당신의 미소는 누군가에게 특별한 순간을 선물합니다.",
"어제의 후회와 내일의 걱정보다 현재를 즐기세요.",
"새로운 기회는 새로운 문을 열어줍니다.",
"당신은 더 강하고 지혜롭습니다. 믿어보세요.",
"작은 변화도 큰 성공의 시작일 수 있습니다.",
"인내는 결국 성취로 이어집니다.",
"모든 도전은 당신을 성장시킵니다.",
"오늘의 웃음은 내일을 밝게 만듭니다.",
"가끔은 쉬어가는 것도 필요합니다.",
"금전운이 있을 것 같은 하루네요! 부럽...",
"자신을 사랑하고 존경하세요. 당신은 특별합니다.",
"성공은 끊임없는 노력과 인내의 결합입니다.",
"당신은 더 나은 미래를 위해 노력하고 있습니다.",
"당신의 꿈은 현실이 될 것입니다. 끝까지 포기하지 마세요.",
"오늘은 새로운 시작의 기회입니다.",
"당신의 긍정적인 에너지는 주변을 밝게 만듭니다.",
"매일 자신에게 작은 선물을 주세요.",
"어려운 순간일수록 더 강해집니다.",
"꽝! 가끔은 꽝도 있어줘야^^",
"당신의 열정은 무한한 가능성을 열어줍니다.",
"계획을 세우고 목표를 향해 나아가세요.",
"당신은 이미 많은 것을 이겨냈습니다. 계속 나아가세요.",
"성공은 자기 자신을 이기는 것이 아니라, 자기 자신과의 싸움에서 나옵니다.",
"당신의 노력은 반드시 결실을 맺을 것입니다.",
"좋은 습관을 기르는 것은 훌륭한 미래를 향한 첫걸음입니다.",
"모든 일에는 의미가 있다. 지금의 어려움도 곧 의미를 찾게 될 것입니다.",
"성공은 자기 자신을 이기는 것이 아니라 여정의 한 부분입니다.",
"자신을 믿고, 당당하게 나아가세요.",
"당신의 꿈을 위해 끝없이 노력하세요.",
"오늘은 당신이 놓치지 말아야 할 기회가 올 것입니다.",
"당신은 더 나은 내일을 위해 노력하고 있습니다.",
"지금 당장 시작할 수 있는 작은 목표를 세워보세요.",
"성공은 끊임없는 노력과 자기 개발에서 나옵니다.",
"당신은 이미 모든 도전을 이겨냈습니다. 계속해서 나아가세요.",
"어려운 시간일수록 당신은 더 강해집니다.",
"매일 자신에게 응원의 말을 해주세요.",
"당신은 미래를 위한 훌륭한 준비를 하고 있습니다.",
"모든 시작은 불안하고 어려운 것입니다. 그럴 때가 가장 중요한 때입니다.",
"당신의 노력은 결코 헛되지 않을 것입니다.",
"자신의 강점을 찾고 최대한 활용하세요.",
"새로운 도전은 당신을 더 강하게 만들어줄 것입니다.",
"당신은 더 나은 미래를 향해 나아가고 있습니다. 계속해서 나아가세요.",
"오늘은 당신이 더 나은 선택을 할 수 있는 날입니다.",
"당신의 꿈은 크고 가치있습니다. 끝까지 포기하지 마세요.",
"자신을 믿고, 더 큰 꿈을 향해 나아가세요.",
"당신의 미래는 당신의 선택에 달려있습니다. 현재를 멋지게 만들어보세요.",
"좋은 사람들과 함께하는 것은 큰 축복입니다.",
"당신은 이미 충분히 강력하고 뛰어난 사람입니다. 그 사실을 잊지 마세요.",
"성공은 지속적인 노력과 포기하지 않는 의지에서 나옵니다.",
"당신의 미래는 당신의 행동에 달려있습니다.",
"자신을 사랑하고 존경하세요. 당신은 가치 있는 존재입니다.",
"매일 새로운 가능성이 열립니다. 그 가능성을 놓치지 마세요.",
"당신은 이미 많은 것을 이겨냈습니다. 계속해서 나아가세요.",
"성공은 자기 자신을 이기는 것이 아니라 자기 자신과의 싸움에서 나옵니다.",
"당신은 더 나은 내일을 향해 나아가고 있습니다. 끝까지 포기하지 마세요.",
"자신의 강점을 믿고, 그것을 최대한 발휘하세요.",
"당신의 꿈을 향해 나아가는 여정은 가치 있습니다. 끝까지 포기하지 마세요.",
"새로운 도전은 당신을 성장시키고 더 강하게 만들어줄 것입니다.",
"자신의 능력과 가능성을 믿어보세요. 당신은 할 수 있습니다.",
"당신은 이미 더 나은 미래를 위한 준비를 하고 있습니다.",
"오늘의 작은 노력이 내일의 큰 성과를 만들어낼 것입니다.",
"자신의 꿈을 향해 나아가는 것은 가치 있는 일입니다.",
"당신은 이미 많은 어려움을 이겨냈습니다. 계속 나아가세요.",
"성공은 끊임없는 노력과 포기하지 않는 의지에서 나옵니다.",
"당신의 강점을 알고, 그것을 최대한 활용하세요.",
"새로운 도전은 당신을 성장시키고 더 강하게 만들어줄 것입니다.",
"자신의 능력과 가능성을 믿어보세요. 당신은 할 수 있습니다.",
"당신은 이미 더 나은 미래를 위한 준비를 하고 있습니다.",
"오늘의 작은 노력이 내일의 큰 성과를 만들어낼 것입니다.",
"자신의 꿈을 향해 나아가는 것은 가치 있는 일입니다.",
"당신은 이미 많은 어려움을 이겨냈습니다. 계속 나아가세요.",
"성공은 끊임없는 노력과 포기하지 않는 의지에서 나옵니다.",
"당신의 강점을 알고, 그것을 최대한 활용하세요."
];

let item = 0
// const fortuneCookie = document.getElementById("cookie1")
const screen1 = ref(null)
const screen2 = ref(null)
// const btnReset = document.querySelector("button")

function screenToggle() {
screen1.value.classList.toggle("hide")
screen2.value.classList.toggle("hide")
}

function generateLuckyNumbers(count) {
const luckyNumbers = new Set();
while (luckyNumbers.size < count) {
  const newNumber = Math.floor(Math.random() * 45) + 1; // 1부터 45 사이의 숫자 생성
  luckyNumbers.add(newNumber);
}
console.log(luckyNumbers)
return Array.from(luckyNumbers);

}

function openCookie() {
screenToggle()
const luckyNumbers = generateLuckyNumbers(7);
document.querySelector(".lucky-text").innerText = fortunePhrases[item]
document.querySelector(".lucky-numbers span").innerText = luckyNumbers.join(', ');
}

function cookieReset(e) {
screenToggle()
item = Math.round(Math.random() * (fortunePhrases.length - 1))
}

function handleEnter(event) {
if(event.key == 'Enter' && screen2.classList.contains("hide")) {
openCookie()
} else if(event.key == 'Enter' && screen1.classList.contains("hide")) {
cookieReset()
} 
}

const closeModal = () => {
  cookieReset()
  emit('closeModal')
}

// fortuneCookie.addEventListener('click', openCookie)
// btnReset.addEventListener('click', closeModal)
document.addEventListener('keypress', handleEnter)



</script>

<style scoped>
* {
margin: 0;
padding: 0;
box-sizing: border-box;
}

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

.fortune-cookie {
width: 400px;
padding: 30px 20px 30px 20px;
background-color: beige;
}

:root {
font-size: 62.5%;
}

body {
height: 100vh;
background: linear-gradient(45deg, #EB5F43 0%, #f3804f 100%);
background-size: 200% 200%;

display: grid;
place-items: center;

animation: gradient 10s ease infinite;
}

.hide {
display: none;
}

main {
background: #FFFFFF;
box-shadow: 0px 2rem 5rem rgba(0, 0, 0, 0.8);
border-radius: .5rem;

padding: 4.8rem 6.4rem;
margin-top: -8rem;

width: min(42.8rem, 90%);
text-align: center;
}

/*screen1*/

h1, p, h2 {
color: #34355B;
}

h1, h2 {
font-family: 'Montserrat';
font-weight: 700;
font-size: 1.8rem;
line-height: 2.9rem;
text-align: center;

margin-bottom: 1.4rem;
}

.screen1 p {
font-family: 'DM Sans', sans-serif;
font-weight: 400;
font-size: 1.5rem;
text-align: center;
line-height: 2rem;

margin-bottom: 2rem;
}

.screen1 img {
margin-bottom: 1.6rem;
cursor: pointer;
}

.screen1 img:hover {
animation: shake .2s ease-in-out infinite;
}

/*screen2*/

.fortune {
background: #FBF6F2;
box-shadow: 2px 2px 2px rgba(85, 85, 85, 0.5);
padding: 1rem;

animation: appearing 1s .2s forwards;
opacity: 0;
transform: translateY(500px);
}

.fortune lucky-text {
font-family: 'Dancing Script', sans-serif;
font-weight: 400;
font-size: 2.4rem;
line-height: 2.9rem;
color: #000000;

margin-bottom: 1.6rem;
}
.fortune lucky-numbers {
font-family: 'Dancing Script', sans-serif;
font-weight: 400;
font-size: 2.4rem;
line-height: 2.9rem;
color: #000000;

margin-bottom: 1.6rem;
}

button {
background: #EB5F43;
border-radius: 4px;
border: none;
padding: 1.6rem;

font-family: 'DM Sans';
font-weight: 700;
font-size: 1.5rem;
line-height: 2rem;
color: #FFFFFF;

cursor: pointer;

transition: background .7s;
}

button:hover {
background: #F38671;
}

.screen2 img {
margin-bottom: 1.7rem;
}

@keyframes shake {
0% {
  transform: rotate(0deg);
}

25% {
  transform: rotate(5deg);
}

50% {
  transform: rotate(0deg);
}

75% {
  transform: rotate(-5deg);
}

100% {
  transform: rotate(0deg);
}
}

@keyframes appearing {
0% {
  transform: translateY(500px);
  scale: 0;
}


100% {
  opacity: 100;
  transform: translateY(0);
}
}

@keyframes gradient {
0% {
  background-position: 0% 50%;
}

50% {
  background-position: 100% 50%;
}

100% {
  background-position: 0% 50%;
}
}

</style>