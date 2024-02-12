import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import DetailsLivret from '../views/DetailsLivret.vue'
import ModifyLivret from '../views/ModifyLivret.vue'
import ListLivrets from '../views/ListLivrets.vue'
import Login from '../views/Login.vue'
import Profile from '../views/Profile.vue'
import PasswordChanger from '../views/PasswordChanger.vue'


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
        path: '/Livret/:id/details',
        name: 'DetailsLivret',
        component: DetailsLivret,
        props: true
    },
    {
        path: '/Livret/:id/modify',
        name: 'ModifyLivret',
        component: ModifyLivret,
        props: true
    },
    {
        path: '/ListLivrets',
        name: 'ListLivrets',
        component: ListLivrets
    },
    {
        path: '/Login',
        name: 'Login',
        component: Login
    },
    {
        path: '/Profile',
        name: 'Profile',
        component: Profile
    },
    {
        path: '/Profile/change-password',
        name: 'PasswordChanger',
        component: PasswordChanger
    },
]

const protectedRoutes = [
    "Home",
    "About",
    "DetailsLivret",
    "ModifyLivret",
    "ListLivrets",
    "Profile",
    "PasswordChanger",
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