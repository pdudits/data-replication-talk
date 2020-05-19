<template>
    <div id="list" class="h-full">
        <div class="shadow-lg bg-white flex">
            <p class="flex-grow">
                <span class="text-grey-700">Prod id:</span>
                <span class="block text-xl">{{ lastId }}</span>
            </p>
            <p class="flex-grow text-orange-700" v-if="consumerInfo">
                <span class="text-grey-700">Last sync:</span>
                <span class="block text-xl">{{ lastSyncTime }}</span>
            </p>            
            <p class="flex-grow text-orange-700" v-if="consumerInfo">
                <span class="text-grey-700">Cons id:</span>
                <span class="block text-xl">{{ consumerInfo.lastId }}</span>
            </p>
        </div>
        <div class="overflow-auto h-full px-4" ref="scrollpane">
            <div v-for="event in events" :key="event.id" class="mt-4">
                <span class="w-10 inline-block">{{ event.id }}</span> <span class="text-green-800">{{event.eventType}}</span>
                <pre class="font-mono mt-0">{{ event.payload }}</pre>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator';

interface Event {
    id: string;
    eventType: string;
    payload: object;
}

interface ConsumerInfo {
    lastSync: string;
    lastId: number|null;
}

@Component
export default class ReplicationList extends Vue {
    public lastId = -1;
    public prevLast = -1;
    public events: Event[] = [];

    public intervalHandle: number = 0;

    public consumerInfo: ConsumerInfo|null = null;

    public mounted() {
        this.intervalHandle = setInterval(this.fetchNew.bind(this), 1000);
    }

    public fetchNew() {
        fetch(`/producer-app/replication?id=${this.lastId}&size=1`).then(
            (response) => response.json(),
        ).then((arr) => {
            if (Array.isArray(arr) && arr.length > 0) {
                this.prevLast = this.lastId;
                this.lastId = arr[arr.length - 1].id;
                this.events.push(...arr);
            }
        });
        fetch(`/consumer-app/replication/info`).then(
            (r) => {
                if (r.ok) {
                    r.json().then((info) => this.consumerInfo = info);
                }
            },
        );
    }

    public beforeDestroy() {
        clearInterval(this.intervalHandle);
    }

    public updated() {
        if (this.prevLast != this.lastId) {
            const scrollpane = this.$refs.scrollpane as Element;
            scrollpane.scrollTop = scrollpane.scrollHeight;
            this.prevLast = this.lastId;
        }
    }

    get lastSyncTime() {
        if (this.consumerInfo == null) {
            return null;
        } else {
            const time = Date.parse(this.consumerInfo.lastSync);
            const format = new Intl.DateTimeFormat('default', {
                hour12: false,
                hour: 'numeric', minute: 'numeric', second: 'numeric',
            });

            return format.format(time);
        }
    }
}
</script>
<style>
#list {
    display: grid;
    grid-template-rows: 5rem 1fr;
}
</style>