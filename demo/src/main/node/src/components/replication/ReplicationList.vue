<template>
    <div>
        <p>Last seed id: {{ lastId }}
        <dl v-for="event in events" :key="event.id">
            <dt>{{ event.id }} <span>{{event.eventType}}</span></dt>
            <dd>{{ event.payload }}</dd>
        </dl>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator';

type Event = {
    id: string,
    eventType: string,
    payload: object
}

@Component
export default class ReplicationList extends Vue {
    lastId = -1
    events: Event[] = [];

    intervalHandle:number = 0;

    mounted() {
        this.intervalHandle = setInterval(this.fetchNew.bind(this), 1000);
    }

    fetchNew() {
        fetch(`/producer-app/replication?id=${this.lastId}&size=1`).then(
            response => response.json()
        ).then(arr => {
            if (Array.isArray(arr)) {
                this.lastId = arr[arr.length-1].id;
                this.events.push(...arr)
            }
        });
    }

    beforeDestroy() {
        clearInterval(this.intervalHandle);
    }
}
</script>