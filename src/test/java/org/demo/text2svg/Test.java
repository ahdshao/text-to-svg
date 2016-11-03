package org.demo.text2svg;

import org.demo.entity.ResultData;
import org.demo.util.Text2SvgUtils;

/**
 * User: zhaodahao
 * Date: 16-11-3
 * Time: 下午2:43
 */
public class Test {
    public static void main(String[] args) {
        String fontPath="D:/IdeaProjects/text2svg/fonts/方正新书宋简体.TTF";
        String text="12312我们大家一起来暨";
        try {
            for (int i = 1;i<10;i++){
                ResultData  resultData = Text2SvgUtils.toConvert(fontPath, text);
                System.out.println(resultData);
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
