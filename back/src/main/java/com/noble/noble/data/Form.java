package com.noble.noble.data;

import lombok.Data;

@Data
public class Form {
    private int idx = -1;
    private String nickname = "";
    private String reason = "";
    private String age = "";
    private String flag = "";
    private String latestGuild = "";
    private String manner = "";
    private String community = "";
    private String dojang = "";
    private int joinCheck = -1;
}
