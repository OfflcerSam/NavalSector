/*
By Tartiflette
 */
package org.officersam.navy.scripts.util;

import com.fs.starfarer.api.Global;

public class USA_txt {   
    private static final String USA="USA";
    public static String txt(String id){
        return Global.getSettings().getString(USA, id);
    }    
}