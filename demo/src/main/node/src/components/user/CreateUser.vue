<template>
    <form v-on:submit.prevent="createUser" class="flex">
        <label for="name" class="flex-initial m-2">User name</label>
        <input name="name" v-model="username" class="flex-grow">
    </form>
</template>

<script lang="ts">
import { Component, Prop, Vue, Emit } from 'vue-property-decorator';

export type UserUri = { uri:string };

@Component
export default class CrateUser extends Vue {
  @Prop() private msg!: string;

  private username: string = "";

  private createUser() {
      fetch('/producer-app/user/' + this.username, {
          method: 'POST'
      }).then(response => {
          console.log(response);
          let location = response.headers.get("Location");
          if (location == null) {
              console.error('User was not created, no location given', response);
          } else {
              // we'll drop the host part as we're proxying
              let userUri = location.replace(/http:\/\/[^\/]+/,'');    
              this.$emit('user-created', {uri: userUri});
          }
      });
  }
}
</script>