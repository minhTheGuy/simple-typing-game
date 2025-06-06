<!-- filepath: c:\Users\Minh\Desktop\simple-typing-game\frontend\src\components\Navbar.vue -->
<template>
    <div class="bg-cyan-800 text-white w-full p-4 flex justify-between items-center">
        <router-link to="/" class="flex flex-row text-2xl font-bold hover:text-cyan-200">
            <div class="flex items-center justify-center gap-2">
                <img src="/src/assets/typing_game.svg" alt="Logo" class="w-8 h-8 rounded">
                Typing Game
            </div>
        </router-link>

        <div class="flex items-center justify-center gap-6">
            <router-link to="/about" class="text-gray-300 hover:text-white transition-colors">
                About Me
            </router-link>

            <router-link to="/game" class="text-gray-300 hover:text-white transition-colors">
                Play Game
            </router-link>

            <!-- User Info Widget -->
            <UserInfoWidget />
        </div>
    </div>
</template>

<script setup lang="ts">
import UserInfoWidget from './UserInfoWidget.vue'
</script>