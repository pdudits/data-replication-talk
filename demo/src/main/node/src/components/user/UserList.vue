<template>
   <div>
    <div v-for="user in fullDataList" :key="user.id" 
        class="p-4 border-b-2 border-blue-300  cursor-pointer" 
        v-on:click="select(user)"
        v-bind:class="{ 'bg-white': user.id == activeUser, 'hover:bg-blue-100': user.id != activeUser}">
        <h3 class="text-2xl text-blue-600">{{ user.name }}</h3>
        <p class="text-sm text-grey-400">{{ user.id}}</p>
    </div>
</div>
</template>
<script lang="ts">
import { Component, Prop, Vue, Watch, Emit } from 'vue-property-decorator';
import { UserUri } from './CreateUser.vue';

export type User = {
    id: string,
    name: string,
    subscriptions: Array<string>,
}

@Component
export default class UserList extends Vue {
    @Prop() userList!: UserUri[];

    private userData: {[uri:string]:User} = {};

    fullDataList: Array<User> = [];

    activeUser: string = "";

    @Watch('userList')
    private onListChanged() {
        this.fullDataList = this.userList.map((u, index) => {
            if (this.userData[u.uri]) {
                return this.userData[u.uri]
            } else {
                return {
                    id: u.uri,
                    name: '<fetching>',
                    subscriptions: [],
                };
            }
        });
        this.fullDataList.forEach((u, index) => {
            if (u.name === '<fetching>') {
                fetch(u.id).then(response => response.json())
                .then(content => {
                    console.log('Fetched', content);
                    this.$set(this.fullDataList, index, content);
                })
                .catch(e => console.error(`Failed to fetch user $u.id`, e));
            }
        });
    }

    @Emit("user-selected")
    select(u:User) {
        if (this.activeUser == u.id) {
            this.activeUser = "";
        } else {
            this.activeUser = u.id;
        }
        console.log("selected", this.activeUser);
        return this.activeUser;
    }
}
</script>
