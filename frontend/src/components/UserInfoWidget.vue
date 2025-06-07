<template>
  <div class="user-info-widget">
    <!-- Authenticated User -->
    <div v-if="currentUser" class="flex items-center space-x-3">
      <!-- User Avatar -->
      <div class="relative">
        <img 
          v-if="currentUser.imageUrl" 
          :src="currentUser.imageUrl" 
          :alt="displayName"
          class="w-8 h-8 rounded-full object-cover"
        >
        <div 
          v-else 
          class="w-8 h-8 rounded-full bg-blue-500 flex items-center justify-center text-white text-sm font-medium"
        >
          {{ getInitials() }}
        </div>
        
        <!-- Online indicator -->
        <div class="absolute -bottom-0.5 -right-0.5 w-3 h-3 bg-green-400 border-2 border-white rounded-full"></div>
      </div>

      <!-- User Info -->
      <div class="flex-1 min-w-0">
        <div class="text-sm font-medium text-gray-900 truncate">
          {{ displayName }}
        </div>
        <div v-if="showStats" class="text-xs text-gray-300">
          Best: {{ currentUser.bestWpm || 0 }} WPM
        </div>
      </div>

      <!-- Dropdown Menu -->
      <div class="relative" v-if="showMenu">
        <button 
          @click="toggleMenu"
          class="p-1 rounded-full hover:bg-gray-100 transition-colors"
          :class="{ 'bg-gray-100': menuOpen }"
        >
          <svg class="w-4 h-4 text-gray-500" fill="currentColor" viewBox="0 0 20 20">
            <path d="M10 6a2 2 0 110-4 2 2 0 010 4zM10 12a2 2 0 110-4 2 2 0 010 4zM10 18a2 2 0 110-4 2 2 0 010 4z"/>
          </svg>
        </button>

        <!-- Dropdown Content -->
        <div 
          v-if="menuOpen"
          class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg ring-1 ring-black ring-opacity-5 z-50"
        >
          <div class="py-1">
            <router-link 
              to="/profile" 
              class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
              @click="closeMenu"
            >
              View Profile
            </router-link>
            <router-link 
              to="/settings" 
              class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
              @click="closeMenu"
            >
              Settings
            </router-link>
            <hr class="my-1">
            <button 
              @click="handleLogout"
              class="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50"
            >
              Sign out
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Not Authenticated -->
    <div v-else-if="!loading" class="flex items-center space-x-2">
      <router-link 
        to="/login"
        class="px-3 py-1.5 text-sm text-blue-600 hover:text-blue-800 transition-colors"
      >
        Sign in
      </router-link>
      <router-link 
        to="/register"
        class="px-3 py-1.5 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700 transition-colors"
      >
        Sign up
      </router-link>
    </div>

    <!-- Loading State -->
    <div v-else class="flex items-center space-x-2">
      <div class="animate-pulse">
        <div class="w-8 h-8 bg-gray-200 rounded-full"></div>
      </div>
      <div class="animate-pulse">
        <div class="h-4 bg-gray-200 rounded w-24"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useAuth } from '../composables/useAuth'

interface Props {
  showStats?: boolean
  showMenu?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  showStats: true,
  showMenu: true
})

const { currentUser, isAuthenticated, loading, logout } = useAuth()
const menuOpen = ref(false)

const displayName = computed(() => {
  if (!currentUser.value) return ''
  
  if (currentUser.value.firstName && currentUser.value.lastName) {
    return `${currentUser.value.firstName} ${currentUser.value.lastName}`
  }
  
  if (currentUser.value.username) {
    return currentUser.value.username
  }
  
  return currentUser.value.email.split('@')[0]
})

const getInitials = (): string => {
  if (!currentUser.value) return ''
  
  if (currentUser.value.firstName && currentUser.value.lastName) {
    return `${currentUser.value.firstName.charAt(0)}${currentUser.value.lastName.charAt(0)}`.toUpperCase()
  }
  
  if (currentUser.value.username) {
    return currentUser.value.username.charAt(0).toUpperCase()
  }
  
  return currentUser.value.email.charAt(0).toUpperCase()
}

const toggleMenu = () => {
  menuOpen.value = !menuOpen.value
}

const closeMenu = () => {
  menuOpen.value = false
}

const handleLogout = async () => {
  closeMenu()
  try {
    console.log('UserInfoWidget: Starting logout process...')
    await logout()
    console.log('UserInfoWidget: Logout completed successfully')
    // Use router navigation instead of window.location for better SPA behavior
    window.location.href = '/'
  } catch (error) {
    console.error('UserInfoWidget: Logout failed:', error)
  }
}

// Close menu when clicking outside
const handleClickOutside = (event: Event) => {
  const target = event.target as HTMLElement
  if (!target.closest('.relative')) {
    closeMenu()
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  console.log('UserInfoWidget mounted, currentUser:', currentUser.value)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})

// Watch for changes in currentUser to debug reactivity
watch(currentUser, (newUser, oldUser) => {
  console.log('UserInfoWidget: currentUser changed', { 
    from: oldUser?.email || 'null', 
    to: newUser?.email || 'null' 
  })
}, { immediate: true })
</script>

<style scoped>

</style>
