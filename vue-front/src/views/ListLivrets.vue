<template>
    <div class="container">
        <div class="card rounded-3 m-5">
            <div class="card-header text-center fs-6">Livrets en cours</div>
            <div class="card-body">
                <div v-if="!livrets" class="d-flex justify-content-center">
                    <div class="align-self-center spinner-border" role="status"></div>
                </div>
                <ul class="list-group list-group-light" v-if="livrets">
                    <elementLivret v-for="livret in livrets" :key="livret.id" :livretId="livret.id" />
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
import elementLivret from '../components/listLivrets/listElementLivret.vue'

export default {

    name: 'ListLivretsPage',
    components: {
        elementLivret
    },

    data() {
        return {
            livrets: null,
            livretIdsToComplete: null,
        }
    },

    mounted() {
        this.fetchLivrets();
    },

    methods: {
        fetchLivrets: function () {
            this.livrets = [];
            this.$axiosApi.get("livrets/mine").then(l => {
                this.livrets = l.data;
            });
        },
    }
}
</script>



<style></style>