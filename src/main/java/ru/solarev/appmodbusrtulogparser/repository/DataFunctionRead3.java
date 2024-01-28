package ru.solarev.appmodbusrtulogparser.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.solarev.appmodbusrtulogparser.model.entityForParse.FunctionRead3;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataFunctionRead3 {
    private Map<Integer, String> stateCode01 = new HashMap<>(
            Map.of(0, "Нет данных", 1, "ISO15118", 2, "DIN70121"));
    private Map<Integer, String> stateCode02 = new HashMap<>(
            Map.of(0, "Нет данных", 1, "Started", 2, "Finished"));
    private Map<Integer, String> stateCode03 = new HashMap<>(
            Map.of(0, "нет данных", 1, "A", 2, "B",
                    3, "C", 4, "D", 5, "E", 6, "F"));

    private Map<Integer, String> stateCode04 = new HashMap<>(
            Map.of(0, "Нет данных", 1, "Нет связи", 2, "Связь установлена"));
    private Map<Integer, String> stateCode05 = new HashMap<>(
            Map.of(0, "OFF", 1, "ON"));
    private Map<Integer, String> stateCode06 = new HashMap<>(
            Map.of(0, "Нет ошибок", 1, "Ошибки", 2, "Ошибки", 3, "Ошибки",
                    4, "Ошибки", 5, "Ошибки", 6, "Ошибки", 7,
                    "Ошибки", 11, "Нет данных"));


    @Getter
    private final Map<String, FunctionRead3> functionRead3Map;

    public DataFunctionRead3() {
        functionRead3Map = new HashMap<>();
        init();
    }

    //PostConstruct
    public void init() {
        functionRead3Map.put("01H76", new FunctionRead3("01H76",
                new int[]{7, 6},
                "Стандарт подключения авто",
                "INIT_PROTOCOL",
                stateCode01,
                "",
                "BITS"));
        functionRead3Map.put("01H32", new FunctionRead3("01H32",
                new int[]{3, 2},
                "Статус авторизации пользователя",
                "AUTH_STATUS ",
                stateCode02,
                "",
                "BITS"));
        functionRead3Map.put("01H54", new FunctionRead3("01H54",
                new int[]{5, 4},
                "Статус подключения авто",
                "INIT_STATUS",
                stateCode02,
                "",
                "BITS"));
        functionRead3Map.put("01H10", new FunctionRead3("01H10",
                new int[]{1, 0},
                "Статус получения параметров ЗУ",
                "PARAMETER_STATUS",
                stateCode02,
                "",
                "BITS"));
        functionRead3Map.put("08H70", new FunctionRead3("08H70",
                new int[]{7, 0},
                "Целевой ток",
                "Itarget",
                null,
                "А",
                "SUM_LH"));
        functionRead3Map.put("09H74", new FunctionRead3("09H74",
                new int[]{7, 4},
                "Статус CP",
                "CP_STATUS",
                stateCode03,
                "",
                "BITS"));
        functionRead3Map.put("09H30", new FunctionRead3("09H30",
                new int[]{3, 0},
                "Статус связи",
                "TCP_STATUS",
                stateCode04,
                "",
                "BITS"));
        functionRead3Map.put("09L70", new FunctionRead3("09L70",
                new int[]{7, 0},
                "Уровень ШИМ",
                "PWM_DUTY_CYCLE",
                null,
                "%",
                "ONE_REG"));
        functionRead3Map.put("0AH70", new FunctionRead3("0AH70",
                new int[]{7, 0},
                "Версия ПО (платы ChS-RA01_CCS)",
                "VERSION_PO",
                null,
                "",
                "ONE_REG"));
        functionRead3Map.put("0AL70", new FunctionRead3("0AL70",
                new int[]{7, 0},
                "ШИМ вкл./выкл.",
                "PWM_STATUS",
                stateCode05,
                "",
                "ONE_REG"));
        functionRead3Map.put("01L76", new FunctionRead3("01L76",
                new int[]{7, 6},
                "Статус проверки изоляции",
                "ISOLATION_STATUS",
                stateCode02,
                "",
                "BITS"));
        functionRead3Map.put("01L54", new FunctionRead3("01L54",
                new int[]{5, 4},
                "Статус предварительной зарядки",
                "PRECHARGE_STATUS",
                stateCode02,
                "",
                "BITS"));
        functionRead3Map.put("01L32", new FunctionRead3("01L32",
                new int[]{3, 2},
                "Статус зарядки",
                "CHARGE_STATUS",
                stateCode02,
                "",
                "BITS"));
        functionRead3Map.put("01L10", new FunctionRead3("01L10",
                new int[]{1, 0},
                "Статус сварки контактов",
                "WELDING_STATUS",
                stateCode02,
                "",
                "BITS"));
        functionRead3Map.put("02H10", new FunctionRead3("02H10",
                new int[]{1, 0},
                "Готовность/остановка",
                "READY_STOP",
                stateCode02,
                "",
                "BITS"));
        functionRead3Map.put("02L50", new FunctionRead3("02L50",
                new int[]{5, 0},
                "Наличие ошибки",
                "ERROR",
                stateCode06,
                "",
                "BITS"));
        functionRead3Map.put("03H70", new FunctionRead3("03H70",
                new int[]{7, 0},
                "Уровень полного заряда батареи",
                "FULLSOC",
                null,
                "%",
                "PROC_MAX"));
        functionRead3Map.put("03L70", new FunctionRead3("03L70",
                new int[]{7, 0},
                "Уровень текущего заряда батареи",
                "RESSOC",
                null,
                "%",
                "PROC_CUR"));
        functionRead3Map.put("04H70", new FunctionRead3("04H70",
                new int[]{7, 0},
                "Максимально допустимое напряжение EV",
                "Vmax",
                null,
                "В",
                "SUM_LH"));
        functionRead3Map.put("05H70", new FunctionRead3("05H70",
                new int[]{7, 0},
                "Максимально допустимый ток EV",
                "Imax",
                null,
                "А",
                "SUM_LH"));
        functionRead3Map.put("06H70", new FunctionRead3("06H70",
                new int[]{7, 0},
                "Максимально допустимая мощность EV",
                "Pmax",
                null,
                "кВт",
                "SUM_LH"));
        functionRead3Map.put("07H70", new FunctionRead3("07H70",
                new int[]{7, 0},
                "Целевое напряжение",
                "Vtarget",
                null,
                "В",
                "SUM_LH"));
        functionRead3Map.put("0BH70", new FunctionRead3("0BH70",
                new int[]{7, 0},
                "EVCC ID",
                "EVCCID Hex",
                null,
                "Hex",
                "SUM_LH"));
        functionRead3Map.put("0CH70", new FunctionRead3("0CH70",
                new int[]{7, 0},
                "EVCC ID",
                "EVCCID Hex",
                null,
                "Hex",
                "SUM_LH"));
        functionRead3Map.put("0DH70", new FunctionRead3("0DH70",
                new int[]{7, 0},
                "EVCC ID",
                "EVCCID Hex",
                null,
                "Hex",
                "SUM_LH"));
        functionRead3Map.put("0EH70", new FunctionRead3("0EH70",
                new int[]{7, 0},
                "EVCC ID",
                "EVCCID Hex",
                null,
                "Hex",
                "SUM_LH"));

        functionRead3Map.put("0FH70", new FunctionRead3("0FH70",
                new int[]{7, 0},
                "Версия ПШМА",
                "VERSION CCS hex",
                null,
                "hex",
                "ONE_REG"));

        functionRead3Map.put("0FL70", new FunctionRead3("0FL70",
                new int[]{7, 0},
                "Установки ПШМА",
                "Settings CCS hex",
                null,
                "hex",
                "ONE_REG"));







    }

}
