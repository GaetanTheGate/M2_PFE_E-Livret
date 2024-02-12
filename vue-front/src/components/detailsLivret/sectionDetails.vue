<template>
    <div v-if="section && displaySection" class="container">
        <h2>{{ section.title }}</h2>
        <p>This section is owned by {{ section.owner }}</p>

        <div v-if="displayVisibility" class="form-check">
            <label for="section_visibility" class="form-check-label">Visible</label>
            <input type="checkbox" :id="'section_' + section.id + '_visibility'" v-model="section.visibility"
                v-on:click="saveVisibility()" class="form-check-input">
        </div>

        <div v-for="question in section.questions" :key="question.id" class="mb-2">
            <questionDetails :questionId="question.id" :editionMode="editionMode" @callable="addQuestionChild" />
        </div>
        <button v-if="editionMode" v-on:click="saveAnswers()" class="btn btn-danger">Sauvegarder</button>
    </div>
</template>

<script>
import questionDetails from './questionDetails.vue';
export default {
    name: "sectionDetails",
    components: {
        questionDetails
    },
    props: {
        sectionId: {
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
            section: null,
            displaySection: null,
            displayVisibility: null,
            questionChilds: null,
        }
    },
    mounted() {
        this.fetchSection();
    },

    methods: {
        fetchSection: function () {
            this.questionChilds = [];

            let id = this["sectionId"]
            this.$axiosApi.get("sections/" + id).then(s => {
                this.section = s.data
                this.computeDisplaySection();
                this.computeDisplayVisibility();
            });
        },

        addQuestionChild: function (child) {
            this.questionChilds.push(child);
        },

        saveAnswers: function () {
            this.questionChilds.forEach(child => {
                child.saveAnswers();
            });
        },

        saveVisibility: function () {
            let sec = {
                id: this.section.id,
                visibility: document.getElementById("section_" + this.section.id + "_visibility").checked
            };

            this.$axiosApi.put("sections/saveVisibility", sec).then(s => {
                this.section = s.data;
                console.log(s.data);
            })
        },

        // TODO : Ameliorer parce que c'est affreux !
        computeDisplaySection: function () {
            let edit = this["editionMode"];
            if (edit) {
                this.displaySection = false;

                this.$axiosLogin.get("whoami").then(u => {
                    let me = u.data;

                    console.log(me);

                    this.$axiosApi.get("/livrets/" + this.section.livretId).then(l => {
                        let livret = l.data;

                        console.log(livret)

                        if (livret.tutor.id == me.id)
                            this.displaySection = true;

                        switch (this.section.owner) {
                            case 'STUDENT':
                                if (livret.student.id != me.id)
                                    this.displaySection |= false;
                                else
                                    this.displaySection = true && this.section.visibility;
                                break;

                            case 'MASTER':
                                if (livret.master.id != me.id)
                                    this.displaySection |= false;
                                else
                                    this.displaySection = true && this.section.visibility;
                                break;

                            case 'TUTOR':
                            default:
                                break;
                        }


                        console.log(this.displaySection);
                    })

                });
            }
            else
                this.displaySection = this.section.visibility;

        },

        computeDisplayVisibility: function () {
            let edit = this["editionMode"];
            if (edit) {
                this.$axiosLogin.get("whoami").then(u => {
                    let me = u.data;

                    this.$axiosApi.get("/livrets/" + this.section.livretId).then(l => {
                        let livret = l.data;

                        if (livret.tutor.id == me.id)
                            this.displayVisibility = true;
                        else
                            this.displayVisibility = false;
                    })

                });
            }
            else
                this.displayVisibility = false;
        }
    },
    watch: {
        sectionId() {
            this.fetchSection();
        },
        editionMode() {
            this.computeDisplaySection();
            this.computeDisplayVisibility();
        }
    }
}
</script>

<style>
.mb-5 {
    background-color: lightgrey;
}
</style>