<template>
   <div>
    <dl v-for="user in fullDataList" :key="user.id">
        <dt>{{ user.name }}</dt>
        <dd>{{ user.id}}</dd>
    </dl>
</div>
</template>
<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import { UserUri } from './CreateUser.vue';

type User = {
    id: string,
    name: string,
    subscriptions: Array<string>,
}

@Component
export default class UserList extends Vue {
    @Prop() userList!: UserUri[];

    private userData: {[uri:string]:User} = {};

    fullDataList: Array<User> = [];

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
}
</script>
