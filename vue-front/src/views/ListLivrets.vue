<template>
    <div class="container" v-if="livrets">
        <div v-for="livret in livrets" :key="livret.id" id="livret">
            <button class="link" v-on:click="this.redirect(livret.id)">Livret {{ livret.id }} <p
                    v-if="livretIdsToComplete.includes(livret.id)" class=""> - A compléter !</p></button>

            <button v-on:click="this.modifyLivret(livret.id)">Modifier livret</button>
        </div>
    </div>
</template>

<script>
// import livretDetails from '../components/livretDetails.vue'

export default {

    name: 'ListLivretsPage',
    components: {
        // livretDetails
    },

    data() {
        return {
            livrets: null,
            livretIdsToComplete: null,
        }
    },

    mounted() {
        this.fetchLivrets();
        this.fetchLivretsToComplete();
    },

    methods: {
        fetchLivrets: function () {
            this.livrets = [];
            this.$axiosApi.get("livrets/mine").then(l => {
                this.livrets = l.data;
            });
        },
        fetchLivretsToComplete: function () {
            this.livretIdsToComplete = [];
            this.$axiosApi.get("livrets/mine/tocomplete").then(l => {
                l.data.forEach(element => this.livretIdsToComplete.push(element.id));
            });
        },
        redirect: function (livretsId) {
            this.$router.push({ path: `/Livret/${livretsId}/details` })
        },
        modifyLivret: function (livretsId) {
            this.$router.push({ path: `/Livret/${livretsId}/modify` })
        }
    }
}
</script>

<style>
button.link {
    background: none;
    border: none;
}
</style>