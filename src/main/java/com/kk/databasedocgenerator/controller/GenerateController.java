package com.kk.databasedocgenerator.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.kk.databasedocgenerator.database.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.nutz.dao.impl.SimpleDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @description: 生成控制器
 * @author: mmkk
 **/
@RestController
public class GenerateController {

    @GetMapping("/generator")
    @ApiImplicitParams({@ApiImplicitParam(name = "dbType", value = "1: MySQLGenerator\n2: OracleGenerator\n3: PostgreSQLGenerator\n4: SQLServer", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "ip", value = "ip地址", defaultValue = "localhost", required = true),
            @ApiImplicitParam(name = "databaseName", value = "数据库名", required = true),
            @ApiImplicitParam(name = "port", value = "端口", defaultValue = "3306", required = true),
            @ApiImplicitParam(name = "userName", value = "用户名", defaultValue = "username", required = true),
            @ApiImplicitParam(name = "password", value = "密码", defaultValue = "root", required = true),
            @ApiImplicitParam(name = "serviceName", value = "Oracle数据库才需要", required = false),
    })
    @ApiOperation(value = "output", produces = "application/octet-stream")
    public String generatorDatabaseDoc(HttpServletResponse response, String dbType,
                                       @RequestParam(defaultValue = "localhost") String ip, String databaseName,
                                       @RequestParam(defaultValue = "5432") String port,
                                       @RequestParam(defaultValue = "postgres") String userName,
                                       @RequestParam(defaultValue = "root") String password,
                                       @RequestParam(required = false) String serviceName) {

        if ("c".equals(dbType)) {
            System.exit(-1);
        }
        if (Integer.valueOf(dbType) < 1 || Integer.valueOf(dbType) > 4) {
            System.out.println("wrong number,will exit");
            System.exit(-1);
        }
        if ("2".equals(dbType)) {
            System.out.println("input service name:");
        }
        if ("1".equals(dbType) || "3".equals(dbType) || "4".equals(dbType)) {
            System.out.println("input database name:");
        }

        // 拼接SQL的url
        SimpleDataSource dataSource = new SimpleDataSource();
        if ("1".equals(dbType)) {
            dataSource.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?characterEncoding=utf8&useSSL=false");
        } else if ("2".equals(dbType)) {
            dataSource.setJdbcUrl("jdbc:oracle:thin:@" + ip + ":" + port + ":" + serviceName);
        } else if ("3".equals(dbType)) {
            dataSource.setJdbcUrl("jdbc:postgresql://" + ip + ":" + port + "/" + databaseName);
        } else if ("4".equals(dbType)) {
            dataSource.setJdbcUrl("jdbc:sqlserver://" + ip + ":" + port + ";database=" + databaseName);
        }

        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        Generator generator = null;

        // 构造方法
        switch (dbType) {
            case "1":
                generator = new MySQLGenerator(databaseName, dataSource);
                break;
            case "2":
                generator = new OracleGenerator(userName, dataSource);
                break;
            case "3":
                generator = new PostgreSQLGenerator(databaseName, dataSource);
                break;
            case "4":
                generator = new SqlServerGenerator(databaseName, dataSource);
        }

        String savePath = generator.generateDoc();

        InputStream input = null;
        OutputStream out = null;
        try {
            // 本地文件夹打包
            ZipUtil.zip(savePath, savePath + ".zip");
            File file = new File(savePath + ".zip");

            // 1-设置响应头
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + URLEncoder.encode(file.getName(), "UTF-8"));
            // 2-读取文件--输入流
            input = new FileInputStream(file);
            // 3-写出文件--输出流
            out = response.getOutputStream();
            byte[] buff = new byte[1024];
            int index = 0;
            // 4-写文件
            while ((index = input.read(buff)) != -1) {
                out.write(buff, 0, index);
                out.flush();
            }
            FileUtil.del(new File(savePath).getParent());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                out.close();
                input.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "文档生成成功";
    }
}
