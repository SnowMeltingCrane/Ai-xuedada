import ACCESS_ENUM from "./accessEnum";

const checkAccess = (
  loginUser: API.LoginUserVO,
  needAccess: string = ACCESS_ENUM.NOT_LOGIN
) => {
  // 获取当前用户角色 如果没有loginUser 则默认为未登录
  const loginUserAccess = loginUser?.userRole ?? ACCESS_ENUM.NOT_LOGIN;
  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    return true;
  }
  // 如果用户需要登录才能访问
  if (needAccess === ACCESS_ENUM.USER) {
    // 如果用户没有登录
    if (loginUserAccess === ACCESS_ENUM.NOT_LOGIN) {
      return false;
    }
  }
  // 如果需要管理员才能登录
  if (needAccess === ACCESS_ENUM.ADMIN) {
    if (loginUserAccess !== ACCESS_ENUM.ADMIN) {
      return false;
    }
  }
  return true;
};

export default checkAccess;
