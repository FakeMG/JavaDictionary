public class Dictionary {
    private int numOfWords = 0;
    private Word[] wordArray;

    public Dictionary() {

    }

    public Dictionary(int numOfWords) {
        wordArray = new Word[numOfWords];
        this.numOfWords = numOfWords;
    }

    public Dictionary(Word[] newWordArray) {
        this.wordArray = newWordArray;
        this.numOfWords = newWordArray.length;
    }

    public int getNumOfWords() {
        return numOfWords;
    }

    public void setNumOfWords(int numOfWords) {
        this.numOfWords = numOfWords;
    }

    public Word[] getWordArray() {
        return wordArray;
    }

    public void setWordArray(Word[] wordArray) {
        this.wordArray = wordArray;
    }
}
