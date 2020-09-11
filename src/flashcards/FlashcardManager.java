package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class FlashcardManager {
    private final Map<String, Flashcard> flashcards = new HashMap<>();
    private final Random random = new Random();

    public Flashcard searchByName(String name) {
        return flashcards.get(name);
    }

    public String searchByDefinition(String definition) {
        String name = null;

        for (Map.Entry<String, Flashcard> entry : flashcards.entrySet()) {
            if (Objects.equals(entry.getValue().getDefinition(), definition)) {
                name = entry.getKey();
            }
        }

        return name;
    }

    public void addCard(String name, String definition) {
        flashcards.put(name, new Flashcard(definition));
    }

    public Flashcard removeCard(String name) {
        return flashcards.remove(name);
    }

    public int exportCards(String filepath) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(new File(filepath));
        flashcards.forEach((name, flashcard) -> printWriter.println(name + ":" + flashcard.getDefinition() + ":" + flashcard.getMistakes()));
        printWriter.close();

        return flashcards.size();
    }

    public int importCards(String filepath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filepath));
        int cardCount = 0;

        while (scanner.hasNextLine()) {
            String[] card = scanner.nextLine().split(":");
            flashcards.put(card[0], new Flashcard(card[1], Integer.parseInt(card[2])));
            cardCount++;
        }

        scanner.close();

        return cardCount;
    }

    public String[] getRandomCardNames(int n) {
        String[] cardNames = new String[n];
        String[] keys = flashcards.keySet().toArray(new String[0]);

        for (int i = 0; i < n; i++) {
            cardNames[i] = keys[random.nextInt(keys.length)];
        }

        return cardNames;
    }

    public Map<String, Integer> hardestCards() {
        Map<String, Integer> hardestCards = new HashMap<>();

        int highestMistakeCount = 0;

        for (Flashcard flashcard : flashcards.values()) {
            highestMistakeCount = Math.max(highestMistakeCount, flashcard.getMistakes());
        }

        if (highestMistakeCount == 0) {
            return Map.of();
        }

        for (Map.Entry<String, Flashcard> entry : flashcards.entrySet()) {
            if (entry.getValue().getMistakes() == highestMistakeCount) {
                hardestCards.put(entry.getKey(), highestMistakeCount);
            }
        }

        return Map.copyOf(hardestCards);
    }

    public void resetStats() {
        flashcards.values().forEach(Flashcard::resetMistakes);
    }
}
