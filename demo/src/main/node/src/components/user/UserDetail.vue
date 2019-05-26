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
export type UserUri = { uri:string };

@Component
export default class UserDetail extends Vue {
    @Prop() userUri!:string;

    user: User|null = null;

    subName:string = ""

    @Watch("userUri")
    onUserUriChanged() {
        if (this.userUri && this.userUri != "") {
            fetch(`/producer-app/user/${this.userUri}`)
                .then(r => r.json())
                .then(u => this.user = u );
        } else {
            this.user = null;
        }

    }

    addSubscription() {
        if (!this.userUri || this.user == null || this.subName == "") {
            return;
        }
        this.changeSub(this.subName, "PUT")
            .then(s => {
                this.subName = "";
            });
    }

    deleteSub(subName:string) {
        this.changeSub(subName, "DELETE");
    }

    changeSub(subName:string, method: "PUT"|"DELETE") {
        if (!this.userUri || this.user == null || subName == null || subName == "") {
            return Promise.reject(new Error("No active user or sub"));
        } else {
            return fetch(`/producer-app/user/${this.userUri}/subscriptions/${subName}`, {method})
                .then(r => r.json())
                .then(s => this.user!.subscriptions = s)
        }
    }
}
</script>