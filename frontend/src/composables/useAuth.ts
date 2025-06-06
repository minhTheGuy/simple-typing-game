import { ref } from 'vue'
import { useApi } from './useApi'
import { User } from '../types/user'

// Global authentication state - shared across all components
const currentUser = ref<User | null>(null)
const isAuthenticated = ref(false)

/**
 * A composable function for managing user authentication.
 * @returns An object containing the current user, authentication status, and methods for checking auth, logging in, registering, and logging out.
 */
export function useAuth() {
  const { execute, loading, error, api } = useApi<User>()
  // Check if user is currently authenticated
  const checkAuth = async () => {
    console.log('🔍 Checking authentication status...')
    try {
      const user = await execute(() => api.get('/auth/user'))
      if (user) {
        console.log('✅ User authenticated:', user.email || user.username)
        currentUser.value = user
        isAuthenticated.value = true
      } else {
        console.log('❌ No user data received')
        currentUser.value = null
        isAuthenticated.value = false
      }
      return user
    } catch (err) {
      console.log('❌ Authentication check failed:', err)
      currentUser.value = null
      isAuthenticated.value = false
      return null
    }
  }

  // Get current user info
  const getCurrentUser = () => {
    return currentUser.value
  }
  // Logout user
  const logout = async () => {
    console.log('🚪 Logging out user...')
    try {
      await api.post('/auth/logout')
      console.log('✅ Logout API call successful')
    } catch (err) {
      console.error('❌ Logout API call failed:', err)
    } finally {
      // Always clear the frontend state regardless of API success/failure
      currentUser.value = null
      isAuthenticated.value = false
      console.log('🧹 Frontend authentication state cleared')
    }
  }

  // Login with username/password
  const login = async (username: string, password: string) => {
    try {
      const response = await api.post('/auth/login', { username, password })
      await checkAuth() // Refresh user info after login
      return response.data
    } catch (err) {
      throw err
    }
  }

  // Register new user
  const register = async (username: string, password: string, email: string) => {
    try {
      const response = await api.post('/auth/register', { username, password, email })
      await checkAuth() // Refresh user info after registration
      return response.data
    } catch (err) {
      throw err
    }
  }

  return {
    currentUser,
    isAuthenticated,
    loading,
    error,
    checkAuth,
    getCurrentUser,
    logout,
    login,
    register
  }
}
