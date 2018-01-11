package generator;

public class UniqueID {

    public static long generateID(Object object){
        long currentTime = System.currentTimeMillis();
        long hash = object.hashCode();
        long id = currentTime+hash;
        return cutID(id);
    }

    private static long cutID(long id){
        String sId = "";
        if(String.valueOf(id).length() > 10)
            sId = String.valueOf(id).substring(0, 10);
        id = Long.valueOf(sId);
        return id;
    }
}
