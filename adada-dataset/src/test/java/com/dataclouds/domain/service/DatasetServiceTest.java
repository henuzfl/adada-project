package com.dataclouds.domain.service;

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

        /**
         * 新建一个数据集目录树
         */
        Long id = datasetService.create();

        datasetService.addDir(id, "/", "d3");
        datasetService.addDir(id, "/", "d2");
        datasetService.addDir(id, "/", "d1");
        datasetService.addFile(id, "/", "file1.csv");
        datasetService.upload(id, "/file1.csv", new FileInputStream(file));

        datasetService.addDir(id, "/d1/", "d11");
        datasetService.addFile(id, "/d1/", "file2.csv");
        datasetService.upload(id, "/d1/file2.csv", new FileInputStream(file));
        datasetService.addFile(id, "/d1/d11/", "file4.csv");
        datasetService.upload(id, "/d1/d11/file4.csv",
                new FileInputStream(file));

        datasetService.addFile(id, "/d2/", "file3.csv");
        datasetService.upload(id, "/d2/file3.csv", new FileInputStream(file));

        System.out.println(datasetService.list(id, "/"));
    }
}
