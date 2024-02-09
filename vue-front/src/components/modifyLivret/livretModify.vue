<template>
    <div v-if="livret" class="container">
        <userModify :userId="livret.studentId" @user_clicked="setStudent"   :userType="'Apprenti(e)'" />
        <userModify :userId="livret.masterId"  @user_clicked="setMaster"    :userType="'Maitre d\'apprentissage'" />
        <userModify :userId="livret.tutorId"   @user_clicked="setTutor"     :userType="'Tuteur'" />
        
        <div v-for="section in livret.sections" :key="section.id" class="myUL">
            {{ section.id }}
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
            livret:         null,
        }
    },
    mounted() {
        this.fetchLivret();
    },

    methods: {
        fetchLivret: function() {
            let id = this["livretId"];
            this.$axiosApi.get("livrets/" + id).then(s => {
                this.livret = s.data
            });
        },

        setStudent: function(user) {
            let l = {
                id:         this.livret.id,
                studentId:  user.id,
                masterId:   this.livret.masterId,
                tutorId:    this.livret.tutorId,
            }

            this.$axiosApi.put("livrets/set-actors?setStudent=true", l).then(l => {
                this.livret.studentId = l.data.studentId;
            });
        },

        setMaster: function(user) {
            let l = {
                id:         this.livret.id,
                studentId:  this.livret.studentId,
                masterId:   user.id,
                tutorId:    this.livret.tutorId,
            }

            this.$axiosApi.put("livrets/set-actors?setMaster=true", l).then(l => {
                this.livret.masterId = l.data.masterId;
            });
        },

        setTutor: function(user) {
            let l = {
                id:         this.livret.id,
                studentId:  this.livret.studentId,
                masterId:   this.livret.masterId,
                tutorId:    user.id,
            }

            this.$axiosApi.put("livrets/set-actors?setTutor=true", l).then(l => {
                this.livret.tutorId = l.data.tutorId;
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