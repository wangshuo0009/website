import Vue from 'vue';
import Router from 'vue-router';
import Home from '@/components/Home.vue';
// import Pay from '@/components/Pay.vue'; // 如果需要为支付弹框配置单独的路由

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home,
    },
    // 如果支付弹框需要单独的路由，可以取消以下注释
    // {
    //   path: '/pay',
    //   name: 'Pay',
    //   component: Pay,
    // },
  ],
});