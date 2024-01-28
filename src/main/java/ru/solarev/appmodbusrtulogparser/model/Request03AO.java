package ru.solarev.appmodbusrtulogparser.model;

import lombok.Data;

@Data
public class Request03AO {
    private String date;
    private String time;
    private String deviceAddress;
    private String codeFunction;
    private String firstRegisterAddress;
    private String countRegisters;
    private String cyclicRedundancyCheck;
}
