package flashcards;

import java.util.Objects;

class Flashcard {
    private final String definition;

    private int mistakes;

    public Flashcard(String definition, int mistakes) {
        this.definition = Objects.requireNonNull(definition);
        this.mistakes = mistakes;
    }

    public Flashcard(String definition) {
        this(definition, 0);
    }

    public String getDefinition() {
        return definition;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void increaseMistakeCount() {
        mistakes++;
    }

    public void resetMistakes() {
        mistakes = 0;
    }
}
