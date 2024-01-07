<template>
    <div v-if="question">
        <ul>
            <li>Type : {{ question.type }}</li>
            <li>Title : {{ question.title }}</li>
        </ul>
        <ul v-for="answer in question.answers" :key="answer.id">
            <li><answersDetails :answerId="answer.id" /></li>
        </ul>
        <t>________________________________</t>
    </div>
</template>

<script>
    import answersDetails from './answersDetails.vue';
    export default {
        name:"questionDetails",
        components:{
            answersDetails
        },
        props: {
            questionId: Number
        },

        data(){
            return {
                question:    null,
            }
        },
        mounted(){
            this.fetchQuestion();
        },

        methods:{
            fetchQuestion: function() {
                let id = this["questionId"]
                this.$axiosApi.get("questions/"+ id ).then(q => {
                    this.question = q.data
                });
            }
        },
        watch:{
            sectionId() {
                this.fetchQuestion();
            }
        }
    }
</script>