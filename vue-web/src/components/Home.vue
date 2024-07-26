<template>
  <div>
    <div id="contentDiv" class="content">
      <p>恭喜你居然找到我了小宝贝。</p>
      <!-- <img src="require('@/assets/images/kisskiss.JPG')" alt="卧槽，我图片呢？"> -->
    </div>
    <!-- <Footer @openDonate="openModal" /> -->
    <Pay :isVisible="isModalVisible" @close="closeModal" />
  </div>
</template>

<script>
import Footer from './Footer.vue';
import Pay from './Pay.vue';

export default {
  components: {
    Footer,
    Pay,
  },
  data() {
    return {
      isModalVisible: false,
      images: [
        '@/assets/images/紫色女.jpeg',
        '@/assets/images/露娜cos.jpeg',
        '@/assets/images/jk女.jpeg',
        '@/assets/images/包臀女.jpeg',
      ],
      currentImageIndex: Math.floor(Math.random() * 4),
    };
  },
  computed: {
    backgroundImage() {
      return `url(${this.images[this.currentImageIndex]})`;
    }
  },
  methods: {
    openModal() {
      this.isModalVisible = true;
    },
    closeModal() {
      this.isModalVisible = false;
    },
    handleClick() {
      if (this.isModalVisible) return; // 如果弹框显示，忽略点击事件

      const contentDiv = document.getElementById('contentDiv');
      contentDiv.style.display = contentDiv.style.display === 'none' ? 'block' : 'none';
    },
    handleDoubleClick() {
      if (this.isModalVisible) return; // 如果弹框显示，忽略双击事件

      if (window.innerWidth <= 768) {
        this.currentImageIndex = (this.currentImageIndex + 1) % this.images.length;
      }
    }
  },
  watch: {
    $route(to, from) {
      if (to.path === '/' && from.path !== '/') {
        this.$nextTick(() => {
          document.body.style.backgroundImage = this.backgroundImage;
        });
      }
    }
  },
  mounted() {
    document.body.style.backgroundImage = this.backgroundImage;

    this.$el.addEventListener('click', this.handleClick);
    this.$el.addEventListener('dblclick', this.handleDoubleClick);
  },
  beforeDestroy() {
    this.$el.removeEventListener('click', this.handleClick);
    this.$el.removeEventListener('dblclick', this.handleDoubleClick);
  }
};
</script>

<style scoped>
.content {
  text-align: center;
}

p {
  font-size: 50px;
  font-weight: bold;
  background: linear-gradient(90deg, red, orange, yellow, green, blue, indigo, violet);
  background-size: 400%;
  -webkit-background-clip: text;
  color: transparent;
  animation: rainbow 5s linear infinite;
  margin: 20px 0 0 0;
}

img {
  max-width: 80%;
  height: auto;
  border: 5px solid white;
  border-radius: 10px;
}

@keyframes rainbow {
  0% {
    background-position: 0% 50%;
  }
  100% {
    background-position: 100% 50%;
  }
}
</style>