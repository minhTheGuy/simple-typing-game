import { ref, Ref } from 'vue'
import axios, { AxiosResponse } from 'axios'

interface ApiState<T> {
  data: Ref<T | null>
  loading: Ref<boolean>
  error: Ref<string | null>
}

/**
 * A composable function for making API calls with error handling and loading state management.
 * @returns An object containing the API state and methods for making requests.
 */
export function useApi<T>() {
  const data = ref<T | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  const api = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 10000,
    withCredentials: true, // Include cookies for session management
    headers: {
      'Content-Type': 'application/json'
    }
  })

  // Request interceptor
  api.interceptors.request.use(
    (config) => {
      loading.value = true
      error.value = null
      return config
    },
    (error) => {
      loading.value = false
      return Promise.reject(error)
    }
  )

  // Response interceptor
  api.interceptors.response.use(
    (response) => {
      loading.value = false
      return response
    },
    (error) => {
      loading.value = false
      
      if (error.response) {
        // Server responded with error status
        const status = error.response.status
        const message = error.response.data?.message || 'Server error'
        
        switch (status) {
          case 401:
            error.value = 'Unauthorized - Please login'
            break
          case 403:
            error.value = 'Access forbidden'
            break
          case 404:
            error.value = 'Resource not found'
            break
          case 500:
            error.value = 'Server error'
            break
          default:
            error.value = message
        }
      } else if (error.request) {
        error.value = 'Network error - please check your connection'
      } else {
        error.value = error.message
      }
      
      return Promise.reject(error)
    }
  )

  const execute = async (apiCall: () => Promise<AxiosResponse<T>>) => {
    try {
      const response = await apiCall()
      data.value = response.data
      return response.data
    } catch (err) {
      throw err
    }
  }

  const reset = () => {
    data.value = null
    loading.value = false
    error.value = null
  }

  return {
    data,
    loading,
    error,
    execute,
    reset,
    api
  }
}
