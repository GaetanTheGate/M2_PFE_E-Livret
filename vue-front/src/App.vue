<template>
  <nav id="nav" class="navbar navbar-expand-lg bd-navbar sticky-top" style="background-color: #5a0cb9">
      <div class="collapse navbar-collapse">
          <ul class="navbar-nav me-auto">

            <li class="nav-item">
                <a >
                    <router-link to="/" class="nav-link">Home</router-link>
                </a>
            </li>
            <li class="nav-item">
                <a >
                    <router-link :to="{ name: 'About' }" class="nav-link">About</router-link>
                </a>
            </li>
            <li class="nav-item">
                <a >
                    <router-link :to="{ name: 'Livrets' }" class="nav-link">Livrets</router-link>
                </a>
            </li>
            <li class="nav-item">
                <a >
                    <button type="button" v-on:click="this.logout()" class="nav-link">Logout</button>
                </a>
            </li>
            <li class="nav-item">
                <a >
                    <button type="button" v-on:click="this.loginStudent()" class="nav-link">Student</button>
                </a>
            </li>
            <li class="nav-item">
                <a >
                    <button type="button" v-on:click="this.loginResponsable()" class="nav-link">Responsable</button>
                </a>
            </li>
              <li class="nav-item">
                  <a>
                      <button type="button" v-on:click="this.loginTutor()" class="nav-link">Tutor</button>
                  </a>
              </li>
          </ul>
      </div>

  </nav>
  <router-view/>
</template>

<script>

export default {
  name: 'app',
  components: {
  },
  mounted() {
    // TODO : un init de l'appli qui vérifie si y a un token, et autre si besoin 
    // this.login('etudiant@mail.com', 'etudiant')
  },
  methods: {
    login: function (email, password) {
      this.$axiosLogin.post("login", { "email": email, "password": password }).then(t => {
        this.$setToken(t.data);
        localStorage.access_token = t.data; // TODO : Mettre dans le $setToken
      })
    },
    logout: function () {
      this.$axiosLogin.post("logout").then(t => {
        this.$setToken(t.data);
        localStorage.removeItem('token');  // TODO : Mettre dans le $unsetToken
        this.$router.push({ path: "/Login"})
      })
    },
    loginStudent: function () {
      this.$axiosLogin.post("login", { "email": "etudiant2@mail.com", "password": "etudiant2" }).then(t => {
        this.$setToken(t.data);
                localStorage.setItem('token', t.data);
                this.$router.push({ path: "/"})
      })
    },
    loginResponsable: function () {
      this.$axiosLogin.post("login", { "email": "responsable@mail.com", "password": "responsable" }).then(t => {
        this.$setToken(t.data);
                localStorage.setItem('token', t.data);
                this.$router.push({ path: "/"})
      })
    },
    loginTutor: function () {
      this.$axiosLogin.post("login", { "email": "tuteur@mail.com", "password": "tuteur" }).then(t => {
        this.$setToken(t.data);
                localStorage.setItem('token', t.data);
                this.$router.push({ path: "/"})
      })
    },
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2e4153;
  /*margin-top: 20px;*/
}

/*#nav {*/
/*  padding: 30px;*/
/*  text-align: center;*/
/*}*/

/*#nav a {*/
/*  font-weight: bold;*/
/*  color: #2c3e50;*/
/*  text-decoration: none;*/
/*  padding: 10px;*/
/*  border-radius: 4px;*/
/*}*/

/*#nav a.router-link-exact-active {*/
/*  color: white;*/
/*  background: crimson;*/
/*}*/

.nav-link {
    color: white;
    padding-bottom: 8px;
    padding-top: 8px;


}
.nav-link:hover {
    border-bottom: 3px solid #9cfb04;
    color : white;
}
.navbar {
    padding-top: 0;
    padding-bottom: 0;
}


</style>
