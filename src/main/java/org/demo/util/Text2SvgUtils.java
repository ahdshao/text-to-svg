package org.demo.util;

import org.apache.batik.svggen.font.Font;
import org.apache.batik.svggen.font.Glyph;
import org.apache.batik.svggen.font.Point;
import org.apache.batik.svggen.font.table.*;
import org.apache.batik.util.SVGConstants;
import org.demo.entity.CharGlyph;
import org.demo.entity.FontFace;
import org.demo.entity.ResultData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 文本转SVG PATH 工具
 * User: zhaodahao
 * Date: 16-11-3
 * <p/>
 * <p/>
 * Time: 下午2:37
 */
public class Text2SvgUtils implements SVGConstants {

    public static ResultData toConvert(String fontPath, String text) throws Exception {
        long start = System.currentTimeMillis();
        Font font = Font.create(fontPath);
        System.out.println("创建Font用时：" + (System.currentTimeMillis() - start));

        ResultData resultData = new ResultData();
        resultData.setFontFace(getSVGFontFaceElement(font));
        resultData.setCharGlyphList(getTextPath(font, text));

        System.out.println("总用时：" + (System.currentTimeMillis() - start));
        return resultData;
    }

    protected static List<CharGlyph> getTextPath(Font font, String text) throws Exception {
        int horiz_advance_x = font.getOS2Table().getAvgCharWidth();
        CmapFormat cmapFmt = getCmapFormat(font);
        if (cmapFmt == null) {
            throw new Exception("Cannot find a suitable cmap table");
        }
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        // 获取文字对应的path
        char[] chArray = text.toCharArray();
        List<CharGlyph> charGlyphList = new ArrayList<CharGlyph>(chArray.length);
        for (char ch : chArray) {
            String code = Integer.toHexString(ch);
            int glyphIndex = cmapFmt.mapCharCode(Integer.parseInt(code, 16));
            CharGlyph charGlyph = getGlyphAsSVG(font, font.getGlyph(glyphIndex), glyphIndex, horiz_advance_x, null, XML_CHAR_REF_PREFIX + code + XML_CHAR_REF_SUFFIX);
            charGlyph.setSource(ch);
            charGlyphList.add(charGlyph);
        }
        return charGlyphList;
    }

    protected static CmapFormat getCmapFormat(Font font) {
        CmapFormat cmapFmt = font.getCmapTable().getCmapFormat(Table.platformMicrosoft, Table.encodingUGL);
        if (cmapFmt == null) {
            // This might be a symbol font, so we'll look for an "undefined" encoding
            cmapFmt = font.getCmapTable().getCmapFormat(Table.platformMicrosoft, Table.encodingUndefined);
        }
        return cmapFmt;
    }


    protected static FontFace getSVGFontFaceElement(Font font) {
        String fontFamily = font.getNameTable().getRecord(Table.nameFontFamilyName);
        short unitsPerEm = font.getHeadTable().getUnitsPerEm();
        String panose = font.getOS2Table().getPanose().toString();
        short ascent = font.getHheaTable().getAscender();
        short descent = font.getHheaTable().getDescender();
        //      <!ELEMENT font-face (%descTitleMetadata;,font-face-src?,definition-src?) >
        //           <!ATTLIST font-face
        //             %stdAttrs;
        //             font-family CDATA #IMPLIED
        //             font-style CDATA #IMPLIED
        //             font-variant CDATA #IMPLIED
        //             font-weight CDATA #IMPLIED
        //             font-stretch CDATA #IMPLIED
        //             font-size CDATA #IMPLIED
        //             unicode-range CDATA #IMPLIED
        //             units-per-em %Number; #IMPLIED
        //             panose-1 CDATA #IMPLIED
        //             stemv %Number; #IMPLIED
        //             stemh %Number; #IMPLIED
        //             slope %Number; #IMPLIED
        //             cap-height %Number; #IMPLIED
        //             x-height %Number; #IMPLIED
        //             accent-height %Number; #IMPLIED
        //             ascent %Number; #IMPLIED
        //             descent %Number; #IMPLIED
        //             widths CDATA #IMPLIED
        //             bbox CDATA #IMPLIED
        //             ideographic %Number; #IMPLIED
        //             alphabetic %Number; #IMPLIED
        //             mathematical %Number; #IMPLIED
        //             hanging %Number; #IMPLIED
        //             v-ideographic %Number; #IMPLIED
        //             v-alphabetic %Number; #IMPLIED
        //             v-mathematical %Number; #IMPLIED
        //             v-hanging %Number; #IMPLIED
        //             underline-position %Number; #IMPLIED
        //             underline-thickness %Number; #IMPLIED
        //             strikethrough-position %Number; #IMPLIED
        //             strikethrough-thickness %Number; #IMPLIED
        //             overline-position %Number; #IMPLIED
        //             overline-thickness %Number; #IMPLIED >

        FontFace fontFace = new FontFace();
        fontFace.setFontFamily(fontFamily);
        fontFace.setUnitsPerEm(unitsPerEm);
        fontFace.setAscent(ascent);
        fontFace.setDescent(descent);
        fontFace.setPanose(panose);

        return fontFace;
    }

