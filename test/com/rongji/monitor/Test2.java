package com.rongji.monitor;

public class Test2 implements Runnable {
public static int i = 0;
	@Override
	public void run() {
		while(true) {
			System.out.println("---------aaa" +i);
			String s = "";
			for(int j=0; j<10000;j++) {
				s += "zofueng";
			}
			i ++;
		}
	}
	public static void main(String[] args) {
		Test2 t = new Test2();
		Thread th = new Thread(t);
		th.setDaemon(true);
		th.start();
	}

}
