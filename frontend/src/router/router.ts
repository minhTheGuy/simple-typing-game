import { createRouter, createWebHistory } from "vue-router";
import Home from "../view/Home.vue";
import LoginForm from "../components/LoginForm.vue";
import Game from "../view/Game.vue";
import Profile from "../view/Profile.vue";
import type { Router, RouteRecordRaw } from "vue-router";
import { useAuth } from "../composables/useAuth";

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

// Navigation guard with JWT validation
router.beforeEach(async (to, from, next) => {
  if (to.meta.requiresAuth) {
    const { isAuthenticated, checkAuth } = useAuth();
    
    // Try to validate existing authentication
    await checkAuth();
    
    if (!isAuthenticated.value) {
      next("/login");
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
