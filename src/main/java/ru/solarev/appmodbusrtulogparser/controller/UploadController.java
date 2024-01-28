package ru.solarev.appmodbusrtulogparser.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.solarev.appmodbusrtulogparser.service.WriteParseData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UploadController {
    private final WriteParseData writeParseData;
    private final String UPLOAD_DIR = "./uploads/";
    private String fileNameGeneral = "";

    @GetMapping("/")
    public String homepage(Model model) {
        if (!fileNameGeneral.isEmpty()) {
            long start = System.nanoTime();
            List<String> dataList = writeParseData.write(fileNameGeneral).stream().flatMap(List::stream).toList();
            long finish = System.nanoTime();
            String time = "Время выполнения расшифровки данных: " + (finish-start)/1_000_000 + " ms";
            model.addAttribute("data", dataList);
            model.addAttribute("time", time);
        }
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Пожалуйста выберите файл для загрузки.");
            return "redirect:/";
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileNameGeneral = fileName;
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        attributes.addFlashAttribute("message", "Ваш файл успешно загружен: " + fileName + '!');

        return "redirect:/";
    }

    @GetMapping("/download/{fileName}")
    @ResponseBody
    @SneakyThrows
    public FileSystemResource downloadFile(@PathVariable String fileName) {
        File file = new File(fileName);
        return new FileSystemResource(file);

    }
}
