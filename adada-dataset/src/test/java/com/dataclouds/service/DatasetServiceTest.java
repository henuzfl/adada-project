package com.dataclouds.service;

import com.dataclouds.DatasetApplicationTest;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @Author: zfl
 * @Date: 2021/3/3 14:15
 * @Version: 1.0.0
 */
public class DatasetServiceTest extends DatasetApplicationTest {

    @Resource(name = "datasetServiceNoDb")
    private IDatasetService datasetService;

    private File file;

    @Before
    public void setUp() {
        ClassLoader classLoader = getClass().getClassLoader();
        file = new File(classLoader.getResource("wine.csv").getFile());
    }

    /**
     * /root
     * -----|d1
     * --------|d11
     * ------------|file4.csv
     * ---------|file2.csv
     * ------|d2
     * --------|file3.csv
     * ------|file1.csv
     */
    @Test
    public void testSave() throws FileNotFoundException {
        datasetService.addDir("/", "d3");
//        datasetService.addDir("/", "d2");
//        datasetService.addFile("/", "file1.csv");
//        datasetService.upload("/file1.csv", new FileInputStream(file));
//
//        datasetService.addDir("/d1/", "d11");
//        datasetService.addFile("/d1/", "file2.csv");
//        datasetService.upload("/d1/file2.csv", new FileInputStream(file));
//        datasetService.addFile("/d1/d11/", "file4.csv");
//        datasetService.upload("/d1/d11/file4.csv", new FileInputStream(file));
//
//        datasetService.addFile("/d2/", "file3.csv");
//        datasetService.upload("/d2/file3.csv", new FileInputStream(file));

        System.out.println(datasetService.list("/"));
    }
}
