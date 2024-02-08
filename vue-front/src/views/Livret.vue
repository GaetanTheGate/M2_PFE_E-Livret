<template>
    <div id="livret">
        <livretDetails :livretId=id />
    </div>
</template>

<script>
import livretDetails from '../components/livretDetails.vue'

export default {

    name: 'LivretPage',
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
        console.log(this.id)
    }
}
</script>

<style></style>