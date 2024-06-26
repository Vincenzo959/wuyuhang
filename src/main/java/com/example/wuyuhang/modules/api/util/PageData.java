package com.example.wuyuhang.modules.api.util;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "分页数据")
public class PageData<T> implements Serializable {

    @ApiModelProperty(value = "总记录数")
    private int total;
    @ApiModelProperty(value = "列表数据")
    private List<T> list;

    public PageData(List<T> list, long total) {
        this.list = list;
        this.total = (int) total;
    }
}