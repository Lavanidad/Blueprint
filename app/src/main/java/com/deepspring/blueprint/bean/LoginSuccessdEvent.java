package com.deepspring.blueprint.bean;


public class LoginSuccessdEvent {

    private Boolean isLogin;

    public LoginSuccessdEvent(Boolean isLogin) {//事件传递参数
        this.isLogin = isLogin;
    }

    public Boolean getMsg() {//取出事件参数
        return isLogin;
    }

    public void setMsg(Boolean isLogin) {
        this.isLogin = isLogin;
    }
}
