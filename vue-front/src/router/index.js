import { createRouter, createWebHistory } from 'vue-router'
import axios from 'axios'

import About from '../views/About.vue'
import DetailsLivret from '../views/DetailsLivret.vue'
import ModifyLivret from '../views/ModifyLivret.vue'
import ListLivrets from '../views/ListLivrets.vue'
import Login from '../views/Login.vue'
import Profile from '../views/Profile.vue'
import PasswordChanger from '../views/PasswordChanger.vue'
import PasswordInit from '../views/PasswordInit.vue'
import CreateUser from '../views/CreateUser.vue'


const routes = [
    {
        path: '/',
        name: 'Home',
        component: ListLivrets
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
    {
        path: '/Profile/init-password',
        name: 'PasswordInit',
        component: PasswordInit
    },
    {
        path: '/Users/create-user',
        name: 'CreateUser',
        component: CreateUser
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
    "CreateUser",
    "PasswordInit"
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

const isLoggedIn = () => {
    return !!localStorage.getItem('token')
}

const axiosLogin = axios.create({
    baseURL: 'http://localhost:8081/authentification/',
    timeout: 30000,
});

const isPasswordSet = async () => {
    if(!isLoggedIn())
        throw new Error("Cannot check password's state when not authentified")
    
    axiosLogin.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('token');

    return axiosLogin.get("whoami").then(u => {
        return u.data.isPasswordSet;
    });
}

router.beforeEach(async (to, from, next) => {
    const isProtected = protectedRoutes.includes(to.name)

    if (isProtected && !isLoggedIn()) {
        next({
            name: 'Login',
        })
    }

    else if ((to.name != 'PasswordInit') && isLoggedIn() && !await isPasswordSet()) {
        next({
            name: 'PasswordInit',
        })
    }

    else if ((to.name == 'Login') && isLoggedIn()) {
        next({
            name: 'Profile'
        })
    }
    else next()
})

export default router