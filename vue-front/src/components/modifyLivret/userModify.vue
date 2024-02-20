<template>
    <div class="container">
        <div class="card rounded-3 m-5">
            <div class="card-header text-center fs-5 fw-bold">{{ userType }}</div>
            <div class="card-body">

                <div class="d-flex justify-content-between">
                    <p v-if="user" class="w-25 align-middle m-1"> <u>Email :</u> {{ user.email }}</p>
                    <p v-if="!user" class="w-25 align-middle m-1"> <u>{{ userType }} non défini !</u></p>

                    <div class="w-50 d-flex justify-content-between">
                        <input class="input-lg rounded w-75 m-1 p-1" placeholder="Chercher un email" v-model="email"
                            type="text" name="lookForUser">
                        <button class="btn btn-warning w-25 m-1 p-1" v-on:click="searchUsers(email)">Chercher</button>
                    </div>
                </div>

                <div v-if="usersFound && usersFound.length" class="m-2 pt-3 rounded-3 bg-light" style="filter: drop-shadow(0 0 0.25rem #cccccc);">
                    <!-- <hr class="border border-dark border-1 rounded-5" /> -->
                    <h5 class="px-3">Sélectionner un utilisateur</h5>
                    <div class="d-flex justify-content-start flex-nowrap overflow-scroll">
                        <div class="container w-25 m-0 pb-3" v-for="userFound in usersFound" :key="userFound.id" >
                            <div class="card rounded-3 mx-1">
                                <div class="card-header text-center fs-8 fw-light">{{ userFound.email }}</div>
                                <div class="card-body">
                                    <button class="w-100 btn btn-secondary"
                                        v-on:click="sendUserToParent(userFound)">Séléctionner</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
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
            if (id)
                this.$axiosApi.get("users/" + id).then(u => {
                    this.user = u.data;
                });
        },
        searchUsers: function (mail) {
            this.usersFound = null;
            this.$axiosApi.post("users/search", { email: mail }).then(u => {
                this.usersFound = u.data
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