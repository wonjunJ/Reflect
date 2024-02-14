import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import Index from '@/views/IndexView.vue'
import MyPage from '@/views/MyInfo/MyPageView.vue'
import Signup from '@/views/User/SignupView.vue'
import Login from '@/views/User/LoginView.vue'
import Diary from '@/views/Diary/DiaryMain.vue'
import DiaryDetail from '@/views/Diary/DiaryDetail.vue'
import Forgot from '@/views/User/ForgotView.vue'
import Feelings from '@/views/MyInfo/FeelingsView.vue'
import Interests from '@/views/MyInfo/InterestsView.vue'
import RoomListView from '@/views/Room/RoomListView.vue'
import RoomDetailView from '@/views/Room/RoomDetailView.vue'
import RoomWaitingView from '@/views/Room/RoomWaitingView.vue'
import DiaryListVue from '@/components/Diary/DiaryList.vue'
import DiaryDetailVue from '@/views/Diary/DiaryDetail.vue'
import QnA from '@/views/MyInfo/QnAView.vue'
import Social from '@/views/User/SocialView.vue'


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
      path: '/DiaryDetail/:id',
      name: 'DiaryDetail',
      component: DiaryDetail,
      props: true,
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
    },
    {
      path: '/forgot',
      name: 'forgot',
      component: Forgot,
    },
    {
      path: '/feelings',
      name: 'feelings',
      component: Feelings,
    },
    {
      path: '/interests',
      name: 'interests',
      component: Interests,
    },
    {
      path: '/room',
      name: 'roomlist',
      component: RoomListView,
      // meta: { requresAuth: true},
    },
    {
      path: '/roomdetail',
      name: 'roomdetail',
      component: RoomDetailView,
      // meta: { requresAuth: true},
    },
    {
      path: '/waiting',
      name: 'waiting',
      component: RoomWaitingView,
      // meta: { requresAuth: true},  
    },
    {
      path: '/100QnA',
      name: '100QnA',
      component: QnA,
    },
    {
      path: '/social',
      name: 'social',
      component: Social
    }
  ]
})

export default router
