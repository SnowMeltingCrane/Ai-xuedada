<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <UserLayout />
    </template>
    <BasicLayout></BasicLayout>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from "vue-router";
import BasicLayout from "./layouts/BasicLayout.vue";
import UserLayout from "./layouts/UserLayout.vue";
import { onMounted } from "vue";

const doInit = () => {
  console.log("hello 欢迎来到我的项目");
};

onMounted(() => {
  doInit();
});

const route = useRoute();
</script>
<style scoped></style>
