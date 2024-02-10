<template>
    <div>
    <!-- 총 페이지 개수 계산 Math.ceil(전체 컨텐츠 개수 / 한 페이지에 보여주고자 하는 컨텐츠의 개수) -->
    <!-- 화면에 보여질 페이지 그룹 계산 화면에 보여질 페이지 그룹 = Math.ceil(현재 페이지 번호 / 한 화면에 보여질 페이지 개수) -->
    <!-- 화면에 보여질 페이지의 첫/마지막 페이지 번호 계산
    어떤 한 페이지 그룹의 첫번째 페이지 번호 = ((페이지 그룹 - 1) * 한 화면에 보여질 페이지 개수) + 1

    어떤 한 페이지 그룹의 마지막 페이지 번호 = 페이지 그룹 * 한 화면에 보여질 페이지 개수 
    (단, 페이지 그룹 * 한 화면에 보여질 페이지 개수의 값이 전체 페이지보다 크다면 전체 페이지가 마지막 페이지 번호가 됩니다)
    -->
        <nav aria-label="Page navigation example">
        <ul class="pagination">
            <li class="page-item" :class="{'disabled' : isBtnPrev}">
            <a class="page-link" @click.prevent="pageArrow('prev')" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
            </li>
            <template v-for="(item, index) in pageList" :key="`list-${index}`">
            <li class="page-item" :class="{'active' : item == currentPage}"><a class="page-link" href="#" @click.prevent="page(item)">{{item+1}}</a></li>
            </template>
            <li class="page-item" :class="{'disabled' : isBtnNext}">
            <a class="page-link" @click.prevent="pageArrow('next')" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
            </li>
        </ul>
        </nav>
    </div>
</template>

<script setup>
import axios from 'axios'
import {ref, watch} from 'vue'

const list = ref([]) //보여지는 리스트
const cache = ref([]) //리스트 전체

const listCunt = ref('5') // 한 페이지에 노출될 게시글 개수

const currentPage = ref(0) //현재 페이지
const pageNum = 10 //페이징 갯수
const pageList = ref([]) // 보여지는 페이지 리스트
const totalPage = ref(0); //페이지 숫자

const isBtnPrev = ref(true); 
const isBtnNext = ref(true); 

const currentPageListStart = () =>{
  return Math.floor(currentPage.value / pageNum) * pageNum
}
// 페이징 
const paging =() => {
  //보여지는 페이지 리셋
  pageList.value = [];

  //몇페이지 까지 있는지 확인
  if(cache.value.length % listCunt.value == 0 ){
    totalPage.value = (cache.value.length / listCunt.value) -1
  } else{
    totalPage.value =  Math.ceil(cache.value.length / listCunt.value) -1
  }

  //현재페이지 기준으로 페이징 숫자 넣기
  const pageListStart = currentPageListStart()
  for(let i= 0; i< pageNum; i++){   
    if(totalPage.value >= pageListStart){
      pageList.value.push(pageListStart)
      pageListStart ++;
    }
  }
}


const getList = () =>{
  
  axios.post('API 주소')
  .then(res => {
    
    const json = res.data
    if(json.result == "success"){
      cache.value = json.data
    }
    list.value = [] //보여지는 게시물 리셋
    
    const listIdx = (listCunt.value * (currentPage.value )); // 보여질 게시물 index
    for(let i= 0; i < listCunt.value; i++ ){       //게시글 수 만큼 루프
      if(cache.value.length > listIdx) { //
        list.value.push(cache.value[listIdx])
        listIdx ++;
      }
    }
    paging()
    pageBtnCheck()

  })  
}

getList()

//페이지 번호 클릭시
const page = (e) =>{
  currentPage.value = e  
  getList()
}

//리스트 갯수 수정시
watch(listCunt,(after, before)  =>{
  currentPage.value = 0
  getList()
})

const pageBtnCheck = () =>{
  isBtnPrev.value = currentPage.value == 0 ? true : false
  
  isBtnNext.value = currentPage.value == totalPage.value ? true : false
}

watch(currentPage,(after, before)  =>{
  pageBtnCheck()
})


//페이지 이전/다음 버튼 클릭시
const pageArrow = (e) => {
  const movePage = parseInt(currentPage.value)
  if(e == 'prev'){    //이전  
    movePage = currentPageListStart() - 1    
    movePage < 0 ? movePage = 0 : ''
  } else{ //다음
    movePage = currentPageListStart() + 10
    movePage >= totalPage.value ? movePage = totalPage.value : ''
  }
  page(movePage)
}



</script>

<style scoped>

</style>