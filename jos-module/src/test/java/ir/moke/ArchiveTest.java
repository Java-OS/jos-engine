package ir.moke;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ArchiveTest {
    private static final Path zipPath = Paths.get("/home/mah454/file.zip");

    public static void main(String[] args) {
        test(null);
    }

    public static void test(String a) {
        Objects.requireNonNull(a);
        System.out.println("Okey " + a);
    }
}
