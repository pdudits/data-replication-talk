<template>
   <div>
    <div v-if="subscription">
        <h3>{{subscription.id}}</h3>
        <form v-on:submit.prevent="addContent" class="flex">
            <label for="name" class="flex-initial m-2">Content</label>
            <input name="name" v-model="content" class="flex-grow">
        </form>
        <ul>
            <li v-for="c in subscription.contents" :key="c" v-on:click="selectContent(c)"
                class="cursor-pointer"
                v-bind:class="{'font-bold': c == selectedContent}"
                >
                {{c}} <a class="inline-block text-red-800 mx-2 float-right" v-on:click.prevent="deleteContent(c)" href="#">delete</a>
            </li>
        </ul>
    </div>
    <div v-else>
        No subscription selected
    </div>
</div>
</template>
<script lang="ts">
import {Component, Emit, Prop, Vue, Watch} from 'vue-property-decorator';

interface Subscription {
  id: string;
  contents: string[];
}

@Component
export default class SubscriptionDetail extends Vue {
  @Prop() public subscriptionId!: string;

  public subscription: Subscription|null = null;

    public content: string = '';

    public selectedContent: string = '';

    @Watch('subscriptionId')
    public onSubscriptionIdChanged() {
        console.log('Fetching', this.subscriptionId);
        if (this.subscriptionId == null || this.subscriptionId == '') {
            this.subscription = null;
        } else {
            fetch(`/consumer-app/subscription/${this.subscriptionId}`)
                .then((r) => r.json())
                .then((r) => this.subscription = r);
        }
    }

    public addContent() {
        fetch(`/consumer-app/subscription/${this.subscriptionId}/${this.content}`, {method: 'PUT'})
            .then((r) => r.json())
            .then((r) => this.subscription = r);
    }

    public deleteContent(c: string) {
        fetch(`/consumer-app/subscription/${this.subscriptionId}/${c}`, {method: 'DELETE'})
            .then((r) => r.json())
            .then((r) => this.subscription = r);
    }

    @Emit('content-selected')
    public selectContent(c: string) {
      if (c == this.selectedContent) {
        this.selectedContent = '';
      } else {
        this.selectedContent = c;
      }
      return this.selectedContent;
    }

}
</script>
<style>
li {
  list-style: none;
}

ul {
  padding: 0;
}
</style>