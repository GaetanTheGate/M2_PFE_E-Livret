<template>
    <div class="container" v-if="currentUser">
        <div class="card rounded-3 m-5">

            <div class="card-header text-center fs-2 fw-bold">Modifier son mot de passe</div>
            <div class="card-body">
                <div class="container">
                    <label for="oldpassword" class="fw-light fs-5">Ancien mot de passe :</label>
                    <input v-model="oldP" type="password" name="oldpassword" placeholder="Ancien mot de passe"
                        class="input-lg form-control rounded">
                </div>
                <hr class="border border-dark border-3 rounded-5" />
                <div class="container">
                    <label for="newpassword" class="fw-light fs-5">Nouveau mot de passe :</label>
                    <input v-model="newP" type="password" name="newpassword" placeholder="Nouveau mot de passe"
                        class="input-lg form-control rounded">
                    <br />
                    <label for="newpassword2" class="fw-light fs-5">Entrez à nouveau le nouveau mot de passe :</label>
                    <input v-model="newP2" type="password" name="newpassword2"
                        placeholder="Une nouvelle fois le nouveau mot de passe" class="input-lg form-control rounded">
                </div>
                <div class="m-3 d-flex flex-row-reverse">
                    <button v-on:click="changePassword()" style="filter: drop-shadow(0 0 0.25rem #972222);"
                        class="rounded-pill btn btn-danger">Changer de mot de
                        passe</button>
                </div>
            </div>
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
            currentUser: null,
            oldP: null,
            newP: null,
            newP2: null,
        }
    },
    mounted() {
        this.fetchCurrentUser();
    },
    methods: {
        fetchCurrentUser: function () {
            this.currentUser = null;
            this.$axiosLogin.get("whoami").then(u => {
                this.currentUser = u.data;
            });
        },
        changePassword: function () {
            if (this.newP == this.newP2) {
                let information = {
                    email: this.currentUser.email,
                    password: this.oldP,
                    newpassword: this.newP,
                }

                this.$axiosLogin.put("change-password", information).then(t => {
                    this.$loginService.setToken(t.data);
                    this.$pageService.gotoProfilePage();
                });
            }
        },
    }
}
</script>

<style></style>