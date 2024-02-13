<template>
    <div>
        <form id="form">
            <label id="label" class="form-label">Email:</label>
            <input id="input" type="email" required v-model="email" class="form-control">

            <label id="label" class="form-label">Password:</label>
            <input id="input" type="password" required v-model="password" class="form-control">

            <button type="button" v-on:click="this.$loginService.login(email, password, this.$pageService.gotoProfilePage)"
                class="btn btn-primary">Login</button>
        </form>
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

<style>
#form {
    max-width: 420px;
    margin: 30px auto;
    background: white;
    text-align: left;
    padding: 40px;
    border-radius: 10px;
}

#label {
    color: #aaa;
    display: inline-block;
    margin: 25px 0 15px;
    font-size: 0.6em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;
}

#input {
    display: block;
    padding: 10px 6px;
    width: 100%;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #ddd;
    color: #555;
}

/*.loginButton{*/
/*    text-align: center*/

/*}*/
</style>