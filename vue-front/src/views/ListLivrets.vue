<template>
    <div class="container">
        <div class="card rounded-3 m-5" v-if="livrets">
            <div class="card-header text-center fs-6">Livrets en cours</div>
            <div class="card-body">
                <ul class="list-group list-group-light">
                    <button type="button" v-on:click="this.redirect(livret.id)" style="filter: drop-shadow(0 0 0.2rem #ddd3ee);" class="btn btn-outline-light list-group-item d-flex justify-content-between align-items-center p-4 border-0 rounded-5 list-group-item-secondary m-4" v-for="livret in livrets" :key="livret.id" id="livret">
                        <div class="text-start">
                            <div class="fw-bold text-primary fs-5 text-wrap mx-2">Livret {{ livret.id }}</div>
                            <hr class="border border-primary" />
                            <div class="fw-light text-secondary fs-8 text-wrap mt-3 mx-4">
                                <div>
                                    <u>Etudiant :</u> {{ livret.student.email }}
                                </div>
                                <div>
                                    <u>Maitre d'apprentissage :</u> {{ livret.master.email }}
                                </div>
                                <div>
                                    <u>Tuteur :</u> {{ livret.tutor.email }}
                                </div>
                            </div>

                        </div>

                        <div>
                            <button type="button" v-on:click="this.modifyLivret(livret.id)" style="filter: drop-shadow(0 0 1.0rem #0b6f33);" class="btn btn-outline-success me-5 p-3 rounded-4 fw-bold border-0 fs-4" >Modifier</button>

                            <a class="d-inline-block" data-bs-toggle="tooltip" data-bs-placement="top" title="Section(s) à completer">
                                <svg v-if="livretIdsToComplete.includes(livret.id)" style="filter: drop-shadow(0 0 0.75rem rgb(255, 155, 42));"  xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="orange" class="bi bi-exclamation-triangle-fill flex-shrink-0 me-2" viewBox="0 0 16 16" role="img" aria-label="Warning:">
                                    <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                                </svg>
                            </a>
                        </div>
                    </button>
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
            this.$pageService.gotoLivretDetailsPage(livretsId);
        },
        modifyLivret: function (livretsId) {
            this.$pageService.gotoLivretModifyPage(livretsId);
        },
    }
}
</script>



<style>
</style>