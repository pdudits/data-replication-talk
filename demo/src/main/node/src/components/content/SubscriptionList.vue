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

export type User = {
    id: string,
    name: string,
    subscriptions: Array<string>,
}

@Component
export default class SubscriptionList extends Vue {
    @Prop() newSubscriptions!: string[];

    activeSub: string = "";

    initialList: string[] = [];

    created() {
        fetch('/consumer-app/subscription')
            .then(r => r.json())
            .then((r:{id:string}[]) => this.initialList = r.map(i => i.id))
    }

    get subscriptions() {
        return this.initialList.concat(this.newSubscriptions);
    }

    @Emit("subscription-selected")
    select(id:string) {
        if (this.activeSub == id) {
            this.activeSub = "";
        } else {
            this.activeSub = id;
        }
        console.log("selected", this.activeSub);
        return this.activeSub;
    }
}
</script>
