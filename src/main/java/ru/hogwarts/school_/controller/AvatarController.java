package ru.hogwarts.school_.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school_.model.Avatar;
import ru.hogwarts.school_.service.AvatarService;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    // 1. Загрузка файла
    @PostMapping(value = "/{studentId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId,
                                               @RequestParam MultipartFile file) {
        avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.ok("Аватар успешно загружен");
    }

    // 2. Получение файла из БД
    @GetMapping("/{studentId}/preview-from-db")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable Long studentId) {
        Avatar avatar = avatarService.getAvatarByStudentId(studentId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .body(avatar.getData());
    }

    // 3. Получение файла с диска
    @GetMapping("/{studentId}/preview-from-file")
    public ResponseEntity<byte[]> getAvatarFromFile(@PathVariable Long studentId) {
        return avatarService.getAvatarFromFile(studentId);
    }

    @GetMapping("/list")
    public Page<Avatar> getAvatars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return avatarService.getAvatars(page, size);
    }

}
