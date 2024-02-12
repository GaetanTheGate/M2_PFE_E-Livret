import { createApp } from 'vue'
import axios from 'axios'
import LoginService from './services/LoginService.js'
// import 'bootstrap/dist/css/bootstrap.min.css'
import '../css/bootstrapStyle.css'
// import '../css/genericStyle.css'
import router from './router'
import App from './App.vue'


const app = createApp(App).use(router)

app.config.globalProperties.$axiosApi = axios.create({
    baseURL: 'http://localhost:8081/api/',
    timeout: 30000,
    headers: { 'Content-show': 'application/json' },
});

app.config.globalProperties.$axiosLogin = axios.create({
    baseURL: 'http://localhost:8081/authentification/',
    timeout: 30000
});

app.config.globalProperties.$loginService = new LoginService();

app.mount('#app')