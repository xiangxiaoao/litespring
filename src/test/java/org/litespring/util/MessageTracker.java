package org.litespring.util;

import java.util.ArrayList;
import java.util.List;

public class MessageTracker {
    private static List<String> MESSAGE = new ArrayList<String>();

    public static void addMsg(String msg) {
        MESSAGE.add(msg);
    }

    public static void clearMsg() {
        MESSAGE.clear();
    }

    public static List<String> getMsgs() {
        return MESSAGE;
    }
}
