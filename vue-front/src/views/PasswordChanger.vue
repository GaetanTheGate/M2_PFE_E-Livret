<template>
    <div v-if="currentUser">
       <h1>Modifier son mot de passe</h1> 
        <div>
            <label for="oldpassword">Ancien mot de passe :</label>
            <input type="password" name="oldpassword">
        </div>
        <hr/>
        <div>
            <label for="newpassword">Nouveau mot de passe :</label>
            <input type="password" name="newpassword">
            <br/>
            <label for="newpassword2">Entrez à nouveau le nouveau mot de passe :</label>
            <input type="password" name="newpassword2">
        </div>
        <div>
            <button v-on:click="changePassword()" class="shadow-lg rounded-pill btn btn-danger">Changer de mot de passe</button>
        </div>
    </div>
</template>

<script>

export default {
    name: 'PasswordChangerPage',
    components: {
    },

    props: {

    },
    data() {
        return {
            currentUser:    null,
            oldP:           null,
            newP:           null,
            newP2:          null,
        }
    },
    mounted() {
        this.fetchCurrentUser();
    },
    methods:{
        fetchCurrentUser: function () {
        this.currentUser = null;
        this.$axiosLogin.get("whoami").then(u => {
            this.currentUser = u.data;
        });
        },
        changePassword: function(){
            if(this.newP == this.newP2){
                let information = {
                    email: this.currentUser.email,
                    password: this.oldP,
                    newpassword: this.newP,
                }
                this.$axiosLogin.put("change-password", information).then(t => {
                    this.$loginService.setToken(t.data);

                    this.$router.push({ path : "/Profile"});
                });
            }
        },
    }
}
</script>

<style></style>