package io.github.fozeton.blog.server.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

public class SavedFile {
    public String saveFile(MultipartFile file, String subFolder) throws IOException {
        Path path = Paths.get("images", subFolder);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        if(file.getContentType() != null) {
            String extension = file.getContentType().substring(6);
            String fileName = UUID.randomUUID() + "." + extension;

            Path filePath = path.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            return fileName;
        }
        return "";
    }

    public String saveFile(byte[] bytes, String subFolder) throws IOException {
        Path path = Paths.get("images", subFolder);
        if (!Files.exists(path)) Files.createDirectories(path);
        System.out.println("bytes " + Arrays.toString(bytes));
        if (bytes == null) return "";

        try (InputStream is = new ByteArrayInputStream(bytes)) {
            String extension = URLConnection.guessContentTypeFromStream(is);
            String fileName = UUID.randomUUID() + "." + extension;

            Path filePath = path.resolve(fileName);
            Files.copy(is, filePath);
            return fileName;
        }
    }
}
