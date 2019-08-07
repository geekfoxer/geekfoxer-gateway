package com.geekfoxer.gateway.ops.resp;


import lombok.Data;

import java.util.List;

/**
 * @author bystander
 * @date 2018/9/15
 */
@Data
public class PageResult<T> {

    private Long total;
    private Integer totalPage;
    private List<T> data;

    public PageResult(Long total, List<T> data) {
        this.total = total;
        this.data = data;
    }

    public PageResult(Long total, Integer totalPage, List<T> data) {
        this.total = total;
        this.totalPage = totalPage;
        this.data = data;
    }


}
