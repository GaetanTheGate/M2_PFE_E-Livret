<template>
  <nav class="navbar nav-bar-expand sticky-top" style="background-color: #712cf9">
    <div class="container-fluid d-flex justify-content-between p-2">
      <div class="d-flex justify-content-start">
        <router-link :to="{ name: 'ListLivrets' }" class="nav-item m-1">
          <button type="button" class="nav-item btn btn-light p-3">
            Mes livrets
          </button>
        </router-link>

      </div>

      <div class="d-flex justify-content-start align-items-center">
        <router-link v-if="!this.currentUser" :to="{ name: 'Login' }" class="nav-item m-1">
          <button type="button" class="btn btn-light p-3">Se connecter</button>
        </router-link>

        <!--        <router-link :to="{ name: 'Profile' }" class="nav-item m-1">-->
        <!--          <button v-if="this.currentUser" type="button" class="nav-item btn btn-light p-3">-->
        <!--            Mon profile-->
        <!--          </button>-->
        <!--        </router-link>-->

        <router-link :to="{ name: 'Profile' }" class="nav-item m-1">
          <userCircle v-if="this.currentUser" :userId="this.currentUser.id" />
        </router-link>

        <div class="nav-item m-1">
          <button v-if="this.currentUser" type="button" class="nav-item btn btn-light p-3"
            v-on:click="this.$loginService.logout((() => { $pageService.gotoLoginPage(); $pageService.refresh() }).bind(this.$pageService));">
            Se déconnecter
          </button>
        </div>

      </div>
    </div>
  </nav>

  <router-view />

</template>

<script>

import userCircle from './components/utility/userCircle.vue';

export default {
  name: 'app',
  components: {
    userCircle
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
      if (localStorage.getItem('token') != null) this.$loginService.setToken(localStorage.getItem('token'), this.fetchCurrentUser);
    },

    checkTokenValidity: function () {
      this.$axiosLogin.get("whoami").then(u => {
        if (u.data == "") {
          // this.$loginService.logout();
        }
      });
      // TODO : peut etre refresh le token si valide
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
