public class DictionaryCommandLine {

    public static void showAllWords() {
        System.out.println(dataBase.readFromDatabase());
    }

    public static void dictionaryBasic(DictionaryManagement manager) {
        manager.insertFromCommandline();
        showAllWords();
    }

    public static void dictionaryAdvanced(DictionaryManagement manager) {
        manager.insertFromFile();
        showAllWords();
        manager.dictionaryLookup();
    }
}
