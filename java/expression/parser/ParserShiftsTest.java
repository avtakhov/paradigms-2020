package expression.parser;

import expression.BaseTest;

import java.util.List;

/**
 * @author Georgiy Korneev
 */
public class ParserShiftsTest extends ParserTest {
    protected ParserShiftsTest() {
        levels.add(0, BaseTest.list(
                BaseTest.op("<<", (a, b) -> (int) a << (int) b),
                BaseTest.op(">>", (a, b) -> (int) a >> (int) b)
        ));

        tests.addAll(List.of(
                BaseTest.op("1 << 5 + 3", (x, y, z) -> 256L),
                BaseTest.op("x + y << z", (x, y, z) -> (int) (x + y) << (int) z),
                BaseTest.op("x * y << z", (x, y, z) -> (int) (x * y) << (int) z),
                BaseTest.op("x << y << z", (x, y, z) -> (int) x << (int) y << (int) z),
                BaseTest.op("1024 >> 5 + 3", (x, y, z) -> 4L),
                BaseTest.op("x + y >> z", (x, y, z) -> x + y >> z),
                BaseTest.op("x * y >> z", (x, y, z) -> x * y >> z),
                BaseTest.op("x >> y >> z", (x, y, z) -> x >> y >> z)
        ));
    }

    public static void main(final String[] args) {
        new ParserShiftsTest().run();
    }
}
