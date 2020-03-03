package expression;

public class Main {

    public static void main(String[] args) {
        Expression t = new Multiply(new Const(2), new Divide(new Variable("x"), new Const(2)));
        System.out.println(t.toString() + "\n" + t.toMiniString());
    }
}
