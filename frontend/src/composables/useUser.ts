import { User, UserStats } from '../types/user'
import { useApi } from './useApi'

/**
 * @returns A composable function for managing user-related operations.
 * This includes fetching user details, stats, and updating user profiles.
 */
export function useUser() {
  const { execute, loading, error, api } = useApi<User>()

  // Get user by ID
  const getUserById = async (userId: number) => {
    try {
      return await execute(() => api.get(`/api/users/${userId}`))
    } catch (err) {
      throw err
    }
  }

  // Get user stats
  const getUserStats = async (userId: number) => {
    try {
      const { execute: executeStats } = useApi<UserStats>()
      return await executeStats(() => api.get(`/api/users/${userId}/stats`))
    } catch (err) {
      throw err
    }
  }

  // Update user profile
  const updateUserProfile = async (userId: number, userData: Partial<User>) => {
    try {
      return await execute(() => api.put(`/api/users/${userId}`, userData))
    } catch (err) {
      throw err
    }
  }

  return {
    loading,
    error,
    getUserById,
    getUserStats,
    updateUserProfile
  }
}
