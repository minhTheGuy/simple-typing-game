import { ref } from 'vue'
import { User } from '../types/user'

// Global authentication state
const currentUser = ref<User | null>(null)
const isAuthenticated = ref(false)
const loading = ref(false)

export function useAuth() {
    // Check if user is authenticated via JWT token
  const checkAuth = async () => {
    console.log('🔍 Checking authentication status...')
    loading.value = true
    
    const token = localStorage.getItem('userToken')
    if (!token) {
      console.log('❌ No token found')
      currentUser.value = null
      isAuthenticated.value = false
      loading.value = false
      return false
    }

    try {
      const response = await fetch(`http://localhost:8080/auth/validate?token=${token}`)
      const data = await response.json()
      
      if (data.valid && data.userId) {
        console.log('✅ Token valid, user authenticated:', data.email)
        currentUser.value = {
          id: data.userId,
          email: data.email,
          firstName: data.firstName,
          lastName: data.lastName,
          provider: data.provider,
          role: data.role
        } as User
        isAuthenticated.value = true
        loading.value = false
        return true
      } else {
        console.log('❌ Token invalid')
        localStorage.removeItem('userToken')
        currentUser.value = null
        isAuthenticated.value = false
        loading.value = false
        return false
      }
    } catch (error) {
      console.error('❌ Auth check failed:', error)
      localStorage.removeItem('userToken')
      currentUser.value = null
      isAuthenticated.value = false
      loading.value = false
      return false
    }
  }

  // Get current user
  const getCurrentUser = () => {
    return currentUser.value
  }

  // Logout user
  const logout = async () => {
    console.log('🚪 Logging out user...')
    localStorage.removeItem('userToken')
    currentUser.value = null
    isAuthenticated.value = false
    console.log('🧹 Authentication state cleared')
  }
  return {
    currentUser,
    isAuthenticated,
    loading,
    checkAuth,
    getCurrentUser,
    logout
  }
}
