package org.demo.entity;

import java.util.List;

/**
 * User: zhaodahao
 * Date: 16-11-3
 * Time: 下午9:02
 */
public class ResultData {
    private FontFace fontFace;
    private List<CharGlyph> charGlyphList;

    public FontFace getFontFace() {
        return fontFace;
    }

    public void setFontFace(FontFace fontFace) {
        this.fontFace = fontFace;
    }

    public List<CharGlyph> getCharGlyphList() {
        return charGlyphList;
    }

    public void setCharGlyphList(List<CharGlyph> charGlyphList) {
        this.charGlyphList = charGlyphList;
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "fontFace=" + fontFace +
                ", charGlyphList=" + charGlyphList +
                '}';
    }
}
