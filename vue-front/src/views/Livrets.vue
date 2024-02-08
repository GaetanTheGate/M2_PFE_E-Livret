<template>
    <div v-if="livretsId" class="container">
        <div v-for="livrets in livretsId" :key="livrets.id" id="livret">
            <button class="link" v-on:click="this.redirect(livrets)">Livret {{livrets}}</button>
            <!-- <livretDetails :livretId="livretsId[0]" /> -->
        </div>
    </div>
</template>

<script>
// import livretDetails from '../components/livretDetails.vue'

export default {

    name: 'LivretPage',
    components: {
        // livretDetails
    },

    data() {
        return {
            livretsId: null
        }
    },

    mounted() {
        this.fetchLivrett();
        this.livretsId = [];
    },

    methods: {
        fetchLivrett: function () {
            this.$axiosApi.get("livrets/mine").then(s => {
                s.data.forEach((element) => this.livretsId.push(element.id))
            });
        },
        redirect: function(livretsId){
            this.$router.push({ path: `/Livret/${livretsId}`})
        }
    }
}
</script>

<style>
button.link {
    background: none;
    border: none;
}</style>