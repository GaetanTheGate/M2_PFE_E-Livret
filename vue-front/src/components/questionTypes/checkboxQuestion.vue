<template>
    <div v-if="question">
        <p>
            {{ question.title }}
        </p>
        <fieldset>
            <div v-for="answer in question.answers" :key="answer.id">
                <input type="checkbox" :name=question.id :id=answer.id>
                <label for="answer.id">{{ answer.proposition }}</label>
            </div>
        </fieldset>
    </div>
</template>


<script>
    export default {
        name:"checkboxQuestion",
        components:{
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
                    this.question = q.data;
                }).then( () => {
                    this.question.answers.forEach(answer => {
                        if(document.getElementById(answer.id))
                            document.getElementById(answer.id).checked = answer.value == 'true';
                    });

                    this.setupEditionMode();
                });
            },

            setupEditionMode: function() {
                let edit = this["editionMode"];

                this.question.answers.forEach(answer => {
                    if(document.getElementById(answer.id))
                        document.getElementById(answer.id).disabled = !edit;
                });
            }
        },
        watch:{
            questionId() {
                this.fetchQuestion();
            },
            editionMode() {
                this.setupEditionMode();
            }
        }
    }
</script>