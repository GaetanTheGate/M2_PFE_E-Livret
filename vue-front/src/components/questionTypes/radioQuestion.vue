<template>
    <div v-if="question">
        <p>
            {{ question.title }}
        </p>
        <fieldset>
            <div v-for="answer in question.answers" :key="answer.id">
                <input type="radio" :name=question.id :id=answer.id>
                <label for="answer.id">{{ answer.proposition }}</label>
            </div>
        </fieldset>
    </div>
</template>


<script>
    export default {
        name:"radioQuestion",
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
                        document.getElementById(answer.id).checked = answer.value == 'true';
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