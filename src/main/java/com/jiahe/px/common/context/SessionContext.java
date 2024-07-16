package com.jiahe.px.common.context;

import java.util.List;

public interface SessionContext {
    String getEntId();
    String getEntCode();
    String getEntName();
    String getUserId();
    String getUserCode();
    String getUserName();
    String getLocale();
    String getSession();
    String getToken();
    Object getExtend();

    List<SessionResource> getResources();
}
