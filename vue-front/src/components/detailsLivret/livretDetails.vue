<template>
    <!-- <button v-on:click="fetchLivret()">SHOW SECTIONS</button> -->
    <div v-if="livret" class="container">

<!--        <ul class="myUL">-->
<!--            <li>student : {{ livret.student }}</li>-->
<!--            <li>master : {{ livret.master }}</li>-->
<!--            <li>responsable : {{ livret.responsable }}</li>-->
<!--            <li>tutor : {{ livret.tutor }}</li>-->
<!--            <li>id : {{ livret.id }}</li>-->
<!--        </ul>-->
        <div class="card rounded-3 m-5">
            <div class="card-header text-center fs-2 fw-bold">{{ livret.name }}</div>
            <div class="card-body">
                <div v-for="section in this.livret.sections.sort((a, b) => a.id - b.id)" :key="section.id" class="mb-5">
        <!--            <sectionDetails :sectionId="section.id" :editionMode=editionMode />-->
                    <sectionDetails :sectionId="section.id" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import sectionDetails from './sectionDetails.vue';
export default {
    name: "livretDetails",
    components: {
        sectionDetails
    },
    props: {
        livretId: {
            type: Number,
            required: true,
        }
    },

    data() {
        return {
            livret: null,
        }
    },
    mounted() {
        this.fetchLivret();
    },

    methods: {
        fetchLivret: function () {
            let id = this["livretId"]
            this.$axiosApi.get("livrets/" + id).then(s => {
                this.livret = s.data
            });
        }


    },
    watch: {
        livretId() {
            this.fetchLivret();
        }
    }
}
</script>

<style>
/*div.container {*/
/*  text-align: center;*/
/*}*/

/*ul.myUL {*/
/*    text-align: left;*/
/*}*/
</style>