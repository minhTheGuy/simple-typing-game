<template>
    <div class="min-h-screen bg-gray-100 flex flex-col items-center justify-center">
        <h1 class="text-4xl font-bold mb-8">Welcome to Typing Game</h1>
        <div class="space-x-4">
            <router-link to="/game"
                class="bg-blue-500 hover:bg-blue-600 text-white px-6 py-3 rounded-lg text-lg font-semibold">
                Start Playing
            </router-link>
            <router-link to="/about"
                class="bg-gray-500 hover:bg-gray-600 text-white px-6 py-3 rounded-lg text-lg font-semibold">
                Learn More
            </router-link>
        </div>
    </div>
</template>