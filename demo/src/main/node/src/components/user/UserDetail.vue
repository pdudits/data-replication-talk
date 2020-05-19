<template>
    <div>
        <div v-if="user != null">
            Selected user:
            <dl>
                <dt>id</dt>
                <dd>{{ user.id }}</dd>
                <dt>name</dt>
                <dd>{{ user.name }}</dd>
                <dt>subscriptions</dt>
                <dd v-for="sub in user.subscriptions" v-bind:key="sub">
                    {{ sub }}
                    <a class="inline-block text-red-800 mx-2" v-on:click.prevent="deleteSub(sub)" href="#">delete</a>
                </dd>
            </dl>
            <form v-on:submit.prevent="addSubscription">
            <label for="add-sub">Add subscription</label>
            <input class="block" id="add-sub" v-model="subName">
            </form>
        </div>
        <div v-else>No user selected</div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Emit, Watch } from 'vue-property-decorator';
import { User } from './UserList.vue';
export interface UserUri { uri: string; }

@Component
export default class UserDetail extends Vue {
    @Prop() public userUri!: string;
    @Prop() public suggestedSub!: string;

    public user: User|null = null;

    public subName: string = '';

    @Watch('userUri')
    public onUserUriChanged() {
        if (this.userUri && this.userUri != '') {
            fetch(`/producer-app/user/${this.userUri}`)
                .then((r) => r.json())
                .then((u) => this.user = u );
        } else {
            this.user = null;
        }

    }

    @Watch('suggestedSub')
    public onSuggestedSubChanged() {
        this.subName = this.suggestedSub;
    }

    public addSubscription() {
        if (!this.userUri || this.user == null || this.subName == '') {
            return;
        }
        this.changeSub(this.subName, 'PUT')
            .then((s) => {
                this.subName = '';
            });
    }

    public deleteSub(subName: string) {
        this.changeSub(subName, 'DELETE');
    }

    public changeSub(subName: string, method: 'PUT'|'DELETE') {
        if (!this.userUri || this.user == null || subName == null || subName == '') {
            return Promise.reject(new Error('No active user or sub'));
        } else {
            return fetch(`/producer-app/user/${this.userUri}/subscriptions/${subName}`, {method})
                .then((r) => r.json())
                .then((s) => this.user!.subscriptions = s);
        }
    }
}
</script>