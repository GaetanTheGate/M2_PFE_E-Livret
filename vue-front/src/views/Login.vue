<template>
    <div class="login">
        <signInForm />
    </div>
</template>

<script>
import signInForm from '../components/login/signInForm.vue'
export default {
    name: 'LoginPage',
    components: {
        signInForm
    },

    mounted() {
        this.$loginService.logout();
    },
}
</script>