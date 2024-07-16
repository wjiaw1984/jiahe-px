package com.jiahe.px.common.session.impl;

import com.jiahe.px.common.context.SessionContext;
import com.jiahe.px.common.context.impl.SessionContextImpl;
import com.jiahe.px.common.session.SessionContextBuilder;

public class DefaultSessionContextBuilder implements SessionContextBuilder {
    @Override
    public SessionContext build(String ent_id, String user_id, String user_code, String user_name, String locale, String session, String token, Object... exts) {
        SessionContextImpl sessionContext = new SessionContextImpl();
        sessionContext.setEntId(ent_id);
        sessionContext.setUserId(user_id);
        sessionContext.setUserName(user_name);
        sessionContext.setUserCode(user_code);
        sessionContext.setLocale(locale);
        sessionContext.setSession(session);
        sessionContext.setToken(token);

        return sessionContext;
    }
}