<template>
    <div class="min-h-screen bg-gray-100 flex flex-col items-center justify-center">
        <!-- User Welcome Message -->
        <div v-if="currentUser" class="mb-4 p-4 bg-green-100 border border-green-400 text-green-700 rounded">
            <div class="flex items-center gap-3">
                <img v-if="currentUser.imageUrl" :src="currentUser.imageUrl" alt="Profile" class="w-8 h-8 rounded-full">
                <span>Welcome back, {{ getUserDisplayName() }}!</span>
                <div v-if="currentUser.bestWpm" class="text-sm bg-green-200 px-2 py-1 rounded">
                    Best: {{ currentUser.bestWpm }} WPM
                </div>
            </div>
        </div>
        
        <!-- Login Success Message -->
        <div v-else-if="showLoginSuccess" class="mb-4 p-4 bg-blue-100 border border-blue-400 text-blue-700 rounded">
            Login successful! Your game progress will be saved.
        </div>
        
        <GameInterface @game-completed="handleGameCompleted" />
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import GameInterface from '../components/GameInterface.vue'
import { useAuth } from '../composables/useAuth'

const route = useRoute()
const { currentUser, checkAuth } = useAuth()

const showLoginSuccess = ref(false)

const getUserDisplayName = () => {
    if (!currentUser.value) return ''
    
    if (currentUser.value.firstName && currentUser.value.lastName) {
        return `${currentUser.value.firstName} ${currentUser.value.lastName}`
    }
    
    if (currentUser.value.username) {
        return currentUser.value.username
    }
    
    return currentUser.value.email.split('@')[0]
}

const handleGameCompleted = async (gameResult: any) => {
    if (!currentUser.value) return
    
    try {
        // Here you would save the game result to the backend
        console.log('Game completed:', gameResult)
        // await saveGameResult(currentUser.value.id, gameResult)
    } catch (error) {
        console.error('Failed to save game result:', error)
    }
}

onMounted(async () => {
    // Check for JWT token in URL parameters (from OAuth2 redirect)
    if (route.query.token) {
        console.log('Game.vue: Found token in URL, storing...')
        localStorage.setItem('userToken', route.query.token as string)
        // Remove token from URL for security
        window.history.replaceState({}, document.title, window.location.pathname)
    }
    
    // Check authentication status
    console.log('Game.vue: Checking auth on mount')
    await checkAuth()
    console.log('Game.vue: Auth check completed, currentUser:', currentUser.value)
    
    // Show login success message if redirected from OAuth
    if (route.query.login === 'success') {
        showLoginSuccess.value = true
        setTimeout(() => {
            showLoginSuccess.value = false
        }, 5000)
    }
})

// Watch for changes in currentUser to debug reactivity
watch(currentUser, (newUser, oldUser) => {
  console.log('Game.vue: currentUser changed', { 
    from: oldUser?.email || 'null', 
    to: newUser?.email || 'null' 
  })
}, { immediate: true })
</script>