package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexSearcher extends AbstractIndexSearcher {
    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     *
     * @param indexFile ：指定索引文件
     */
    @Override
    public void open(String indexFile) {
        super.index.load(new File(indexFile));
        super.index.optimize();     // 对各种数据排序
    }

    /**
     * 根据单个检索词进行搜索
     *
     * @param queryTerm ：检索词
     * @param sorter    ：排序器
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        AbstractPostingList postList = index.search(queryTerm);
        List<AbstractHit> hitArray = new ArrayList<AbstractHit>();
        for(int i=0;i<postList.size();i++){     // 遍历查询单词的倒排索引表
            AbstractPosting post = postList.get(i);
            String path = index.getDocName(post.getDocId());
            Map<AbstractTerm, AbstractPosting> mp =
                    new HashMap<AbstractTerm, AbstractPosting>();
            mp.put(queryTerm, post);
            hitArray.add(new Hit(post.getDocId(), path, mp));
        }
        new SimpleSorter().sort(hitArray);
        return (AbstractHit[]) hitArray.toArray();
    }

    /**
     * 根据二个检索词进行搜索
     *
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter     ：    排序器
     * @param combine    ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        AbstractPostingList postList1 = index.search(queryTerm1);
        AbstractPostingList postList2 = index.search(queryTerm2);
        List<AbstractHit> hitArray = new ArrayList<AbstractHit>();
        int i=0, j=0;
        while(i < postList1.size() && j < postList2.size()) {
            AbstractPosting post1 = postList1.get(i);
            AbstractPosting post2 = postList1.get(j);
            // 这里默认索引中的数据都是按文档ID从小到大排序了
            if(post1.getDocId() == post2.getDocId()) {
                String path = index.getDocName(post1.getDocId());
                Map<AbstractTerm, AbstractPosting> mp =
                        new HashMap<AbstractTerm, AbstractPosting>();
                // 把两个单词都放入映射表中
                mp.put(queryTerm1, post1);
                mp.put(queryTerm2, post2);
                hitArray.add(new Hit(post1.getDocId(), path, mp));
                i++;    j++;
            } else if(post1.getDocId() < post2.getDocId()) {
                if(combine == LogicalCombination.OR) {
                    String path = index.getDocName(post1.getDocId());
                    Map<AbstractTerm, AbstractPosting> mp =
                            new HashMap<AbstractTerm, AbstractPosting>();
                    mp.put(queryTerm1, post1);
                    hitArray.add(new Hit(post1.getDocId(), path, mp));
                    i++;
                }
            } else {        // post1 > post2
                if(combine == LogicalCombination.OR) {
                    String path = index.getDocName(post2.getDocId());
                    Map<AbstractTerm, AbstractPosting> mp =
                            new HashMap<AbstractTerm, AbstractPosting>();
                    mp.put(queryTerm2, post2);
                    hitArray.add(new Hit(post2.getDocId(), path, mp));
                    j++;
                }
            }
        }
        if(combine == LogicalCombination.OR) {  // 把还没读完的读完
            while(i < postList1.size()){
                AbstractPosting post1 = postList1.get(i);
                String path = index.getDocName(post1.getDocId());
                Map<AbstractTerm, AbstractPosting> mp =
                        new HashMap<AbstractTerm, AbstractPosting>();
                mp.put(queryTerm1, post1);
                hitArray.add(new Hit(post1.getDocId(), path, mp));
                i++;
            }
            while(j < postList2.size()){
                AbstractPosting post2 = postList2.get(i);
                String path = index.getDocName(post2.getDocId());
                Map<AbstractTerm, AbstractPosting> mp =
                        new HashMap<AbstractTerm, AbstractPosting>();
                mp.put(queryTerm2, post2);
                hitArray.add(new Hit(post2.getDocId(), path, mp));
                j++;
            }
        }
        new SimpleSorter().sort(hitArray);
        return (AbstractHit[]) hitArray.toArray();
    }
}
