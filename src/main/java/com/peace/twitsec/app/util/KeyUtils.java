package com.peace.twitsec.app.util;

import com.netflix.astyanax.util.TimeUUIDUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.UUID;

public class KeyUtils {

    public static UUID currentTimeUUID() {
        return TimeUUIDUtils.getUniqueTimeUUIDinMicros();
    }

    public static Date UUIDtoDate(UUID ts) {

        return new Date(TimeUUIDUtils.getTimeFromUUID(ts));
    }

    public static UUID dateToUUID(Date date) {
        return TimeUUIDUtils.getMicrosTimeUUID(date.getTime());
    }

    public static String getHexId(String text) {
        return DigestUtils.md5Hex(text);
    }

    public static void main(String[] args) {
        System.out.println(currentTimeUUID());
    }

}
