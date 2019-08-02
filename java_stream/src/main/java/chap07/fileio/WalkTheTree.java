package chap07.fileio;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * 7.3 文件系统遍历
 */
public class WalkTheTree {
    public static void main(String[] args) {
        try (Stream<Path> paths = Files.walk(Paths.get("src/main/java"))) {
            paths.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileVisitOption[] values = FileVisitOption.values();
        System.out.println(values.length);
        System.out.println(values[0]);
    }
}
