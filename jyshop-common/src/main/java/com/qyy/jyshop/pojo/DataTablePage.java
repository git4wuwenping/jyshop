package com.qyy.jyshop.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * DataTable封装实体类
 * @author chk
 * 2018年1月9日 16:30:30
 */
public class DataTablePage<T> implements Serializable {

    private Integer draw;
    private List<T> aaData; //aaData 与datatales 加载的“dataSrc"对应
    private Long iTotalDisplayRecords;
    private Long iTotalRecords;

    public DataTablePage() {

    }

    public DataTablePage(Integer draw, List<T> aaData, Long iTotalDisplayRecords, Long iTotalRecords) {
        this.draw = draw;
        this.aaData = aaData;
        this.iTotalDisplayRecords = iTotalDisplayRecords;
        this.iTotalRecords = iTotalRecords;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public List<T> getAaData() {
        return aaData;
    }

    public void setAaData(List<T> aaData) {
        this.aaData = aaData;
    }

    public Long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(Long iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public Long getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(Long iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }
}
