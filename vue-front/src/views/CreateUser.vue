<template>
    <div>
       <h1>Créer un utilisateur</h1> 
        <div>
            <label for="email">Email de l'utilisateur :</label>
            <input v-model="email" type="email" name="email">
        </div>
        <hr/>
        <div>
            <button v-on:click="createUser()" class="shadow-lg rounded-pill btn btn-warning">Créer utilisateur</button>
        </div>
        <p v-if="url">{{ url }}</p>
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
            email:    null,
            url:      null,
        }
    },
    mounted() {

    },
    methods:{
        createUser() {
            let newuser = {
                email: this.email,
            }

            this.$axiosLogin.post("create-user", newuser).then(t => {
                this.url = t.data;
            });
        }
    }
}
</script>

<style></style>