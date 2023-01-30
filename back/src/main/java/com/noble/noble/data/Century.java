package com.noble.noble.data;

import lombok.Data;

@Data
public class Century {
    private int idx = -1;
    private String nickname = "";
    private int mainChar = 0;
    private String mainCharNick = "";
    private int level = -1;
    private String job = "";
    private int upperNoble = -1;
    private String grantor = "";
}
