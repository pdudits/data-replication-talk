<template>
  <div id="app" class="m-0 p-0">
    <div id="user" class="bg-gray-200">
      <CreateUser v-on:user-created="onUserCreated"/>
      <UserList v-bind:user-list = "userUris" v-on:user-selected="onUserSelected"/>
    </div>
    <div id="currentUser" class="bg-gray-200 p-4">
      <UserDetail v-bind:user-uri = "selectedUri" v-bind:suggested-sub = "selectedSub"/>
    </div>
    <div id="replication" class="bg-gray-200 overflow-y-hidden">
      <ReplicationList/>
    </div>
    <div id="subscriptions" class="bg-orange-300 p-4">
      <CreateSubscription v-on:subscription-created="onSubCreated"/>
      <SubscriptionList v-bind:new-subscriptions="createdSubs" v-on:subscription-selected="onSubSelected"/>
    </div>
    <div id="currentSubscripion" class="bg-orange-200 p-4">
      <SubscriptionDetail v-bind:subscription-id="selectedSub" v-on:content-selected="onContentSelected"/>
    </div>
    <div id="selectedUser">Selected User<p>{{selectedUri}}</p></div>
    <div id="selectedContent">Selected Content<p>{{selectedContent}}</p></div>
    <div id="result" class="p-4" v-bind:class="resultClass">{{authStatus}}</div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import HelloWorld from './components/HelloWorld.vue';
import CreateUser , { UserUri } from './components/user/CreateUser.vue';
import UserList from './components/user/UserList.vue';
import UserDetail from './components/user/UserDetail.vue';
import ReplicationList from './components/replication/ReplicationList.vue';
import CreateSubscription from './components/content/CreateSubscription.vue';
import SubscriptionList from './components/content/SubscriptionList.vue';
import SubscriptionDetail from './components/content/SubscriptionDetail.vue';


@Component({
  components: {
    CreateUser,
    UserList,
    ReplicationList,
    UserDetail,
    CreateSubscription,
    SubscriptionList,
    SubscriptionDetail
  },
})
export default class App extends Vue {
  userUris: UserUri[] = [];
  selectedUri: string = "";

  createdSubs: string[] = [];

  selectedSub: string = "";

  selectedContent: string = "";

  contentChecked = false;
  contentAuthorized = false;
  authStatus = "";
  checkInterval:number = 0;

  created() {
    // Oh, I should have used VueX. Random backend code all over the place
    fetch('/producer-app/user')
      .then(r => r.json())
      .then(r => this.userUris = r.map(u => {
        return {uri: `/producer-app/user/${u.id}`};}));
  }

  onUserCreated(uri: UserUri) {
    this.userUris.push(uri);
  }

  onUserSelected(uri: string) {
    this.selectedUri = uri;
    this.checkAuth();
  }

  onSubCreated(id: string) {
    this.createdSubs.push(id);
  }

  onSubSelected(id: string) {
    console.log("Propagating sub", id)
    this.selectedSub = id;
  }

  onContentSelected(id: string) {
    this.selectedContent = id;
    this.checkAuth();
  }

  checkAuth() {
    if (this.selectedUri != "" && this.selectedContent != "") {
      fetch(`/consumer-app/content/${this.selectedContent}`, {
        credentials: 'include',
        headers: {
          'Authorization': this.selectedUri
        }
      }).then(r => {
        this.contentChecked = true;
        this.authStatus = `${r.status} ${r.statusText}`
        this.contentAuthorized = r.ok;
        this.scheduleCheck()
      });
    } else {
      this.contentChecked = false;
      this.authStatus = "";
      this.unscheduleCheck();
    }
  }

  scheduleCheck() {
    if (!this.checkInterval) {
      this.checkInterval = setInterval(this.checkAuth.bind(this), 1000);
    }
  }

  unscheduleCheck() {
    if (this.checkInterval) {
      clearInterval(this.checkInterval);
      this.checkInterval = 0;
    }
  }

  get resultClass() {
    return {
      'bg-green-300': this.contentChecked && this.contentAuthorized,
      'bg-red-600': this.contentChecked && !this.contentAuthorized,
      'bg-orange-100': !this.contentChecked,
    }
  }

}
</script>

<style src="@/main.scss" lang="scss"></style>
<style>
#app {
  display:grid;
  grid-template-rows: 1fr [detail] 1fr [result] 6rem;
  grid-template-columns: 1fr [sub] 1fr 1fr;
  grid-gap: 1rem;
  height: 98vh;
}
#result, #selectedUser, #selectedContent {
  grid-row-start: result;
}
#currentUser {
  grid-row-start: detail;
}
#subscriptions {
  grid-row-start: 1;
  grid-column-start: sub;
}
#currentSubscription {
  grid-row-start:detail;
}
#replication {
  grid-row-start: span 2;
}
</style>
