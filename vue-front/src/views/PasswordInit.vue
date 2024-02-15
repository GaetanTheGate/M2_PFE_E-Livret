<template>
    <div v-if="currentUser" class="container">
        <div class="card rounded-3 m-5">

            <div class="card-header text-center fs-2 fw-bold">Créer son mot de passe</div>
            <div class="card-body">
                <div class="container">
                    <label for="newpassword" class="fw-light fs-5">Mot de passe :</label>
                    <input v-model="newP" type="password" name="newpassword" placeholder="Mot de passe"
                        class="input-lg form-control rounded">
                    <br />
                    <label for="newpassword2" class="fw-light fs-5">Entrez à nouveau le nouveau mot de
                        passe :</label>
                    <input v-model="newP2" type="password" name="newpassword2"
                        placeholder="Une nouvelle fois le mot de passe" class="input-lg form-control rounded">
                </div>
                <div class="m-3 d-flex flex-row-reverse">
                    <button v-on:click="createPassword()" style="filter: drop-shadow(0 0 0.25rem #593196);"
                        class="rounded-pill btn btn-primary">Créer le mot de
                        passe</button>
                </div>
            </div>
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
            currentUser: null,
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
        createPassword: function () {
            if (this.newP == this.newP2) {
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