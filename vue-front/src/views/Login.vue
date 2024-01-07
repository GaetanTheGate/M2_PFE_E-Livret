<template>
    
    <div class="login">
        <signInForm />
    </div>
</template>

<script>
import signInForm from '../components/signInForm.vue'
    export default {
        name: 'LoginPage',
        components:{
            signInForm
         }
    }
</script>