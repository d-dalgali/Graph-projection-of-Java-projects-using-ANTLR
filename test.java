package com;

public class A {
    static void m1() {
        m2();
        B.m2();
    }

    static void m2() {
        B.m1();
    }
}

class B {
    static void m1() {
        A.m1();
    }

    static void m2() {
        C.m3();
    }
}

class C {
    static void m1() {}
    static void m2() {A.m4();
    m1();}
    static void m3(){B.m1();}
}
