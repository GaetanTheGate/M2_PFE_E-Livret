<template>
    <p>{{ section }}</p>
</template>

<script>
    export default {
        name:"ESection",
        props: {
            sectionId: Number
        },

        data(){
            return {
                section:    null,
            }
        },
        mounted(){
            this.fetchSection();
        },

        methods:{
            fetchSection: function() {
                let id = this["sectionId"]
                this.$axiosApi.get("sections/"+ id ).then(s => {
                    console.log("test : " + s.data);
                    this.section = s.data
                });
            }
        },
        watch:{
            sectionId() {
                this.fetchSection();
            }
        }
    }
</script>