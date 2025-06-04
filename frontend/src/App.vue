<template>
  <div class="min-h-screen bg-gray-100 flex flex-col items-center justify-center">
    <div class="bg-white p-8 rounded-lg shadow-lg max-w-2xl w-full">
      <h1 class="text-3xl font-bold text-center mb-6">Simple Typing Game</h1>

      <!-- Game Stats -->
      <div class="flex justify-between mb-4 text-sm text-gray-600">
        <span>WPM: {{ wpm }}</span>
        <span>Accuracy: {{ accuracy }}%</span>
        <span>Time: {{ timeLeft }}s</span>
      </div>

      <!-- Text to type -->
      <div class="mb-4 p-4 border rounded bg-gray-50 text-lg leading-relaxed">
        <span v-for="(char, index) in textToType" :key="index" :class="getCharClass(index)">
          {{ char }}
        </span>
      </div>

      <!-- Input area -->
      <textarea v-model="userInput" @input="handleInput" :disabled="gameOver" placeholder="Start typing here..."
        class="w-full p-3 border rounded resize-none focus:outline-none focus:ring-2 focus:ring-blue-500" rows="4" />

      <!-- Game controls -->
      <div class="mt-4 text-center">
        <button @click="startGame" :disabled="gameActive"
          class="px-6 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:opacity-50">
          {{ gameActive ? 'Game Active' : 'Start Game' }}
        </button>
        <button @click="resetGame" class="ml-2 px-6 py-2 bg-gray-500 text-white rounded hover:bg-gray-600">
          Reset
        </button>
      </div>
    </div>
    <div>
      <!-- result -->
      <div v-if="gameOver" class="mt-6 text-center">
        <h2 class="text-xl font-semibold">Game Over!</h2>
        <p class="mt-2">Your WPM: {{ finalWpm }}</p>
        <p>Your Accuracy: {{ finalAccuracy }}%</p>
      </div>
    </div>
    <div class="mt-4 text-center text-gray-500 text-xs">
      <p>Developed by Minh</p>
      <p>© 2025</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

// Reactive state
const textToType = ref<string>('')
const userInput = ref<string>('')
const startTime = ref<number>(0)
const timeLeft = ref<number>(60)
const gameActive = ref<boolean>(false)
const gameOver = ref<boolean>(false)
const finalWpm = ref<number>(0)
const finalAccuracy = ref<number>(100)
let timer: ReturnType<typeof setInterval> | null = null // Track timer

// Sample texts for the game
const sampleTexts: string[] = [
  "The quick brown fox jumps over the lazy dog.",
  "Programming is the art of telling another human being what one wants the computer to do.",
  "Vue.js is a progressive framework for building user interfaces."
]

// Computed properties
const wpm = computed(() => {
  if (!gameActive.value || userInput.value.length === 0) return 0
  const words = userInput.value.trim().split(' ').length
  const minutes = (60 - timeLeft.value) / 60
  return minutes > 0 ? Math.round(words / minutes) : 0
})

const accuracy = computed(() => {
  if (userInput.value.length === 0) return 100
  let correct = 0
  for (let i = 0; i < userInput.value.length; i++) {
    if (userInput.value[i] === textToType.value[i]) {
      correct++
    }
  }
  return Math.round((correct / userInput.value.length) * 100)
})

// Methods
const getCharClass = (index: number): string => {
  if (index >= userInput.value.length) return ''
  if (userInput.value[index] === textToType.value[index]) {
    return 'text-green-500 bg-green-100'
  }
  return 'text-red-500 bg-red-100'
}

const handleInput = (): void => {
  if (!gameActive.value) return
  
  if (userInput.value.length === textToType.value.length) {
    endGame()
  }
}

const startGame = (): void => {
  if (timer) clearInterval(timer)
  gameActive.value = true
  gameOver.value = false
  userInput.value = ''
  timeLeft.value = 60
  textToType.value = sampleTexts[Math.floor(Math.random() * sampleTexts.length)]
  startTime.value = Date.now()

  // Timer
  timer = setInterval(() => {
    timeLeft.value--
    if (timeLeft.value <= 0) {
      clearInterval(timer!)
      endGame()
    }
  }, 1000)
}

const endGame = (): void => {
  // Calculate final WPM and accuracy
  const words = userInput.value.trim().split(' ').length
  const minutes = (60 - timeLeft.value) / 60
  finalWpm.value = minutes > 0 ? Math.round(words / minutes) : 0

  let correct = 0
  for (let i = 0; i < userInput.value.length; i++) {
    if (userInput.value[i] === textToType.value[i]) {
      correct++
    }
  }
  finalAccuracy.value = userInput.value.length > 0
    ? Math.round((correct / userInput.value.length) * 100)
    : 100

  gameActive.value = false
  gameOver.value = true
  if (timer) {
    clearInterval(timer)
    timer = null
  }
}

const resetGame = (): void => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
  gameActive.value = false
  gameOver.value = false
  userInput.value = ''
  timeLeft.value = 60
  textToType.value = sampleTexts[0]
}

// Lifecycle
onMounted(() => {
  textToType.value = sampleTexts[0]
})
</script>

<style scoped>
/* Component-specific styles */
</style>
