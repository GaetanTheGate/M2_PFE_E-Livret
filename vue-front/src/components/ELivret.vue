<template>
    <div class = "container">
        <table class="table table-striped">
            <t> test</t>
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
                result: {},
                elivrets : {
                    elivretid: '',
                    elivretstudent: '',
                    elivretmaster: '',
                    elivrettutor: ''
                }
            }
            
        },
        methods: {
            getLivret(){
                ELivretService.getLivret().then((response) =>{
                    this.elivrets = response.data;
                })
            },

            livretLoad(){
                var page = "http://localhost:8081/api/livrets/getAll";
                axios.get(page)
                 .then(
                    ({data})=>{
                        console.log(data);
                        this.result = data;
                    }
                 )
            }
        },
        created() {
            this.livretLoad()
        }
    }
</script>
