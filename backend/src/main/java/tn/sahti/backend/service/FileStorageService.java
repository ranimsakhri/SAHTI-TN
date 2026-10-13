package tn.sahti.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final long MAX_SIZE = 5L * 1024 * 1024;
    private final Path root;

    public FileStorageService(@Value("${sahti.upload-dir}") String dir) throws IOException {
        this.root = Paths.get(dir).toAbsolutePath().normalize();
        Files.createDirectories(root);
    }

    public String storePdf(MultipartFile file, String label) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, label + " est obligatoire");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, label + " dépasse 5 Mo");
        }
        try (InputStream in = file.getInputStream()) {
            String head = new String(in.readNBytes(5), StandardCharsets.US_ASCII);
            if (!"%PDF-".equals(head)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, label + " doit être un fichier PDF");
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lecture du fichier impossible");
        }
        String name = UUID.randomUUID() + ".pdf";
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, root.resolve(name));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Enregistrement du fichier impossible");
        }
        return name;
    }

    public Resource load(String name) {
        Path p = root.resolve(name).normalize();
        if (!p.startsWith(root) || !Files.exists(p)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document introuvable");
        }
        return new FileSystemResource(p);
    }
}