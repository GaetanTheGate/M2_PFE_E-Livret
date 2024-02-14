<template>
    <div v-if="currentUser">
       <h1>Créer son mot de passe</h1> 
        <div>
            <label for="newpassword">Mot de passe :</label>
            <input v-model="newP" type="password" name="newpassword">
            <br/>
            <label for="newpassword2">Entrez à nouveau le nouveau mot de passe :</label>
            <input v-model="newP2" type="password" name="newpassword2">
        </div>
        <div>
            <button v-on:click="createPassword()" class="shadow-lg rounded-pill btn btn-danger">Créer le mot de passe</button>
        </div>
    </div>
</template>

<script>

export default {
    name: 'PasswordInitPage',
    components: {
    },

    props: {

    },
    data() {
        return {
            currentUser:    null,
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
        createPassword: function(){
            if(this.newP == this.newP2){
                let information = {
                    password: this.newP,
                }
                this.$axiosLogin.post("init-password", information).then(t => {
                    this.$loginService.setToken(t.data);

                    this.$pageService.gotoProfilePage();
                });
            }
        },
    }
}
</script>

<style></style>