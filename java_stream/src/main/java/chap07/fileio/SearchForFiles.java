package chap07.fileio;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
/**
 * 7.4 文件系统的检索
 */
public class SearchForFiles {
    public static void main(String[] args) {
        try (Stream<Path> paths = Files.find(Paths.get("src/main/java"), Integer.MAX_VALUE,
                (path, attributes) -> !attributes.isDirectory() && path.toString().contains(
                    "chap07/fileio"))) {
            paths.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
