// User types matching your backend DTOs
export interface User {
  id: number
  username?: string
  email: string
  firstName?: string
  lastName?: string
  imageUrl?: string
  provider?: AuthProvider
  role?: Role
  createdAt?: Date
  updatedAt?: Date
  lastLoginAt?: Date
  bestWpm?: number
  totalGames?: number
  totalWords?: number
}

export enum AuthProvider {
  LOCAL = 'LOCAL',
  GOOGLE = 'GOOGLE',
  GITHUB = 'GITHUB'
}

export enum Role {
  ROLE_USER = 'ROLE_USER',
  ROLE_ADMIN = 'ROLE_ADMIN'
}

export interface GameResult {
  id?: number
  userId: number
  wpm: number
  accuracy: number
  wordsTyped: number
  timeSpent: number
  createdAt?: Date
}

export interface UserStats {
  totalGames: number
  bestWpm: number
  averageWpm: number
  totalWords: number
  totalTime: number
  averageAccuracy: number
}
