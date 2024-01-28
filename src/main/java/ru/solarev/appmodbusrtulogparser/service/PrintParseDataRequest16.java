package ru.solarev.appmodbusrtulogparser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.solarev.appmodbusrtulogparser.model.Request16AO;
import ru.solarev.appmodbusrtulogparser.repository.DataEntityRepository;
import ru.solarev.appmodbusrtulogparser.repository.DataFunctionWrite16;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintParseDataRequest16 implements PrintParseData{
    private final DataEntityRepository dataEntityRepository;
    private final DataFunctionWrite16 dataFunctionWrite16;
    @Override
    public List<String> print() {
        List<Request16AO> request16AOS = dataEntityRepository.getRequest16AOS();
        List<String> parseRequests16 = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nВсе запросы 0х10:\n");
        for (Request16AO request : request16AOS) {
            sb.append("\nДата/Время: ").append(request.getDate())
                    .append(" / ").append(request.getTime());
            sb.append("\nАдрес устройства/Функциональный код: 0")
                    .append(parseHexToInt(request.getDeviceAddress())).append(" / ")
                    .append(request.getCodeFunction());
            sb.append("\nАдрес первого регистра (Hi, Lo): ").append(parseHexToInt(request.getFirstRegisterAddress()));
            sb.append("\nКоличество регистров (Hi, Lo): ").append(parseHexToInt(request.getCountRegisters()));
            sb.append("\nКоличество байт далее: ").append(parseHexToInt(request.getCountBytes()));
            sb.append("\nДанные записаны:");
            List<String> registers = request.getDataRegisters();
            List<String> updateRegister = registersWriteInTableValues(registers);
            for (String s : updateRegister) {
                if (!s.isEmpty()) {
                    sb.append(s);
                }
            }
            sb.append("\nCRC: ").append(request.getCyclicRedundancyCheck());
            sb.append("\n").append("-".repeat(40));
            parseRequests16.add(sb.toString());
        }
        return parseRequests16;
    }

    private List<String> registersWriteInTableValues(List<String> registers) {
        List<String> presentData = new ArrayList<>();
        int count = 0;
        for (String reg : registers) {
            ++count;
            if (reg.equals("0000")) {
                continue;
            }
            final int middle = 2;
            String high = reg.substring(0, 2); //FF
            String low = reg.substring(middle); //0A

            presentData.add(addElement16(count, "H", high, reg));
            presentData.add(addElement16(count, "L", low, reg));
        }
        return presentData;
    }

    private String addElement16(int count, String h, String high, String reg) {
        String hex = String.format("%X", count);
        StringBuilder sb = new StringBuilder();
        String code = "0" + hex + h;
        var data = dataFunctionWrite16.getFunctionWrite16Map().getOrDefault(code, null);
        if (data != null) {
            sb.append("\nАдрес: ").append(data.getAddress()).append(", Описание: ")
                    .append(data.getDescription());
            int countBits = 0;

            switch (data.getState()) {
                case "ONE_REG":
                    sb.append(", Значение: ").append(parseHexToInt(high)* data.getAccuracy()).append(data.getUnit());
                    break;
                case "SUM_LH":
                    sb.append(", Значение: ").append(parseHexToInt(reg)*data.getAccuracy()).append(data.getUnit());
                    break;
                case "SEC":
                    sb.append(", Значение: ").append(parseHexToInt(reg)).append(data.getUnit());
                    break;
                case "SUM_BITS":
                    String s = hexToBin(reg);
                    if (s.length() < 16) {
                        s = "0".repeat(16 - s.length()) + s;
                    }
                    String sr = new StringBuilder(s).reverse().toString();
                    for (int i = 0; i < 16; i++) {
                        if (sr.charAt(i) == '1') {
                            countBits++;
                        }
                    }
                    sb.append(", Значение: ").append(data.getFlags().get(countBits));
                    break;
                case "BITS":
                    String binaryBytes = fullStringBitsAndReverse(hexToBin(high));
                    for (int i = 0; i < 7; i++) {
                        if (binaryBytes.charAt(i) == '1') {
                            countBits++;
                        }
                    }
                    sb.append(", Значение: ").append(data.getFlags().get(countBits));
                    break;
                case "08H_BITS":
                    String bitss = fullStringBitsAndReverse(hexToBin(high));
                    if (bitss.charAt(0) == '1') {
                        sb.append(", Значение: ").append(data.getFlags().get(0));
                    }
                    break;
                case "08L_BITS":
                    String bits = fullStringBitsAndReverse(hexToBin(high));
                    if (bits.charAt(0) == '1') {
                        sb.append(", Значение: ").append(data.getFlags().get(0));
                    } else if (bits.charAt(1) == '1') {
                        sb.append(", Значение: ").append(data.getFlags().get(1));
                    }
                    else if (bits.charAt(2) == '1') {
                        sb.append(", Значение: ").append(data.getFlags().get(2));
                    }
                    else if (bits.charAt(3) == '1') {
                        sb.append(", Значение: ").append(data.getFlags().get(3));
                    }
                    break;
            }
        }
        return sb.toString();
    }

    public String hexToBin(String s) {
        return new BigInteger(s, 16).toString(2);
    }

    public String fullStringBitsAndReverse(String s) {
        if (s.length() < 8) {
            s = "0".repeat(8 - s.length()) + s;
        }
        String sr = new StringBuilder(s).reverse().toString();
        return sr;
    }

    public int parseHexToInt(String value) {
        return Integer.parseInt(value, 16);
    }
}
