<template>
  <dif id="app">
    <livretDetails :livretId="7"/>
  </dif>
</template>

<script>
// import axios from 'axios';
// import ELivret from './components/ELivret.vue'
import livretDetails from './components/livretDetails.vue'

  export default {
    name: 'app',
    components: {
      livretDetails
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
  
<script>

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
