<template>
    <div v-if="livret" class="container">
        <userModify :userId="livret.student ? livret.student.id : null" @user_clicked="setStudent"
            :userType="'Apprenti(e)'" />
        <userModify :userId="livret.master ? livret.master.id : null" @user_clicked="setMaster"
            :userType="'Maitre d\'apprentissage'" />
        <userModify :userId="livret.tutor ? livret.tutor.id : null" @user_clicked="setTutor" :userType="'Tuteur'" />

        <!-- <div v-for="section in livret.sections" :key="section.id" class="myUL">
            {{ section.id }}
        </div> -->
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
        }
    },
    mounted() {
        this.fetchLivret();
    },

    methods: {
        fetchLivret: function () {
            let id = this["livretId"];
            this.$axiosApi.get("livrets/" + id).then(s => {
                this.livret = s.data
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
    },
    watch: {
        livretId() {
            this.fetchLivret();
        }
    }
}
</script>