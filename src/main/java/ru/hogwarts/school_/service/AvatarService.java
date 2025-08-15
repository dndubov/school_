package ru.hogwarts.school_.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school_.model.Avatar;
import ru.hogwarts.school_.model.Student;
import ru.hogwarts.school_.repository.AvatarRepository;
import ru.hogwarts.school_.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AvatarService {

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public AvatarService(StudentRepository studentRepository,
                         AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        try {
            byte[] data = file.getBytes();
            String extension = getExtension(file.getOriginalFilename());
            String fileName = "avatar_" + studentId + "." + extension;
            Path path = Paths.get(avatarsDir, fileName);
            Files.write(path, data);

            Avatar avatar = avatarRepository.findByStudentId(studentId)
                    .orElse(new Avatar());
            avatar.setStudent(student);
            avatar.setFilePath(path.toString());
            avatar.setData(data);
            avatar.setFileSize(data.length);
            avatar.setMediaType(file.getContentType());

            avatarRepository.save(avatar);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла", e);
        }
    }

    public Avatar getAvatarByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Аватар не найден"));
    }

    public ResponseEntity<byte[]> getAvatarFromFile(Long studentId) {
        Avatar avatar = getAvatarByStudentId(studentId);
        try {
            Path path = Paths.get(avatar.getFilePath());
            byte[] data = Files.readAllBytes(path);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                    .body(data);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public Page<Avatar> getAvatars(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be > 0");
        }
        return avatarRepository.findAll(PageRequest.of(page, size));
    }

}
