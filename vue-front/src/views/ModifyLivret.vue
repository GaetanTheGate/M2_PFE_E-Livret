<template>
    <div id="livret">
        <livretModify v-if="true" :livretId=id />
    </div>
</template>

<script>

import livretModify from '../components/modifyLivret/livretModify.vue';

export default {

    
    name: 'LivretModifyPage',
    components: {
        livretModify
    },

    props: {
        id: {
            type: Number,
            required: true
        }
    },
    data() {
        return {

        }
    },
    mounted() {
        
    }
}
</script>

<style></style>