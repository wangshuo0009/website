import { createRouter, createWebHashHistory } from 'vue-router';
import Login from '@/components/Login.vue';
import UserList from '@/components/UserList.vue';
import Chat from '@/components/Chat.vue';

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/userList',
    name: 'userList',
    component: UserList
  },
  {
    path: '/chat',
    name: 'chat',
    component: Chat
  }
];

const router = createRouter({
  history: createWebHashHistory(), // 使用 hash 模式
  routes,
});
// 全局路由守卫（如有需要）
// router.beforeEach((to, from, next) => {
//   const isAuthenticated = localStorage.getItem('userId'); // 用户登录状态检查
//
//   if (to.path !== '/login' && !isAuthenticated) {
//     next('/login'); // 重定向到登录页面
//   } else {
//     next(); // 允许访问
//   }
// });
export default router;