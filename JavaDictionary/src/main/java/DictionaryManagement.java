import java.io.*;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DictionaryManagement {
    private FileInputStream fileInputStream = null;
    private BufferedReader bufferedReader = null;
    private FileOutputStream fileOutputStream = null;
    private BufferedWriter bufferedWriter = null;

    private Scanner sc = new Scanner(System.in);
    private Dictionary mainDictionary = new Dictionary();

    public void insertFromCommandline() {
        try {
            int newNumOfWords;

            //input
            System.out.print("Nhập số lượng từ: ");
            newNumOfWords = sc.nextInt();
            sc.nextLine(); //tránh nhập kí tự xuống dòng

            //insert new words
            String target;
            String explain;
            for (int i = 0; i < newNumOfWords; i++) {
                System.out.print("Enter word " + (i + 1) + ": ");
                target = sc.nextLine();

                if (dataBase.checkRepeatedWord(target)) {
                    System.out.println("ERROR: This word has already existed!");
                } else {
                    System.out.print("Meaning: ");
                    explain = sc.nextLine();

                    dataBase.insertToDatabase(target, explain);
                    System.out.println("Word is added!");
                }
            }
        } catch (InputMismatchException ex) {
            Logger.getLogger(DictionaryManagement.class.getName())
                    .log(Level.SEVERE, "ERROR: Please type a number!", ex);
        }
    }

    public void insertFromFile() {
        try {
            String url = "D:\\Projects\\JavaDictionary\\JavaDictionary\\src\\dictionaries.txt";
            fileInputStream = new FileInputStream(url);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder currentWord = new StringBuilder();
            String target = "";
            String explain = "";
            Word word = new Word();
            int data = bufferedReader.read();
            char c;

            while (data != -1) {
                c = (char) data;
                //input có dạng "target\texplain"
                //Đọc đến kí tự \t thì lưu lại target word
                if (c == '\t') {
                    //check từ đã tồn tại
                    if (dataBase.checkRepeatedWord(currentWord.toString())) {
                        bufferedReader.readLine(); //nếu từ đã tồn tại thì chuyển dòng tiếp theo
                        System.out.println("\"" + currentWord.toString()
                                + "\" has not been added because it's already existed");
                    } else {
                        target = currentWord.toString();  //nếu từ chưa tồn tại thì lưu lại vào target
                    }
                    currentWord = new StringBuilder(); //tạo mới lại "currentWord" để lưu từ mới
                } else if (c == '\n') {
                    explain = currentWord.toString();
                    currentWord = new StringBuilder();
                    dataBase.insertToDatabase(target, explain); //insert vào database
                } else {
                    currentWord.append(c);   //nếu kí tự tiếp theo ko phải \t hoặc \n thì đọc tiếp
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

    public void deleteWord() {
        String target;
        System.out.print("Enter a word: ");
        target = sc.nextLine();

        if (!dataBase.checkRepeatedWord(target)) {
            System.out.println("ERROR: This word doesn't exist in dictionary!");
            return;
        }

        dataBase.deleteFromDatabase(target);
        System.out.println("Word is deleted from dictionary!");
    }

    public void updateWord() {
        String target;
        String explain;
        System.out.print("Enter a word: ");
        target = sc.nextLine();

        if (!dataBase.checkRepeatedWord(target)) {
            System.out.println("ERROR: This word doesn't exist in dictionary!");
            return;
        }

        System.out.print("Enter new meaning of the word: ");
        explain = sc.nextLine();

        dataBase.updateDatabase(target, explain);
        System.out.println("Word is updated!");
    }

    public void dictionaryLookup() {
        String target;
        System.out.print("Enter a word: ");
        target = sc.nextLine();

        if (!dataBase.checkRepeatedWord(target)) {
            System.out.println("ERROR: This word doesn't exist in dictionary!");
            return;
        }

        System.out.println(dataBase.lookup(target));
    }

    public void dictionaryExportToFile() {
        try {
            String url = "D:\\Projects\\JavaDictionary\\JavaDictionary\\src\\output.txt";
            fileOutputStream = new FileOutputStream(url);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            //Check empty
            if (dataBase.checkEmptyDatabase()) {
                throw new IllegalArgumentException();
            }

            bufferedWriter.write(dataBase.readFromDatabase());
            System.out.println("Exported successfully!");
            bufferedWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(DictionaryManagement.class.getName()).log(Level.SEVERE, "FAILED TO WRITE FILE!", ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(DictionaryManagement.class.getName()).log(Level.INFO, "Dictionary is empty!", ex);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
                fileOutputStream.close();
            } catch (NullPointerException | IOException ex) {
                Logger.getLogger(DictionaryManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Dictionary getMainDictionary() {
        return mainDictionary;
    }

    public static void main(String[] args) {
        DictionaryManagement manager = new DictionaryManagement();
        manager.updateWord();
    }
}
