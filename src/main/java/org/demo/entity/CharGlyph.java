package org.demo.entity;

import java.io.Serializable;

/**
 * 文字字符
 * User: zhaodahao
 * Date: 16-11-3
 * Time: 下午6:02
 */
public class CharGlyph implements Serializable {
    private char source;
    private String glyphName = "";
    private String unicode = "";
    private Integer horizAdvX;
    private String arabicForm = "";
    private String d = "";
    /**
     * 是否丢失（不支持），默认支持
     */
    private boolean missing = false;

    public char getSource() {
        return source;
    }

    public void setSource(char source) {
        this.source = source;
    }

    public String getGlyphName() {
        return glyphName;
    }

    public void setGlyphName(String glyphName) {
        this.glyphName = glyphName;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public Integer getHorizAdvX() {
        return horizAdvX;
    }

    public void setHorizAdvX(Integer horizAdvX) {
        this.horizAdvX = horizAdvX;
    }

    public String getArabicForm() {
        return arabicForm;
    }

    public void setArabicForm(String arabicForm) {
        this.arabicForm = arabicForm;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public boolean isMissing() {
        return missing;
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }

    @Override
    public String toString() {
        return "CharGlyph{" +
                "source=" + source +
                ", glyphName='" + glyphName + '\'' +
                ", unicode='" + unicode + '\'' +
                ", horizAdvX=" + horizAdvX +
                ", arabicForm='" + arabicForm + '\'' +
                ", d='" + d + '\'' +
                ", missing=" + missing +
                '}';
    }
}
