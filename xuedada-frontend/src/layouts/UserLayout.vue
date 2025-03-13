<template>
  <div id="userLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <a-space>
          <img src="../assets/logo.png" class="logo" />
          <div>雪答答 AI 答题应用平台</div>
        </a-space>
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
</template>

<style scoped>
#userLayout {
  text-align: center;
  background: url("https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png")
    0% 0% / 100% 100%;

  .logo {
    width: 48px;
    height: 48px;
  }
  .header {
    margin-top: 16px;
    font-size: 21px;
  }
  .content {
    margin-bottom: 16px;
    padding: 20px;
  }
  .footer {
    position: fixed;
    right: 0;
    bottom: 0;
    left: 0;
    padding: 16px;
    text-align: center;
  }
}
</style>
