<template>
  <div id="app" class="h-screen">
    <div id="user" class="bg-gray-200 p-4 m-2">User
      <CreateUser v-on:user-created="onUserCreated"/>
      <UserList v-bind:user-list = "userUris"/>
    </div>
    <div id="subscription" class="bg-gray-200 p-4 m-2">Subscription</div>
    <div id="replication" class="bg-gray-200 p-4 m-2">Replication result
      <ReplicationList/>
    </div>
    <div id="result" class="bg-gray-200 p-4 m-2">Result</div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import HelloWorld from './components/HelloWorld.vue';
import CreateUser , { UserUri } from './components/user/CreateUser.vue';
import UserList from './components/user/UserList.vue';
import ReplicationList from './components/replication/ReplicationList.vue';

@Component({
  components: {
    CreateUser,
    UserList,
    ReplicationList
  },
})
export default class App extends Vue {
  userUris: UserUri[] = [];

  onUserCreated(uri: UserUri) {
    this.userUris.push(uri);
  }

}
</script>

<style src="@/main.scss" lang="scss"></style>
<style>
#app {
  display:grid;
  grid-template-rows: auto auto [result] 6rem;
  grid-template-columns: auto auto auto;
}
#result {
  grid-row-start: result
}
</style>
