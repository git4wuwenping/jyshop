package com.qyy.jyshop.vo;



import java.util.List;

import com.qyy.jyshop.model.Spec;
import com.qyy.jyshop.model.SpecValues;

public class SpecVo extends Spec{

    private List<SpecValues> specValuesList;

    public List<SpecValues> getSpecValuesList() {
        return specValuesList;
    }

    public void setSpecValuesList(List<SpecValues> specValuesList) {
        this.specValuesList = specValuesList;
    }
}
