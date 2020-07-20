package com.btm.back;

public class test {
    int a =10;
    private test(int a) {
        a = a;
    }

        public static void main(String[] args) {
        String m1="HelloWorld";
        String m2=new String("HelloWorld");
        System.out.println(m1==m2);
        System.out.println(m1.equals(m2));
        String m3=new String("HelloWorld");
        System.out.println(m2==m3);
    }

}
