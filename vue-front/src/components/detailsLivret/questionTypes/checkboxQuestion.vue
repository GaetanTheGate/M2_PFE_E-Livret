<template>
    <div class="container d-flex  justify-content-between align-items-center" v-if="question">
        <div class="col">
            <h4>
                {{ question.title }}
            </h4>
        </div>
        <fieldset class="col ps-3 border-start border-dark border-3">
            <div v-for="answer in question.answers" :key="answer.id" class="form-check">
                <label for="answer.id" class="form-check-label">{{ answer.proposition }}</label>
                <input type="checkbox" :name=question.id :id=answer.id class="form-check-input">
            </div>
        </fieldset>
    </div>
</template>


<script>
export default {
    name: "checkboxQuestion",
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
                this.question = q.data;
            }).then(() => {
                this.question.answers.forEach(answer => {
                    if (document.getElementById(answer.id))
                        document.getElementById(answer.id).checked = answer.value == 'true';
                });

                this.setupEditionMode();
            });
        },

        setupEditionMode: function () {
            let edit = this["editionMode"];

            this.question.answers.forEach(answer => {
                if (document.getElementById(answer.id))
                    document.getElementById(answer.id).disabled = !edit;
            });
        },

        saveAnswers: function () {
            this.question.answers.forEach(answer => {
                if (document.getElementById(answer.id)) {
                    let ans = {
                        id: answer.id,
                        value: document.getElementById(answer.id).checked,
                    }
                    this.$axiosApi.put("answers/saveValue", ans).then(a => {
                        answer = a.data;
                    });
                }
            });
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
        },
        editionMode() {
            this.setupEditionMode();
        }
    }
}
</script>