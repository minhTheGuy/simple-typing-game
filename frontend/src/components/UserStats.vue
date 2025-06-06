<template>
  <div class="user-stats-dashboard">
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
      <!-- Best WPM -->
      <div class="stat-card bg-gradient-to-br from-blue-500 to-blue-600 text-white">
        <div class="flex items-center justify-between">
          <div>
            <div class="text-2xl font-bold">{{ user?.bestWpm || 0 }}</div>
            <div class="text-blue-100">Best WPM</div>
          </div>
          <div class="text-blue-200">
            <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
              <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
          </div>
        </div>
      </div>

      <!-- Total Games -->
      <div class="stat-card bg-gradient-to-br from-green-500 to-green-600 text-white">
        <div class="flex items-center justify-between">
          <div>
            <div class="text-2xl font-bold">{{ user?.totalGames || 0 }}</div>
            <div class="text-green-100">Total Games</div>
          </div>
          <div class="text-green-200">
            <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
              <path d="M9 2a1 1 0 000 2h2a1 1 0 100-2H9z"/>
              <path fill-rule="evenodd" d="M4 5a2 2 0 012-2v1a1 1 0 001 1h6a1 1 0 001-1V3a2 2 0 012 2v6a2 2 0 01-2 2H6a2 2 0 01-2-2V5zm8 3a1 1 0 10-2 0v.01a1 1 0 102 0V8zm-3 3a1 1 0 10-2 0v.01a1 1 0 102 0V11zm-1 2a1 1 0 011-1v.01a1 1 0 11-2 0V12a1 1 0 011-1z" clip-rule="evenodd"/>
            </svg>
          </div>
        </div>
      </div>

      <!-- Total Words -->
      <div class="stat-card bg-gradient-to-br from-purple-500 to-purple-600 text-white">
        <div class="flex items-center justify-between">
          <div>
            <div class="text-2xl font-bold">{{ formatNumber(user?.totalWords || 0) }}</div>
            <div class="text-purple-100">Words Typed</div>
          </div>
          <div class="text-purple-200">
            <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M4 4a2 2 0 012-2h8a2 2 0 012 2v12a2 2 0 01-2 2H6a2 2 0 01-2-2V4zm2 0v12h8V4H6z" clip-rule="evenodd"/>
            </svg>
          </div>
        </div>
      </div>

      <!-- Average WPM -->
      <div class="stat-card bg-gradient-to-br from-orange-500 to-orange-600 text-white">
        <div class="flex items-center justify-between">
          <div>
            <div class="text-2xl font-bold">{{ calculateAverageWpm() }}</div>
            <div class="text-orange-100">Avg WPM</div>
          </div>
          <div class="text-orange-200">
            <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M3 3a1 1 0 000 2v8a2 2 0 002 2h2.586l-1.293 1.293a1 1 0 101.414 1.414L10 15.414l2.293 2.293a1 1 0 001.414-1.414L12.414 15H15a2 2 0 002-2V5a1 1 0 100-2H3zm11.707 4.707a1 1 0 00-1.414-1.414L10 9.586 8.707 8.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- Recent Activity Section -->
    <div v-if="showRecentActivity" class="bg-white rounded-lg shadow-md p-6">
      <h3 class="text-lg font-semibold text-gray-900 mb-4">Recent Activity</h3>
      
      <div v-if="loadingStats" class="flex items-center justify-center py-8">
        <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"></div>
        <span class="ml-2 text-gray-600">Loading stats...</span>
      </div>
      
      <div v-else-if="statsError" class="text-red-600">
        {{ statsError }}
      </div>
      
      <div v-else class="space-y-4">
        <!-- Progress towards next milestone -->
        <div class="bg-gray-50 rounded-lg p-4">
          <div class="flex justify-between items-center mb-2">
            <span class="text-sm font-medium text-gray-700">Progress to 1000 words</span>
            <span class="text-sm text-gray-500">{{ Math.round(progressPercentage) }}%</span>
          </div>
          <div class="w-full bg-gray-200 rounded-full h-2">
            <div 
              class="bg-blue-600 h-2 rounded-full transition-all duration-300"
              :style="{ width: `${Math.min(progressPercentage, 100)}%` }"
            ></div>
          </div>
          <div class="text-xs text-gray-500 mt-1">
            {{ user?.totalWords || 0 }} / 1000 words
          </div>
        </div>

        <!-- Quick Stats -->
        <div class="grid grid-cols-2 gap-4">
          <div class="text-center">
            <div class="text-lg font-semibold text-gray-900">{{ user?.totalGames || 0 }}</div>
            <div class="text-sm text-gray-500">Games Played</div>
          </div>
          <div class="text-center">
            <div class="text-lg font-semibold text-gray-900">{{ calculateAccuracy() }}%</div>
            <div class="text-sm text-gray-500">Avg Accuracy</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import { useUser } from '../composables/useUser';
import { User, UserStats } from '../types/user';

interface Props {
  user: User | null
  showRecentActivity?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  showRecentActivity: true
})

const { getUserStats } = useUser()
const loadingStats = ref(false)
const statsError = ref<string | null>(null)
const userStats = ref<UserStats | null>(null)

const formatNumber = (num: number): string => {
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + 'M'
  }
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num.toString()
}

const calculateAverageWpm = (): number => {
  if (!props.user?.totalGames || props.user.totalGames === 0) {
    return 0
  }
  
  // This is a rough calculation - you might want to store this in the backend
  const estimatedAverage = Math.round((props.user.bestWpm || 0) * 0.7)
  return estimatedAverage
}

const calculateAccuracy = (): number => {
  // This should come from actual game data - placeholder calculation
  if (!props.user?.totalWords || props.user.totalWords === 0) {
    return 0
  }
  
  // Estimate based on user performance (this should be real data from backend)
  return Math.round(Math.min(95, 80 + (props.user.bestWpm || 0) / 10))
}

const progressPercentage = computed(() => {
  const totalWords = props.user?.totalWords || 0
  return (totalWords / 1000) * 100
})

const loadUserStats = async () => {
  if (!props.user?.id) return
  
  loadingStats.value = true
  statsError.value = null
  
  try {
    userStats.value = await getUserStats(props.user.id)
  } catch (error) {
    statsError.value = 'Failed to load user statistics'
    console.error('Error loading user stats:', error)
  } finally {
    loadingStats.value = false
  }
}

onMounted(() => {
  if (props.showRecentActivity) {
    loadUserStats()
  }
})
</script>

<style scoped>
</style>
