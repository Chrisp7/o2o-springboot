package com.imooc.o2o.util;

public class Love {
	public static void starLove() {
		for (double y = 1.3; y >= -1.1; y -= 0.06) {
			for (double x = -1.2; x <= 1.2; x += 0.025) {
				if (Math.pow((x * x + y * y - 1.0), 3) - x * x * y * y * y <= 0.0) {
					System.out.print("*");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("");
		}
	}

	public static void jianEngLove() {
		String str = "I Love Jian ~ ";
		for (double y = 1.3; y >= -1.1; y -= 0.06) {
			int idx = 0;
			for (double x = -1.2; x <= 1.2; x += 0.025) {
				if (Math.pow((x * x + y * y - 1.0), 3) - x * x * y * y * y <= 0.0) {
					System.out.print(str.charAt((idx++) % str.length()));
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("");
		}
	}

	public static void main(String[] args) {
		jianEngLove();
	}
}
