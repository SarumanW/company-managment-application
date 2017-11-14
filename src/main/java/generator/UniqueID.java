package generator;

public class UniqueID {

    public static long generateID(Object object){
        long currentTime = System.currentTimeMillis();
        long hash = object.hashCode();
        return currentTime+hash;
    }
}
