// websocket.js

// 使用 IIFE (立即调用的函数表达式) 确保模块作用域
(function() {
    let socket = null;

    // 创建 WebSocket 实例
    function createWebSocket(url) {
        if (socket) {
            // 如果已经存在 WebSocket 实例，直接返回
            return socket;
        }

        socket = new WebSocket(url);

        // 处理 WebSocket 事件
        socket.onopen = () => console.log('WebSocket connection opened');
        socket.onmessage = (event) => {
            console.log('Message received:', event.data);
            // 广播消息给所有注册的处理函数
            if (window.webSocketHandlers) {
                window.webSocketHandlers.forEach(handler => handler(event.data));
            }
        };
        socket.onclose = () => {
            console.log('WebSocket connection closed');
            socket = null; // 清除 WebSocket 实例
        };
        socket.onerror = (error) => console.error('WebSocket error:', error);

        return socket;
    }

    // 公开的接口
    window.createWebSocket = createWebSocket;
})();