package com.wangshuos.common.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class Generator {
    public static void main(String[] args) {


        //FastAutoGenerator.create("jdbc:mysql://localhost:3306/itanylife?serverTimezone=UTC", "root", "12345678")
        //FastAutoGenerator.create("jdbc:postgresql://192.168.1.165:5432/document-management?currentSchema=public&useUnicode=true&characterEncoding=utf8&useSSL=true&autoReconnect=true&reWriteBatchedInserts=true", "postgres", "postgres")
        //FastAutoGenerator.create("jdbc:mysql://piscn.vicp.cc:52306/jn-laboratory-prod?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull&useSSL=false&allowMultiQueries=true", "root", "sgwl2015")
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/website?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "root", "12345678")
                .globalConfig(builder -> {
                    builder.author("wangshuo") // 设置作者
                            //.enableSwagger()//开启swagger
                            .enableSpringdoc()//开启SpringDoc
                            //.disableOpenDir()//不跳转文件夹目录
                            .dateType(DateType.TIME_PACK)//时间策略
                            .commentDate("yyyy/MM/dd HH:mm:ss")//注释日期
                            .outputDir(System.getProperty("user.dir")+"/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    //builder.parent("com.jssgwl.data.messages.documentation") // 设置父包名
                    builder.parent("com.wangshuos.websocket2") // 设置父包名
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .xml("mapper.xml")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir")+"/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("t_file") // 设置需要生成的表名
                            .addTablePrefix("t_") // 设置过滤表前缀
                            .controllerBuilder()
                            .enableRestStyle()//开启restful风格
                            .enableFileOverride()//覆盖旧文件
                            .entityBuilder()
                            .naming(NamingStrategy.underline_to_camel)// 数据库表映射到实体的命名策略,驼峰命名法
                            .columnNaming(NamingStrategy.underline_to_camel)//数据库表字段映射到实体的命名策略
                            .enableLombok()//开启 Lombok
                            .enableTableFieldAnnotation()//属性加上注解说明
                            .enableFileOverride()
                            .serviceBuilder()
                            .enableFileOverride()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .mapperBuilder()
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()//开启 @Mapper
                            .formatXmlFileName("%sMapper")
                            .enableFileOverride();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}

