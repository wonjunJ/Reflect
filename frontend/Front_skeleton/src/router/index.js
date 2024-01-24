import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import Index from '@/views/IndexView.vue'
import MyPage from '@/views/MyInfo/MyPageView.vue'
import Signup from '@/views/User/SignupView.vue'
import Login from '@/views/User/LoginView.vue'
import Diary from '@/views/Diary/DiaryMain.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/index',
      name: 'index',
      component: Index,
    },
    {
      path: '/signup',
      name: 'signup',
      component: Signup
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/Diary',
      name: 'Diary',
      component: Diary
    },
    {
      path: '/',
      name: 'mypage',
      component: MyPage,
      beforeEnter: (to, from) => {
        const userStore = useUserStore()
        if (!userStore.isLogin) {
          return {name: 'index'}
        }
      }
    }
  ]
})

export default router
