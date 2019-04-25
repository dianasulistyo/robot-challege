package com.technical.challenge.service;

import com.technical.challenge.exception.IllegalCommandException;
import com.technical.challenge.exception.IllegalMoveException;
import com.technical.challenge.model.Direction;
import com.technical.challenge.model.Robot;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Log4j2
@Data
public class RobotService {

    private Queue<String> commands;

    private static final int BOARD_SIZE = 5;
    Robot[][] board = new Robot[BOARD_SIZE][BOARD_SIZE];
    boolean flag = false;

    public void run(Robot robot, Queue<String> commands) {
        if(commands.size() < 1) {
            log.error("Command should start with 'PLACE' then [MOVE|LEFT|RIGHT] and end with 'REPORT");
            return;
        }

        this.commands = commands;
        String place = commands.poll();

        Pattern pattern = Pattern.compile("PLACE\\s(\\d),(\\d),(NORTH|EAST|SOUTH|WEST)");
        Matcher matcher = pattern.matcher(place);

        if(!matcher.find()) {
            log.error("First command should start with 'PLACE x,y,[NORTH|EAST|SOUTH|WEST]'");
            throw new IllegalCommandException("First command did not start with 'PLACE'");
        }

        int x = Integer.parseInt(matcher.group(1));

        //To flip the index number for Y, to make southwest(0,0)
        int y = BOARD_SIZE-1-Integer.parseInt(matcher.group(2));

        Direction direction = Direction.valueOf(matcher.group(3));
        robot.setX(x);
        robot.setY(y);
        robot.setDirection(direction);
        board[x][y] = robot;
        log.debug("Place Robot id={} {}", robot.getId(), robot.printForUser(BOARD_SIZE));

        while(!commands.peek().equals("REPORT") && !flag) {
            moveAll(robot);
            log.debug("Robot id={} {}", robot.getId(), robot.printForUser(BOARD_SIZE));
        }

        //flip y again to print in user's board value
        log.info(robot.printForUser(BOARD_SIZE));

        if(flag) {
            throw new IllegalMoveException("Cannot move outside board. board size="+ BOARD_SIZE);
        }

        log.debug("Move completed for Robot id={} {}", robot.getId(), robot.printForUser(BOARD_SIZE));
    }

    private void moveAll(Robot robot) {
        String command = commands.poll();

        Direction current = robot.getDirection();
        switch (command){
            case "MOVE":
                move(robot);
                break;
            case "LEFT":
                robot.setDirection(Direction.valueOf(current.getLeft()));
                break;
            case "RIGHT":
                robot.setDirection(Direction.valueOf(current.getRight()));
                break;
            default: break;
        }
    }

    private void move(Robot robot) {
        int previousX = robot.getX();
        int previousY = robot.getY();
        int x = robot.getX();
        int y = robot.getY();
        int next;
        switch (robot.getDirection()) {
            case NORTH:
                next = --y;
                if(!validateMove(next)) {
                    log.debug("Cannot move outside board.");
                    flag = true;
                    y = previousY;
                    break;
                }
                board[x][next] = robot;
                board[x][previousY] = null;
                break;
            case EAST:
                next = ++x;
                if(!validateMove(next)) {
                    log.debug("Cannot move outside board.");
                    flag = true;
                    x = previousX;
                    break;
                }
                board[next][y] = robot;
                board[previousX][y] = null;
                break;
            case SOUTH:
                next = ++y;
                if(!validateMove(next)) {
                    log.debug("Cannot move outside board.");
                    flag = true;
                    y = previousY;
                    break;
                }
                board[x][next] = robot;
                board[x][previousY] = null;
                break;
            case WEST:
                next = --x;
                if(!validateMove(next)) {
                    log.debug("Cannot move outside board.");
                    flag = true;
                    x = previousX;
                    break;
                }
                board[next][y] = robot;
                board[previousX][y] = null;
                break;
        }

        robot.setX(x);
        robot.setY(y);
        robot.setYInUserPerspective(BOARD_SIZE-1-y);
    }

    private boolean validateMove(int next) {
        if(next >=0 && next < BOARD_SIZE) return true;
        return false;
    }
}
