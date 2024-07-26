<template>
    <footer>
      <a href="https://github.com/wangshuo0009/cloudflare" target="_blank">&copy; 2024 wangshuo. 保留所有权利.</a> |
      <a href="#" @click.prevent="openDonateModal">捐赠</a>
    </footer>
  </template>
  
  <script>
  export default {
    methods: {
      openDonateModal() {
        this.$emit('openDonate');
      }
    }
  };
  </script>
  
  <style scoped>
  footer {
    position: absolute;
    bottom: 10px;
    text-align: center;
    width: 100%;
    font-size: 12px;
    color: #ccc;
  }
  
  a {
    color: #fff;
    text-decoration: none;
  }
  
  a:hover,
  a:active,
  a:visited {
    color: #fff;
  }
  </style>