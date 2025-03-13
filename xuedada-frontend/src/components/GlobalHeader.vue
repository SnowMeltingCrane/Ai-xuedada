<template>
  <div id="globalHeader">
    <a-row class="grid-demo" align="center" :wrap="false">
      <a-col flex="auto">
        <a-menu
          mode="horizontal"
          :selected-keys="selectedKeys"
          @menu-item-click="doMenuClick"
        >
          <a-menu-item
            key="0"
            :style="{ padding: 0, marginRight: '38px' }"
            disabled
          >
            <div class="titleBar">
              <img class="logo" src="../assets/logo.png" alt="" />
              <div class="title">雪答答</div>
            </div>
          </a-menu-item>
          <a-menu-item v-for="item in visibleRoutes" :key="item.path">
            {{ item.name }}
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col flex="100px">
        <div v-if="loginUserStore.loginUser.id">
          {{ loginUserStore.loginUser.userName ?? "无名" }}
        </div>
        <div v-else>
          <a-button type="primary" href="/user/login">登录</a-button>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import checkAccess from "@/access/checkAccess";
import { routes } from "@/router/routes";
import { useLoginUserStore } from "@/store/userStore";
import { computed, ref } from "vue";
import { useRouter } from "vue-router";

const loginUserStore = useLoginUserStore();

const router = useRouter();
// 记录当前选中的菜单
const selectedKeys = ref(["/"]);
// 路由跳转时自动更新菜单项

const visibleRoutes = computed(() => {
  return routes.filter((item) => {
    // 从路由中查看是否需要再菜单中隐藏
    if (item.meta?.hideInMenu) {
      return false;
    }

    // 从路由中查看权限
    if (!checkAccess(loginUserStore.loginUser, item.meta?.access as string)) {
      return false;
    }
    return true;
  });
});

router.afterEach((to) => {
  selectedKeys.value = [to.path];
});

// 点击菜单跳转到对应页面
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};
</script>

<style scoped lang="scss">
#globalHeader {
  .grid-demo {
    margin-top: 16px;
    .titleBar {
      display: flex;
      align-items: center;
      .logo {
        height: 48px;
      }
      .title {
        color: black;
        margin-left: 16px;
      }
    }
  }
}
</style>
