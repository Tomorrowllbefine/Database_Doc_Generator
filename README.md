# 数据库表结构文档生成器 | SpringBoot + Swagger

> 原项目地址 [database-doc-generator](https://github.com/enilu/database-doc-generator)
>
> 使用springboot对原项目进行改造

## 1. 功能介绍

根据数据库表DDL生成markdown文档，支持Mysql, postgresql, Oracle, SqlServer等数据库

## 2. 快速使用

idea打开项目, 运行项目后，在浏览器上打开swagger地址，填写数据库参数，执行generator接口

localhost:8888/doc.html


### 2.1 使用MySQL数据库生成文档示例



### 2.2 使用PostgreSQL数据库生成文档示例




### 2.3 使用gitbook在线查看数据文档

确保安装了gitbook后，进入上述文件目录的命令行窗口运行：gitbook serve

```bash
Live reload server started on port: 35729
Press CTRL+C to quit ...

info: 7 plugins are installed 
info: loading plugin "livereload"... OK 
info: loading plugin "highlight"... OK 
info: loading plugin "search"... OK 
info: loading plugin "lunr"... OK 
info: loading plugin "sharing"... OK 
info: loading plugin "fontsettings"... OK 
info: loading plugin "theme-default"... OK 
info: found 3 pages 
info: found 0 asset files 
info: >> generation finished with success in 0.9s ! 

Starting server ...
Serving book on http://localhost:4000
```

访问 http://localhost:4000 ，即可在线查看数据库文档

如果遇到 You already have a server listening on 35729 错误信息，使用以下命令解决

```
sudo lsof -i :35729
kill 查到的pid即可
```


### 2.4 查看生成的HTMl页面和word文档

