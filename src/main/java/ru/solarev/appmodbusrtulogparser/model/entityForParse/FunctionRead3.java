package ru.solarev.appmodbusrtulogparser.model.entityForParse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class FunctionRead3 {
    private String address;
    private int[] bit;
    private String description;
    private String symbolParameter;
    private Map<Integer, String> stateCode;
    private String unit;
    private String state;

}
