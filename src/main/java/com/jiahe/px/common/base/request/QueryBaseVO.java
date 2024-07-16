package com.jiahe.px.common.base.request;

import lombok.Data;

/**
 * Created on 2019/11/13
 * 基础请求查询类
 * @author wjw
 * @since 1.0
 */
@Data
public class QueryBaseVO extends RequestBaseVO{
    private Integer page_no;
    private Integer page_size;

    /*public Integer getPage_no1(){
        if (page_no == null || page_no == 0) {
            return 1;
        }else{
            return page_no;
        }
    }


    public Integer getPage_size1(){
        if (page_size == null || page_size == 0) {
            return 20;
        }else{
            return page_size;
        }
    }*/


    public Integer getPage_no() {
        if (page_no == null || page_no == 0) {
            return 1;
        }else{
            return page_no;
        }
    }

    public Integer getPage_size() {
        if (page_size == null || page_size == 0) {
            return 20;
        }else{
            return page_size;
        }
    }

    /*public void setPage_no(Integer page_no) {
        this.page_no = page_no;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }*/
}
