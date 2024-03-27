<template>
    <div v-if="livret" class="container">
        <div class="my-5 d-flex justify-content-between">
            <button type="button" class="nav-item btn btn-primary p-3" v-on:click="saveModel()">
                Exporter la forme du livret
            </button>
            <button type="button" class="nav-item btn btn-secondary p-3" v-on:click="setModel()">
                Importer la forme du livret
            </button>
        </div>

        <userModify :userId="livret.student ? livret.student.id : null" @user_clicked="openModal($event, 'STUDENT')"
            :userType="'Apprenti(e)'" />
        <userModify :userId="livret.master ? livret.master.id : null" @user_clicked="openModal($event, 'MASTER')"
            :userType="'Maitre d\'apprentissage'" />
        <userModify :userId="livret.tutor ? livret.tutor.id : null" @user_clicked="openModal($event, 'TUTOR')"
            :userType="'Tuteur'" />

        <div class="modal" tabindex="-1" role="dialog" id="confirmModal">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirmer votre choix</h5>
                        <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Êtes-vous sûr de vouloir choisir cet utilisateur ?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                        <button type="button" class="btn btn-primary" @click="confirmModal">Confirmer</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

import userModify from './userModify.vue'

export default {
    name: "livretModify",
    components: {
        userModify
    },
    props: {
        livretId: {
            type: Number,
            required: true,
        }
    },

    data() {
        return {
            livret: null,
            selectedUser: null,
            selectedRole: null,
            modal: null,

        }
    },
    mounted() {
        this.fetchLivret();
    },

    watch: {
        livretId() {
            this.fetchLivret();
        }
    },
    methods: {
        fetchLivret: function () {
            let id = this["livretId"];
            this.$axiosApi.get("livrets/" + id).then(s => {
                this.livret = s.data
            });


        },

        saveModel: function () {
            this.$axiosApi.get("livrets/" + this.livret.id + "/model").then(m => {
                this.$download(JSON.stringify(m.data, undefined, 4), this.livret.name + "_model.json", "application/json");
            })
        },

        setModel: function () {
            this.$selectFileThen(c => {
                let model = JSON.parse(c);
                this.$axiosApi.put("livrets/" + this.livret.id + "/model", model).then(() => {
                    this.$pageService.gotoLivretDetailsPage(this.livret.id);
                })
            });
        },

        setStudent: function (user) {
            let l = {
                id: this.livret.id,
                studentId: user.id,
                masterId: this.livret.master.id,
                tutorId: this.livret.tutor.id,
            }
            this.livret.student = null;
            this.$axiosApi.put("livrets/set-actors?setStudent=true", l).then(l => {
                this.livret.student = l.data.student;
            });
        },

        setMaster: function (user) {
            let l = {
                id: this.livret.id,
                studentId: this.livret.student.id,
                masterId: user.id,
                tutorId: this.livret.tutor.id,
            }

            this.$axiosApi.put("livrets/set-actors?setMaster=true", l).then(l => {
                this.livret.master = l.data.master;
            });
        },

        setTutor: function (user) {
            let l = {
                id: this.livret.id,
                studentId: this.livret.student.id,
                masterId: this.livret.master.id,
                tutorId: user.id,
            }

            this.$axiosApi.put("livrets/set-actors?setTutor=true", l).then(l => {
                this.livret.tutor = l.data.tutor;
            });
        },

        openModal: function (user, role) {
            this.selectedRole = role;
            this.selectedUser = user;
            this.modal = new window.bootstrap.Modal(document.getElementById("confirmModal"));
            this.modal.show();
        },
        confirmModal: function () {
            switch (this.selectedRole) {
                case "STUDENT":
                    this.setStudent(this.selectedUser);
                    break;
                case "MASTER":
                    this.setMaster(this.selectedUser);
                    break;
                case "TUTOR":
                    this.setTutor(this.selectedUser);
                    break;
            }
            this.selectedUser = null;
            this.selectedRole = null;
            this.modal.hide();
        }




    }
}
</script>