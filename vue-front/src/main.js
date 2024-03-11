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


app.config.globalProperties.$download = (data, filename, type) => {
    var file = new Blob([data], { type: type });
    if (window.navigator.msSaveOrOpenBlob) // IE10+
        window.navigator.msSaveOrOpenBlob(file, filename);
    else { // Others
        var a = document.createElement("a"),
            url = URL.createObjectURL(file);
        a.href = url;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        setTimeout(function () {
            document.body.removeChild(a);
            window.URL.revokeObjectURL(url);
        }, 0);
    }
};

app.config.globalProperties.$selectFileThen = (then) => {
    var input = document.createElement('input');
    input.type = 'file';
    input.onchange = e => {
        var file = e.target.files[0];
        var reader = new FileReader();
        reader.readAsText(file, 'UTF-8');

        reader.onload = readerEvent => {
            var content = readerEvent.target.result;
            then(content);
        }
    };
    input.click();
}

app.mount('#app')