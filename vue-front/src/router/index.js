import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import Livret from '../views/Livret.vue'
import Login from '../views/Login.vue'
import Livrets from '../views/Livrets.vue'
import LivretModify from '../views/ModifyLivret.vue'


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
        path: '/Livret/:id',
        name: 'Livret',
        component: Livret,
        props: true
    },
    {
        path: '/Login',
        name: 'Login',
        component: Login
    },
    {
        path: '/Livrets',
        name: 'Livrets',
        component: Livrets
    },
    {
        path: '/Livret/:id/modify',
        name: 'LivretModify',
        component: LivretModify,
        props: true
    },
]

const protectedRoutes = [
    "Home",
    "About",
    "Livret",
    "Livrets"
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

const isLoggedIn = () => {
    return localStorage.getItem('token')
}

router.beforeEach(async (to, from, next) => {
    const isProtected = protectedRoutes.includes(to.name)
    if (isProtected && !isLoggedIn()) {
        next({
            path: '/login'
        })
    } else next()
})

export default router