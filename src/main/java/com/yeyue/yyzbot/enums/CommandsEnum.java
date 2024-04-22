package com.yeyue.yyzbot.enums;

public enum CommandsEnum {
    HELP("help", "菜单列表", "@bot help"),
    EVERYDAY60S("60s", "每天60秒读懂世界", "@bot 60s"),
    SETUTIME("setutime", "你懂的", "@bot setutime [标签]/[模式]"),
    WHATANIME("whatAnime", "whatAnime搜番", "@bot whatAnime [图片]"),
    SAUCENAO("saucenao", "sauceNAO搜图", "@bot saucenao [图片]"),
    GPT("gpt", "ChatGPT对话", "@bot gpt [内容]"),
    BA("ba", "ba相关功能", "@bot ba [想要查询的功能]");

    private String order;
    private String comment;
    private String from;

    CommandsEnum(String order, String comment, String from) {
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

}
