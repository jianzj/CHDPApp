package com.chdp.chdpapp.bean;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String usercode;
    private String name;
    private String password;
    private int authority;
    @SuppressWarnings("unused")
    private String authority_str;
    private String session_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getAuthority_str() {
        String str = "";
        if ((authority & 1024) > 0) {
            str += "管理员 ";
        }
        if ((authority & 512) > 0) {
            str += "接方 ";
        }
        if ((authority & 256) > 0) {
            str += "审方 ";
        }
        if ((authority & 128) > 0) {
            str += "调配 ";
        }
        if ((authority & 64) > 0) {
            str += "调配审核 ";
        }
        if ((authority & 32) > 0) {
            str += "浸泡 ";
        }
        if ((authority & 16) > 0) {
            str += "煎煮 ";
        }
        if ((authority & 8) > 0) {
            str += "灌装 ";
        }
        if ((authority & 4) > 0) {
            str += "清场 ";
        }
        if ((authority & 2) > 0) {
            str += "包装 ";
        }
        if ((authority & 1) > 0) {
            str += "运输 ";
        }
        return str;
    }

    public List<UserAuthority> getAuthorityList() {
        List<UserAuthority> auths = new ArrayList<UserAuthority>(11);
        if ((this.getAuthority() & 1024) > 0) {
            auths.add(UserAuthority.ADMIN);
        }
        if ((this.getAuthority() & 512) > 0) {
            auths.add(UserAuthority.RECEIVE);
        }
        if ((this.getAuthority() & 256) > 0) {
            auths.add(UserAuthority.CHECK);
        }
        if ((this.getAuthority() & 128) > 0) {
            auths.add(UserAuthority.MIX);
        }
        if ((this.getAuthority() & 64) > 0) {
            auths.add(UserAuthority.MIXCHECK);
        }
        if ((this.getAuthority() & 32) > 0) {
            auths.add(UserAuthority.SOAK);
        }
        if ((this.getAuthority() & 16) > 0) {
            auths.add(UserAuthority.DECOCT);
        }
        if ((this.getAuthority() & 8) > 0) {
            auths.add(UserAuthority.POUR);
        }
        if ((this.getAuthority() & 4) > 0) {
            auths.add(UserAuthority.CLEAN);
        }
        if ((this.getAuthority() & 2) > 0) {
            auths.add(UserAuthority.PACKAGE);
        }
        if ((this.getAuthority() & 1) > 0) {
            auths.add(UserAuthority.SHIP);
        }
        return auths;
    }
}
