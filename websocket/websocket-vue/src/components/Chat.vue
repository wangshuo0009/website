<template id="chat">
  <div class="chat-container">
    <div class="chat-header">
      <button @click="goBack">☜</button>
      <h2>{{ userName }}</h2>
    </div>
    <div class="chat-body" id="chat-body" @scroll="handleScroll">
      <div v-if="noMoreMessagesDisplayed" class="no-more-messages">已无更早的消息</div>
      <div v-for="msg in messages" :key="msg.id" class="message-wrapper"
           :class="{'align-left': msg.sender === '对方', 'align-right': msg.sender === '自己'}">
        <template v-if="!isBase64Image(msg.content)">
          <div class="message" v-html="msg.content"></div>
        </template>
        <img v-else :src="msg.content" @click="showImageInModal(msg.content)" class="message-image">
        <div class="message-time">{{ msg.timestamp }}</div>
      </div>
    </div>
    <div class="chat-footer">
      <input v-model="messageInput" type="text" placeholder="输入消息..." @focus="addKeyListener" @blur="removeKeyListener">
      <button class="upload-button" @click="clickUploadFile">📎</button>
      <button @click="sendInputMessage">发送</button>
      <input id="fileInput" type="file" accept="image/*,video/*" @change="uploadFile" ref="fileInput" hidden>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, nextTick } from 'vue';
import router from '@/route.ts';
import websocketStore from '@/websocket';

const apiUrl = import.meta.env.VITE_APP_API_BASE_URL;

const messageInput = ref('');
const userId = ref(localStorage.getItem('userId'));
const userName = ref(localStorage.getItem('userName'));
const messages = reactive([]); // 存储消息数据
const pageNum = ref(1);
const noMoreMessagesDisplayed = ref(false);
const fileInput = ref(null);

// 滚动相关状态
let previousScrollHeight = 0;

const handleScroll = () => {
  const messagesDiv = document.getElementById('chat-body');
  if (messagesDiv.scrollTop === 0) {
    pageNum.value++;
    loadHistoryMessages();
  }
};

const loadHistoryMessages = async () => {
  if (noMoreMessagesDisplayed.value) return;

  try {
    const response = await fetch(`${apiUrl}/chatMessage/searchMessage?acceptUserId=${userId.value}&pageNum=${pageNum.value}`);
    if (!response.ok) throw new Error(`网络响应失败: ${response.statusText}`);

    const historyMessages = await response.json();
    if (historyMessages.code === 200) {
      if (historyMessages.data.current >= historyMessages.data.pages) {
        noMoreMessagesDisplayed.value = true;
      }
      const newMessages = historyMessages.data.records.map((msg) => ({
        id: msg.id,
        sender: String(msg.sendUserId) === String(userId.value) ? '对方' : '自己',
        content: msg.content,
        timestamp: msg.createTime,
        isReader: msg.isReader,
      }));
      messages.unshift(...newMessages);

      nextTick(() => adjustScrollAfterLoad());
    }
  } catch (error) {
    console.error('加载历史消息时出错:', error);
  }
};

const adjustScrollAfterLoad = () => {
  const messagesDiv = document.getElementById('chat-body');
  const newScrollHeight = messagesDiv.scrollHeight;
  messagesDiv.scrollTop = newScrollHeight - previousScrollHeight;
  previousScrollHeight = newScrollHeight;
};

const sendInputMessage = () => {
  if (messageInput.value.trim()) {
    sendMessage('自己', messageInput.value);
    moveChatBodyToBottom();
  }
};

const sendMessage = (sender, content) => {
  websocketStore.dispatch('sendMessage', content); // 使用 WebSocket 发送消息
  messages.push({
    sender,
    content,
    timestamp: new Date().toLocaleString(),
  });
  messageInput.value = '';
};

const moveChatBodyToBottom = () => {
  nextTick(() => {
    const messagesDiv = document.getElementById('chat-body');
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
  });
};

const clickUploadFile = () => {
  fileInput.value?.click();
};

const uploadFile = (event) => {
  const file = event.target.files[0];
  if (!file) return;

  const reader = new FileReader();
  reader.onload = (e) => {
    const fileUrl = e.target.result;
    sendMessage('自己', fileUrl); // 本地显示上传的文件
    uploadFileToServer(file); // 上传到服务器
    moveChatBodyToBottom();
  };
  reader.readAsDataURL(file);
};

