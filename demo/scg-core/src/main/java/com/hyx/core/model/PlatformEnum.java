package com.hyx.core.model;

public enum PlatformEnum {
    android(1, "android", 1),
    ios(2, "ios", 1),
    wx(4, "wx", 2),
    wap(5, "wap", 3),
    cms(6, "cms", 4),
    pc(3, "pc", 5),
    ocoa(7, "ocoa", 6),
    oclf(8, "oclf", 7),
    lawfirm(9, "lawfirm", 8);

    private final Integer key;
    private final Integer group;
    private final String value;

    private PlatformEnum(Integer key, String value, int group) {
        this.key = key;
        this.value = value;
        this.group = group;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Integer getGroup() {
        return group;
    }

    public boolean equals(String name) {
        return this.name().equals(name);
    }

    public static PlatformEnum valueOf(Integer type) {
        if (type == null)
            return null;
        PlatformEnum[] ps = values();
        for (PlatformEnum platformEnum : ps) {
            if (platformEnum.key == type.intValue())
                return platformEnum;
        }
        return android;
    }

    public static PlatformEnum valueOfName(String curPlatform) {
        PlatformEnum[] ps = values();
        for (PlatformEnum platformEnum : ps) {
            if (platformEnum.value.equals(curPlatform))
                return platformEnum;
        }
        return android;
    }
}