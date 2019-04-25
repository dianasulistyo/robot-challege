package com.technical.challenge;

import com.technical.challenge.model.Robot;
import com.technical.challenge.service.RobotService;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


@Log4j2
public class ChallengeApplication {
    public static void main(String[] args) {
        System.out.println("=== START ===");

        Queue<String> commands = new LinkedList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Start");
            while (true) {
                String line = scanner.nextLine();
                commands.add(line);
                if (line.equals("REPORT")) {
                    break;
                }
            }
        }

        RobotService robotService = new RobotService();
        Robot robot = new Robot("DEFAULT");
        robotService.run(robot, commands);
    }
}
