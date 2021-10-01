import java.util.Scanner;

public class DictionaryManagement {
    Scanner sc = new Scanner(System.in);
    Dictionary mainDictionary = new Dictionary();

    public void insertFromCommandline() {
        int newNumOfWords;
        int oldNumOfWord = mainDictionary.getNumOfWords();
        Word[] currentWordArray = mainDictionary.getWordArray();

        System.out.print("Nhap so luong tu: ");
        newNumOfWords = sc.nextInt();
        sc.nextLine();

        Word[] newWordArray = new Word[oldNumOfWord + newNumOfWords];

        //copy old array
        for (int i = 0; i < oldNumOfWord; i++) {
            newWordArray[i] = currentWordArray[i];
        }

        //insert new words
        String target;
        String explain;
        for (int i = 0; i < newNumOfWords; i++) {
            System.out.print("English word: ");
            target = sc.nextLine();

            System.out.print("Meaning: ");
            explain = sc.nextLine();

            Word newWord = new Word(target, explain);
            newWordArray[oldNumOfWord + i] = newWord;
        }

        mainDictionary.setWordArray(newWordArray);
        mainDictionary.setNumOfWords(oldNumOfWord + newNumOfWords);
    }

    public void insertFromFile() {
        //TODO:
    }

    public void dictionaryLookup() {
        //TODO:
    }

    public Dictionary getMainDictionary() {
        return mainDictionary;
    }

    public static void main(String[] args) {
        DictionaryManagement manager = new DictionaryManagement();
        DictionaryCommandLine.dictionaryBasic(manager);
        DictionaryCommandLine.dictionaryBasic(manager);
    }
}
