package com.drozd.ecaps.utils;

public class StringUtils {

    private StringUtils(){}

    public static String toProperFilename(String string){
        return string.strip().replaceAll(" +", "_").replaceAll("[^a-zA-Z0-9_]", "");
    }

    public static String toEcapsSpaceFolderName(String string){
        return "ecaps_" + toProperFilename(string);
    }
}
