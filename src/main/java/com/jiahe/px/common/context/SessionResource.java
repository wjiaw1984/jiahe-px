package com.jiahe.px.common.context;

import lombok.Data;

import java.util.Set;

@Data
public class SessionResource {
    /**
     * 资源类型
     */
    private String type;
    /**
     * 资源代码
     */
    private Set<String> codes;
}
