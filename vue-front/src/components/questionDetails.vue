<template>
    <div v-if="question">
        
        <ul class="myUL">
            <li>Type : {{ question.type }}</li>
            <li>Title : {{ question.title }}</li>
        </ul>
        <ul v-for="answer in question.answers" :key="answer.id" class="myUL">
            <li><answersDetails :answerId="answer.id" /></li>
        </ul>
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

<style>

</style>