<template>
    <div v-if="currentUser">
        <h1>Page utilisateur</h1>

        <p><u>Email :</u> {{ currentUser.email }}</p>

        <router-link :to="{ name: 'PasswordChanger' }" class="nav-link"><button>Modifier mot de passe</button></router-link>
        <router-link v-if="currentUser.permission == 'RESPONSABLE'" :to="{ name: 'CreateUser' }" class="nav-link"><button>Créer un utilisateur</button></router-link>
    </div>
</template>

<script>

export default {


    name: 'ProfilePage',
    components: {
    },

    props: {

    },
    data() {
        return {
            currentUser:    null,
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
    }
}
</script>

<style></style>