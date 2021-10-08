public class DictionaryCommandLine {

    public static void showAllWords(Dictionary mainDictionary) {
        for (int i = 0; i < mainDictionary.getWordArray().size(); i++) {
            System.out.println(mainDictionary.getWordArray().get(i).toString());
        }
    }

    public static void dictionaryBasic(DictionaryManagement manager) {
        manager.insertFromCommandline();
        showAllWords(manager.getMainDictionary());
    }

    public static void dictionaryAdvanced(DictionaryManagement manager) {
        manager.insertFromFile();
        showAllWords(manager.getMainDictionary());
        manager.dictionaryLookup();
    }
}
