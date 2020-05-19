<template>
   <div>
    <p  v-for="sub in subscriptions" :key="sub" 
        class="p-4 border-b-2 cursor-pointer text-2xl text-orange-600" 
        v-on:click="select(sub)"
        v-bind:class="{ 'bg-white': sub == activeSub, 'hover:bg-orange-100': sub != activeSub}">
        {{ sub }}
    </p>
</div>
</template>
<script lang="ts">
import { Component, Prop, Vue, Watch, Emit } from 'vue-property-decorator';

export interface User {
    id: string;
    name: string;
    subscriptions: string[];
}

@Component
export default class SubscriptionList extends Vue {
    @Prop() public newSubscriptions!: string[];

    public activeSub: string = '';

    public initialList: string[] = [];

    public created() {
        fetch('/consumer-app/subscription')
            .then((r) => r.json())
            .then((r: Array<{id: string}>) => this.initialList = r.map((i) => i.id));
    }

    get subscriptions() {
        return this.initialList.concat(this.newSubscriptions);
    }

    @Emit('subscription-selected')
    public select(id: string) {
        if (this.activeSub == id) {
            this.activeSub = '';
        } else {
            this.activeSub = id;
        }
        console.log('selected', this.activeSub);
        return this.activeSub;
    }
}
</script>
