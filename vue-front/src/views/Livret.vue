<template>
    
    <div id="livret">
        <livretDetails :livretId="80" />
    </div>
</template>

<script>
import livretDetails from '../components/livretDetails.vue'

    export default {
        name: 'LivretPage',
        components:{ 
            livretDetails
        }
    }
</script>

<style>
</style>