const uploadFileToServer = (file) => {
  const formData = new FormData();
  formData.append('file', file);

  fetch(`${apiUrl}/chatMessage/file/${userId.value}`, {
    method: 'POST',
    body: formData,
  })
      .then((res) => res.json())
      .then((data) => console.log('文件上传成功:', data))
      .catch((err) => console.error('文件上传失败:', err));
};

const showImageInModal = (imageSrc) => {
  const modal = document.createElement('div');
  modal.style.cssText = `
    position: fixed; top: 0; left: 0; width: 100%; height: 100%;
    background-color: rgba(0, 0, 0, 0.8); display: flex;
    justify-content: center; align-items: center; z-index: 1000;
  `;

  const fullImg = document.createElement('img');
  fullImg.src = imageSrc;
  fullImg.style.cssText = 'max-width: 90%; max-height: 90%;';
  modal.appendChild(fullImg);

  modal.addEventListener('click', () => document.body.removeChild(modal));
  document.body.appendChild(modal);
};

const initializeWebSocket = () => {
  websocketStore.dispatch('initWebSocket');
  websocketStore.dispatch('addWebSocketListener', {
    event: 'message',
    callback: (event) => {
      messages.push({
        sender: '对方',
        content: event.data,
        timestamp: new Date().toLocaleString(),
        isReader: false,
      });
      moveChatBodyToBottom();
    },
  });
};

const goBack = () => {
  router.push('/userList');
};

// 键盘事件绑定
const onKeyUp = (event) => {
  if (event.key === 'Enter') sendInputMessage();
};

const addKeyListener = () => window.addEventListener('keyup', onKeyUp);
const removeKeyListener = () => window.removeEventListener('keyup', onKeyUp);

onMounted(() => {
  loadHistoryMessages();
  initializeWebSocket();
});

onBeforeUnmount(() => removeKeyListener());
</script>
<style scoped>

.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
  overflow: hidden;

}

.chat-header {
  display: flex;
  align-items: center;
  background-color: white;
  color: #4a90e2;
  border-bottom: 2px solid #b0c7e1;
}

.chat-header button {
  background-color: transparent;
  border: none;
  font-size: 4rem;
  color: #4a90e2;
  cursor: pointer;
  margin-left: 5%;
  position: absolute;
  padding: 0;
  transform: translateY(-12%);

}

.chat-header h2 {
  flex: 1;
  text-align: center;
}

.chat-body {
  height: calc(100vh - 60px);
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.625rem;
  overflow-y: scroll;
  background-color: #e9f4ff;
  padding-top: 10px;
}
.message-wrapper{
  display: flex;
  flex-direction: column;
}
.message {
  max-width: 70%;
  padding: 0.4rem 1rem;
  border-radius: 1.25rem;
  display: inline-block;
  word-wrap: break-word;
  position: relative;
  margin-bottom: 0.3125rem;
}

.message-left {
  background-color: #ffffff;
  border: 1px solid #b0c7e1;
  align-self: flex-start;
  margin-left: 10px;
}

.message-right {
  background-color: #4a90e2;
  color: white;
  align-self: flex-end;
  margin-left: auto;
  margin-right: 10px;
}
.message-time{
  font-size: 0.75rem;
  color: #888;
  margin: 10px;
}
.message-unread{
  color: #F44336;
}
.message-image {
  width: auto;
  height: auto;
  max-width: 40%;
  min-width: 40%;
  cursor: pointer;
  margin: 0px 10px;
}
.no-more-messages{
  text-align: center;
  color: #888;
  padding: 0.5rem;

}
.chat-footer {
  display: flex;
  align-items: center;
  padding: 0.75rem;
  background-color: white;
  border-top: 2px solid #b0c7e1;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
}

.chat-footer input[type="text"] {
  flex: 1;
  padding: 0.625rem;
  border-radius: 1.25rem;
  border: 1px solid #b0c7e1;
  font-size: 1rem;
  box-sizing: border-box;
  min-width: 65%
}

.chat-footer button {
  padding: 0.625rem 1.25rem;
  border-radius: 1.25rem;
  border: none;
  background-color: #4a90e2;
  color: white;
  cursor: pointer;
  font-size: 1rem;
  margin-left: 10px;
}

.chat-footer input[type="file"] {
  display: none;
}

.upload-button {
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  margin-left: 10px;
}


/* 移动端样式 */
@media (max-width: 768px) {
  .chat-container {
    border-radius: 15px; /* 圆角边框 */
  }
}
</style>