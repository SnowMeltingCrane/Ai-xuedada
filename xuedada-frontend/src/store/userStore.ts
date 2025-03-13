import ACCESS_ENUM from "@/access/accessEnum";
import { getLoginUserUsingGet } from "@/api/userController";
import { defineStore } from "pinia";
import { ref } from "vue";

/**
 * 登陆用户信息全局状态
 */

export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<API.LoginUserVO>({
    userName: "未登录",
  });
  function setLoginUser(newLoginUser: API.LoginUserVO) {
    loginUser.value = newLoginUser;
  }

  async function fetchLoginUser() {
    const res = await getLoginUserUsingGet();
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data;
    } else {
      loginUser.value = {
        userRole: ACCESS_ENUM.NOT_LOGIN,
      };
    }
  }

  return { loginUser, setLoginUser, fetchLoginUser };
});
