package flashcards;

import java.io.*;
import java.util.*;

public class Main {
    private static final FlashcardManager flashcardManager = new FlashcardManager();
    private static final List<String> logger = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String importPath = null;
        String exportPath = null;

        for (int i = 0; i < args.length - 1; i++) {
            if ("-import".equals(args[i])) {
                importPath = args[i + 1];
            } else if ("-export".equals(args[i])) {
                exportPath = args[i + 1];
            }
        }

        if (importPath != null) {
            try {
                int cardCount = flashcardManager.importCards(importPath);

                System.out.println(cardCount + " cards have been loaded.");
                logger.add(cardCount + " cards have been loaded.");
            } catch (FileNotFoundException e) {
                System.out.println("File not found.");
                logger.add("File not found.");
            }
        }

        String command;
        do {
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            logger.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");

            command = scanner.nextLine().trim();
            logger.add(command);

            switch (command) {
                case "exit":
                    break;
                case "add":
                    Main.addCard();
                    break;
                case "remove":
                    Main.removeCard();
                    break;
                case "import":
                    Main.importCards();
                    break;
                case "export":
                    Main.exportCards();
                    break;
                case "ask":
                    Main.askCards();
                    break;
                case "log":
                    Main.log();
                    break;
                case "hardest card":
                    Main.hardestCard();
                    break;
                case "reset stats":
                    Main.resetStats();
                    break;
                default:
                    // Do nothing for now
            }
        } while (!command.equals("exit"));

        logger.clear();
        scanner.close();

        System.out.println("Bye bye!");

        if(exportPath != null){
            try {
                int cardCount = flashcardManager.exportCards(exportPath);
                System.out.println(cardCount + " cards have been saved.");
            } catch (FileNotFoundException e) {
                System.out.println("Can't export cards: " + e.getMessage());
            }
        }
    }

    private static void addCard() {
        System.out.println("The card:");
        logger.add("The card:");

        String name = scanner.nextLine().trim();
        logger.add(name);

        if (flashcardManager.searchByName(name) != null) {
            System.out.println("The card \"" + name + "\" already exists");
            logger.add("The card \"" + name + "\" already exists");
            return;
        }

        System.out.println("The definition of the card:");
        logger.add("The definition of the card:");

        String definition = scanner.nextLine().trim();
        logger.add(definition);

        if (flashcardManager.searchByDefinition(definition) != null) {
            System.out.println("The definition \"" + definition + "\" already exists.");
            logger.add("The definition \"" + definition + "\" already exists.");
            return;
        }

        flashcardManager.addCard(name, definition);
        System.out.println("The pair (\"" + name + "\":\"" + definition + "\") has been added.");
        logger.add("The pair (\"" + name + "\":\"" + definition + "\") has been added.");
    }

    private static void removeCard() {
        System.out.println("The card:");
        logger.add("The card:");

        String name = scanner.nextLine().trim();
        logger.add(name);

        if (flashcardManager.removeCard(name) != null) {
            System.out.println("The card has been removed.");
            logger.add("The card has been removed.");
        } else {
            System.out.println("Can't remove \"" + name + "\": there is no such card.");
            logger.add("Can't remove \"" + name + "\": there is no such card.");
        }
    }

    private static void importCards() {
        System.out.println("File name:");
        logger.add("File name:");

        String filepath = scanner.nextLine().trim();
        logger.add(filepath);

        try {
            int cardCount = flashcardManager.importCards(filepath);

            System.out.println(cardCount + " cards have been loaded.");
            logger.add(cardCount + " cards have been loaded.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            logger.add("File not found.");
        }
    }

    private static void exportCards() {
        System.out.println("File name:");
        logger.add("File name:");

        String filepath = scanner.nextLine().trim();
        logger.add(filepath);

        try {
            int cardCount = flashcardManager.exportCards(filepath);

            System.out.println(cardCount + " cards have been saved.");
            logger.add(cardCount + " cards have been saved.");
        } catch (FileNotFoundException e) {
            System.out.println("Can't export cards: " + e.getMessage());
            logger.add("Can't export cards: " + e.getMessage());
        }
    }

    private static void askCards() {
        System.out.println("How many times to ask?");
        logger.add("How many times to ask?");

        String askCount = scanner.nextLine().trim();
        logger.add(askCount);

        String[] cardNames = flashcardManager.getRandomCardNames(Integer.parseInt(askCount));

        for (String name : cardNames) {
            String correctAnswer = flashcardManager.searchByName(name).getDefinition();

            System.out.println("Print the definition of \"" + name + "\":");
            logger.add("Print the definition of \"" + name + "\":");

            String userAnswer = scanner.nextLine().trim();
            logger.add(userAnswer);

            if (userAnswer.equals(correctAnswer)) {
                System.out.println("Correct answer.");
                logger.add("Correct answer.");
            } else {
                String flashcard = flashcardManager.searchByDefinition(userAnswer); // @TODO rename variable to better describe a correct answer for a different card

                if (flashcard != null) {
                    System.out.println("Wrong answer. The right answer is \"" + correctAnswer + "\", but your definition is correct for \"" + flashcard + "\".");
                    logger.add("Wrong answer. The right answer is \"" + correctAnswer + "\", but your definition is correct for \"" + flashcard + "\".");
                } else {
                    System.out.println("Wrong answer. The right answer is \"" + correctAnswer + "\".");
                    logger.add("Wrong answer. The right answer is \"" + correctAnswer + "\".");
                }

                flashcardManager.searchByName(name).increaseMistakeCount();
            }
        }
    }

    private static void log() {
        System.out.println("File name:");
        logger.add("File name:");

        String filepath = scanner.nextLine().trim();
        logger.add(filepath);

        try (PrintWriter printWriter = new PrintWriter(new File(filepath))) {
            logger.forEach(printWriter::println);
        } catch (FileNotFoundException e) {
            System.out.println("Can't create log: " + e.getMessage());
            logger.add("Can't create log: " + e.getMessage());
        }

        System.out.println("The log has been saved.");
        logger.add("The log has been saved.");
    }

    private static void hardestCard() {
        Map<String, Integer> hardestCards = flashcardManager.hardestCards();

        if (hardestCards.size() == 0) {
            System.out.println("There are no cards with errors.");
            logger.add("There are no cards with errors.");
        } else {
            String[] names = hardestCards.keySet().toArray(new String[0]);
            Integer mistakes = hardestCards.values().toArray(new Integer[0])[0];

            if (names.length == 1) {
                System.out.println("The hardest card is \"" + names[0] + "\". You have " + mistakes + " errors answering it.");
                logger.add("The hardest card is \"" + names[0] + "\". You have " + mistakes + " errors answering it.");
            } else {
                System.out.println("The hardest cards are \"" + String.join("\", \"", names) + "\". You have " + mistakes + " errors answering them.");
                logger.add("The hardest cards are \"" + String.join("\", \"", names) + "\". You have " + mistakes + " errors answering them.");
            }
        }
    }

    private static void resetStats() {
        flashcardManager.resetStats();

        System.out.println("Card statistics has been reset.");
        logger.add("Card statistics has been reset.");
    }
}
