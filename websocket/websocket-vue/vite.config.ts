import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import * as path from 'path';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  server: {
    port: 5173,          // 配置服务器端口为 5173
    strictPort: true,    // 如果端口已被占用，将不会使用其他端口
    hmr: {
      port: 5173,        // 配置 HMR 使用的端口
    },
  }
});