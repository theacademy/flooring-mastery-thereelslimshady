package org.example;

import org.example.controller.FlooringController;
import org.example.dao.OrderDataPersistanceException;
import org.example.view.FlooringView;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) throws OrderDataPersistanceException {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("org.example");
        appContext.refresh();

        FlooringController controller = appContext.getBean("flooringController", FlooringController.class);
        controller.run();
    }
}