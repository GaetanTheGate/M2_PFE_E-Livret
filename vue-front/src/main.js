import { createApp } from 'vue'
import axios from 'axios'
import 'bootstrap/dist/css/bootstrap.min.css'

import App from './App.vue'


const app = createApp(App)

app.config.globalProperties.$axiosApi = axios.create({
    baseURL: 'http://localhost:8081/api/',
    timeout: 5000,
    headers: { 'Content-show': 'application/json' },
});

app.config.globalProperties.$setToken = token => { app.config.globalProperties.$axiosApi.defaults.headers.common['Authorization'] = 'Bearer ' + token; },
app.config.globalProperties.$unSetToken = () => { app.config.globalProperties.$axiosApi.defaults.headers.common['Authorization'] = null };

app.config.globalProperties.$axiosLogin = axios.create({
    baseURL: 'http://localhost:8081/authentification/',
    timeout: 5000
});



app.mount('#app')