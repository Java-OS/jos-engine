package ir.moke.jos;

public class Echo {
    public static void execute(String params) {
        if (params != null && !params.isBlank()) {
            System.out.println(params);
        }
    }
}
