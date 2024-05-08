package com.scp.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class WordTest {
    public static void genWord(){
        try {
            Map<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("name", "张三111");
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            //.xml 模板文件所在目录
            configuration.setDirectoryForTemplateLoading(new File("D:\\template"));
            // 输出文档路径及名称
            File outFile = new File("D:\\template\\test.doc");
            //以utf-8的编码读取模板文件
            Template t =  configuration.getTemplate("test.xml","utf-8");
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"),10240);
            t.process(dataMap, out);
            out.close();
            System.out.println("生成成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("生成失败");
        }
    }

    public static void main(String[] args) {
        genWord();
    }
}
