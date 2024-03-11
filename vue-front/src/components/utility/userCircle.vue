<template>
    <button v-if="this.innitials" type="button" class="rounded-circle nav-item btn btn-light"
        style="height: 3pc; width: 3pc;">
        <b>{{ this.innitials }}</b>
    </button>
</template>

<script>


export default {
    name: "userCircle",
    components: {
    },
    props: {
        userId: {
            type: Number,
            required: true,
        }
    },

    data() {
        return {
            innitials: null,
            currentUser: null,
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

                this.computeInnitials();
            });
        },

        computeInnitials: function () {
            this.innitials = this.currentUser.firstName.charAt(0).toUpperCase() + this.currentUser.lastName.charAt(0).toUpperCase()
        }
    },
    watch: {
        userId() {
            this.fetchCurrentUser();
        }
    }
}
</script>