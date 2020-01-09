package com.zk.openrs.utils.parseImpl;

import com.zk.openrs.utils.MsgParser;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouKuMsgParserImpl implements MsgParser {
    @Override
    public String parseCode(String content) {
        String[] strings = content.split("@");
        List<String> stringList = Arrays.asList(strings);
        Pattern pattern = Pattern.compile("\\d{6}");
        Matcher matcher = pattern.matcher(stringList.get(2));
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
