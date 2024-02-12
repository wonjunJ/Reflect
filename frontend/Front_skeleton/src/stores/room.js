import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import { useUserStore } from './user'
import axios from 'axios'

export const useRoomStore = defineStore('room', () => {
  const API_URL = 'http://localhost:8080'
  const userStore = useUserStore()
  const router = useRouter()
  const rooms = ref([])
  const filteredRooms = ref([])
  const myRooms = ref([])
  const isFiltered = ref(false)
  const searchedRooms = ref([])
  const roomDetail = ref([])

  
  //방 상세정보 조회
  const getRoomDetail = (roomId) => {
    axios({
      method: 'get',
      url: `${API_URL}/room/detail/${roomId}`,
      headers : {
        Authorization: `Token ${userStore.token}`
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
    const {type,question_id,thumbnail,title,intro,hash_name,mode,total_num} = payload
    axios({
      method: 'post',
      url: `${API_URL}/room/create`,
      headers : {
        Authorization: `Token ${userStore.token}`
      },
      data: {type,question_id,thumbnail,title,intro,hash_name,mode,total_num}
    })
    .then((res) => {
        console.log(res.data)
        console.log('방 생성 완료')
        router.push({name:'waiting'})
      })
    .catch((err) => {
      console.log(err)
      console.log('방 생성 실패')
    })
  }




    // 모든 방 목록을 가져옴 
  const getRooms = () => {
    axios({
      method: 'get',
      url: `${API_URL}/room`,
      headers : {
        Authorization: `Token ${userStore.token}`
      }
    })
      .then((res) => {
        console.log(res.data)
        rooms.value = res.data
        return rooms
      })
      .catch((err) => 
        console.log(err))

  }

  // 필터링된 방의 목록을 가져옴
  // 100문100답, 자유주제, 인원수, 모집중 기준으로 필터링
  const getFilteredRooms = (filter, from, to, pageable) => {
    axios({
      method:'get',
      url:`${API_URL}/room`,
      headers: {
        Authorization: `Token ${userStore.token}`
      },
      params: {
        filter: filter.join(','), // "FREE,BACKMOON,Waiting"와 같은 형태로 변환
        from: from,
        to: to,
        pageable: pageable
      }
    })
      .then((res) => {
        console.log(res.data)
        filteredRooms.value = res.data
        isFiltered.value = true
      })
      .catch((err) => console.log(err))
  }

  // 내가 가입한 방 리스트 조회
  const getMyRooms = () => {
    axios({
      method: 'get',
      url: `${API_URL}/room/mine`,
      headers : {
        Authorization: `Token ${userStore.accessToken}`
      }
    })
      .then((res) => {
        console.log(res.data)
        myRooms.value = res.data
        // rooms.value = ref('a')
        return myRooms
      })
      .catch((err) => 
        console.log(err))
  }

  // 검색 조회
  const getSearchRooms = (search) => {
    const from = 2
    const to = 8
    const pageable = 0
    axios({
      method: 'get',
      url: `${API_URL}/room`,
      headers : {
        Authorization: `Token ${userStore.accessToken}`
      },
      params: {searchWord:search, from:from, to:to, pageable:pageable}
    })
    .then((res) => {
        console.log(res.data)
        searchedRooms.value = res.data
      })
    .catch((err) => console.log(err))
  }



  return { roomDetail, getRoomDetail,rooms, getRooms, myRooms, getMyRooms,API_URL, createRoom, getFilteredRooms, getSearchRooms, searchedRooms, filteredRooms }
})