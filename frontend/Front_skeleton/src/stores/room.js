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
  



    // 모든 방 목록을 가져옴 
  const getRooms = () => {
    // axios({
    //   method: 'get',
    //   url: `${API_URL}/room/`,
    //   headers : {
    //     Authorization: `Token ${userStore.token}`
    //   }
    // })
    //   .then((res) => {
    //     console.log(res.data)
    //     rooms.value = res.data
    //     // rooms.value = ref('a')
    //     return rooms
    //   })
    //   .catch((err) => 
    //     console.log(err))

  }

  // 필터링된 방의 목록을 가져옴
  // 100문100답, 자유주제, 인원수 기준으로 필터링
  const getFilteredRooms = (type, num1, num2, status) => {
    // axios({
    //   method:'get',
    //   url:'${API_URL}/',
    //   headers: {
    //     Authorization: `Token ${userStore.token}`
    //   }
    // })
    //   .then((res) => {
    //     console.log(res.data)
    //     const temp = res.data.filter((item) => {
    //       return item.type === type && item.total_num  >= num1 && item.total_num <= num2 && item.status === status
    //     })
    //     return temp
    //   })
    //   .then((temp) => {
    //     console.log(temp)
    //     filteredRooms.value = temp
    //     isFiltered.value = true
    //   })
    //   .catch((err) => console.log(err))
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



  return { rooms, getRooms, myRooms, getMyRooms,API_URL }
})