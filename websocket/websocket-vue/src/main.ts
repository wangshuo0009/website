import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './route' // 引入 router
import webSocketStore from './websocket';



let app = createApp(App);
app.use(router) // 添加这一行
app.use(webSocketStore)
app.mount('#app')
