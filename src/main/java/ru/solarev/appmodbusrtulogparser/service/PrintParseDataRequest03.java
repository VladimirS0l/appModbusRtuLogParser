package ru.solarev.appmodbusrtulogparser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.solarev.appmodbusrtulogparser.model.Request03AO;
import ru.solarev.appmodbusrtulogparser.repository.DataEntityRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintParseDataRequest03 implements PrintParseData{
    private final DataEntityRepository dataEntityRepository;
    @Override
    public List<String> print() {
        List<Request03AO> requests = dataEntityRepository.getRequest03AOS();
        List<String> parseRequests = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nВсе запросы 0х03:");
        for (Request03AO request : requests) {
            sb.append("\nДата/Время: ").append(request.getDate())
                    .append(" / ").append(request.getTime());
            sb.append("\nАдрес устройства/Функциональный код: 0")
                    .append(parseHexToInt(request.getDeviceAddress())).append(" / ")
                    .append(request.getCodeFunction());
            sb.append("\nАдрес первого регистра (Hi, Lo): ").append(parseHexToInt(request.getFirstRegisterAddress()));
            sb.append("\nКоличество регистров (Hi, Lo): ").append(parseHexToInt(request.getCountRegisters()));
            sb.append("\nCRC: ").append(request.getCyclicRedundancyCheck());
            sb.append("\n").append("-".repeat(40));
            parseRequests.add(sb.toString());
        }
        return parseRequests;
    }

    public int parseHexToInt(String value) {
        return Integer.parseInt(value, 16);
    }
}
