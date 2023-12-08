<template>
  <dif id="app">
    <p>t</p>
    <ESection :sectionId="8"/>
  </dif>
</template>

<script>
// import axios from 'axios';
// import ELivret from './components/ELivret.vue'
  import ESection from './components/sectionDetails.vue'

  export default {
    name: 'app',
    components: {
      ESection
    },
    mounted(){

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
