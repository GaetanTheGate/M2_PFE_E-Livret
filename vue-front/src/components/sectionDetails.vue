<template>
    <div v-if="section" class="container">
        <hr />
        <h2>{{ section.title }}</h2>
        <ul class="myUL">
            <li>This section is owned by {{ section.owner }}</li>
        </ul>
        <ul v-for="question in section.questions" :key="question.id" class="myUL">
            <li><questionDetails :questionId="question.id" /></li>
        </ul>
    </div>
</template>

<script>
    import questionDetails from './questionDetails.vue';
    export default {
        name:"sectionDetails",
        components:{
            questionDetails
        },
        props: {
            sectionId: Number
        },

        data(){
            return {
                section:    null,
            }
        },
        mounted(){
            this.fetchSection();
        },

        methods:{
            fetchSection: function() {
                let id = this["sectionId"]
                this.$axiosApi.get("sections/"+ id ).then(s => {
                    this.section = s.data
                });
            }
        },
        watch:{
            sectionId() {
                this.fetchSection();
            }
        }
    }
</script>

<style>

</style>