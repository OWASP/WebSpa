package net.seleucus.wsp.crypto.fwknop;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class FwknopBase64 {

    private static final char CHAR_TO_REMOVE = '=';

    public static String encode(byte[] input){
        return StringUtils.remove(Base64.encodeBase64String(input), CHAR_TO_REMOVE);
    }

    public static String encode(final String input) {
        return StringUtils.remove(Base64.encodeBase64String(input.getBytes(Charsets.UTF_8)), CHAR_TO_REMOVE);
    }

    public static byte[] decode(final String input){
        return Base64.decodeBase64(input);
    }
}
