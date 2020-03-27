package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;

/**
 * 实现 AbstractTermTuple 抽象类
 * 三元组：单词，常量1（频度），出现位置
 * @author :刘静平
 */
public class TermTuple extends AbstractTermTuple {
    /**
     * 默认构造函数
     */
    public TermTuple(){};

    /**
     * 使用term和curPos来构造TermTuple
     * @param term
     * @param curPos
     */
    public TermTuple(AbstractTerm term, int curPos){
        super.term = term;
        super.curPos = curPos;
    }

    /**
     * 使用String类型代替Term，更便于使用
     * @param content
     * @param curPos
     */
    public TermTuple(String content, int curPos){
        super.term = new Term();
        super.term.setContent(content);
        super.curPos = curPos;
    }

    /**
     * 判断二个三元组内容是否相同
     *
     * @param obj ：要比较的另外一个三元组
     * @return 如果内容相等（三个属性内容都相等）返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TermTuple){
            return (super.curPos == ((TermTuple) obj).curPos &&
                    super.freq == ((TermTuple) obj).freq &&
                    super.term.equals((TermTuple) obj));    // 不能用==
        }
        return false;
    }

    /**
     * 获得三元组的字符串表示
     *
     * @return ： 三元组的字符串表示
     */
    @Override
    public String toString() {
        return "(" + super.term.toString() + ", " + super.freq + ", " + super.curPos + ")";
    }
}
