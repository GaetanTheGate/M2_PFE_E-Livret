<template>
    <div class="container" v-if="question">
        <h4>
            {{ question.title }}
        </h4>
        <div class="container" v-for="answer in question.answers" :key="answer.id">
            <label v-if="answer.proposition" for="answer.id">{{ answer.proposition }}</label>
            <textarea :name=question.id :id=answer.id class="form-control" @input="resizeTextArea(answer.id)" style="height: 17px;resize: none;scrollbar-width: none"></textarea>
        </div>
    </div>
</template>


<script>
export default {
    name: "textQuestion",
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
        resizeTextArea(id) {
            const textarea = document.getElementById(id);
            if (textarea) {
                textarea.style.height = '17px';
                textarea.style.height = textarea.scrollHeight + 'px';
            }
        },

        fetchQuestion: function () {
            let id = this["questionId"];

            this.$axiosApi.get("questions/" + id).then(q => {
                this.question = q.data
            }).then(() => {
                this.question.answers.forEach(answer => {
                    if (document.getElementById(answer.id))
                        document.getElementById(answer.id).value = answer.value;
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
                        value: document.getElementById(answer.id).value,
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