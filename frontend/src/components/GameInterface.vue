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
            <div class="mb-4 p-4 border rounded bg-gray-50 text-lg leading-relaxed font-mono overflow-hidden">
                <div class="flex flex-wrap gap-x-2 gap-y-1">
                    <span v-for="(word, wordIndex) in words" :key="wordIndex" class="inline-block">
                        <span v-for="(char, charIndex) in word" :key="`${wordIndex}-${charIndex}`"
                            :class="getCharClass(wordIndex, charIndex)" class="relative">
                            {{ char }}
                        </span>
                        <!-- Show cursor -->
                        <span v-if="isCurrentPosition(wordIndex, word.length)"
                            class="inline-block w-0.5 h-6 bg-blue-500 animate-pulse ml-0.5"></span>
                    </span>
                </div>
            </div>

            <!-- Current word input -->
            <div class="mb-4">
                <input ref="inputRef" v-model="currentWordInput" @keydown="handleKeydown" @input="handleInput"
                    :disabled="gameOver" placeholder="Type here..."
                    class="w-full p-3 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 font-mono text-lg"
                    autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false" />
            </div>

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

        <!-- Result -->
        <div v-if="gameOver" class="mt-6 text-center bg-white p-6 rounded-lg shadow-lg">
            <h2 class="text-2xl font-semibold text-green-600 mb-4">Game Over!</h2>
            <div class="grid grid-cols-2 gap-4 text-left">
                <div>
                    <p class="text-gray-600">Words Per Minute</p>
                    <p class="text-2xl font-bold text-blue-600">{{ finalWpm }}</p>
                </div>
                <div>
                    <p class="text-gray-600">Accuracy</p>
                    <p class="text-2xl font-bold text-green-600">{{ finalAccuracy }}%</p>
                </div>
                <div>
                    <p class="text-gray-600">Total Errors</p>
                    <p class="text-2xl font-bold text-red-600">{{ errorCount }}</p>
                </div>
                <div>
                    <p class="text-gray-600">Correct Words</p>
                    <p class="text-2xl font-bold text-purple-600">{{ correctWords }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue';
import sampleTexts from '../assets/data';

// Reactive state
const textToType = ref<string>('')
const currentWordInput = ref<string>('')
const currentWordIndex = ref<number>(0)
const typedWords = ref<string[]>([])
const timeLeft = ref<number>(60)
const gameActive = ref<boolean>(false)
const gameOver = ref<boolean>(false)
const finalWpm = ref<number>(0)
const finalAccuracy = ref<number>(100)
const errorCount = ref<number>(0)
const correctWords = ref<number>(0)
const inputRef = ref<HTMLInputElement>()
let timer: ReturnType<typeof setInterval> | null = null

// Computed properties
const words = computed(() => {
    return textToType.value.split(' ').filter(word => word.length > 0)
})

const currentWord = computed(() => {
    return words.value[currentWordIndex.value] || ''
})

const wpm = computed(() => {
    if (!gameActive.value) return 0
    const timeElapsed = (60 - timeLeft.value) / 60
    if (timeElapsed <= 0) return 0
    return Math.round(correctWords.value / timeElapsed)
})

const accuracy = computed(() => {
    const totalAttempts = correctWords.value + errorCount.value
    if (totalAttempts === 0) return 100
    return Math.round((correctWords.value / totalAttempts) * 100)
})

// Methods
const getCharClass = (wordIndex: number, charIndex: number): string => {
    const isCurrentWord = wordIndex === currentWordIndex.value
    const typedWord = typedWords.value[wordIndex] || ''

    if (wordIndex < currentWordIndex.value) {
        // Already typed words
        const originalChar = words.value[wordIndex][charIndex]
        const typedChar = typedWord[charIndex]

        if (typedChar === originalChar) {
            return 'bg-green-200 text-green-800'
        } else if (typedChar !== undefined) {
            return 'bg-red-200 text-red-800'
        }
        return 'text-gray-400'
    } else if (isCurrentWord) {
        // Current word being typed
        const originalChar = currentWord.value[charIndex]
        const typedChar = currentWordInput.value[charIndex]

        if (typedChar === undefined) {
            return 'text-gray-900' // Not yet typed
        } else if (typedChar === originalChar) {
            return 'bg-green-200 text-green-800' // Correct
        } else {
            return 'bg-red-200 text-red-800' // Incorrect
        }
    } else {
        // Future words
        return 'text-gray-400'
    }
}

const isCurrentPosition = (wordIndex: number, position: number): boolean => {
    return wordIndex === currentWordIndex.value && position === currentWordInput.value.length
}

const handleKeydown = (event: KeyboardEvent): void => {
    // Handle space or enter to move to next word
    if (event.code === 'Space' || event.code === 'Enter') {
        event.preventDefault()
        moveToNextWord()
    }

    // Handle backspace
    if (event.code === 'Backspace' && currentWordInput.value.length === 0 && currentWordIndex.value > 0) {
        event.preventDefault()
        moveToPreviousWord()
    }

    if (!gameActive.value) return

}

const handleInput = (): void => {
    if (!gameActive.value) return
}

const moveToNextWord = (): void => {
    // Record the typed word
    typedWords.value[currentWordIndex.value] = currentWordInput.value

    // Check if word is correct
    if (currentWordInput.value === currentWord.value) {
        correctWords.value++
    } else {
        errorCount.value++
    }

    // Move to next word
    currentWordIndex.value++
    currentWordInput.value = ''

    // Check if game is complete
    if (currentWordIndex.value >= words.value.length) {
        endGame()
        return
    }

    // Focus input
    nextTick(() => {
        inputRef.value?.focus()
    })
}

const moveToPreviousWord = (): void => {
    if (currentWordIndex.value > 0) {
        // Move back to previous word
        currentWordIndex.value--
        currentWordInput.value = typedWords.value[currentWordIndex.value] || ''

        // Adjust stats
        const wasCorrect = typedWords.value[currentWordIndex.value] === words.value[currentWordIndex.value]
        if (wasCorrect) {
            correctWords.value--
        } else {
            errorCount.value--
        }
    }
}

const startGame = (): void => {
    
    if (timer) clearInterval(timer) // Clear the previous timer

    gameActive.value = true
    gameOver.value = false
    currentWordInput.value = ''
    currentWordIndex.value = 0
    typedWords.value = []
    timeLeft.value = 60
    errorCount.value = 0
    correctWords.value = 0
    textToType.value = sampleTexts[Math.floor(Math.random() * sampleTexts.length)]

    // Focus input
    nextTick(() => {
        inputRef.value?.focus()
    })

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
    // Record final word if partially typed
    if (currentWordInput.value.length > 0) {
        typedWords.value[currentWordIndex.value] = currentWordInput.value
        if (currentWordInput.value === currentWord.value) {
            correctWords.value++
        } else {
            errorCount.value++
        }
    }

    // Calculate final stats
    const timeElapsed = (60 - timeLeft.value) / 60
    finalWpm.value = timeElapsed > 0 ? Math.round(correctWords.value / timeElapsed) : 0

    const totalAttempts = correctWords.value + errorCount.value
    finalAccuracy.value = totalAttempts > 0 ? Math.round((correctWords.value / totalAttempts) * 100) : 100

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
    currentWordInput.value = ''
    currentWordIndex.value = 0
    typedWords.value = []
    timeLeft.value = 60
    errorCount.value = 0
    correctWords.value = 0
    textToType.value = sampleTexts[0]
}

// Lifecycle
onMounted(() => {
    textToType.value = sampleTexts[0]
})
</script>