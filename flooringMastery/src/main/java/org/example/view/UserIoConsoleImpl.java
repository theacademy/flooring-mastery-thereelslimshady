package org.example.view;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

@Component
public class UserIoConsoleImpl implements UserIo{

    Scanner scanner = new Scanner(System.in);

    public void print(String message){
        System.out.println(message);
    }

    public String readString(String prompt){
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    public LocalDate readDate(String prompt) {
        System.out.println(prompt);
        try {
            return LocalDate.parse(scanner.nextLine().trim());
        }catch (Exception e){
            throw new InvalidUserInputException("Date format invalid");
        }
    }

    public int readInt(String prompt){
        System.out.println(prompt);
        try{
            return Integer.parseInt(scanner.nextLine());
        }catch (Exception e){
            throw new InvalidUserInputException("Invalid number provided");
        }
    }
}
