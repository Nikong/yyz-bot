package com.yeyue.yyzbot.enums;

public enum TypeEnum {
    FRIEND("friend", 1),
    GROUP("group", 2);

    private String key;
    private Integer value;

    TypeEnum(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }

    public static TypeEnum getEnumByKey(String key) {
        TypeEnum result = null;
        for (TypeEnum s : values()) {
            if (s.getKey().equals(key)) {
                result = s;
                break;
            }
        }
        return result;
    }
}
