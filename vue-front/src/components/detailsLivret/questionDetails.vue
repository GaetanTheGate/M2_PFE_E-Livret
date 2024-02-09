<template>
    <div v-if="question">
        <labelQuestion v-if="question.type == 'LABEL'" :questionId="questionId" :editionMode="editionMode"
            @callable="setSubQuestion" />
        <textQuestion v-if="question.type == 'TEXT'" :questionId="questionId" :editionMode="editionMode"
            @callable="setSubQuestion" />
        <checkboxQuestion v-if="question.type == 'CHECKBOX'" :questionId="questionId" :editionMode="editionMode"
            @callable="setSubQuestion" />
        <radioQuestion v-if="question.type == 'RADIO'" :questionId="questionId" :editionMode="editionMode"
            @callable="setSubQuestion" />
    </div>
</template>

<script>
import labelQuestion from './questionTypes/labelQuestion.vue';
import textQuestion from './questionTypes/textQuestion.vue';
import checkboxQuestion from './questionTypes/checkboxQuestion.vue';
import radioQuestion from './questionTypes/radioQuestion.vue';

export default {
    name: "questionDetails",
    components: {
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

    data() {
        return {
            question: null,
            subQ: null,
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

        setSubQuestion: function (subQ) {
            this.subQ = subQ;
        },

        saveAnswers: function () {
            this.subQ.saveAnswers();
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

<style></style>