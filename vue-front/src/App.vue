<template>
  <nav v-if="true" class="navbar nav-bar-expand sticky-top" style="background-color: #712cf9">
    <div class="container-fluid d-flex justify-content-between p-2">
      <div class=" d-flex justify-content-start">
        <router-link to="/" class="nav-item m-1">
          <button type="button" class="btn btn-outline-dark p-3">
            Home
          </button>
        </router-link>

        <router-link :to="{ name: 'About' }" class="nav-item m-1">
          <button type="button" class="nav-item btn btn-outline-dark p-3">
            About
          </button>
        </router-link>

        <router-link :to="{ name: 'ListLivrets' }" class="nav-item m-1">
          <button type="button" class="nav-item btn btn-outline-dark p-3">
            Mes livrets
          </button>
        </router-link>

      </div>

      <div class=" d-flex justify-content-start">

        <div class="nav-item m-1">
          <button type="button" class="nav-item btn btn-outline-warning p-3"
              v-on:click="this.$loginService.login(`etudiant2@mail.com`, `etudiant2`, fetchCurrentUser)">
            Etudiant
          </button>
        </div>

        <div class="nav-item m-1">
          <button type="button" class="nav-item btn btn-outline-warning p-3"
              v-on:click="this.$loginService.login(`maitre@mail.com`, `maitre`, fetchCurrentUser)">
            Maitre
          </button>
        </div>

        <div class="nav-item m-1">
          <button type="button" class="nav-item btn btn-outline-warning p-3"
              v-on:click="this.$loginService.login(`tuteur@mail.com`, `tuteur`, fetchCurrentUser)">
            Tuteur
          </button>
        </div>

        <div class="nav-item m-1">
          <button type="button" class="nav-item btn btn-outline-warning p-3"
              v-on:click="this.$loginService.login(`responsable@mail.com`, `responsable`, fetchCurrentUser)">
            Tuteur
          </button>
        </div>

      </div>

      <div class=" d-flex justify-content-start">
        <router-link v-if="!this.currentUser" :to="{ name: 'Login' }" class="nav-item m-1">
          <button type="button" class="btn btn-outline-dark p-3">Se connecter</button>
        </router-link>

        <router-link :to="{ name: 'Profile' }" class="nav-item m-1">
          <button v-if="this.currentUser" type="button" class="nav-item btn btn-outline-dark p-3">
            Mon profile
          </button>
        </router-link>

        <div class="nav-item m-1">
          <button v-if="this.currentUser" type="button" class="nav-item btn btn-outline-dark p-3"
            v-on:click="this.$loginService.logout(this.$pageService.gotoLoginPage.bind(this.$pageService))">
            Se déconnecter
          </button>
        </div>

      </div>
    </div>
  </nav>

  <nav v-if="false" id="nav" class="navbar navbar-expand-lg bd-navbar sticky-top" style="background-color: #712cf9">
    <div class="mx-auto p-2">
      <ul class="navbar-nav me-auto d-flex justify-content-between">
        <div>

          <li class="nav-item">
            <a>
              <router-link to="/" class="nav-link">Home</router-link>
            </a>
          </li>

          <li class="nav-item">
            <a>
              <router-link :to="{ name: 'About' }" class="nav-link">About</router-link>
            </a>
          </li>
          <li class="nav-item">
            <a>
              <router-link :to="{ name: 'ListLivrets' }" class="nav-link">Livrets</router-link>
            </a>
          </li>
        </div>
        <div>
        </div>
        <div>
          <li v-if="this.currentUser" class="nav-item">
            <a>
              <router-link :to="{ name: 'Profile' }" class="nav-link">Mon Profil</router-link>
            </a>
          </li>
          <li v-if="this.currentUser" class="nav-item">
            <a>
              <button type="button"
                v-on:click="this.$loginService.logout(this.$pageService.gotoLoginPage.bind(this.$pageService))"
                class="nav-link">Logout</button>
            </a>
          </li>
        </div>

        <li class="nav-item">
          <a>
            <button type="button"
              v-on:click="this.$loginService.login(`etudiant2@mail.com`, `etudiant2`, fetchCurrentUser)"
              class="nav-link">Student</button>
          </a>
        </li>
        <li class="nav-item">
          <a>
            <button type="button" v-on:click="this.$loginService.login(`maitre@mail.com`, `maitre`, fetchCurrentUser)"
              class="nav-link">Master</button>
          </a>
        </li>
        <li class="nav-item">
          <a>
            <button type="button" v-on:click="this.$loginService.login(`tuteur@mail.com`, `tuteur`, fetchCurrentUser)"
              class="nav-link">Tutor</button>
          </a>
        </li>
        <li class="nav-item">
          <a>
            <button type="button"
              v-on:click="this.$loginService.login(`responsable@mail.com`, `responsable`, fetchCurrentUser)"
              class="nav-link">Responsable</button>
          </a>
        </li>
      </ul>
    </div>

  </nav>
  <router-view />
</template>

<script>

export default {
  name: 'app',
  components: {
  },
  data() {
    return {
      currentUser: null,
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    fetchCurrentUser: function () {
      this.currentUser = null;
      this.$axiosLogin.get("whoami").then(u => {
        this.currentUser = u.data;
      });
    },

    init: function () {
      this.setApiToken();
      this.checkTokenValidity();
    },

    setApiToken: function () {
      this.$loginService.setToken(localStorage.getItem('token'), this.fetchCurrentUser);
    },

    checkTokenValidity: function () {
      // TODO : vérifier que le token fonctionne toujours, si non, unsetToken
      // TODO : peut etre refresh le token si 
    },
  },
  watch: {

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
</style>
