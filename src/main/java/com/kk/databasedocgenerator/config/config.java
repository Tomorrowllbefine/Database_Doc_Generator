package com.kk.databasedocgenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
/**
 * @description: 接口文档配置类
 * @author: mmkk
 **/
@Configuration
public class config {
    @Bean
    public Docket docket() {
        // 创建一个 swagger 的 bean 实例
        return new Docket(DocumentationType.SWAGGER_2)
                // 配置基本信息
                .apiInfo(apiInfo())
                ;
    }

    /**
     * 基本信息设置
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 标题
                .title("数据库SQL文档生成器-接口文档")
                // 描述
                .description("课程《实用型软件设计》 数据库SQL文档生成器")
                // 服务条款链接
                .termsOfServiceUrl("https://www.baidu.com")
                // 许可证
                .license("swagger-的使用(详细教程)")
                // 许可证链接
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0.html")
                // 联系我
                .contact(new Contact(
                        "实用型软件设计——第二组",
                        "xxx@qq.com",
                        "xxx@qq.com"))
                // 版本
                .version("1.0")
                .build();
    }

}
