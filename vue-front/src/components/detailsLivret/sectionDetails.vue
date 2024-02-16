<template>
    <div v-if="section && displaySection" class="container">
        <div class="card rounded-3 m-5">
            <div  class="card-header btn btn-outline-light list-group-item d-flex  justify-content-between align-items-center p-4 border-0 list-group-item-secondary" data-bs-toggle="collapse" v-bind:data-bs-target="'#sectionCollapse_' + section.id">
                <div >
                    <h2>{{ section.title }}</h2>
                    <p>This section is owned by {{ section.owner }}</p>
                </div>
                <div>
                    <div class="collapse" v-bind:id="'sectionCollapse_' + section.id">
                        <svg   id="caret_up"
                               xmlns="http://www.w3.org/2000/svg"
                               width="24" height="24" fill="black" class="bi bi-caret-up-fill"
                               viewBox="0 0 16 16" role="img" >
                            <path d="m7.247 4.86-4.796 5.481c-.566.647-.106 1.659.753 1.659h9.592a1 1 0 0 0 .753-1.659l-4.796-5.48a1 1 0 0 0-1.506 0z"/>
                        </svg>
                    </div>
                    <div class="collapse show" v-bind:id="'sectionCollapse_' + section.id">
                        <svg id="caret_down"
                             xmlns="http://www.w3.org/2000/svg"
                             width="24" height="24" fill="black" class="bi bi-caret-down-fill"
                             viewBox="0 0 16 16" role="img" >
                            <path d="M7.247 11.14 2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z"/>
                        </svg>
                    </div>
                </div>


            </div>
            <div  class="collapse" v-bind:id="'sectionCollapse_' + section.id" >
                <div class="card-body bg-body-tertiary ">
                    <div v-if="displayVisibility" class="form-check">
                        <label for="section_visibility" class="form-check-label">Visible</label>
                        <input type="checkbox" :id="'section_' + section.id + '_visibility'" v-model="section.visibility"
                               v-on:click="saveVisibility()" class="form-check-input">
                    </div>

                    <div v-for="question in section.questions" :key="question.id" class="container my-4">
                        <questionDetails :questionId="question.id" :editionMode="editionMode" @callable="addQuestionChild" />
                    </div>
                    <button v-if="editionMode" v-on:click="saveAnswers()" class="btn btn-danger">Sauvegarder</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import questionDetails from './questionDetails.vue';
// import collapse from "bootstrap/js/src/collapse";
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
            })
        },

        // TODO : Ameliorer parce que c'est affreux !
        computeDisplaySection: function () {
            let edit = this["editionMode"];
            if (edit) {
                this.displaySection = false;

                this.$axiosLogin.get("whoami").then(u => {
                    let me = u.data;

                    this.$axiosApi.get("/livrets/" + this.section.livretId).then(l => {
                        let livret = l.data;

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
                        
                        this.displayVisibility = (livret.tutor.id == me.id)
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
.collapsing{
    #caret_up{
        visibility: hidden;
    }
}
</style>