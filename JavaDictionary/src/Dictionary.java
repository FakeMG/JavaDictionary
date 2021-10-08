import java.util.ArrayList;

public class Dictionary {
    public ArrayList<Word> wordArray;

    public Dictionary() {
        wordArray = new ArrayList<Word>();
    }

    public Dictionary(ArrayList<Word> newWordArray) {
        this.wordArray = newWordArray;
    }

    public ArrayList<Word> getWordArray() {
        return wordArray;
    }

    public void setWordArray(ArrayList<Word> wordArray) {
        this.wordArray = wordArray;
    }

    public void addWord(Word word) {
        wordArray.add(word);
    }
}
