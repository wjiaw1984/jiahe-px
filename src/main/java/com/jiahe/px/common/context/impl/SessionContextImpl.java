package com.jiahe.px.common.context.impl;

import com.jiahe.px.common.context.SessionContext;
import com.jiahe.px.common.context.SessionResource;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SessionContextImpl implements SessionContext {
    protected String entId;
    protected String entCode;
    protected String entName;
    protected String userId;
    protected String userCode;
    protected String userName;
    protected String locale;
    protected String session;
    protected String token;
    protected Object extend;

    /**
     * 权限资源
     */
    protected List<SessionResource> resources;

    public SessionContextImpl(){
        this.resources = new ArrayList<SessionResource>();
    }

    public SessionContextImpl(String entId, String entCode, String entName, String userId, String userCode, String userName, String locale){
        this.entId = entId;
        this.entCode = entCode;
        this.entName = entName;
        this.userId = userId;
        this.userCode = userCode;
        this.userName = userName;
        this.locale = locale;
        this.resources = new ArrayList<SessionResource>();
    }

    public SessionContextImpl(String entId, String entCode, String entName, String userId, String userCode, String userName, String session, String token, String locale){
        this.entId = entId;
        this.entCode = entCode;
        this.entName = entName;
        this.userId = userId;
        this.userCode = userCode;
        this.userName = userName;
        this.token = token;
        this.session = session;
        this.locale = locale;
        this.resources = new ArrayList<SessionResource>();
    }
}
