package ru.solarev.appmodbusrtulogparser.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.solarev.appmodbusrtulogparser.model.Request03AO;
import ru.solarev.appmodbusrtulogparser.model.Request16AO;
import ru.solarev.appmodbusrtulogparser.model.Response03AO;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Класс для парсинга логов (01 10, 01 03) из файла
 */

@Component
@Getter
public class DataEntityRepository {
    private final List<Request03AO> request03AOS;
    private final List<Response03AO> response03AOS;
    private final List<Request16AO> request16AOS;
    private final static int THREADS = 4;

    public DataEntityRepository() {
        request03AOS = new ArrayList<>();
        response03AOS = new ArrayList<>();
        request16AOS = new ArrayList<>();
    }

    public void parseDocument(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
            while ((line = br.readLine()) != null) {
                if (line.matches("^\\S+ \\S+ (01 03|01 10) .*")) {
                    String finalLine = line;
                    executorService.execute(() -> parseModbusRTULine(finalLine));
                }
            }
            executorService.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseModbusRTULine(String line) {
        String[] clearLine = line.split("/");

        String[] parts = clearLine[0].split("\\s+");
        int partsMaxLengthData = parts.length - 2;
        String date = parts[0];
        String time = parts[1];
        String address = parts[2];
        String functionCode = parts[3];

        int funcCode = parseHexToInt(functionCode);

        String crc = parts[partsMaxLengthData] + parts[partsMaxLengthData+1];

        if (funcCode == 3 && parts.length == 10) {
            String firstRegister = parts[4] + parts[5];
            String countRegisters = parts[6] + parts[7];
            Request03AO request03AO = new Request03AO();
            request03AO.setDate(date);
            request03AO.setTime(time);
            request03AO.setDeviceAddress(address);
            request03AO.setCodeFunction(functionCode);
            request03AO.setFirstRegisterAddress(firstRegister);
            request03AO.setCountRegisters(countRegisters);
            request03AO.setCyclicRedundancyCheck(crc);
            request03AOS.add(request03AO);
        } else if (funcCode == 3) {
            Response03AO response03AO = new Response03AO();
            response03AO.setDate(date);
            response03AO.setTime(time);
            response03AO.setDeviceAddress(address);
            response03AO.setCodeFunction(functionCode);
            response03AO.setCountBytes(parts[4]);
            for (int i = 5; i < partsMaxLengthData; i += 2) {
                response03AO.getDataRegisters().add(parts[i] + parts[i+1]);
            }
            response03AO.setCyclicRedundancyCheck(crc);
            response03AOS.add(response03AO);
        } else if (funcCode == 16) {
            Request16AO request16AO = new Request16AO();
            request16AO.setDate(date);
            request16AO.setTime(time);
            request16AO.setDeviceAddress(address);
            request16AO.setCodeFunction(functionCode);
            request16AO.setFirstRegisterAddress(parts[4] + parts[5]);
            request16AO.setCountRegisters(parts[6] + parts[7]);
            request16AO.setCountBytes(parts[8]);
            for (int i = 9; i < partsMaxLengthData; i += 2) {
                request16AO.getDataRegisters().add(parts[i] + parts[i+1]);
            }
            request16AO.setCyclicRedundancyCheck(crc);
            request16AOS.add(request16AO);
        }
    }

    public int parseHexToInt(String value) {
        return Integer.parseInt(value, 16);
    }

//    public String hexToBin(String s) {
//        return new BigInteger(s, 16).toString(2);
//    }
//
//    public void printReq03() {
//        request03AOS.forEach(System.out::println);
//    }
//
//    public void printRes03() {
//        response03AOS.forEach(System.out::println);
//    }
//
//    public void printReq16() {
//        request16AOS.forEach(System.out::println);
//    }
}
