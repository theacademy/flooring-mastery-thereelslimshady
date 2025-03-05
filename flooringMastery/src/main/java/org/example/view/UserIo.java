package org.example.view;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserIo {

    void print(String msg);
    int readInt(String prompt);
    String readString(String prompt);
    BigDecimal readBigDecimal(String prompt);
    LocalDate readDate(String prompt);

}
