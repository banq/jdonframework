/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package com.jdon.util;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;

public class RegisterCode {

  public Color getRandColor(int fc, int bc) { //给定范围获得随机颜色
    Random random = new Random();
    if (fc > 255)
      fc = 255;
    if (bc > 255)
      bc = 255;
    int r = fc + random.nextInt(bc - fc);
    int g = fc + random.nextInt(bc - fc);
    int b = fc + random.nextInt(bc - fc);
    return new Color(r, g, b);
  }

  public BufferedImage getBufferedImage(int width, int height) {
    return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  public BufferedImage getBufferedImage(InputStream in) throws
      Exception {
    return ImageIO.read(in);
  }

  public void create(int imageWidth, int imageHeight, String randNumber,
                     String fontType, int fontSize, int x, int y,
                     OutputStream out) {
    BufferedImage image = getBufferedImage(imageWidth, imageHeight);
    generate(image, randNumber, fontType, fontSize, x, y, out);
  }

  public void generate(BufferedImage image, String randNumber,
                       String fontType, int fontSize, int x, int y,
                       OutputStream out) {

    try {

      int width = image.getWidth();
      int height = image.getHeight();

      // 获取图形上下文
      Graphics g = image.getGraphics();

      // 设定背景色
      g.setColor(getRandColor(200, 250));
      g.fillRect(0, 0, width, height);

      //设定字体
      g.setFont(new Font(fontType, Font.PLAIN, fontSize));

      //画边框
      //g.setColor(new Color());
      //g.drawRect(0,0,width-1,height-1);

      // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
      g.setColor(getRandColor(160, 200));
      //生成随机类
      Random random = new Random();
      for (int i = 0; i < 155; i++) {
        int x2 = random.nextInt(width);
        int y2 = random.nextInt(height);
        int x3 = random.nextInt(12);
        int y3 = random.nextInt(12);
        g.drawLine(x2, y2, x2 + x3, y2 + y3);
      }

      // 将认证码显示到图象中
      g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110),
                           20 + random.nextInt(110)));

      g.drawString(randNumber, x, y);

      // 图象生效
      g.dispose();

      // 输出图象到页面
      ImageIO.write( (BufferedImage) image, "JPEG", out);
    } catch (Exception ex) {
      System.err.println("generate image error: " + ex);
    }

  }

}
