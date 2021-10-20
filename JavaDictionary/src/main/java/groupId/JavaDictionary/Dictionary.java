package groupId.JavaDictionary;

import java.util.ArrayList;

public class Dictionary {
    public ArrayList<Word> wordArray;

    public Dictionary() {
        DataBase.connect();
    }
}
