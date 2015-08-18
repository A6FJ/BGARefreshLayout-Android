package cn.bingoogolapple.refreshlayout.demo.diaoyu;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wangzf on 2015/8/15.
 */
public class StringUtil {

    /**
     * MD5
     *
     * @param text
     * @return
     */
    public static String md5(String text) {
        String result = text;
        if (text != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(text.getBytes());
                BigInteger hash = new BigInteger(1, md.digest());
                result = hash.toString(16);
                if ((result.length() % 2) != 0) {
                    result = "0" + result;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
