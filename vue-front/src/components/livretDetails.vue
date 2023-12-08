<template>
    <button v-on:click="fetchLivret()">SHOW SECTIONS</button>
    <div v-if="livret">
        <ul>
            <li>{{ livret.student }}</li>
            <li>{{ livret.master }}</li>
            <li>{{ livret.responsable }}</li>
            <li>{{ livret.tutor }}</li>
        </ul>
        <ul v-for="section in livret.sections" :key="section.id">
            <li><sectionDetails :sectionId="section.id" /></li>
        </ul>
    </div>
</template>

<script>
    import sectionDetails from './sectionDetails.vue';
    export default {
        name:"livretDetails",
        components:{
            sectionDetails
        },
        props: {
            livretId: Number
        },

        data(){
            return {
                livret:    null,
            }
        },
        mounted(){
            //this.fetchLivret();
        },

        methods:{
            fetchLivret: function() {
                let id = this["livretId"]
                this.$axiosApi.get("livrets/"+ id ).then(s => {
                    this.livret = s.data
                });
            }
        },
        watch:{
            livretId() {
                this.fetchLivret();
            }
        }
    }
</script>