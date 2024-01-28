package ru.solarev.appmodbusrtulogparser.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.solarev.appmodbusrtulogparser.repository.DataEntityRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class WriteParseData {
    private final String UPLOAD_DIR = "./uploads/";
    private final DataEntityRepository dataEntityRepository;
    private final PrintParseDataRequest03 request03;
    private final PrintParseDataRequest16 request16;
    private final PrintParseDataResponse03 response03;

    public List<List<String>> write(String file) {
        File fileLog = new File("parseData.txt");
        if (!file.isEmpty()) {
            clearFile(fileLog);
        }
        List<List<String>> listDataFinal = new ArrayList<>();

        dataEntityRepository.parseDocument(UPLOAD_DIR + file);
        List<PrintParseData> printParseDataList = List.of(response03, request03, request16);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (PrintParseData data : printParseDataList) {
            executorService.execute(() -> {
                List<String> temp = data.print();
                writeData(temp);
                listDataFinal.add(temp);
            });
        }
        executorService.shutdown();

        return listDataFinal;
    }

    @SneakyThrows
    public void writeData(List<String> data) {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter("parseData.txt", true))) {
            for(String s : data) {
                bf.write(s);
            }
        }
    }

    @SneakyThrows
    private void clearFile(File file) {
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
    }
}
