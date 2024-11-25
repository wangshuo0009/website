<template>
  <div class="login-container">
    <h2>Welcome to the Chat</h2>
    <!-- 用户名输入框 -->
    <input
        v-model="account"
        type="text"
        placeholder="Account"
        @focus="addKeyListener"
        @blur="removeKeyListener"
    />
    <!-- 密码输入框 -->
    <input
        v-model="password"
        type="password"
        placeholder="Password"
        @focus="addKeyListener"
        @blur="removeKeyListener"
    />
    <!-- 登录按钮 -->
    <button @click="login">Login</button>
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

// 定义响应式变量
const account = ref('');  // 用户名
const password = ref(''); // 密码

const router = useRouter();       // Vue Router 实例
const websocketStore = useStore(); // Vuex Store 实例

// 键盘事件监听器
function onKeyUp(event) {
  if (event.key === 'Enter') {
    login(); // 按下 Enter 键时触发登录
  }
}

// 添加键盘监听事件
function addKeyListener() {
  window.addEventListener('keyup', onKeyUp);
}

// 移除键盘监听事件
function removeKeyListener() {
  window.removeEventListener('keyup', onKeyUp);
}

// 组件销毁前移除监听器
onBeforeUnmount(() => {
  removeKeyListener();
});

// 登录方法
const login = async () => {
  if (!account.value || !password.value) {
    alert('请填写用户名和密码！'); // 检查输入是否为空
    return;
  }

  try {
    // 发送登录请求
    const response = await fetch(`${import.meta.env.VITE_APP_API_BASE_URL}/login/signIn`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        account: account.value,
        password: password.value,
      }),
    });
    const data = await response.json();

    // 处理登录结果
    if (data.success) {
      alert('登录成功！');
      router.push('/userList'); // 跳转到用户列表页
      await websocketStore.dispatch('initWebSocket'); // 初始化 WebSocket 连接
    } else {
      alert(`登录失败: ${data.message}`);
    }
  } catch (error) {
    console.error('登录请求失败:', error);
    alert('登录失败，请检查网络或稍后再试！');
  }
};
</script>

<style>

body, html {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  font-family: Arial, sans-serif;
  background-color: #d0e7ff; /* 使用浅蓝色背景 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-container {
  background-color: white;
  padding: 2rem;
  border-radius: 1.25rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 300px;
  text-align: center;
}

.login-container h2 {
  margin-bottom: 1.5rem;
  color: #4a90e2; /* 标题颜色 */
}

.login-container input[type="text"], .login-container input[type="password"] {
  width: 100%;
  padding: 0.625rem;
  margin-bottom: 1rem;
  border-radius: 1.25rem;
  border: 1px solid #b0c7e1;
  font-size: 1rem;
}

.login-container button {
  width: 100%;
  padding: 0.625rem;
  border-radius: 1.25rem;
  border: none;
  background-color: #4a90e2; /* 按钮颜色 */
  color: white;
  cursor: pointer;
  font-size: 1rem;
}
</style>