import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DictionaryManagement {
    private FileInputStream fileInputStream = null;
    private BufferedReader bufferedReader = null;

    private Scanner sc = new Scanner(System.in);
    private Dictionary mainDictionary = new Dictionary();

    public void insertFromCommandline() {
        try {
            int newNumOfWords;

            //input
            System.out.print("Nhap so luong tu: ");
            newNumOfWords = sc.nextInt();
            sc.nextLine(); //tránh nhập kí tự xuống dòng

            //insert new words
            String target;
            String explain;
            for (int i = 0; i < newNumOfWords; i++) {
                System.out.print("English word " + (i + 1) + ": ");
                target = sc.nextLine();

                if (checkRepeatedWord(target)) {
                    System.out.println("ERROR: This word has already existed!");
                } else {
                    System.out.print("Meaning: ");
                    explain = sc.nextLine();

                    Word newWord = new Word(target, explain);
                    mainDictionary.getWordArray().add(newWord);
                }
            }
        } catch (InputMismatchException ex) {
            Logger.getLogger(DictionaryManagement.class.getName()).log(Level.SEVERE, "Please type a number!", ex);
        }
    }

    public void insertFromFile() {
        try {
            String url = "D:\\Projects\\JavaDictionary\\JavaDictionary\\src\\dictionaries.txt";
            fileInputStream = new FileInputStream(url);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder currentWord = new StringBuilder();
            Word word = new Word();
            int data = bufferedReader.read();
            char c;

            while (data != -1) {
                c = (char) data;
                //Đọc đến kí tự \t thì lưu lại target word
                if (c == '\t') {
                    if (checkRepeatedWord(currentWord.toString())) {
                        bufferedReader.readLine();
                        System.out.println("\"" + currentWord.toString()
                                + "\" has not been added because it's already existed");
                    } else {
                        word.setWordTarget(currentWord.toString());
                    }
                    currentWord = new StringBuilder();
                } else
                    //Đọc đến kí tự \n thì lưu lại định nghĩa
                    if (c == '\n') {
                        word.setWordExplain(currentWord.toString());
                        currentWord = new StringBuilder();
                        mainDictionary.wordArray.add(word);
                        word = new Word();
                    } else {
                        currentWord.append(c);
                    }
                data = bufferedReader.read();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DictionaryManagement.class.getName()).log(Level.SEVERE, "FILE NOT FOUND!", ex);
        } catch (IOException ex) {
            Logger.getLogger(DictionaryManagement.class.getName()).log(Level.SEVERE, "FAILED TO READ FILE!", ex);
        } finally {
            try {
                bufferedReader.close();
                fileInputStream.close();
            } catch (NullPointerException | IOException ex) {
                Logger.getLogger(DictionaryManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void dictionaryLookup() {
        String target;
        System.out.print("Nhập từ cần tra: ");
        target = sc.nextLine();
        for (int i = 0; i < mainDictionary.getWordArray().size(); i++) {
            if (mainDictionary.getWordArray().get(i).getWordTarget().equals(target)) {
                System.out.println("Meaning: " + mainDictionary.getWordArray().get(i).getWordExplain());
                return;
            }
        }
        System.out.println("Can not find meaning!");
    }

    public Dictionary getMainDictionary() {
        return mainDictionary;
    }

    public boolean checkRepeatedWord(String word) {
        for (int i = 0; i < mainDictionary.getWordArray().size(); i++) {
            if (word.equals(mainDictionary.getWordArray().get(i).getWordTarget())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        DictionaryManagement manager = new DictionaryManagement();
        manager.insertFromCommandline();
        DictionaryCommandLine.showAllWords(manager.getMainDictionary());
    }
}
