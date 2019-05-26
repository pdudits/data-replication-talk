<template>
  <div id="app" class="h-screen m-0 p-0">
    <div id="user" class="bg-gray-200">
      <CreateUser v-on:user-created="onUserCreated"/>
      <UserList v-bind:user-list = "userUris" v-on:user-selected="onUserSelected"/>
    </div>
    <div id="currentUser" class="bg-gray-200 p-4">
      <UserDetail v-bind:user-uri = "selectedUri"/>
    </div>
    <div id="replication" class="bg-gray-200 p-4 overflow-y-hidden relative">
      <ReplicationList/>
    </div>
    <div id="subscription" class="bg-orange-300 p-4">Subscription</div>
    <div id="result" class="bg-orange-300 bg-gray-200 p-4">Result</div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import HelloWorld from './components/HelloWorld.vue';
import CreateUser , { UserUri } from './components/user/CreateUser.vue';
import UserList from './components/user/UserList.vue';
import UserDetail from './components/user/UserDetail.vue';
import ReplicationList from './components/replication/ReplicationList.vue';


@Component({
  components: {
    CreateUser,
    UserList,
    ReplicationList,
    UserDetail
  },
})
export default class App extends Vue {
  userUris: UserUri[] = [];
  selectedUri: string = "";

  onUserCreated(uri: UserUri) {
    this.userUris.push(uri);
  }

  onUserSelected(uri: string) {
    this.selectedUri = uri;
  }

}
</script>

<style src="@/main.scss" lang="scss"></style>
<style>
#app {
  display:grid;
  grid-template-rows: auto [sub] auto [result] 6rem;
  grid-template-columns: auto [user] auto auto;
  grid-gap: 1rem;
}
#result {
  grid-row-start: result
}
#subscription {
  grid-row-start: sub;
  grid-column-start: user;
}
#user, #replication {
  grid-row-start: span 2;
}
</style>
