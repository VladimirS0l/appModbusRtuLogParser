package ru.solarev.appmodbusrtulogparser.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Request16AO {
    private String date;
    private String time;
    private String deviceAddress;
    private String codeFunction;
    private String firstRegisterAddress;
    private String countRegisters;
    private String countBytes;
    private List<String> dataRegisters = new ArrayList<>();
    private String cyclicRedundancyCheck;

}
