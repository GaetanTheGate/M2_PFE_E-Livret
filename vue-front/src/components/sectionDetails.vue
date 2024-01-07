<template>
    <div v-if="section">
        <ul>
            <t>_________________________________________________________________</t>
            <li>Title : {{ section.title }}</li>
            <li>This section is owned by {{ section.owner }}</li>
            <t>_________________________________________________________________</t>
            
        </ul>
        <ul v-for="question in section.questions" :key="question.id">
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