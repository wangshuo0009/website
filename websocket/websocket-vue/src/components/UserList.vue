<template>
  <div class="container">
    <div class="user-list-container">
      <div id="notification" class="browser-notification"></div>

      <div class="header">
        <h2>用户列表</h2>
      </div>
      <ul class="user-list">
        <li v-for="user in users" :key="user.id" class="user-list-item" @click="openChat(user.id, user.name)">
          <div class="user-avatar">{{ user.name.charAt(0) }}</div>
          <div class="user-details">
            <div class="user-name-container">
              <div class="user-name">
                {{ user.name }}
                <span v-if="user.unreadMessageCount > 0" class="unread-message-count" id="unreadMessageCount">
                  {{ user.unreadMessageCount }}
                </span>
              </div>
            </div>
            <div style="display: flex;">
              <div class="user-message">
                {{ user.lastMessage === null ? (user.lastMessageTime === null ? '' : '[图片]') : user.lastMessage }}
              </div>
              <div class="last-MessageTime">{{ user.lastMessageTime }}</div>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <!-- 桌面端显示聊天容器 -->
    <div v-if="isDesktop" class="chat-container" id="chat-container">
      <!-- Chat content will be loaded dynamically -->
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import router from "@/route.ts";
import { useStore } from 'vuex';

const apiUrl = import.meta.env.VITE_APP_API_BASE_URL;
const websocketStore = useStore();

// 用户列表
const users = ref([]);
// 检测是否为桌面端
const isDesktop = ref(window.innerWidth > 768);

// 从后端获取用户列表
const fetchUserList = async () => {
  try {
    const response = await fetch(`${apiUrl}/user/searchAllUser`);
    const data = await response.json();
    if (data.success && data.code === 200) {
      users.value = data.data; // 成功获取用户数据
    } else {
      console.error('获取用户列表失败');
    }
  } catch (error) {
    console.error('获取用户列表时出错:', error);
  }
};

// 初始化 WebSocket 连接并监听消息
const initializeWebSocket = async () => {
  await websocketStore.dispatch('initWebSocket');
  await websocketStore.dispatch('addWebSocketListener', {
    event: 'message',
    callback: (event) => {
      const message = event.data;
      showNotification('新消息', message);
      console.log('WebSocket 消息回调触发');

      // 刷新用户展示列表
      fetchUserList();
    },
  });
};

// 显示通知的方法
const showNotification = (title, body) => {
  const notification = document.getElementById('notification');
  notification.textContent = `${title}: ${body}`;
  notification.classList.add('show');

  // 处理特定的消息提示逻辑
  if (body !== '您的连接已在另一台设备上登录，当前连接将关闭。') {
    setTimeout(() => {
      notification.classList.remove('show');
    }, 3000); // 3秒后隐藏
  }

  // 使用浏览器通知
  if (Notification.permission === 'granted') {
    new Notification(title, { body });
  } else if (Notification.permission !== 'denied') {
    Notification.requestPermission().then((permission) => {
      if (permission === 'granted') {
        new Notification(title, { body });
      }
    });
  }
};

// 请求通知权限
const requestNotificationPermission = () => {
  console.log('当前通知权限状态:', Notification.permission);
  if (Notification.permission === 'default') {
    Notification.requestPermission().then((permission) => {
      if (permission === 'granted') {
        console.log('用户已授予通知权限');
      } else {
        console.log('用户拒绝了通知权限');
      }
    });
  }
};

// 打开聊天页面
const openChat = (userId, userName) => {
  localStorage.setItem('userId', userId);
  localStorage.setItem('userName', userName);

  if (window.innerWidth <= 768) {
    // 移动端直接跳转到聊天页面
    router.push('/chat');
  } else {
    // 桌面端动态加载聊天页面到容器
    const chatContainer = document.getElementById('chat-container');
    chatContainer.innerHTML = '';

    const iframe = document.createElement('iframe');
    iframe.src = '/#/chat'; // 确保 `chat` 页面路径正确
    iframe.style.width = '100%';
    iframe.style.height = '100%';
    iframe.style.border = 'none';
    chatContainer.appendChild(iframe);

    // 清除未读消息标记
    const unreadDom = document.getElementById('unreadMessageCount');
    if (unreadDom) {
      unreadDom.remove();
    }
  }
};

// 初始化操作
onMounted(() => {
  requestNotificationPermission();
  fetchUserList();
  initializeWebSocket();

  // 监听窗口大小变化以更新设备类型状态
  window.addEventListener('resize', () => {
    isDesktop.value = window.innerWidth > 768;
  });
});
</script>

<style scoped>
body, html {
  margin: 0;
  padding: 0;
  width: 100vw;
  height: 100vh;
  font-family: Arial, sans-serif;
  background-color: #d0e7ff; /* 浅蓝色背景 */
  display: flex;
  justify-content: center; /* 居中对齐 */
  align-items: center; /* 垂直居中 */
  overflow: hidden; /* 禁用父页面的滚动 */

}
.browser-notification {
  position: fixed;
  top: 10px;
  right: 10px;
  background-color: #444;
  color: #fff;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  z-index: 10000;
  display: none; /* 默认为隐藏 */
}
.browser-notification.show {
  display: block;
}

.container {
  display: flex;
  flex-direction: row;
  width: 100vw;
  height: 100vh;
  background-color: #e9f4ff; /* 较浅的背景色 */
  border-radius: 15px; /* 圆角边框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
  overflow: hidden;
}

.user-list-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 400px; /* 用户列表宽度 */
  height: 100%;
  background-color: #e9f4ff; /* 较浅的背景色 */
  border-right: 2px solid #b0c7e1; /* 右侧边框 */
  overflow: hidden; /* 隐藏超出内容 */
}

.header {
  background-color: white; /* 白色背景 */
  color: #4a90e2; /* 蓝色标题 */
  text-align: center;
  border-bottom: 2px solid #b0c7e1; /* 添加底部边框 */
  /*padding: 20px 10px;*/
}

.user-list {
  padding: 0;
  margin: 0;
  list-style-type: none;
  flex: 1;
  overflow-y: auto;
  /*padding: 1rem; !* 内边距 *!*/
}

.user-list-item {
  display: flex;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid #b0c7e1;
  cursor: pointer;
  transition: background-color 0.3s;
  background-color: #ffffff; /* 白色背景 */
}

.user-list-item:hover {
  background-color: #c0ddff; /* 鼠标悬停时背景色 */
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%; /* 圆形头像 */
  background-color: #b0c7e1;
  margin-right: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.5rem;
}

.user-details {
  flex: 1;
}

.user-name {
  font-size: 1.25rem;
  color: #333;
  margin: 0;
  text-align: left;
}

.user-message {
  font-size: 1rem;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis; /* 超长消息显示省略号 */
  text-align: left;

}
.last-MessageTime{
  font-size: 1rem;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis; /* 超长消息显示省略号 */
  text-align: right;
  margin-left: auto;
}
.unread-message-count {
  background-color: red;
  color: white;
  border-radius: 50%;
  padding: 0.1rem 0.4rem;
  font-size: 0.75rem;
  margin-left: 0.5rem;
  vertical-align: top
}
/* 移动端样式 */
@media (max-width: 768px) {
  .container {
    flex-direction: column;
  }

  .user-list-container {
    width: 100%;
    max-width: none; /* 取消最大宽度限制 */
    border: none;
  }

  .chat-container {
    display: none; /* 移动端不显示 chat 容器 */
  }
}

/* 电脑端样式 */
@media (min-width: 769px) {
  .chat-container {
    flex: 1;
    background-color: #f7f7f7; /* chat 容器背景色 */
  }
}
</style>