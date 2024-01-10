<template>
    <div v-if="question">
        <labelQuestion      v-if="question.type == 'LABEL'"     :questionId="questionId" :editionMode="editionMode"/>
        <textQuestion       v-if="question.type == 'TEXT'"      :questionId="questionId" :editionMode="editionMode"/>
        <checkboxQuestion   v-if="question.type == 'CHECKBOX'"  :questionId="questionId" :editionMode="editionMode"/>
        <radioQuestion      v-if="question.type == 'RADIO'"     :questionId="questionId" :editionMode="editionMode"/>
    </div>
</template>

<script>
    import labelQuestion from './questionTypes/labelQuestion.vue';
    import textQuestion from './questionTypes/textQuestion.vue';
    import checkboxQuestion from './questionTypes/checkboxQuestion.vue';
    import radioQuestion from './questionTypes/radioQuestion.vue';

    export default {
        name:"questionDetails",
        components:{
            labelQuestion,
            textQuestion,
            checkboxQuestion,
            radioQuestion,
        },
        props: {
            questionId: {
                type: Number,
                required: true,  
            },
            editionMode: {
                type: Boolean,
                default: false,
            }
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
                let id = this["questionId"];
                
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