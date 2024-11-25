// websocket.ts
import { createStore, ActionContext, MutationTree, Store } from "vuex";

const websocketUrl = import.meta.env.VITE_APP_WEBSOCKET_BASE_URL;

// 定义 State 接口
interface WebSocketState {
    webSocket: WebSocket | null;
}

// 定义 State
const state: WebSocketState = {
    webSocket: null,
};

// 定义 MutationTree
const mutations: MutationTree<WebSocketState> = {
    SET_WEBSOCKET(state: WebSocketState, webSocket: WebSocket) {
        state.webSocket = webSocket;
    },
};
// 定义 ActionContext 的类型
type WebSocketActionContext = ActionContext<WebSocketState, WebSocketState>;

// 定义 Actions
const actions = {
    initWebSocket({ commit }: WebSocketActionContext) {
        const socket = new WebSocket(`${websocketUrl}/chat`);
        socket.onopen = () => {
            console.log("WebSocket 连接成功");
            commit("SET_WEBSOCKET", socket);
        };
        socket.onmessage = (event: MessageEvent) => {
            console.log("收到消息:", event.data);
        };
        socket.onclose = () => {
            console.log("WebSocket 连接关闭");
        };
        socket.onerror = (error) => {
            console.error("WebSocket 错误:", error);
        };
    },
    sendMessage({ state }: WebSocketActionContext, message: string) {
        if (state.webSocket && state.webSocket.readyState === WebSocket.OPEN) {
            state.webSocket.send(message);
        } else {
            console.error("WebSocket 未连接");
        }
    },
    addWebSocketListener(
        { state }: WebSocketActionContext,
        { event, callback }: { event: string; callback: EventListener }
    ) {
        if (state.webSocket) {
            state.webSocket.addEventListener(event, callback);
        } else {
            console.error("WebSocket 未初始化");
        }
    },
    closeWebSocket({ state }: WebSocketActionContext) {
        if (state.webSocket) {
            state.webSocket.close();
        }
    },
};

// 创建 Store
const webSocketStore: Store<WebSocketState> = createStore({
    state,
    mutations,
    actions,
});

export default webSocketStore;