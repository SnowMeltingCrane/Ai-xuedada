<template>
  <div id="basicLayout">
    <div class="layout-demo">
      <a-layout style="height: 100vh">
        <a-layout-header class="header">
          <GlobalHeader />
        </a-layout-header>
        <a-layout-content class="content">
          <router-view />
        </a-layout-content>
        <a-layout-footer class="footer">
          <a href="https://github.com/SnowMeltingCrane" target="_blank">
            雪融鹤的github主页
          </a>
        </a-layout-footer>
      </a-layout>
    </div>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";
</script>

<style scoped>
#basicLayout {
  .layout-demo {
    .header {
      margin-bottom: 16px;
      box-shadow: #eee 1px 1px 5px;
    }
    .content {
      background: linear-gradient(to right, #fefefe, #fff);
      margin-bottom: 28px;
      padding: 20px;
    }
    .footer {
      background: #efefef;
      padding: 16px;
      text-align: center;
    }
  }
}
</style>
