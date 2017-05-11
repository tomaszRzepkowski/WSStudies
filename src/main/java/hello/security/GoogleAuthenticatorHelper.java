package hello.security;


import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tomaszrzepkowski on 08.05.2017.
 */
@RestController("GoogleAuthenticatorHelper")
@RequestMapping("/gauth")
public class GoogleAuthenticatorHelper {

    private static final int INTERVAL = 30;
    private static final Random rand = new Random();

    private static final int[] DIGITS_POWER
            // 0 1 2 3 4 5 6 7 8
            = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

    @Autowired
    private Authentication authService;

    public String getCode(String secret) {
        Base32 decoder = new Base32();
        long time = getCurrentInterval();
        byte[] decodedKey = decoder.decode(secret);
        int digits = 6;
        String crypto = "HmacSHA1";


        byte[] msg = ByteBuffer.allocate(8).putLong(time).array();
        byte[] hash = hmacSha(crypto, decodedKey, msg);

        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[digits];

        return String.valueOf(otp);
    }

//    @RequestMapping(value = "/gen", method = RequestMethod.GET, produces = "text/plain")
    public static String generateSecret() {
        byte[] buffer = new byte[10];
        rand.nextBytes(buffer);

        Base32 codec = new Base32();
        byte[] secretKey = Arrays.copyOf(buffer, 10);
        byte[] encodedKey = codec.encode(secretKey);
        return new String(encodedKey);
    }

    @RequestMapping(value = "/validate", method = RequestMethod.GET, produces = "text/plain")
    public String validateCode(@RequestParam String codeProvided, @RequestParam String token) {
        String actualCodeValue = getCodeForUser(token);
        boolean codeIsValid = false;
        if(codeProvided != null) {
            codeIsValid = actualCodeValue.equals(codeProvided);
        }
        return String.valueOf(codeIsValid);
    }

    private String getCodeForUser(String token) {
        String userSecret = authService.getSecretForUserUUID(token);
        if(!userSecret.isEmpty()) {
            return getCode(userSecret);
        }
        return org.apache.commons.lang3.StringUtils.EMPTY;
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
