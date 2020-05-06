package com.btm.back;

public class test1 {

    public static void main (String args[]) {
        class Foo {
            public int i = 3;
        }
        Object o = (Object) new Foo();
        Foo foo = (Foo)o;
        System.out.println(foo. i);
    }
}
