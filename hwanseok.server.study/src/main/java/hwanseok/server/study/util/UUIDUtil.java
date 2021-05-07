package hwanseok.server.study.util;

import java.util.UUID;

public class UUIDUtil {

    public static String generate(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static long toLong(String uuid){
        long h = 0;
        if(uuid != null) {
            int len = uuid.length();

            for (int i = 0; i < len; i++) {
                h = 31*h + uuid.charAt(i);
            }
        }
        return h;
    }

}
