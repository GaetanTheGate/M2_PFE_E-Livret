class LoginService {
    axiosApi;
    axiosLogin;

    constructor(axiosApi, axiosLogin) {
        this.axiosApi = axiosApi;
        this.axiosLogin = axiosLogin;
    }

    login(email, password, then = () => {}) {
        this.axiosLogin.post("login", { "email": email, "password": password }).then(t => {
            this.setToken(t.data, then);
        })
    }

    logout(then = () => {}) {
        this.axiosLogin.post("logout").then(() => {
            this.unsetToken(then);
        })
    }

    isLoggedIn() {
        return this.axiosApi.defaults.headers.common['Authorization'] != null;
    }

    setToken(token, then = () => {}) {
        this.axiosApi.defaults.headers.common['Authorization'] = 'Bearer ' + token;
        this.axiosLogin.defaults.headers.common['Authorization'] = 'Bearer ' + token;
        localStorage.setItem('token', token);

        then();
    }

    unsetToken(then = () => {}) {
        this.axiosApi.defaults.headers.common['Authorization'] = null;
        this.axiosLogin.defaults.headers.common['Authorization'] = null;
        localStorage.removeItem('token');

        then();
    }
}

export default LoginService