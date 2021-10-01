public class DictionaryCommandLine {

    public static void showAllWords(Dictionary mainDictionary) {
        for (int i = 0; i < mainDictionary.getNumOfWords(); i++) {
            System.out.println(mainDictionary.getWordArray()[i].getWordTarget() + " - "
                    + mainDictionary.getWordArray()[i].getWordExplain());
        }
    }

    public static void dictionaryBasic(DictionaryManagement manager) {
        manager.insertFromCommandline();
        showAllWords(manager.getMainDictionary());
    }

    public static void dictionaryAdvanced(DictionaryManagement manager) {
        //TODO:
        //insertFromFile()
        showAllWords(manager.getMainDictionary());
        //dictionaryLookup()
    }
}
