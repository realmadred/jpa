package com.example.jpa;

public class ThreadTest {

    private static int a = 0;

    public static class AThread extends Thread {
        private String name;
        private Object syc;

        AThread(String name, Object syc) {
            this.name = name;
            this.syc = syc;
        }

        @Override
        public void run() {
            for (int j = 0; j < 1000; j++) {
                a++;
                System.out.println(name+":"+a);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        final Object o = new Object();
        new AThread("123", o).start();
        new AThread("456", o).start();
    }
}
