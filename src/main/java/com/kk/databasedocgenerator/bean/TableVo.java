package com.kk.databasedocgenerator.bean;

import java.util.List;

/**
 * @description: 表Vo对象
 * @author: mmkk
 **/
public class TableVo {
    private String table;
    private String comment;
    private List<ColumnVo> columns;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ColumnVo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnVo> columns) {
        this.columns = columns;
    }
}
