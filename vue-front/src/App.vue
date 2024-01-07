<template>
  <div id="nav">
    <router-link to="/">Home</router-link>
    <router-link :to="{ name: 'About' }">About</router-link>
    <router-link :to="{ name: 'Livret'}">Livret</router-link>
    <router-link :to="{ name: 'Login'}">Login</router-link>

  </div>
  <router-view />
</template>

<script>

  export default {
    name: 'app',
    components: {
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
  color: #2e4153;
  margin-top: 20px;
}

#nav{
  padding: 30px;
  text-align: center;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
  text-decoration: none;
  padding: 10px;
  border-radius: 4px;
}

#nav a.router-link-exact-active {
  color: white;
  background: crimson;
}
</style>
