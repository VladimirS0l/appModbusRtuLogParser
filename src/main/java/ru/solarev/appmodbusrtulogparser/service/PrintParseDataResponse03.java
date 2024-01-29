package ru.solarev.appmodbusrtulogparser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.solarev.appmodbusrtulogparser.model.Response03AO;
import ru.solarev.appmodbusrtulogparser.model.entityForParse.FunctionRead3;
import ru.solarev.appmodbusrtulogparser.repository.DataEntityRepository;
import ru.solarev.appmodbusrtulogparser.repository.DataFunctionRead3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintParseDataResponse03 implements PrintParseData{
    private final DataEntityRepository dataEntityRepository;
    private final DataFunctionRead3 dataFunctionRead3;
    @Override
    public List<String> print() {
        List<Response03AO> responses = dataEntityRepository.getResponse03AOS();
        List<String> parseResponses = new ArrayList<>();

        for (Response03AO response : responses) {
            StringBuilder sb = new StringBuilder();
            sb.append("\nДата/Время: ").append(response.getDate())
                    .append(" / ").append(response.getTime());
            sb.append("\nАдрес устройства/Функциональный код: 0")
                    .append(parseHexToInt(response.getDeviceAddress())).append(" / ")
                    .append(response.getCodeFunction());
            sb.append("\nКоличество байт далее: ").append(parseHexToInt(response.getCountBytes()));
            sb.append("\nПолученные данные:");
            List<String> registers = response.getDataRegisters();
            List<String> updateRegister = registersInTableValues(registers);
            for (String s : updateRegister) {
                if (!s.isEmpty()) {
                    sb.append(s);
                }
            }
            sb.append("\nCRC: ").append(response.getCyclicRedundancyCheck());
            sb.append("\n").append("-".repeat(40));
            parseResponses.add(sb.toString());
        }
        return parseResponses;
    }

    public List<String> registersInTableValues(List<String> registers) {
        List<String> presentData = new ArrayList<>();
        String[] bitCodes = {"76","54", "32", "10", "70", "74", "50", "30"};
        int count = 0;
        for (String reg : registers) {
            ++count;
            if (reg.equals("0000")) {
                continue;
            }
            final int middle = 2;
            String high = hexToBin(reg.substring(0, 2)); //FF
            String low = hexToBin(reg.substring(middle)); //0A

            presentData.add(addElement(count, "H", bitCodes, high, reg));
            presentData.add(addElement(count, "L", bitCodes, low, reg));
        }
        return presentData;
    }

    public String addElement(int count, String bitName,
                             String[] bitCodes, String bits, String reg) {
        String hex = String.format("%X", count);
        StringBuilder sb = new StringBuilder();
        for (String bit : bitCodes) {

            String code = "0" + hex + bitName + bit;
            var data = dataFunctionRead3.getFunctionRead3Map().getOrDefault(code, null);
            if (data != null) {
                sb = printData(sb, data, bits, reg);
            }
        }
        return sb.toString();
    }

    public StringBuilder printData(StringBuilder sb, FunctionRead3 data, String high, String reg) {
        String decr = data.getDescription();
        String symbolParameter = data.getSymbolParameter();

        sb.append("\nАдрес: ").append(data.getAddress()).append(", Описание: ").append(decr)
                .append(", Символ: (").append(symbolParameter).append("), ");
        int[] bits = data.getBit();
        int countBits = 0;
        String binaryBytes = fullStringBitsAndReverse(high);
        for (int i = bits[1]; i < bits[0]+1; i++) {
            if (binaryBytes.charAt(i) == '1') {
                countBits++;
            }
        }
        switch (data.getState()) {
            case "ONE_REG":
                sb.append("Код состояния: ").append(parseHexToInt(high)).append(data.getUnit());
                break;
            case "SUM_LH":
                sb.append("Код состояния: ").append(parseHexToInt(reg)).append(data.getUnit());
                break;
            case "PROC_MAX":
                sb.append("Код состояния: 100%");
                break;
            case "PROC_CUR":
                String highBit = reg.substring(0, 2);
                String lowBit = reg.substring(2, 4);
                int procCurrent = (parseHexToInt(highBit) / 100) * parseHexToInt(lowBit);
                sb.append("Код состояния: ").append(procCurrent).append(data.getUnit());
                break;
            case "BITS":
                sb.append("Код состояния: ").append(data.getStateCode().get(countBits));
                break;

        }
        return sb;
    }

    public int parseHexToInt(String value) {
        return Integer.parseInt(value, 16);
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
}
