package hw4;

import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static hw4.Main.triangleArea;
import static org.junit.jupiter.api.Assertions.*;

public class TriangleAreaTest {
    private static Logger logger = LoggerFactory.getLogger(TriangleAreaTest.class);



    @BeforeAll
    static public void init() {
        logger.debug("Начало тестирования");
    }


    @AfterAll
    static public void close() {
        logger.debug("Конец тестирования");
    }

    @ParameterizedTest(name = "Проверка базового функционала на данных: {0}, {1}, {2}")
    @MethodSource("testProvider_basic")
    public void test_triangleArea_basic(int a, int b, int c, double expected) {
        logger.debug("Проверка базового функционала на данных: {}, {}, {}", a, b, c);
        double actual  = triangleArea(a, b, c);
        logger.debug("Полученный/Ожидаемый результат: {}/{}", actual, expected);
        assertEquals(expected, actual, 0.001);
    }

    @ParameterizedTest(name = "Проверка функционала на расширенных данных: {0}, {1}, {2}")
    @MethodSource("testProvider_extended")
    public void test_triangleArea_extended(int a, int b, int c, double expected) {
        logger.debug("Проверка функционала на расширенных данных: {}, {}, {}", a, b, c);
        double actual  = triangleArea(a, b, c);
        logger.debug("Полученный/Ожидаемый результат: {}/{}", actual, expected);
        assertEquals(expected, actual, 0.001);
    }


    @ParameterizedTest(name = "Проверка недопустимого ввода (нулевые стороны) на данных: {0}, {1}, {2}")
    @MethodSource("testProvider_zero")
    public void test_triangleArea_illegalInput_zero(int a, int b, int c) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            logger.debug("Проверка недопустимого ввода (нулевые стороны) на данных: {}, {}, {}", a, b, c);
            double actual  = triangleArea(a, b, c);
            logger.debug("Не получено исключения, полученный результат: {}", actual);
        });

        assertTrue(exception.getMessage().contains("Стороны должны быть положительными"));
    }

    @ParameterizedTest(name = "Проверка недопустимого ввода (отрицательные стороны) на данных: {0}, {1}, {2}")
    @MethodSource("testProvider_negative")
    public void test_triangleArea_illegalInput_negative(int a, int b, int c) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            logger.debug("Проверка недопустимого ввода (отрицательные стороны) на данных: {}, {}, {}", a, b, c);
            double actual  = triangleArea(a, b, c);
            logger.debug("Не получено исключения, полученный результат: {}", actual);
        });
        assertTrue(exception.getMessage().contains("Стороны должны быть положительными"));
    }

    @ParameterizedTest(name = "Проверка недопустимого ввода (невыполнение неравенства треугольника) на данных: {0}, {1}, {2}")
    @MethodSource("testProvider_triangleinequality")
    public void test_triangleArea_illegalInput_triangleinequality(int a, int b, int c) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            logger.debug("Проверка недопустимого ввода (невыполнение неравенства треугольника) на данных: {}, {}, {}", a, b, c);
            double actual  = triangleArea(a, b, c);
            logger.debug("Не получено исключения, полученный результат: {}", actual);
        });

        assertTrue(exception.getMessage().contains("Не выполняется неравенство треугольника или он вырожденный"));
    }

    static Stream<Arguments> testProvider_basic() {
        return Stream.of(
                Arguments.of(3, 4, 5, 6)
        );
    }

    static Stream<Arguments> testProvider_extended() {
        return Stream.of(
                Arguments.of(7, 9, 12, 31.305),
                Arguments.of(93457, 134615, 73548, 3289777977.859),
                Arguments.of(30000, 40000, 50000, 600000000)
        );
    }

    static Stream<Arguments> testProvider_zero() {
        return Stream.of(
                Arguments.of(0, 4, 5),
                Arguments.of(0, 0, 0),
                Arguments.of(3, 0, 0)
        );
    }

    static Stream<Arguments> testProvider_negative() {
        return Stream.of(
                Arguments.of(-6, 4, 5),
                Arguments.of(-3, -2, -3),
                Arguments.of(-3, 0, -5)
        );
    }

    static Stream<Arguments> testProvider_triangleinequality() {
        return Stream.of(
                Arguments.of(15, 4, 5),
                Arguments.of(80, 2, 3),
                Arguments.of(179, 1, 1)
        );
    }
}