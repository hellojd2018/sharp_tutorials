package chap07.fileio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * 7.2 以流的形式检索文件
 */
public class FileList {
    public static void main(String[] args) {
        try (Stream<Path> list = Files.list(Paths.get("src", "main", "java"))) {
            list.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
