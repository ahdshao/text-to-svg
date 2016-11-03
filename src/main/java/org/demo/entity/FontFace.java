package org.demo.entity;

import java.io.Serializable;

/**
 * User: zhaodahao
 * Date: 16-11-3
 * Time: 下午8:38
 */
public class FontFace implements Serializable {
    private  String fontFamily;
    private short unitsPerEm;
    private String panose;
    private short ascent;
    private short descent;

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public short getUnitsPerEm() {
        return unitsPerEm;
    }

    public void setUnitsPerEm(short unitsPerEm) {
        this.unitsPerEm = unitsPerEm;
    }

    public String getPanose() {
        return panose;
    }

    public void setPanose(String panose) {
        this.panose = panose;
    }

    public short getAscent() {
        return ascent;
    }

    public void setAscent(short ascent) {
        this.ascent = ascent;
    }

    public short getDescent() {
        return descent;
    }

    public void setDescent(short descent) {
        this.descent = descent;
    }

    @Override
    public String toString() {
        return "FontFace{" +
                "fontFamily='" + fontFamily + '\'' +
                ", unitsPerEm=" + unitsPerEm +
                ", panose='" + panose + '\'' +
                ", ascent=" + ascent +
                ", descent=" + descent +
                '}';
    }
}
