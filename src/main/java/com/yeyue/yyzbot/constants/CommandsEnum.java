package com.yeyue.yyzbot.constants;

public enum CommandsEnum {
    HELP("help","菜单列表","@bot help"),
    EVERYDAY60S("60s","每天60秒读懂世界","@bot 60s"),
    SETUTIME("setutime","你懂的","@bot setutime [标签]/[模式]"),
    WHATANIME("whatAnime","搜番","@bot whatAnime [图片]"),
    SAUCENAO("saucenao","搜图","@bot saucenao [图片]");

    private String order;
    private String comment;
    private String from;

    CommandsEnum(String order, String comment,String from){
        this.order = order;
        this.comment = comment;
        this.from = from;
    }

    public String getOrder() {
        return order;
    }

    public String getComment() {
        return comment;
    }

    public String getFrom() {
        return from;
    }

    public static CommandsEnum getEnumByOrder(String order) {
        CommandsEnum result = null;
        for (CommandsEnum s : values()) {
            if (s.getOrder().equals(order)) {
                result = s;
                break;
            }
        }
        return result;
    }


}
