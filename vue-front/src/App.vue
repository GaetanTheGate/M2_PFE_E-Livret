<template>
  <nav id="nav" class="navbar navbar-expand-lg bd-navbar sticky-top" style="background-color: #712cf9">
    <div class="mx-auto p-2">
      <ul class="navbar-nav me-auto">

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
        <li v-if="this.currentUser" class="nav-item">
          <a>
            <router-link :to="{ name: 'Profile' }" class="nav-link">Mon Profil</router-link>
          </a>
        </li>
        <li v-if="this.currentUser" class="nav-item">
          <a>
            <button type="button" v-on:click="this.$loginService.logout(fetchCurrentUser)" class="nav-link">Logout</button>
          </a>
        </li>
        <li class="nav-item">
          <a>
            <button type="button" v-on:click="this.$loginService.login(`etudiant2@mail.com`, `etudiant2`, fetchCurrentUser)"
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
            <button type="button" v-on:click="this.$loginService.login(`responsable@mail.com`, `responsable`, fetchCurrentUser)"
              class="nav-link">Responsable</button>
          </a>
        </li>
      </ul>
    </div>

  </nav>
  <router-view />
</template>

<script>
// import LoginService from './services/LoginService.js'

export default {
  name: 'app',
  components: {
  },
  data() {
    this.$loginService.app = this; //  Todo : Si possible, le déplacer dans le main.js
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
    }
  },
  watch: {
    currentUser() {
      
    }
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
}

.navbar {
  align-content: center;
}
</style>
