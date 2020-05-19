<template>
    <form v-on:submit.prevent="createSub" class="flex p-4">
        <label for="name" class="flex-initial m-2">Sub name</label>
        <input name="name" v-model="subName" class="flex-grow">
    </form>
</template>

<script lang="ts">
import { Component, Prop, Vue, Emit } from 'vue-property-decorator';

export interface UserUri { uri: string; }

@Component
export default class CreateSubscription extends Vue {

  private subName: string = '';

  @Emit('subscription-created')
  private createSub(): Promise<String> {
      return fetch(`/consumer-app/subscription/${this.subName}`, {
          method: 'PUT',
      }).then((response) => {
          console.log(response);
          return response.json();
      }).then((r) => r.id);
  }
}
</script>