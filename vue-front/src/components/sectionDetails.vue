<template>
    <div v-if="section && displaySection" class="container">
        <hr />
        <h2>{{ section.title }}</h2>
        <p>This section is owned by {{ section.owner }}</p>
        <div v-for="question in section.questions" :key="question.id" class="myUL">
            <questionDetails :questionId="question.id" :editionMode="editionMode"/>
        </div>
    </div>
</template>

<script>
    import questionDetails from './questionDetails.vue';
    export default {
        name:"sectionDetails",
        components:{
            questionDetails
        },
        props: {
            sectionId: {
                type: Number,
                required: true,  
            },
            editionMode: {
                type: Boolean,
                default: true,
            }
        },

        data(){
            return {
                section:            null,
                displaySection:     null,
            }
        },
        mounted(){
            this.fetchSection();
        },

        methods:{
            fetchSection: function() {
                let id = this["sectionId"]
                this.$axiosApi.get("sections/"+ id ).then(s => {
                    this.section = s.data
                    this.computeDisplaySection();
                });
            },

            // TODO : Ameliorer parce que c'est affreux !
            computeDisplaySection: function() {
                let edit = this["editionMode"];
                if(edit){
                    this.displaySection = false;

                    this.$axiosLogin.get("whoami").then( u => {
                        let me = u.data;

                        this.$axiosApi.get("/livrets/"+this.section.livretId).then( l => {
                            let livret = l.data;

                            if(livret.tutorId == me.id)
                                this.displaySection = true;
                            
                            switch(this.section.owner){
                                case 'STUDENT':
                                    if(livret.studentId != me.id)
                                        this.displaySection |= false;
                                    else
                                        this.displaySection = true && this.section.visibility;
                                    break;

                                case 'MASTER':
                                    if(livret.masterId != me.id)
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

            }
        },
        watch:{
            sectionId() {
                this.fetchSection();
            },
            editionMode() {
                this.computeDisplaySection();
            }
        }
    }
</script>

<style>

</style>