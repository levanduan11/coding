package com.coding.datastructure.array;


import java.util.Map;
import java.util.function.Consumer;

public class ControllerTable {
    public static class CommandHandler {
        public static final String PRINT = "print";
        public static final String EXIT = "exit";
        private final Map<String, Consumer<String[]>> commandTable;

        public CommandHandler() {
            this.commandTable = Map.of(PRINT, this::handlePrint, EXIT, this::handleExit);
        }

        private void handlePrint(String[] args) {
            if (args.length > 1) {
                System.out.println(args[1]);
            } else {
                System.out.println("Nothing to print");
            }
        }

        private void handleExit(String[] args) {
            System.out.println("Exiting");
            System.exit(0);
        }

        public void executeCommand(String commandLine) {
            String[] parts = commandLine.split(" ");
            Consumer<String[]> command = commandTable.get(parts[0]);
            if (command != null) {
                command.accept(parts);
            } else {
                System.out.println("Unknown command: " + parts[0]);
            }
        }
    }

    public static CommandHandler commandHandler() {
        return new CommandHandler();
    }

    public static void main(String[] args) {
        CommandHandler commanded = commandHandler();
        commanded.executeCommand("print hello");
    }
}
