<template>
    <div v-if="isVisible" class="modal" @click.self="closeModal">
      <div class="modal-content">
        <span @click="closeModal" class="close">&times;</span>
        <div class="payment-container">
          <h2>支付页面</h2>
          <label for="amount">输入金额</label>
          <input type="text" v-model="amount" id="amount" inputmode="decimal" placeholder="0.00" required>
          <button @click="submitPayment" :disabled="isSubmitting">{{ isSubmitting ? '处理中...' : '确认支付' }}</button>
          <div v-html="qrCode"></div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      isVisible: Boolean,
    },
    data() {
      return {
        amount: '',
        isSubmitting: false,
        qrCode: '',
      };
    },
    methods: {
      closeModal() {
        this.$emit('close');
        this.amount = '';
        this.qrCode = '';
      },
      async submitPayment() {
        if (parseFloat(this.amount) < 0.01) {
          alert('请输入有效的金额，最小为0.01元');
          return;
        }
  
        this.isSubmitting = true;
  
        try {
          const response = await fetch('https://pay.wangshuos.com/pay/alipay', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              orderId: 'wangshuo-orderId',
              productId: 'wangshuo-productId',
              productName: '商品名称',
              productPrice: 0.99,
              productQuantity: 1,
              amount: this.amount,
            }),
          });
          const data = await response.json();
          if (data.code === 200) {
            this.qrCode = `<img src="${data.data}" alt="QR Code">`;
          } else {
            alert('支付失败：' + data.message);
          }
        } catch (error) {
          console.error('支付请求失败:', error);
          alert('支付请求失败，请稍后重试。');
        } finally {
          this.isSubmitting = false;
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .modal {
    display: flex;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    justify-content: center;
    align-items: center;
  }
  
  .modal-content {
    background: #fff;
    border-radius: 15px;
    width: 100%;
    max-width: 400px;
    box-sizing: border-box;
    position: relative;
    overflow: hidden;
    margin: auto;
  }
  
  .close {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 20px;
    cursor: pointer;
  }
  
  .payment-container {
    background-color: #fff;
    padding: 20px;
    border-radius: 15px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 400px;
    text-align: center;
    box-sizing: border-box;
  }
  
  .payment-container input,
  .payment-container button {
    width: 85%;
    padding: 10px;
    margin: 10px 0;
    border-radius: 5px;
    border: 1px solid #ccc;
    box-sizing: border-box;
    text-align: center;
    position: relative;
  }
  
  .payment-container button {
    background-color: #007bff;
    color: white;
    border: none;
    cursor: pointer;
    overflow: hidden;
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }
  
  .payment-container button:hover {
    background-color: #0056b3;
  }
  
  .payment-container button.loading {
    background-color: #ddd;
    color: #666;
    cursor: not-allowed;
  }
  
  .payment-container button.loading::after {
    content: '';
    display: inline-block;
    width: 20px;
    height: 20px;
    border: 3px solid rgba(0, 0, 0, 0.3);
    border-radius: 50%;
    border-top: 3px solid #007bff;
    animation: spin 1s linear infinite;
    margin-left: 10px;
    box-sizing: border-box;
  }
  
  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
  
  .hidden {
    display: none !important;
  }
  </style>