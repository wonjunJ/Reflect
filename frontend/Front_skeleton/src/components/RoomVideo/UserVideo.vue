<template>
    <div class = "myVideo" v-if="streamManager">
        <div ><p>{{ clientData }}</p></div>

        <ov-video :stream-manager="streamManager"/>
       
    </div>
</template>
    
<script>
    import OvVideo from './OvVideo.vue';
    
    export default {
        name: 'UserVideo',
    
        components: {
            OvVideo,
        },
    
        props: {
            streamManager: Object,
        },
    
        computed: {
            clientData () {
                const { clientData } = this.getConnectionData();
                return clientData;
            },
        },
    
        methods: {
            getConnectionData () {
                const { connection } = this.streamManager.stream;
                console.log(connection.session.sessionId);
                return JSON.parse(connection.data);
            },
        },
    };
</script>

<style >
/* .myVideo{
    margin:50px;
   width: 500px;
    background-color: #AAD9BB;
} */
</style>