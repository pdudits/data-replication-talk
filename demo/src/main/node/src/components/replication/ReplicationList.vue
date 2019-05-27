<template>
    <div class="overflow-y-auto max-h-full " ref="scrollpane">
        <p class="shadow-lg float-right m-2 p-2 bg-white absolute right-0 z-30"><span class="text-grey-700">Last seed id:</span>
         <span class="block text-xl">{{ lastId }}</span>
         </p>
        <div v-for="event in events" :key="event.id" class="px-4">
            <span class="w-10 inline-block">{{ event.id }}</span> <span class="text-green-800">{{event.eventType}}</span>
            <pre class="font-mono mt-0">{{ event.payload }}</pre>
        </div>
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
            if (Array.isArray(arr) && arr.length > 0) {
                this.lastId = arr[arr.length-1].id;
                this.events.push(...arr)
            }
        });
    }

    beforeDestroy() {
        clearInterval(this.intervalHandle);
    }

    updated() {
        let scrollpane = this.$refs.scrollpane as Element;
        scrollpane.scrollTop = scrollpane.scrollHeight;
    }
}
</script>