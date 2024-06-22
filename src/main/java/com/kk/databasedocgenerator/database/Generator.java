package com.kk.databasedocgenerator.database;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.kk.databasedocgenerator.bean.ColumnVo;
import com.kk.databasedocgenerator.bean.TableVo;
import com.kk.databasedocgenerator.doc.WordGenerator;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;

import java.io.File;
import java.util.List;

/**
 * @description: Generator抽象类
 * @author: mmkk
 **/
public abstract class Generator {
    private static String savePath = "";
    private SimpleDataSource dataSource; // 数据库连接
    protected Dao dao = null; // 数据库操作接口
    protected String dbName; // 数据库名
    protected String docPath; // 最终生成的文件夹名字

    public Generator(String dbName, SimpleDataSource dataSource) {
        this.dataSource = dataSource;
        dao = new NutDao(dataSource);
        this.dbName = dbName;
        this.docPath = dbName + "-doc";
    }

    public abstract List<TableVo> getTableData();


    /**
     * @description: 获取表结构数据
     * @param: []
     * @return: String 文件存储地址
     * @author: mmkk
     **/
    public String generateDoc() {
        String home = System.getProperty("user.home");
        savePath = home + "/" + UUID.fastUUID() + "/" + docPath;
        File docDir = new File(savePath);
        if (docDir.exists()) {
            FileUtil.clean(docDir);
        } else {
            docDir.mkdirs();
        }
        List<TableVo> list = getTableData();
        save2File(list, savePath);
        //保存word
        WordGenerator.createDoc(dbName, list, savePath);
        return savePath;
    }

    /**
     * @description: 输出文件
     * @param: [tables, savePath]
     * @return: void
     * @author: mmkk
     **/
    public void save2File(List<TableVo> tables, String savePath) {
        saveSummary(tables, savePath);
        saveReadme(tables, savePath);
        saveMerge(tables, savePath);
        // tables.parallelStream().forEach(tableVo -> saveTableFile(tableVo, savePath));
        for (int i = 0; i < tables.size(); i++) {
            saveTableFile(tables.get(i), savePath, i);
        }
    }

    /**
     * @description: 输出Summary.md文档
     * @param: [tables, savePath]
     * @return: void
     * @author: mmkk
     **/
    private void saveSummary(List<TableVo> tables, String savePath) {
        // 拼接md语法
        StringBuilder builder = new StringBuilder("# Summary").append("\r\n").append("* [Introduction](README.md)")
                .append("\r\n");
        for (TableVo tableVo : tables) {
            String name = Strings.isEmpty(tableVo.getComment()) ? tableVo.getTable() : tableVo.getComment();
            builder.append("* [" + name + "](" + tableVo.getTable() + ".md)").append("\r\n");
        }
        try {
            Files.write(new File(savePath + File.separator + "SUMMARY.md"), builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 输出README.md文档
     * @param: [tables, savePath]
     * @return: void
     * @author: mmkk
     **/
    private void saveReadme(List<TableVo> tables, String savePath) {
        StringBuilder builder = new StringBuilder("# " + dbName + "数据库文档").append("\r\n");
        for (TableVo tableVo : tables) {
            builder.append("- [" + (Strings.isEmpty(tableVo.getComment()) ? tableVo.getTable() : tableVo.getComment())
                    + "]" +
                    "(" + tableVo
                    .getTable() + ".md)")
                    .append
                            ("\r\n");
        }
        try {
            Files.write(new File(savePath + File
                    .separator + "README.md"), builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 输出所有表的md文档
     * @param: [tables, savePath]
     * @return: void
     * @author: mmkk
     **/
    private void saveMerge(List<TableVo> tables, String savePath) {
        StringBuffer builder = new StringBuffer("# ").append(dbName).append("数据库设计文档").append("\r\n")
                .append("\r\n");

        for (TableVo tableVo : tables) {
            builder.append("#### " + (Strings.isBlank(tableVo.getComment()) ? tableVo.getTable() : tableVo
                    .getComment()) + "(" + tableVo.getTable() + ")").append("\r\n");
            builder.append("| 列名   | 类型   | KEY  | 可否为空 | 注释   |").append("\r\n");
            builder.append("| ---- | ---- | ---- | ---- | ---- |").append("\r\n");

            // 构造列
            List<ColumnVo> columnVos = tableVo.getColumns();
            for (int i = 0; i < columnVos.size(); i++) {
                ColumnVo column = columnVos.get(i);
                builder.append("|").append(column.getName())
                        .append("|").append(column.getType())
                        .append("|").append(Strings.sNull(column.getKey()))
                        .append("|").append(column.getIsNullable())
                        .append("|").append(column.getComment())
                        .append("|\r\n");
            }
        }
        try {
            Files.write(new File(savePath + File.separator + dbName + ".md"), builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 输出单个表结构的md文档
     * @param: [table, savePath]
     * @return: void
     * @author: mmkk
     **/
    private void saveTableFile(TableVo table, String savePath, int idx) {
        StringBuilder builder = new StringBuilder("# " + (Strings.isBlank(table.getComment()) ? table.getTable() : table
                .getComment()) + "(" + table.getTable() + ")").append("\r\n");
        builder.append("| 列名   | 类型   | KEY  | 可否为空 | 注释   |").append("\r\n");
        builder.append("| ---- | ---- | ---- | ---- | ---- |").append("\r\n");
        List<ColumnVo> columnVos = table.getColumns();
        for (int i = 0; i < columnVos.size(); i++) {
            ColumnVo column = columnVos.get(i);
            builder.append("|").append(column.getName()).append("|").append(column.getType()).append("|").append
                    (Strings.sNull(column.getKey())).append("|").append(column.getIsNullable()).append("|").append
                    (column.getComment()).append("|\r\n");
        }
        try {
            Files.write(new File(savePath + File
                    .separator  + (idx+1) + "_" +  table.getTable() + ".md"), builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 执行sql语句
     * @param: [sqlStr]
     * @return: java.util.List<org.nutz.dao.entity.Record>
     * @author: mmkk
     **/
    public List<Record> getList(String sqlStr) {
        Sql sql = Sqls.create(sqlStr);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        List<Record> list = sql.getList(Record.class);
        return list;
    }
}
