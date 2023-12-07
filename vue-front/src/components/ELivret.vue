<template>
    <div class = "container">
        <table class="table table-striped">
            <t> test</t>

            <tr v-for="livret in livrets" :key="livret.id">
                {{livret}}
            </tr>
            <thead>
                <th>ELivret Id</th>
                <th>ELivret Student</th>
                <th>ELivret Master</th>
                <th>ELivret Tutor</th>
            </thead>
        </table>
    </div>
</template>

<script>
import axios from 'axios';
import ELivretService from '../services/ELivretService'
    export default {
        name: 'ELivrets',
        data(){
            return {
                livrets:    null,
            }
            
        },

        mounted(){
            this.axios = axios.create({
                baseURL: 'http://localhost:8081/api/',
                timeout: 5000,
                headers: { 'Content-show': 'application/json' },
            });
            this.livretLoad();
        },

        methods: {
            getLivret(){
                ELivretService.getLivret().then((response) =>{
                    this.elivrets = response.data;
                })
            },

            livretLoad(){
                this.axios.get("livrets/getAll").then(list => {
                        console.log(list);
                        this.livrets = list.data;
                    }
                 )
            }
        }
    }
</script>
