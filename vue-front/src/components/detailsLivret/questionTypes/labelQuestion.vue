<template>
    <div v-if="question">
        <h4>
            {{ question.title }}
        </h4>
    </div>
</template>


<script>
export default {
    name: "labelQuestion",
    components: {
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

    data() {
        return {
            question: null,
        }
    },
    mounted() {
        this.fetchQuestion();
        this.emitCallable();
    },

    methods: {
        fetchQuestion: function () {
            let id = this["questionId"];

            this.$axiosApi.get("questions/" + id).then(q => {
                this.question = q.data
            });
        },

        saveAnswers: function () {
            // Do nothing
        },

        emitCallable: function () {
            this.$emit("callable", {
                saveAnswers: () => this.saveAnswers()
            });
        },
    },
    watch: {
        questionId() {
            this.fetchQuestion();
        }
    }
}
</script>