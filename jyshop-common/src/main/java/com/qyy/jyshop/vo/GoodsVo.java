package com.qyy.jyshop.vo;

import com.qyy.jyshop.model.Goods;

public class GoodsVo extends Goods {

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;
    private String brandName;
    private String companyName;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
