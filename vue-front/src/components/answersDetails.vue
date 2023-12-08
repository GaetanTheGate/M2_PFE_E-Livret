<template>
    <div v-if="answer">
        <ul>
            {{ answer.value }}
            <!-- <li>{{ question.type }}</li>
            <li>{{ question.title }}</li> -->
        </ul>
    </div>
</template>

<script>
    export default {
        name:"answerDetails",
        props: {
            answerId: Number
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
            sectionId() {
                this.fetchAnswer();
            }
        }
    }
</script>