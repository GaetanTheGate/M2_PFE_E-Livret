<template>
    <div v-if="question">
        <p>
            {{ question.title }}
        </p>
        <div v-for="answer in question.answers" :key="answer.id">
            <input v-if="answer.value == 'true' " checked type="checkbox" id="answer.id" name="answer.id" disabled> <!-- Enable quand on est en mode "édition" -->
            <input v-if="answer.value == 'false'"         type="checkbox" id="answer.id" name="answer.id" disabled> <!-- Enable quand on est en mode "édition" -->
            <label for="answer.id">{{ answer.proposition }}</label>
            {{ answer.value }}
        </div>
    </div>
</template>


<script>
    export default {
        name:"checkboxQuestion",
        components:{
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
                    this.question = q.data;
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