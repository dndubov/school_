package ru.hogwarts.school_.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Slf4j
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
        log.info("Was invoked method uploadAvatar for studentId={}", studentId);
        Student student = studentRepository.findById(studentId).orElseThrow(() -> {
            log.error("Student not found with id={}", studentId);
            return new RuntimeException("Student not found");
        });

        try {
            byte[] data = file.getBytes();
            String extension = getExtension(file.getOriginalFilename());
            String fileName = "avatar_" + studentId + "." + extension;
            Path path = Paths.get(avatarsDir, fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, data);

            Avatar avatar = avatarRepository.findByStudentId(studentId)
                    .orElse(new Avatar());
            avatar.setStudent(student);
            avatar.setFilePath(path.toString());
            avatar.setData(data);
            avatar.setFileSize(data.length);
            avatar.setMediaType(file.getContentType());

            avatar.setUrl("/avatar/" + studentId);
            avatarRepository.save(avatar);
            log.info("Avatar uploaded successfully for studentId={}", studentId);
        } catch (IOException e) {
            log.error("Error saving avatar for studentId={}", studentId, e);
            throw new RuntimeException("Ошибка при сохранении файла", e);
        }
    }

    public Avatar getAvatarByStudentId(Long studentId) {
        log.info("Was invoked method getAvatarByStudentId for studentId={}", studentId);
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    log.warn("Avatar not found for studentId={}", studentId);
                    return new RuntimeException("Аватар не найден");
                });
    }

    @Transactional
    public ResponseEntity<byte[]> getAvatarFromFile(Long studentId) {
        log.info("Was invoked method getAvatarFromFile for studentId={}", studentId);
        Avatar avatar = getAvatarByStudentId(studentId);
        try {
            Path path = Paths.get(avatar.getFilePath());
            byte[] data = Files.readAllBytes(path);
            log.debug("Read avatar file from disk for studentId={}, size={}", studentId, data.length);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                    .body(data);
        } catch (IOException e) {
            log.error("Error reading avatar file from disk for studentId={}", studentId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    public ResponseEntity<byte[]> getAvatarFromDb(Long studentId) {
        log.info("Was invoked method getAvatarFromDb for studentId={}", studentId);
        Avatar avatar = getAvatarByStudentId(studentId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .body(avatar.getData());
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public Page<Avatar> getAvatars(int page, int size) {
        log.info("Was invoked method getAvatars with page={} and size={}", page, size);
        if (page < 0) {
            log.warn("Page index must be >= 0, received {}", page);
            throw new IllegalArgumentException("Page index must be >= 0");
        }
        if (size <= 0) {
            log.warn("Page size must be > 0, received {}", size);
            throw new IllegalArgumentException("Page size must be > 0");
        }
        return avatarRepository.findAll(PageRequest.of(page, size));
    }
}
