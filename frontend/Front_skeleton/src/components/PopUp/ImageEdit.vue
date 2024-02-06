<template>
    <div>
        <h1>이미지 첨부 팝업</h1>
            <div class="custom-file">
                <input id="customFile" type="file" @change="readInputFile"/>
            </div>
            <br>
            <div id="imagePreview">
                <img id="img" />
            </div>

    </div>
</template>

<script setup>

import $ from 'jquery'; 
const readInputFile = (e) => {// 미리보기 기능구현
    $('#imagePreview').empty();
    var files = e.target.files;
    var fileArr = Array.prototype.slice.call(files);
    console.log(fileArr);
    fileArr.forEach(function(f){
    	if(!f.type.match("image/.*")){
        	alert("이미지 확장자만 업로드 가능합니다.");
            return;
        };
        var reader = new FileReader();
        reader.onload = function(e){
            var html = `<img src=${e.target.result} />`;
            $('#imagePreview').append(html);
        };
        reader.readAsDataURL(f);
    })
}




</script>

<style scoped>

</style>