<template>
    <div class="container">

        <div class="card rounded-3 m-5">
            <div class="card-header text-center fs-2 fw-bold">Connexion</div>
            <div class="card-body">
                <div class="container mb-3">
                    <label id="label" class="form-label">Email:</label>
                    <input id="input" type="email" required v-model="email" class="form-control">
                </div>
                <div class="container mb-3">
                    <label id="label" class="form-label">Password:</label>
                    <input id="input" type="password" required v-model="password" class="form-control">
                </div>
                <button type="button"
                    v-on:click="this.$loginService.login(email, password, (() => { $pageService.gotoProfilePage(); $pageService.refresh() }).bind($pageService))"
                    class="btn btn-primary">Login</button>


            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            email: '',
            password: ''
        }
    },
    mounted() {
        this.email = null,
            this.password = null
    },
    methods: {

    },
}
</script>