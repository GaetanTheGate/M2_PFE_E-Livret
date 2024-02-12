class LoginService {
    app;

    constructor(app) {
        this.app = app;
    }

    login(email, password, then = () => {}) {
        this.app.$axiosLogin.post("login", { "email": email, "password": password }).then(t => {
            this.setToken(t.data, then);
            this.app.$router.push({ path: "/" })
        })
    }

    logout(then = () => {}) {
        this.app.$axiosLogin.post("logout").then(() => {
            this.unsetToken(then);
            this.app.$router.push({ path: "/Login" })
        })
    }

    isLoggedIn() {
        return this.app.$axiosApi.defaults.headers.common['Authorization'] != null;
    }

    setToken(token, then = () => {}) {
        this.app.$axiosApi.defaults.headers.common['Authorization'] = 'Bearer ' + token;
        this.app.$axiosLogin.defaults.headers.common['Authorization'] = 'Bearer ' + token;
        localStorage.setItem('token', token);

        then();
    }

    unsetToken(then = () => {}) {
        this.app.$axiosApi.defaults.headers.common['Authorization'] = null;
        this.app.$axiosLogin.defaults.headers.common['Authorization'] = null;
        localStorage.removeItem('token');

        then();
    }
}

export default LoginService