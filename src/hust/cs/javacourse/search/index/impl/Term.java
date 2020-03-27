package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Term 表示一个文档中的一个单词
 * @author :刘静平
 */

public class Term extends AbstractTerm {
    /**
     * @param obj ：要比较的另外一个Term
     * @return  :比较结果
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Term){    // 先判断类型是否相同
            return super.content.equals(((Term) obj).content);
        }
        return false;
    }

    /**
     * @return ：Term返回content字符串，外加双引号
     */
    @Override
    public String toString() {
        return "\"" + super.content + "\"";
    }

    /**
     * @return ：返回content
     */
    @Override
    public String getContent() {
        return super.content;
    }

    /**
     * @param content：Term的内容
     */
    @Override
    public void setContent(String content) {
        super.content = content;
    }

    /**
     * @param o： 要比较的Term对象
     * @return ：content字符串比较的结果
     */
    @Override
    public int compareTo(AbstractTerm o) {
        return super.content.compareTo(o.getContent());
    }

    /**
     * 向输出流中写入content内容
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeUTF(this.content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从输入流中读取字符串并赋值给content
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.content = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}