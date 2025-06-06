<template>
  <div class="profile-page min-h-screen bg-gray-50 py-8">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- Page Header -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">User Profile</h1>
        <p class="mt-2 text-gray-600">Manage your account and view your typing statistics</p>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="flex items-center justify-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        <span class="ml-3 text-gray-600">Loading profile...</span>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded mb-6">
        <strong class="font-bold">Error:</strong>
        <span class="block sm:inline"> {{ error }}</span>
        <button 
          @click="loadUserProfile" 
          class="mt-2 text-sm underline hover:no-underline"
        >
          Try again
        </button>
      </div>

      <!-- Profile Content -->
      <div v-else-if="currentUser" class="space-y-8">
        <!-- User Profile Component -->
        <UserProfile 
          :user="currentUser" 
          :loading="false" 
          :error="null"
          @edit-profile="showEditDialog = true"
        />

        <!-- User Statistics -->
        <UserStats :user="currentUser" />

        <!-- Recent Games Section -->
        <div class="bg-white rounded-lg shadow-md p-6">
          <h3 class="text-lg font-semibold text-gray-900 mb-4">Recent Games</h3>
          <div class="text-gray-500 text-center py-8">
            <svg class="w-12 h-12 mx-auto mb-4 text-gray-300" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M4 4a2 2 0 012-2h8a2 2 0 012 2v12a2 2 0 01-2 2H6a2 2 0 01-2-2V4zm2 0v12h8V4H6z" clip-rule="evenodd"/>
            </svg>
            <p>Recent games will appear here</p>
            <router-link 
              to="/game" 
              class="inline-block mt-4 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              Start Typing Game
            </router-link>
          </div>
        </div>
      </div>

      <!-- No User State -->
      <div v-else class="text-center py-12">
        <div class="text-gray-500 mb-4">Please sign in to view your profile</div>
        <router-link 
          to="/login"
          class="inline-block px-6 py-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
        >
          Sign In
        </router-link>
      </div>
    </div>

    <!-- Edit Profile Modal -->
    <div 
      v-if="showEditDialog" 
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      @click="showEditDialog = false"
    >
      <div 
        class="bg-white rounded-lg p-6 w-full max-w-md mx-4"
        @click.stop
      >
        <h3 class="text-lg font-semibold mb-4">Edit Profile</h3>
        <form @submit.prevent="updateProfile">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">First Name</label>
              <input 
                v-model="editForm.firstName"
                type="text" 
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Last Name</label>
              <input 
                v-model="editForm.lastName"
                type="text" 
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
            </div>
            
            <div v-if="currentUser?.provider === 'LOCAL'">
              <label class="block text-sm font-medium text-gray-700 mb-2">Username</label>
              <input 
                v-model="editForm.username"
                type="text" 
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
            </div>
          </div>
          
          <div class="flex justify-end space-x-3 mt-6">
            <button 
              type="button"
              @click="showEditDialog = false"
              class="px-4 py-2 text-gray-600 border border-gray-300 rounded-md hover:bg-gray-50"
            >
              Cancel
            </button>
            <button 
              type="submit"
              :disabled="updating"
              class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
            >
              {{ updating ? 'Saving...' : 'Save Changes' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useAuth } from '../composables/useAuth'
import { useUser } from '../composables/useUser'
import UserProfile from '../components/UserProfile.vue'
import UserStats from '../components/UserStats.vue'

const { currentUser, checkAuth } = useAuth()
const { updateUserProfile } = useUser()

const loading = ref(true)
const error = ref<string | null>(null)
const showEditDialog = ref(false)
const updating = ref(false)

const editForm = reactive({
  firstName: '',
  lastName: '',
  username: ''
})

const loadUserProfile = async () => {
  loading.value = true
  error.value = null
  
  try {
    await checkAuth()
    if (currentUser.value) {
      editForm.firstName = currentUser.value.firstName || ''
      editForm.lastName = currentUser.value.lastName || ''
      editForm.username = currentUser.value.username || ''
    }
  } catch (err) {
    error.value = 'Failed to load user profile'
    console.error('Error loading profile:', err)
  } finally {
    loading.value = false
  }
}

const updateProfile = async () => {
  if (!currentUser.value) return
  
  updating.value = true
  
  try {
    await updateUserProfile(currentUser.value.id, {
      firstName: editForm.firstName,
      lastName: editForm.lastName,
      username: editForm.username
    })
    
    // Refresh user data
    await checkAuth()
    showEditDialog.value = false
  } catch (err) {
    console.error('Error updating profile:', err)
    alert('Failed to update profile. Please try again.')
  } finally {
    updating.value = false
  }
}

onMounted(() => {
  loadUserProfile()
})
</script>

<style scoped>
.profile-page {
  min-height: calc(100vh - 64px); /* Adjust based on your navbar height */
}
</style>
