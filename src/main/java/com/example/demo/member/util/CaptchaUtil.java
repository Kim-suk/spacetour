package com.example.demo.member.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtil {

    public static BufferedImage createCaptchaImage(String text) {
    	 int width = 250;  // 기존보다 크게
    	    int height = 80;  // 기존보다 크게

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 배경색
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRect(0, 0, width, height);

        // 폰트 설정
        Font font = new Font("Arial", Font.BOLD, 40);
        g2d.setFont(font);

        // 글자 색깔 & 위치 잡기
        Random rand = new Random();

        for (int i = 0; i < text.length(); i++) {
            g2d.setColor(new Color(rand.nextInt(150) + 100, rand.nextInt(150) + 100, rand.nextInt(150) + 100));

            int x = 20 + i * 25 + rand.nextInt(10);
            int y = 40 + rand.nextInt(10);

            g2d.drawString(String.valueOf(text.charAt(i)), x, y);
        }

        // 노이즈 선
        for (int i = 0; i < 8; i++) {
            g2d.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
            int x1 = rand.nextInt(width);
            int y1 = rand.nextInt(height);
            int x2 = rand.nextInt(width);
            int y2 = rand.nextInt(height);
            g2d.drawLine(x1, y1, x2, y2);
        }

        g2d.dispose();
        return image;
    }

    public static String generateCaptchaText(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder captchaStr = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            captchaStr.append(chars.charAt(index));
        }

        return captchaStr.toString();
    }
}
