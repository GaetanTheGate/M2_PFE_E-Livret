<template>
    <!-- <button v-on:click="fetchLivret()">SHOW SECTIONS</button> -->
    <div v-if="livret" class="container">
        <button v-if="!editionMode" v-on:click="setEditionMode(true)">Editer</button>
        <button v-if=" editionMode" v-on:click="setEditionMode(false)">Consulter</button>
        <ul class="myUL">
            <li>student : {{ livret.student }}</li>
            <li>master : {{ livret.master }}</li>
            <li>responsable : {{ livret.responsable }}</li>
            <li>tutor : {{ livret.tutor }}</li>
            <li>id : {{ livret.id }}</li>
        </ul>
        <div v-for="section in livret.sections" :key="section.id" class="myUL">
            <sectionDetails :sectionId="section.id" :editionMode=editionMode />
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
            livret:         null,
            editionMode:    null,
        }
    },
    mounted() {
        this.setEditionMode(false);
        this.fetchLivret();
    },

    methods: {
        fetchLivret: function() {
            let id = this["livretId"]
            this.$axiosApi.get("livrets/" + id).then(s => {
                this.livret = s.data
            });
        },

        setEditionMode: function(state) {
            this.editionMode = state;
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

div.container {
  text-align: center;
}

ul.myUL {
    text-align: left;
}
</style>