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
        this.init();
    },
    methods:{
        init: function(){
            this.$loginService.logout(this.checkToken.bind(this));
        },

        checkToken: function(){
            let token = this.$route.query.token;
            if(token){
                this.$loginService.setToken(token);

                this.$axiosLogin.get("whoami").then(() => {
                    this.$pageService.gotoProfilePage();
                });
            }
        }
    }
}
</script>