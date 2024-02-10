<template>
    <div v-if="true">
        <p v-if="user"> <u>{{ userType }}</u> : {{ user.email }}</p>
        <p v-if="!user"> <u>{{ userType }} non défini !</u></p>

        <div>
            <label for="lookForUser">Rechercher un utilisateur avec son mail</label>
            <input v-model="email" type="text" name="lookForUser">
            <button v-on:click="searchUsers(email)">Chercher</button>
        </div>

        <div>
            <button v-for="userFound in usersFound" :key="userFound.id" v-on:click="sendUserToParent(userFound)">{{
                userFound.email }}</button>
        </div>
    </div>
</template>

<script>

export default {
    name: "userModify",
    components: {
    },
    props: {
        userType: {
            type: String,
            required: true,
        },
        userId: {
            type: Number,
            required: false,
        }
    },

    data() {
        return {
            user: null,
            usersFound: null,
        }
    },
    mounted() {
        this.fetchUser();
    },

    methods: {
        fetchUser: function () {
            this.user = null;
            let id = this["userId"];
            this.$axiosApi.get("users/" + id).then(u => {
                this.user = u.data;
            });
        },
        searchUsers: function (mail) {
            this.usersFound = null;
            this.$axiosApi.post("users/search", { email: mail }).then(u => {
                this.usersFound = u.data;
            });
            this.email = "";
        },
        sendUserToParent: function (user) {
            this.$emit('user_clicked', user);
        }
    },
    watch: {
        userId() {
            this.fetchUser();
            this.usersFound = null;
        }
    }
}
</script>