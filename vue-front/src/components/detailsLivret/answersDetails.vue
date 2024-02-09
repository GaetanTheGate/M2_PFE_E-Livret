<template>
    <div v-if="answer" >
        <ul >
            {{ answer.value }}
        </ul>
    </div>
</template>

<script>
    export default {
        name:"answerDetails",
        props: {
            answerId: {
                type: Number,
                required: true,  
            },
        },

        data(){
            return {
                answer:    null,
            }
        },
        mounted(){
            this.fetchAnswer();
        },

        methods:{
            fetchAnswer: function() {
                let id = this["answerId"]
                this.$axiosApi.get("answers/"+ id ).then(a => {
                    this.answer = a.data
                });
            }
        },
        watch:{
            answerId() {
                this.fetchAnswer();
            }
        }
    }
</script>

<style>
</style>