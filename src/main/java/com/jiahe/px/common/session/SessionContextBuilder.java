package com.jiahe.px.common.session;


import com.jiahe.px.common.context.SessionContext;

/**
 * Created by wzm on 2019/2/21.
 */
public interface SessionContextBuilder {
    SessionContext build(String ent_id, String user_id, String user_code, String user_name, String locale, String session, String token, Object ... exts);
}
