package com.technical.challenge;

import com.technical.challenge.exception.IllegalCommandException;
import com.technical.challenge.exception.IllegalMoveException;
import com.technical.challenge.model.Direction;
import com.technical.challenge.model.Robot;
import com.technical.challenge.service.RobotService;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

public class RobotServiceTest {

    private RobotService robotService;

    @Before
    public void setUp() {
        robotService = new RobotService();
    }

    @Test
    public void run() {
        Queue<String> commands = new LinkedList<>();
        commands.add("PLACE 0,0,NORTH");
        commands.add("MOVE");
        commands.add("REPORT");

        Robot robot = new Robot("A");
        robotService.run(robot, commands);

        assertThat(robot.getX()).isEqualTo(0);
        assertThat(robot.getYInUserPerspective()).isEqualTo(1);
        assertThat(robot.getDirection()).isEqualTo(Direction.NORTH);
    }

    @Test
    public void run2() {
        Queue<String> commands = new LinkedList<>();
        commands.add("PLACE 0,0,NORTH");
        commands.add("LEFT");
        commands.add("REPORT");

        Robot robot = new Robot("A");
        robotService.run(robot, commands);

        assertThat(robot.getX()).isEqualTo(0);
        assertThat(robot.getYInUserPerspective()).isEqualTo(0);
        assertThat(robot.getDirection()).isEqualTo(Direction.WEST);
    }

    @Test
    public void shouldMoveAndTurn() {
        Queue<String> commands = new LinkedList<>();
        commands.add("PLACE 1,2,EAST");
        commands.add("MOVE");
        commands.add("MOVE");
        commands.add("LEFT");
        commands.add("MOVE");
        commands.add("REPORT");

        Robot robot = new Robot("A");
        robotService.run(robot, commands);

        assertThat(robot.getX()).isEqualTo(3);
        assertThat(robot.getYInUserPerspective()).isEqualTo(3);
        assertThat(robot.getDirection()).isEqualTo(Direction.NORTH);
    }

    @Test(expected = IllegalMoveException.class)
    public void shouldThrowExceptionWhenMoveOutsideBoardSize() {
        Queue<String> commands = new LinkedList<>();
        commands.add("PLACE 1,2,EAST");
        commands.add("MOVE");
        commands.add("MOVE");
        commands.add("LEFT");
        commands.add("MOVE");
        commands.add("MOVE");
        commands.add("MOVE");
        commands.add("REPORT");

        Robot robot = new Robot("A");
        robotService.run(robot, commands);
    }

    @Test(expected = IllegalCommandException.class)
    public void invalidRun() {
        Queue<String> commands = new LinkedList<>();
        commands.add("MOVE");
        commands.add("REPORT");

        Robot robot = new Robot("A");
        robotService.run(robot, commands);

        assertThat(robot.getX()).isEqualTo(0);
        assertThat(robot.getY()).isEqualTo(0);
        assertThat(robot.getDirection()).isNull();
    }


    @Test(expected = IllegalCommandException.class)
    public void shouldThrowExceptionWhenInvalidCommand() {
        Queue<String> commands = new LinkedList<>();
        commands.add("ABC 0,0,NORTH");

        Robot robot = new Robot("A");
        robotService.run(robot, commands);
    }
}