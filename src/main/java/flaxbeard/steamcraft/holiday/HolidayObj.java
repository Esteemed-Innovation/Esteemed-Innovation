package flaxbeard.steamcraft.holiday;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.common.CommonProxy;

/**
 * @author SatanicSanta
 */
public class HolidayObj {

    public static final boolean holidayDebug = false;
    private String name;
    private DateObj date;
    private String icon;
    private String key;

    public HolidayObj(String name, DateObj date, String icon, String key){
        this.name = name;
        this.date = date;
        this.icon = icon;
        this.key = key;
    }

    public String getName(){
        return name;
    }

    public DateObj getDate(){
        return date;
    }

    public boolean isDay(){
        if (Config.enableFunDays) {
            if (holidayDebug) {
                CommonProxy.logInfo("OMG ITS A HOLIDAY! " + this.name + "(" + this.getDate().toString() + ")");
            }
        }
        return this.getDate().isValidDate();
    }

    public String getIcon(){
        return icon;
    }

    public void setIcon(String icon){
        this.icon = icon;
    }

    public String getKey(){
        return key;
    }

    public void setKey(){
        this.key = key;
    }
}
