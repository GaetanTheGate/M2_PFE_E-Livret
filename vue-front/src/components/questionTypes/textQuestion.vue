<template>
    <div v-if="question">
        <p>
            {{ question.title }}
        </p>
        <div v-for="answer in question.answers" :key="answer.id">
            <label for="answer.id">{{ answer.proposition }}</label>
            <input type="text" :name=question.id :id=answer.id>
        </div>
    </div>
</template>


<script>
    export default {
        name:"textQuestion",
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
                    this.question = q.data
                }).then( () => {
                    this.question.answers.forEach(answer => {
                        document.getElementById(answer.id).value = answer.value;
                        document.getElementById(answer.id).disabled = true; // enable quand on est en mode édition 
                    });
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