import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import { useUserStore } from './user'
import axios from 'axios'

export const useRoomStore = defineStore('room', () => {
  const API_URL = 'https://i10d207.p.ssafy.io:8443/api'
  const userStore = useUserStore()
  const token = userStore.accessToken
  const router = useRouter()
  const rooms = ref([])
  const filteredRooms = ref([])
  const myRooms = ref([])
  const isFiltered = ref(false)
  const searchedRooms = ref([])
  const roomDetail = ref([])
  const pagingInfo = ref([])
  const waitingId = ref(0)
  

  
  //방 상세정보 조회
  const getRoomDetail = (roomId) => {
  
    axios({
      method: 'get',
      url: `${API_URL}/room/detail/${roomId}`,
      headers : {
        Authorization: `Bearer ${token}`
      }
    })
    .then((res) => {
        console.log(res.data)
        roomDetail.value = res.data
      })
    .catch((err) => 
        console.log(err))
  }




  // 방 생성
  const createRoom = (payload) => {
    //const {type,questionId,thumbnail,title,intro,hashTags,mode,totalNum} = payload
    console.log(token)
    console.log(payload)
    const json = {
      "totalNum" : payload.totalNum,
      "type" : payload.type,
      "mode" : payload.mode,
      "title" : payload.title,
      "intro" : payload.intro,
      "thumbnail" : payload.thumbnail,
      "questionId" : payload.questionId,
      "hashTags" : payload.hashTags
    }
    console.log(json);
    axios({
      method: 'post',
      url: `${API_URL}/room/create`,
      headers : {
        Authorization: `Bearer ${token}`
      },
      data: json
    })
    .then((res) => {
        console.log(res.data)
        console.log('방 생성 완료')
        waitingId.value = res.data.roomId
        router.push({path:`/waiting/${res.data.roomId}`, params:{id:res.data.roomId}})
      })
    .catch((err) => {
      console.log(err)
      console.log('방 생성 실패')
    })
  }




    // 모든 방 목록을 가져옴 
  const getRooms = (page) => {
    const from = 2
    const to = 8
    axios({
      method: 'get',
      url: `${API_URL}/room`,
      headers : {
        Authorization: `Bearer ${token}`
      },
      params: {
        from: from,
        to: to,
        page: page
      }
    })
      .then((res) => {
        console.log(res.data.data)
        // console.log(res.data.pagingInfo)
        rooms.value = res.data.data
        pagingInfo.value = res.data.pagingInfo
        console.log(pagingInfo.value)
        return rooms
      })
      .catch((err) => 
        console.log(err))

  }

  // 필터링된 방의 목록을 가져옴
  // 100문100답, 자유주제, 인원수, 모집중 기준으로 필터링
  const getFilteredRooms = (filter, from, to, page) => {
    console.log(from)
    console.log(to)
    console.log(page)
    console.log(filter)
    axios({
      method:'get',
      url:`${API_URL}/room`,
      headers: {
        Authorization: `Bearer ${token}`
      },
      params: {
        filter: filter.join(','), // "FREE,BACKMOON,Waiting"와 같은 형태로 변환
        from: from,
        to: to,
        page: page
      }
    })
      .then((res) => {
        console.log(res.data.data)
        filteredRooms.value = res.data.data
        pagingInfo.value = res.data.pagingInfo
        console.log(pagingInfo.value)
        // isFiltered.value = true
        return filteredRooms
      })
      .catch((err) => console.log(err))
  }

  // 내가 가입한 방 리스트 조회
  const getMyRooms = (page) => {
    axios({
      method: 'get',
      url: `${API_URL}/room/mine`,
      headers : {
        Authorization: `Bearer ${token}`
      }
    })
      .then((res) => {
        console.log(res.data.data)
        myRooms.value = res.data.data
        pagingInfo.value = res.data.pagingInfo
        console.log(pagingInfo.value)
        // rooms.value = ref('a')
        return myRooms
      })
      .catch((err) => 
        console.log(err))
  }

  // 검색 조회
  const getSearchRooms = (searchKey, page) => {
    const from = 2
    const to = 8
    axios({
      method: 'get',
      url: `${API_URL}/room`,
      headers : {
        Authorization: `Bearer ${token}`
      },
      params: {search:searchKey, from:from, to:to, page:page}
    })
    .then((res) => {
        console.log(res.data.data)
        searchedRooms.value = res.data.data
        pagingInfo.value = res.data.pagingInfo
        console.log(pagingInfo.value)
        console.log('검색완료')
        return searchedRooms
      })
    .catch((err) => {
      console.log('검색 실패')
      console.log(err)
    })
  }



  return { token,pagingInfo,roomDetail, getRoomDetail,rooms, getRooms, myRooms, getMyRooms,API_URL, createRoom, getFilteredRooms, getSearchRooms, searchedRooms, filteredRooms }
})