class LoginService {
    app;

    constructor(app) {
        this.app = app;
    }

    login(email, password) {
        this.app.$axiosLogin.post("login", { "email": email, "password": password }).then(t => {
            this.setToken(t.data);
            this.app.$router.push({ path: "/" })
        })
    }

    logout() {
        this.app.$axiosLogin.post("logout").then(() => {
            this.unsetToken();
            this.app.$router.push({ path: "/Login" })
        })
    }

    isLoggedIn() {
        return this.app.$axiosApi.defaults.headers.common['Authorization'] != null;
    }

    setToken(token) {
        this.app.$axiosApi.defaults.headers.common['Authorization'] = 'Bearer ' + token;
        this.app.$axiosLogin.defaults.headers.common['Authorization'] = 'Bearer ' + token;
        localStorage.setItem('token', token);
    }

    unsetToken() {
        this.app.$axiosApi.defaults.headers.common['Authorization'] = null;
        this.app.$axiosLogin.defaults.headers.common['Authorization'] = null;
        localStorage.removeItem('token');
    }
}

export default LoginService