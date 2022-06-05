package ir.moke.jos.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

    public static void delete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteDirectory(Path directory) {
        try (Stream<Path> list = Files.list(directory)) {
            List<Path> pathList = list.toList();
            for (Path path : pathList) {
                if (Files.isDirectory(path)) {
                    deleteDirectory(path);
                } else {
                    delete(path);
                }
            }

            delete(directory);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
