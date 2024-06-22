package com.kk.databasedocgenerator.database;

import com.kk.databasedocgenerator.bean.ColumnVo;
import com.kk.databasedocgenerator.bean.TableVo;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.SimpleDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: DatabaseDocService
 * @author: mmkk
 **/
public class MySQLGenerator extends Generator {
    String sqlTables = "select table_name,table_comment from information_schema.tables where table_schema = '@dbname'" +
            " order by table_name asc";
    String sqlColumns = "select column_name,column_type,column_key,is_nullable,column_comment from information_schema" +
            ".columns where table_schema = '@dbname'  and table_name " +
            "='@tablename'";

    public MySQLGenerator(String dbName, SimpleDataSource dataSource) {
        super(dbName, dataSource);
    }

    /**
     * @description: 根据sqlTables执行查询获取指定数据库的所有表名及表注释
     * @param: []
     * @return: List<TableVo>
     * @author: mmkk
     **/
    @Override
    public List<TableVo> getTableData() {

        String sql = sqlTables.replace("@dbname", dbName);
        // 获取所有的表名及表注释--> record<表名，表注释>
        List<Record> list = getList(sql);
        List<TableVo> tables = new ArrayList<>();
        list.parallelStream().forEach(record -> {
            String table = record.getString("table_name");
            String comment = record.getString("table_comment");
            TableVo tableVo = getTableInfo(table, comment);
            tables.add(tableVo);
        });
        return tables;
    }

    /**
     * @description: 组件TableVo
     * @param: [table, comment]
     * @return: org.hackerandpainter.databasedocgenerator.bean.TableVo
     * @author: mmkk
     **/
    public TableVo getTableInfo(String table, String comment) {
        TableVo tableVo = new TableVo();
        tableVo.setTable(table);
        tableVo.setComment(comment);
        String sql = sqlColumns.replace("@dbname", dbName);
        sql = sql.replace("@tablename", table);
        List<Record> list = getList(sql);
        List<ColumnVo> columns = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Record record = list.get(i);
            ColumnVo column = new ColumnVo();
            column.setName(record.getString("column_name"));
            column.setType(record.getString("column_type"));
            column.setKey(record.getString("column_key"));
            column.setIsNullable(record.getString("is_nullable").equals("NO") ? "否" : "是");
            column.setComment(record.getString("column_comment"));
            columns.add(column);
        }
        tableVo.setColumns(columns);
        return tableVo;
    }

}