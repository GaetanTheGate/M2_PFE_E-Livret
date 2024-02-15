import { createApp } from 'vue'
import axios from 'axios'
import LoginService from './services/LoginService.js'
import PageService from './services/PageService.js'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

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

app.config.globalProperties.$loginService = new LoginService(app.config.globalProperties.$axiosApi, app.config.globalProperties.$axiosLogin);
app.config.globalProperties.$pageService = new PageService(app.config.globalProperties.$router);

app.mount('#app')