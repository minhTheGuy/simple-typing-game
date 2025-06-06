import { createRouter, createWebHistory } from "vue-router";
import Home from "../view/Home.vue";
import LoginForm from "../components/LoginForm.vue";
import Game from "../view/Game.vue";
import Profile from "../view/Profile.vue";
import type { Router, RouteRecordRaw } from "vue-router";

const routes: RouteRecordRaw[] = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/login",
    name: "Login",
    component: LoginForm,
  },
  {
    path: "/game",
    name: "Game",
    component: Game,
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
    meta: { requiresAuth: true }
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Navigation guard example
router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    // Check if user is authenticated
    const isAuthenticated = localStorage.getItem("userToken");
    if (!isAuthenticated) {
      next("/login");
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
