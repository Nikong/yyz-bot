package com.yeyue.yyzbot.constants;

public enum ErnieVilgEnum {
    HELP("1","油画"),
    EVERYDAY60S("2","水彩画"),
    SETUTIME("3","卡通"),
    WHATANIME("4","粉笔画"),
    SAUCENAO("5","儿童画"),
    ERNIEVILG("6","蜡笔画");

    private String num;
    private String value;

    ErnieVilgEnum(String num, String value){
        this.num = num;
        this.value = value;
    }

    public String getNum() {
        return num;
    }

    public String getValue() {
        return value;
    }

    public static ErnieVilgEnum getEnumByOrder(String num) {
        ErnieVilgEnum result = null;
        for (ErnieVilgEnum s : values()) {
            if (s.getNum().equals(num)) {
                result = s;
                break;
            }
        }
        return result;
    }
}
