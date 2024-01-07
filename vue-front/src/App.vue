<template>
  <div id="nav">
  
  <router-link to="/">Home</router-link>
  <router-link :to="{ name: 'About' }">About</router-link>
  <router-link :to="{ name: 'Livret'}">Livret</router-link>
  </div>
  <router-view />
</template>

<script>
// import axios from 'axios';
// import ELivret from './components/ELivret.vue'
//import livretDetails from './components/livretDetails.vue'

  export default {
    name: 'app',
    components: {
    //livretDetails
},
    mounted(){
      this.login("etudiant@mail.com", "etudiant");
    },
    methods:{
      login: function(email, password) {
        this.$axiosLogin.post("login", {"email":email, "password":password}).then(t => {
          this.$setToken(t.data);
        })
      }
    }
  }
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
