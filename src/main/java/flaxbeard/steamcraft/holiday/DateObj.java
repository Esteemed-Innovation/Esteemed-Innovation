package flaxbeard.steamcraft.holiday;

import flaxbeard.steamcraft.common.CommonProxy;

import java.util.Calendar;

/**
 * @author SatanicSanta
 */
public class DateObj {

    private static final Calendar cal = Calendar.getInstance();
    private int month;
    private int startDay = -1;
    private int endDay = -1;
    private boolean wholeMonth = false;

    static {
        CommonProxy.logInfo("OMG ITS A HOLIDAY! " + (DateObj.getCal().get(Calendar.MONTH) + 1) + "/" + DateObj.getCal().get(Calendar.DAY_OF_MONTH));
    }

    public DateObj(int month, int startDay, int endDay){
        this.month = month;
        this.startDay = startDay;
        this.endDay = endDay;
    }

    public boolean isValidDate(){
        if (cal.get(Calendar.MONTH) == month){
            if (wholeMonth){
                return wholeMonth;
            }
            if (cal.get(Calendar.DAY_OF_MONTH) >= startDay && cal.get(Calendar.DAY_OF_MONTH) <= endDay){
                return true;
            }
        }
        return false;
    }

    public DateObj setWholeMonth(boolean wholeMonth){
        this.wholeMonth = wholeMonth;
        return this;
    }

    public static Calendar getCal(){
        return cal;
    }

    public int getMonth(){
        return month;
    }

    public int getStartDay(){
        return startDay;
    }

    public int getEndDay(){
        return endDay;
    }

    public boolean isWholeMonth(){
        return wholeMonth;
    }

}
