package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.util.Config;

import java.io.File;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * 测试索引构建
 */
public class TestBuildIndex {
    /**
     *  索引构建程序入口
     * @param args : 命令行参数
     */
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        int opt = scan.nextInt();
        AbstractIndex index;
        switch (opt){
            case 1:
                AbstractIndexBuilder indexBuilder = new IndexBuilder(new DocumentBuilder());
                index = indexBuilder.buildIndex(Config.DOC_DIR);
                System.out.println(index.toString());
                break;
            case 0:
                index = new Index();
                index.load(new File(Config.INDEX_DIR + "index.dat"));
                System.out.println(index.toString());
                break;
        }
    }
}
