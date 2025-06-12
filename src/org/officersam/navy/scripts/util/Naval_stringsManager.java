/*
By Tartiflette
 */
package org.officersam.navy.scripts.util;

import com.fs.starfarer.api.Global;

public class Naval_stringsManager {
    private static final String USNC="USNC";

    public static String txt(String id){
        return Global.getSettings().getString(USNC, id);
    }
}