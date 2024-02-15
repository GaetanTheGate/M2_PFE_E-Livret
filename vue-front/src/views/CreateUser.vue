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
                </div>
                <div class="m-3 d-flex flex-row-reverse">
                    <button v-on:click="createUser()" style="filter: drop-shadow(0 0 0.25rem #efa31d);"
                        class="rounded-pill btn btn-warning">Créer l'utilisateur</button>
                </div>
            </div>
            <div v-if="url" class="card-body">
                <hr />
                <p class="user-select-all">{{ url }}</p>
                <button v-on:click="copyToClipBoard()" class="rounded-pill btn btn-success">Copy to Clipboard</button>
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
            url: null,
        }
    },
    mounted() {

    },
    methods: {
        createUser: function () {
            let newuser = {
                email: this.email,
            }

            this.$axiosApi.post("users/create-user", newuser).then(t => {
                this.url = `http://localhost:8080/Login?token=${t.data}`;
            });
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