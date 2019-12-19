package com.umssonline.dms.api;

import com.umssonline.dms.api.restws.api.MaintenanceController;
import com.umssonline.dms.api.restws.impl.MaintenanceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author franco.arratia@umssonline.com
 */

@SpringBootApplication
public class Application {

    public static void main(String args[]) {

        MaintenanceController maintenance = new MaintenanceImpl();
        SpringApplication.run(Application.class, args);
    }
}