    protected static CharGlyph getGlyphAsSVG(Font font, Glyph glyph, int glyphIndex, int defaultHorizAdvanceX, String attrib, String code) {
        CharGlyph charGlyph = new CharGlyph();
        int firstIndex = 0;
        int count = 0;
        int i;
        int horiz_advance_x;
        horiz_advance_x = font.getHmtxTable().getAdvanceWidth(glyphIndex);

        if (glyphIndex == 0) {
            // sb.append("<missing-glyph");
            // 不支持
            charGlyph.setMissing(true);
        } else {
            // Unicode value
            // sb.append("<glyph unicode=\"").append(code).append("\"");
            charGlyph.setUnicode(code);
            // Glyph name
            charGlyph.setGlyphName(font.getPostTable().getGlyphName(glyphIndex));
        }
        if (horiz_advance_x != defaultHorizAdvanceX) {
            // sb.append(" horiz-adv-x=\"").append(horiz_advance_x).append("\"");
            charGlyph.setHorizAdvX(horiz_advance_x);
        } else {
            charGlyph.setHorizAdvX(defaultHorizAdvanceX);
        }
        if (attrib != null) {
            charGlyph.setArabicForm(attrib);
        }

        if (glyph != null) {
            StringBuffer sb = new StringBuffer();
            // sb.append(" d=\"");
            for (i = 0; i < glyph.getPointCount(); i++) {
                count++;
                if (glyph.getPoint(i).endOfContour) {
                    sb.append(getContourAsSVGPathData(glyph, firstIndex, count));
                    firstIndex = i + 1;
                    count = 0;
                }
            }
            charGlyph.setD(sb.toString());
        }

        return charGlyph;
    }

    protected static String getContourAsSVGPathData(Glyph glyph, int startIndex, int count) {

        // If this is a single point on it's own, we can't do anything with it
        if (glyph.getPoint(startIndex).endOfContour) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int offset = 0;

        while (offset < count) {
            Point point = glyph.getPoint(startIndex + offset % count);
            Point point_plus1 = glyph.getPoint(startIndex + (offset + 1) % count);
            Point point_plus2 = glyph.getPoint(startIndex + (offset + 2) % count);

            if (offset == 0) {
                sb.append(PATH_MOVE)
                        .append(String.valueOf(point.x))
                        .append(XML_SPACE)
                        .append(String.valueOf(point.y));
            }

            if (point.onCurve && point_plus1.onCurve) {
                if (point_plus1.x == point.x) { // This is a vertical line
                    sb.append(PATH_VERTICAL_LINE_TO)
                            .append(String.valueOf(point_plus1.y));
                } else if (point_plus1.y == point.y) { // This is a horizontal line
                    sb.append(PATH_HORIZONTAL_LINE_TO)
                            .append(String.valueOf(point_plus1.x));
                } else {
                    sb.append(PATH_LINE_TO)
                            .append(String.valueOf(point_plus1.x))
                            .append(XML_SPACE)
                            .append(String.valueOf(point_plus1.y));
                }
                offset++;
            } else if (point.onCurve && !point_plus1.onCurve && point_plus2.onCurve) {
                // This is a curve with no implied points
                sb.append(PATH_QUAD_TO)
                        .append(String.valueOf(point_plus1.x))
                        .append(XML_SPACE)
                        .append(String.valueOf(point_plus1.y))
                        .append(XML_SPACE)
                        .append(String.valueOf(point_plus2.x))
                        .append(XML_SPACE)
                        .append(String.valueOf(point_plus2.y));
                offset += 2;
            } else if (point.onCurve && !point_plus1.onCurve && !point_plus2.onCurve) {
                // This is a curve with one implied point
                sb.append(PATH_QUAD_TO)
                        .append(String.valueOf(point_plus1.x))
                        .append(XML_SPACE)
                        .append(String.valueOf(point_plus1.y))
                        .append(XML_SPACE)
                        .append(String.valueOf(midValue(point_plus1.x, point_plus2.x)))
                        .append(XML_SPACE)
                        .append(String.valueOf(midValue(point_plus1.y, point_plus2.y)));
                offset += 2;
            } else if (!point.onCurve && !point_plus1.onCurve) {
                // This is a curve with two implied points
                sb.append(PATH_SMOOTH_QUAD_TO)
                        .append(String.valueOf(midValue(point.x, point_plus1.x)))
                        .append(XML_SPACE)
                        .append(String.valueOf(midValue(point.y, point_plus1.y)));
                offset++;
            } else if (!point.onCurve && point_plus1.onCurve) {
                sb.append(PATH_SMOOTH_QUAD_TO)
                        .append(String.valueOf(point_plus1.x))
                        .append(XML_SPACE)
                        .append(String.valueOf(point_plus1.y));
                offset++;
            } else {
                System.out.println("drawGlyph case not catered for!!");
                break;
            }
        }
        sb.append(PATH_CLOSE);

        return sb.toString();
    }

    private static int midValue(int a, int b) {
        return a + (b - a) / 2;
    }


}
