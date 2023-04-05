package io.ylab.intensive.lesson02.sequence_generator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class SequencesImplTests {
    private PrintStream systemOut;
    private ByteArrayOutputStream testOut;
    SequenceGenerator sequenceGenerator = new SequencesImpl();

    @BeforeEach
    void setUp() {
        systemOut = System.out;
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setOut(systemOut);
    }

    @Test
    void a() {
        String expected = "A. 2, 4, 6, 8, 10";
        sequenceGenerator.a(5);
        Assertions.assertEquals(expected, testOut.toString().trim());
    }

    @Test
    void b() {
        String expected = "B. 1, 3, 5, 7, 9";
        sequenceGenerator.b(5);
        Assertions.assertEquals(expected, testOut.toString().trim());
    }

    @Test
    void c() {
        String expected = "C. 1, 4, 9, 16, 25";
        sequenceGenerator.c(5);
        Assertions.assertEquals(expected, testOut.toString().trim());
    }

    @Test
    void d() {
        String expected = "D. 1, 8, 27, 64, 125";
        sequenceGenerator.d(5);
        Assertions.assertEquals(expected, testOut.toString().trim());
    }

    @Test
    void e() {
        String expected = "E. 1, -1, 1, -1, 1, -1";
        sequenceGenerator.e(6);
        Assertions.assertEquals(expected, testOut.toString().trim());
    }

    @Test
    void f() {
        String expected = "F. 1, -2, 3, -4, 5, -6";
        sequenceGenerator.f(6);
        Assertions.assertEquals(expected, testOut.toString().trim());
    }

    @Test
    void g() {
        String expected = "G. 1, -4, 9, -16, 25";
        sequenceGenerator.g(5);
        Assertions.assertEquals(expected, testOut.toString().trim());
    }

    @Test
    void h() {
        String expected = "H. 1, 0, 2, 0, 3, 0, 4";
        sequenceGenerator.h(7);
        Assertions.assertEquals(expected, testOut.toString().trim());
    }

    @Test
    void i() {
        String expected = "I. 1, 2, 6, 24, 120, 720";
        sequenceGenerator.i(6);
        Assertions.assertEquals(expected, testOut.toString().trim());
    }

    @Test
    void j() {
        String expected = "J. 1, 1, 2, 3, 5, 8, 13, 21";
        sequenceGenerator.j(8);
        Assertions.assertEquals(expected, testOut.toString().trim());
    }
}