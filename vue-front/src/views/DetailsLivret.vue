<template>
    <div id="livret">
        <livretDetails :livretId=id />
    </div>
</template>

<script>
import livretDetails from '../components/detailsLivret/livretDetails.vue'

export default {

    name: 'DetailsLivretPage',
    components: {
        livretDetails
    },

    props: {
        id: {
            type: Number,
            required: true
        }
    },

    mounted() {

    }
}
</script>

<style></style>