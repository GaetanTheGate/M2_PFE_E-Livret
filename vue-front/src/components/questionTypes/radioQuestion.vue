<template>
    <div v-if="question">
        <p>
            {{ question.title }}
        </p>
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