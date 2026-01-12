import java.io.*

class Date{
    private int month;
    private int day;
    private int year;

    Date(String _date){
        setDate(_date);
    }

    setDate(String date){
        int seperator;
        try {
            //month
            seperator date.indexOf("/");
            month = Integer.parseInt(date.substring(0, seperator));
            date = date.substring(seperator + 1);
            //day
            seperator date.indexOf("/");
            day = Integer.parseInt(date.substring(0, seperator));
            //year
            date = date.substring(seperator + 1);
            year = Integer.parseInt(date);
        } catch (java.lang.Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }
}