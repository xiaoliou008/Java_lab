package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.Sort;

import java.util.Comparator;
import java.util.List;

public class SimpleSorter implements Sort {
    /**
     * 对命中结果集合根据文档得分排序
     *
     * @param hits ：命中结果集合
     */
    @Override
    public void sort(List<AbstractHit> hits) {
        hits.sort(new Comparator<AbstractHit>() {
            @Override
            public int compare(AbstractHit abstractHit, AbstractHit t1) {
                return (int) Math.round(score(abstractHit) - score(t1));
            }
        });
    }

    /**
     * 计算命中文档的得分, 作为命中结果排序的依据.
     * Simple的排序策略，就是以命中的频数作为一个文档的得分
     * @param hit ：命中文档
     * @return ：命中文档的得分
     */
    @Override
    public double score(AbstractHit hit) {
        double res  =0;
        for(AbstractPosting posting: hit.getTermPostingMapping().values()){
            res = res + posting.getFreq();
        }
        return res;
    }
}
