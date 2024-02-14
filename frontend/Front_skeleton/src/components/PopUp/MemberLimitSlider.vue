<template>
  <div class="limit-value">{{ currentValue }}</div>
  <input type="range" min=2 max=8 step=1 v-model="currentValue">
</template>

<script>
export default {
data() {
  return {
    currentValue: 2,
  };
},
emits: ['updateValue'],
watch: {
    currentValue(newValue) {
      // 'update-value' 이벤트를 발생시키고, 현재 값을 이벤트와 함께 전달합니다.
      this.$emit('update-value', newValue);
    },
  },
};
</script>
<style scoped>
@import "https://fonts.googleapis.com/css?family=Dosis:700";
body {
font-family: "Dosis", Helvetica, Arial, sans-serif;
background: #ecf0f1;
color: #34495e;
padding-top: 40px;
text-shadow: white 1px 1px 1px;
}
.limit-value {
text-align: center;
font-weight: bold;
font-size: 4em;
width: 30px;
height: 10px;
line-height: 6px;
margin: -10px auto 50px auto;
letter-spacing: -.07em;
text-shadow: white 2px 2px 2px;
}
input[type="range"] {
display: block;
-webkit-appearance: none;
background-color: #bdc3c7;
width: 150px;
height: 1px;
border-radius: 5px;
margin: 0 auto;
outline: 0;
}
input[type="range"]::-webkit-slider-thumb {
-webkit-appearance: none;
background-color: #e74c3c;
width: 20px;
height: 20px;
border-radius: 50%;
border: 2px solid white;
cursor: pointer;
transition: .3s ease-in-out;
}
input[type="range"]::-webkit-slider-thumb:hover {
background-color: white;
border: 2px solid #e74c3c;
}
input[type="range"]::-webkit-slider-thumb:active {
transform: scale(1.6);
}
</style>