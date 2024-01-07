import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import Livret from '../views/Livret.vue'
import Login from '../views/Login.vue'


const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/about',
        name: 'About',
        component: About
    },
    {
        path: '/Livret',
        name: 'Livret',
        component: Livret
    },
    {
        path: '/Login',
        name: 'Login',
        component: Login
    }
]

const protectedRoutes = [
    "Home",
    "About",
    "Livret"
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

const isLoggedIn = () => {
    return localStorage.getItem('token')
}

router.beforeEach(async (to, from, next) => {
    //    const token = localStorage.getItem('access_token')
    //    if (!token) next ({ name: 'Login' });
    //   //else continue
    //   else next();

    const isProtected = protectedRoutes.includes(to.name)
    if (isProtected && !isLoggedIn()) {
        next({
            path: '/login'
        })
    } else next()
})

export default router