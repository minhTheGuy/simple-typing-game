<template>
  <div id="app">
    <Navbar />
      <!-- Router outlet - this is where pages will be displayed -->
      <router-view />
    <Footer />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import Navbar from './components/Navbar.vue'
import Footer from './components/Footer.vue'
import { useAuth } from './composables/useAuth'

const { checkAuth } = useAuth()

// Initialize authentication state when the app starts
onMounted(() => {
  checkAuth()
})
</script>

<style scoped>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>