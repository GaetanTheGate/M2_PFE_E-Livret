<template>
    <div class="container">
        <div class="card rounded-3 m-5">
            <div class="card-header text-center fs-2 fw-bold">Créer un utilisateur</div>
            <div class="card-body">
                <div class="container">
                    <label for="email" class="fw-light fs-5">Email de l'utilisateur :</label>
                    <input v-model="email" type="email" name="email" placeholder="Email du nouvel utilisateur"
                        class="input-lg form-control rounded">
                    <br />
                    <div class="d-flex justify-content-center">
                        <div class="m-2">
                            <label for="firstName" class="fw-light fs-5">Prénom de l'utilisateur :</label>
                            <input v-model="firstName" type="firstName" name="firstName"
                                placeholder="Prénom du nouvel utilisateur" class="input-lg form-control rounded">
                        </div>
                        <div class="m-2">
                            <label for="lastName" class="fw-light fs-5">Nom de l'utilisateur :</label>
                            <input v-model="lastName" type="lastName" name="lastName"
                                placeholder="Nom du nouvel utilisateur" class="input-lg form-control rounded">
                        </div>
                    </div>
                </div>
<!--                <div class="m-3 d-flex flex-row-reverse">-->
<!--                    <button v-on:click="createUser()" style="filter: drop-shadow(0 0 0.25rem #efa31d);"-->
<!--                        class="rounded-pill btn btn-warning">Créer l'utilisateur</button>-->
<!--                </div>-->
                <div class="m-3 d-flex flex-row-reverse">
                    <button data-bs-toggle="modal" data-bs-target="#modalCreateUser" style="filter: drop-shadow(0 0 0.25rem #efa31d);"
                            class="rounded-pill btn btn-warning">Créer l'utilisateur</button>
                </div>
            </div>
            <div v-if="url" class="card-body">
                <hr />
                <p class="user-select-all">{{ url }}</p>
                <button v-on:click="copyToClipBoard()" class="rounded-pill btn btn-success">Copy to Clipboard</button>
            </div>

            <div class="modal" id="modalCreateUser" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Confirmer votre choix</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            Êtes-vous sûr de vouloir créer cet utilisateur ?
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                            <button type="button" class="btn btn-primary" data-bs-dismiss="modal" v-on:click="createUser">Confirmer</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: 'CreateUserPage',
    components: {

    },
    props: {

    },
    data() {
        return {
            email: null,
            firstName: null,
            lastName: null,
            url: null,
        }
    },
    mounted() {

    },
    methods: {
        createUser: function () {
            let newuser = {
                email: this.email,
                firstName: this.firstName,
                lastName: this.lastName,
            }

            this.$axiosApi.post("users/create-user", newuser).then(t => {
                this.url = `http://localhost:8080/Login?token=${t.data}`;
            })
                .catch(err => alert(err));
        },
        copyToClipBoard: async function () {
            try {
                const element = document.querySelector(".user-select-all");
                await navigator.clipboard.writeText(element.textContent);
            } catch (error) {
                console.error("Failed to copy to clipboard:", error);
            }
        }
    }
}
</script>

<style></style>