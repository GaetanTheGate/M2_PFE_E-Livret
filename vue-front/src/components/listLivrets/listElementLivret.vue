<template>
    <div v-if="!livret" style="filter: drop-shadow(0 0 0.2rem #f9f8fc);"
        class="btn-outline-light list-group-item d-flex justify-content-center align-items-center p-4 border-0 rounded-5 list-group-item-secondary m-4">
        <div class="align-self-center spinner-border" role="status"></div>
    </div>

    <button v-if="livret" type="button" v-on:click="redirect()" style="filter: drop-shadow(0 0 0.2rem #adb5bd);"
        class="btn btn-outline-light list-group-item d-flex justify-content-between align-items-center p-4 border-0 rounded-5 list-group-item-secondary m-4"
        :key="livret.id" id="livret">

        <div class="text-start">
            <div class="fw-bold text-primary fs-5 text-wrap mx-2">{{ livret.name }}</div>
            <hr class="border border-primary" />
            <div class="fw-light text-secondary fs-8 text-wrap mt-3 mx-4">
                <div>
                    <u>Etudiant :</u> {{ livret.student.email }}
                </div>
                <div>
                    <u>Maitre d'apprentissage :</u> {{ livret.master.email }}
                </div>
                <div>
                    <u>Tuteur :</u> {{ livret.tutor.email }}
                </div>
            </div>

        </div>

        <div>
            <button v-if="showModifyButton" type="button"
                v-on:click="(event) => { event.stopPropagation(); modifyLivret() }"
                style="filter: drop-shadow(0 0 1.0rem #0b6f33);"
                class="btn btn-outline-success me-5 p-3 rounded-4 fw-bold border-0 fs-4">Modifier</button>

            <a class="d-inline-block" data-bs-toggle="tooltip" data-bs-placement="top" title="Section(s) à completer">
                <svg v-if="livretIdsToComplete.includes(livret.id)"
                    style="filter: drop-shadow(0 0 0.75rem rgb(255, 155, 42));" xmlns="http://www.w3.org/2000/svg"
                    width="24" height="24" fill="orange" class="bi bi-exclamation-triangle-fill flex-shrink-0 me-2"
                    viewBox="0 0 16 16" role="img" aria-label="Warning:">
                    <path
                        d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z" />
                </svg>
            </a>
        </div>
    </button>
</template>

<script>

export default {

    name: 'listElementLivret',
    components: {
    },
    props: {
        livretId: {
            type: Number,
            required: true,
        },
    },

    data() {
        return {
            livret: null,
            livretIdsToComplete: null,
            showModifyButton: null,
        }
    },

    mounted() {
        this.fetchLivret();
        this.fetchLivretsToComplete();
    },

    methods: {
        fetchLivret: function () {
            let id = this["livretId"]
            this.$axiosApi.get("livrets/" + id).then(l => {
                this.livret = l.data;
                this.computeDisplayModifyButton();
            });
        },
        fetchLivretsToComplete: function () {
            this.livretIdsToComplete = [];
            this.$axiosApi.get("livrets/mine/tocomplete").then(l => {
                l.data.forEach(element => this.livretIdsToComplete.push(element.id));
            });
        },
        redirect: function () {
            this.$pageService.gotoLivretDetailsPage(this["livretId"]);
        },
        modifyLivret: function () {
            this.$pageService.gotoLivretModifyPage(this["livretId"]);
        },
        computeDisplayModifyButton() {
            this.$axiosLogin.get("whoami").then(u => {
                let me = u.data;
                this.showModifyButton = (this.livret.responsable.id == me.id)
            });
        }
    }
}
</script>



<style></style>