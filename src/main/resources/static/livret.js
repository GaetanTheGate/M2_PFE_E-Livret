const myApp = {

    // Préparation des données
    data() {
        console.log("Data");
        return {
            axios:      null,

            axiosLogin: null,
            token:   null,
        }
    },

    // Mise en place de l'application
    mounted() {
        console.log("Mounted");

        this.axios = axios.create({
            baseURL: 'http://localhost:8081/api/',
            timeout: 5000,
            headers: { 'Content-show': 'application/json' },
        });

        this.axiosLogin = axios.create({
            baseURL: 'http://localhost:8081/authentification/',
            timeout: 5000,
        });

    },

    methods: {
        
    }
}

const app = Vue.createApp(myApp);
app.mount('#myApp');