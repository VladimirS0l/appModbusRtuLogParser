package ru.solarev.appmodbusrtulogparser.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.solarev.appmodbusrtulogparser.model.entityForParse.FunctionWrite16;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataFunctionWrite16 {

    @Getter
    private final Map<String, FunctionWrite16> functionWrite16Map;

    public DataFunctionWrite16() {
        functionWrite16Map = new HashMap<>();
        init();
    }

    public void init() {
        functionWrite16Map.put("01H", new FunctionWrite16("01H",
                "Время зарядки (оставшее время зарядки)",
                "Сек",
                1.0,
                null,
                "SUM_LH"));

        functionWrite16Map.put("02H", new FunctionWrite16("02H",
                "Текущая мощность на выходе",
                "кВт",
                0.1,
                null,
                "SUM_LH"));

        functionWrite16Map.put("03H", new FunctionWrite16("03H",
                "Потребленная мощность за сессию",
                "кВт*ч",
                0.1,
                null,
                "SUM_LH"));

        functionWrite16Map.put("04H", new FunctionWrite16("04H",
                "Потребленная мощность за все время",
                "кВт*ч",
                0.1,
                null,
                "SUM_LH"));
        functionWrite16Map.put("05L", new FunctionWrite16("05L",
                "Уровень зарядки",
                "%",
                1,
                null,
                "ONE_REG"));
        functionWrite16Map.put("06H", new FunctionWrite16("06H",
                "Напряжение выхода",
                "B",
                1.0,
                null,
                "SUM_LH"));
        functionWrite16Map.put("07H", new FunctionWrite16("07H",
                "Ток выхода",
                "A",
                1.0,
                null,
                "SUM_LH"));
        functionWrite16Map.put("08H", new FunctionWrite16("08H",
                "Флаги",
                "",
                0.0,
                new HashMap<>(Map.of(0, "1 — аварийное откл.")),
                "08H_BITS"));
        functionWrite16Map.put("08L", new FunctionWrite16("08L",
                "Флаги",
                "",
                0.0,
                new HashMap<>(Map.of(0, "1 – Start", 1, "1 – Stop",
                        2, " 1 – Work (в работе, по окончанию -> 0)",
                        3, "1 – Interrupt (запрос снижения нагрузки)",
                        4, "1 – ШИМ вкл.")),
                "08L_BITS"));
        functionWrite16Map.put("09H", new FunctionWrite16("09H",
                "Максимальное напряжение EVSE ",
                "B",
                1.0,
                null,
                "SUM_LH"));
        functionWrite16Map.put("0AH", new FunctionWrite16("0AH",
                "Максимальный ток EVSE ",
                "A",
                1.0,
                null,
                "SUM_LH"));
        functionWrite16Map.put("0BH", new FunctionWrite16("0BH",
                "Максимальная мощность EVSE",
                "кВт",
                1.0,
                null,
                "SUM_LH"));
        functionWrite16Map.put("0СH", new FunctionWrite16("08L",
                "Индикация",
                "",
                0.0,
                new HashMap<>(Map.of(1, "Загрузка",
                        2, "Вставьте разъем CHAdeMO подключение",
                        3, "Вставьте разъем CCS Combo 2 / подключение",
                        4, "Связь установлена",
                        5, "Нажмите «СТАРТ»",
                        6, "Запуск",
                        7, "Зарядка/Pout/Уровень...",
                        8, "Остановка",
                        9, "Извлеките разъем / Нажмите «СТОП»",
                        10, "Авария")),
                "SUM_BITS"));
        functionWrite16Map.put("0DH", new FunctionWrite16("08L",
                "Индикация",
                "",
                0.0,
                new HashMap<>(Map.of(0, "INVALID", 1, "VALID",
                        2, "WARNING",
                        3, "FAULT",
                        4, "No_IMD")),
                "BITS"));
        functionWrite16Map.put("0DL", new FunctionWrite16("08L",
                "Индикация",
                "",
                0.0,
                new HashMap<>(Map.of(0, "EVSE_NotREADY", 1, "EVSE_Ready",
                        2, "EVSE_Shutdown",
                        3, "EVSE_UtilityInterruptEvent",
                        4, "EVSE_IsolationMonitoringActive",
                        5, "EVSE_EmergencyShutdown",
                        6, "EVSE_Malfunction")),
                "BITS"));
        functionWrite16Map.put("0EH", new FunctionWrite16("0BH",
                "EVSE_NOTIFICATIONMAXDELAY",
                "Cек",
                0.0,
                null,
                "SEC"));

    }


}
