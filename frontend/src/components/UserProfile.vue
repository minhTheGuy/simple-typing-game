<template>
  <div class="user-profile bg-white rounded-lg shadow-md p-6">
    <!-- Loading State -->
    <div v-if="loading" class="flex items-center justify-center py-8">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      <span class="ml-2 text-gray-600">Loading user profile...</span>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
      <strong class="font-bold">Error:</strong>
      <span class="block sm:inline"> {{ error }}</span>
    </div>

    <!-- User Profile Content -->
    <div v-else-if="user" class="space-y-6">
      <!-- Profile Header -->
      <div class="flex items-center space-x-4">
        <div class="relative">
          <img 
            v-if="user.imageUrl" 
            :src="user.imageUrl" 
            :alt="`${user.firstName || user.username}'s profile`"
            class="w-20 h-20 rounded-full object-cover border-4 border-blue-100"
          >
          <div 
            v-else 
            class="w-20 h-20 rounded-full bg-blue-500 flex items-center justify-center text-white text-2xl font-bold"
          >
            {{ getInitials(user) }}
          </div>
          
          <!-- Provider Badge -->
          <div 
            v-if="user.provider && user.provider !== 'LOCAL'"
            class="absolute -bottom-1 -right-1 bg-white rounded-full p-1 shadow-md"
          >
            <component :is="getProviderIcon(user.provider)" class="w-4 h-4" />
          </div>
        </div>

        <div class="flex-1">
          <h1 class="text-2xl font-bold text-gray-900">
            {{ user.firstName && user.lastName 
                ? `${user.firstName} ${user.lastName}` 
                : user.username || 'User' }}
          </h1>
          <p class="text-gray-600">{{ user.email }}</p>
          <div class="flex items-center space-x-4 mt-2">
            <span class="px-2 py-1 bg-blue-100 text-blue-800 text-xs font-medium rounded-full">
              {{ user.role === 'ROLE_ADMIN' ? 'Admin' : 'User' }}
            </span>
            <span v-if="user.provider" class="px-2 py-1 bg-gray-100 text-gray-800 text-xs font-medium rounded-full">
              {{ user.provider === 'LOCAL' ? 'Local Account' : `${user.provider} Account` }}
            </span>
          </div>
        </div>

        <!-- Edit Button -->
        <button 
          v-if="canEdit"
          @click="$emit('edit-profile')"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
        >
          Edit Profile
        </button>
      </div>

      <!-- User Stats -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div class="bg-gradient-to-r from-blue-50 to-blue-100 p-4 rounded-lg">
          <div class="text-2xl font-bold text-blue-600">{{ user.bestWpm || 0 }}</div>
          <div class="text-sm text-blue-700">Best WPM</div>
        </div>
        
        <div class="bg-gradient-to-r from-green-50 to-green-100 p-4 rounded-lg">
          <div class="text-2xl font-bold text-green-600">{{ user.totalGames || 0 }}</div>
          <div class="text-sm text-green-700">Total Games</div>
        </div>
        
        <div class="bg-gradient-to-r from-purple-50 to-purple-100 p-4 rounded-lg">
          <div class="text-2xl font-bold text-purple-600">{{ user.totalWords || 0 }}</div>
          <div class="text-sm text-purple-700">Words Typed</div>
        </div>
      </div>

      <!-- Account Information -->
      <div class="border-t pt-6">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Account Information</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
          <div>
            <span class="font-medium text-gray-700">Username:</span>
            <span class="ml-2 text-gray-600">{{ user.username || 'Not set' }}</span>
          </div>
          
          <div>
            <span class="font-medium text-gray-700">Member since:</span>
            <span class="ml-2 text-gray-600">
              {{ user.createdAt ? formatDate(user.createdAt) : 'Unknown' }}
            </span>
          </div>
          
          <div>
            <span class="font-medium text-gray-700">Last login:</span>
            <span class="ml-2 text-gray-600">
              {{ user.lastLoginAt ? formatDate(user.lastLoginAt) : 'Unknown' }}
            </span>
          </div>
          
          <div>
            <span class="font-medium text-gray-700">Account type:</span>
            <span class="ml-2 text-gray-600">
              {{ user.provider === 'LOCAL' ? 'Email & Password' : `OAuth (${user.provider})` }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else class="text-center py-8">
      <div class="text-gray-500">No user data available</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { User } from '../types/user'

interface Props {
  user: User | null
  loading?: boolean
  error?: string | null
  canEdit?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  error: null,
  canEdit: true
})

const emit = defineEmits<{
  'edit-profile': []
}>()

const getInitials = (user: User): string => {
  if (user.firstName && user.lastName) {
    return `${user.firstName.charAt(0)}${user.lastName.charAt(0)}`.toUpperCase()
  }
  if (user.username) {
    return user.username.charAt(0).toUpperCase()
  }
  return user.email.charAt(0).toUpperCase()
}

const getProviderIcon = (provider: string) => {
  // You can replace these with actual icon components
  switch (provider) {
    case 'GOOGLE':
      return 'div' // Replace with Google icon component
    case 'GITHUB':
      return 'div' // Replace with GitHub icon component
    default:
      return 'div'
  }
}

const formatDate = (date: Date | string): string => {
  const d = new Date(date)
  return d.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}
</script>

<style scoped>
.user-profile {
  max-width: 800px;
}
</style>
