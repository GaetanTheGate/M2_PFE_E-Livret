<template>
  <dif id="app">
    <p>t</p>
    <sectionDetails :sectionId="8"/>
  </dif>
</template>

<script>
// import axios from 'axios';
// import ELivret from './components/ELivret.vue'
import sectionDetails from './components/sectionDetails.vue'

  export default {
    name: 'app',
    components: {
      sectionDetails
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
