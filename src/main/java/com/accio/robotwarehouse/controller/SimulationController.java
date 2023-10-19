package com.accio.robotwarehouse.controller;

import com.accio.robotwarehouse.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    @Autowired
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/start")
    public void startSimulation() {
        simulationService.startSimulation();
    }

    @GetMapping("/download-csv")
    public void downloadCSV(HttpServletResponse response) {
        try {
            String csvFilePath = "performance_report.csv";
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=performance_report.csv");
            FileInputStream fileInputStream = new FileInputStream(new File(csvFilePath));
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            fileInputStream.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
