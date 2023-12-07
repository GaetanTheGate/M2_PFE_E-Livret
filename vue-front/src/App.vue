<template>
  <dif id="app">
    <ELivret />
  </dif>
</template>

<script>

import ELivret from './components/ELivret.vue'

export default {
  name: 'App',
  components: {
    ELivret
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
