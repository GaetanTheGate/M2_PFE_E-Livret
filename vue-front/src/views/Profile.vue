<template>
    <div class="container" v-if="currentUser">
        <div class="card rounded-3 m-5">

            <div class="card-header text-center fs-2 fw-bold">Page utilisateur</div>
            <div class="card-body">
                <div class="container mb-3">
                    <label for="oldpassword" class="fw-light fs-5">Email : {{ currentUser.email }}</label>
                </div>
                <div class="container mb-3">
                    <router-link :to="{ name: 'PasswordChanger' }" class="nav-link"><button class="btn btn-primary">Modifier mot de passe</button></router-link>
                </div>
                <div class="container">
                    <router-link v-if="currentUser.permission == 'RESPONSABLE'" :to="{ name: 'CreateUser' }" class="nav-link"><button class="btn btn-primary">Créer un utilisateur</button></router-link>
                </div>
            </div>
        </div>



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