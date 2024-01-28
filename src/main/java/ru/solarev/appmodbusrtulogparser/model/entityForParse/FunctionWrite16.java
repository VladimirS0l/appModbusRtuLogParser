package ru.solarev.appmodbusrtulogparser.model.entityForParse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class FunctionWrite16 {
    private String address;
    private String description;
    private String unit;
    private double accuracy;
    private Map<Integer, String> flags;
    private String state;
}
