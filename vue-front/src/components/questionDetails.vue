<template>
    <div v-if="question">
        <labelQuestion      v-if="question.type == 'LABEL'"     :questionId="questionId" />
        <textQuestion       v-if="question.type == 'TEXT'"      :questionId="questionId" />
        <checkboxQuestion   v-if="question.type == 'CHECKBOX'"  :questionId="questionId" />
        <radioQuestion      v-if="question.type == 'RADIO'"     :questionId="questionId" />

        <div v-if="false">
            <ul class="myUL">
                <li>Type : {{ question.type }}</li>
                <li>Title : {{ question.title }}</li>
            </ul>
            <ul v-for="answer in question.answers" :key="answer.id" class="myUL">
                <li><answersDetails :answerId="answer.id" /></li>
            </ul>
        </div>
    </div>
</template>

<script>
    import labelQuestion from './questionTypes/labelQuestion.vue';
    import textQuestion from './questionTypes/textQuestion.vue';
    import checkboxQuestion from './questionTypes/checkboxQuestion.vue';
    import radioQuestion from './questionTypes/radioQuestion.vue';
    import answersDetails from './answersDetails.vue';

    export default {
        name:"questionDetails",
        components:{
            labelQuestion,
            textQuestion,
            checkboxQuestion,
            radioQuestion,
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
            questionId() {
                this.fetchQuestion();
            }
        }
    }
</script>

<style>

</style>