package hello.security;


import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tomaszrzepkowski on 08.05.2017.
 */
@RestController("GoogleAuthenticatorHelper")
@RequestMapping("/test")
public class GoogleAuthenticatorHelper {

    private static final String SECRET = "YouWontGetMySecretKey";
    private static final String SECRET2 = "o5db y2sj ozqd sezh qnmf 2cow ytmp d5l4";
    private  String secret3 = "5PT3WXYAQOKSELH3";
    private static final int INTERVAL = 30;
    private static final Random rand = new Random();

    private static final int[] DIGITS_POWER
            // 0 1 2 3 4 5 6 7 8
            = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

    @RequestMapping(value = "/kod", method = RequestMethod.GET, produces = "text/plain")
    public String getCode() {
        Base32 decoder = new Base32();
        long time = getCurrentInterval();
        byte[] decodedKey = decoder.decode(secret3);
        int digits = 6;
        String crypto = "HmacSHA1";


        byte[] msg = ByteBuffer.allocate(8).putLong(time).array();
        byte[] hash = hmacSha(crypto, decodedKey, msg);

        // put selected bytes into result int1
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[digits];

        return String.valueOf(otp);
    }

    @RequestMapping(value = "/gen", method = RequestMethod.GET, produces = "text/plain")
    public static String generateSecret() {

        // Allocating the buffer
        byte[] buffer = new byte[10];

        // Filling the buffer with random numbers.
        rand.nextBytes(buffer);

        // Getting the key and converting it to Base32
        Base32 codec = new Base32();
        byte[] secretKey = Arrays.copyOf(buffer, 10);
        byte[] encodedKey = codec.encode(secretKey);
        return new String(encodedKey);
    }

    private static byte[] hmacSha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            return null;
        }
    }

    private static long getCurrentInterval() {
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        return currentTimeSeconds / INTERVAL;
    }
}
