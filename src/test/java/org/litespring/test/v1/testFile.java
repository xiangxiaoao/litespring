package org.litespring.test.v1;

import org.junit.Test;
import org.litespring.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class testFile {

    @Test
    public void testFilePath() {
        String path = Class.class.getClass().getResource("/").getPath();
        String userPath = System.getProperty("user.dir");

        //URL url = Thread.currentThread().getContextClassLoader().getResource("petstore-v1.xml");
        URL url = ClassUtils.getDefaultClassLoader().getResource("petstore-v1.xml");
        System.out.println(path);
        System.out.println(userPath);
        System.out.println(url.getPath());
    }

    @Test
    public void testFilePathOne() throws IOException {
        File directory = new File("");//设定为当前文件夹
        System.out.println(directory.getCanonicalFile());//返回类型为File
        System.out.println(directory.getCanonicalPath());//获取标准的路径  ，返回类型为String
        System.out.println(directory.getAbsoluteFile());//返回类型为File
        System.out.println(directory.getAbsolutePath());//获取绝对路径，返回类型为String
    }

    @Test
    public void testFilePathTwo() throws IOException {
        File directory = new File(".");//设定为当前文件夹
        System.out.println(directory.getCanonicalFile());//返回类型为File
        System.out.println(directory.getCanonicalPath());//获取标准的路径  ，返回类型为String
        System.out.println(directory.getAbsoluteFile());//返回类型为File
        System.out.println(directory.getAbsolutePath());//获取绝对路径，返回类型为String
    }

    @Test
    public void testFilePathThree() throws IOException {
        File directory = new File("..");//设定为当前文件夹
        System.out.println(directory.getCanonicalFile());//返回类型为File
        System.out.println(directory.getCanonicalPath());//获取标准的路径  ，返回类型为String
        System.out.println(directory.getAbsoluteFile());//返回类型为File
        System.out.println(directory.getAbsolutePath());//获取绝对路径，返回类型为String
    }
}
