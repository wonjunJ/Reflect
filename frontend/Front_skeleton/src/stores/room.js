import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'

export const useRoomStore = defineStore('room', () => {
  const router = useRouter()
  const allRooms = ref(null)
  const filteredRooms = ref(null)



    // 모든 방 목록을 가져옴 
  const getRooms = () => {
    // axios({
    //     method: 'get',
    //     url: `${API_URL}/deposits/`,
    //     headers : {
    //         Authorization: `Token ${authStore.token}`
    //     }
    // })
    //     .then((res) => {
    //         console.log(res.data)
    //         deposits.value = res.data
    allRooms.value = ref('a')
    //         return res.data
    //     })
    //     .catch((err) => console.log(err))
    
    }

    // 필터링된 방의 목록을 가져옴
    const getFilteredRooms = () => {
        filteredRooms.value = ref('b')
    }



  return { allRooms, filteredRooms, getRooms,getFilteredRooms }
})