<template>
    <div class="container">
        <div class="card rounded-3" v-if="livrets">
            <div class="card-header  text-center">Livrets en cours</div>
            <div class="card-body">
                <ul class="list-group list-group-light">
                    <li class="list-group-item d-flex justify-content-between align-items-center px-3 border-0 rounded-5 list-group-item-primary mb-2" v-for="livret in livrets" :key="livret.id" id="livret">
                        <button class="link" v-on:click="this.redirect(livret.id)">Livret {{ livret.id }}</button>

                        <div>

                            <button v-on:click="this.modifyLivret(livret.id)" type="button" class="btn btn-secondary me-5" >Modifier livret</button>

                            <a href="#" class="d-inline-block" data-bs-toggle="tooltip" data-bs-placement="top" title="Section(s) à completer">
                                <svg v-if="livretIdsToComplete.includes(livret.id)" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="orange" class="bi bi-exclamation-triangle-fill flex-shrink-0 me-2" viewBox="0 0 16 16" role="img" aria-label="Warning:">
                                    <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                                </svg>
                            </a>
                        </div>
                    </li>
                </ul>
            </div>
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