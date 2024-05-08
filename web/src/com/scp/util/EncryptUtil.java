package com.scp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加解密工具类
 * 
 */
public class EncryptUtil
{
    private static final String DEFAULT_ENCODE = "UTF-8";

    /**
     * md5加密
     * 
     * @param inputText 待加密字符串
     * @return md5加密字符串
     */
    public static String md5Encode(String inputText)
    {
        try
        {
            return md5Encode(inputText, DEFAULT_ENCODE);
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }

    /**
     * md5加密
     * 
     * @param inputText 待加密字符串
     * @param charset 字符集
     * @return md5加密字符串
     * @throws UnsupportedEncodingException
     */
    public static String md5Encode(String inputText, String charset) throws UnsupportedEncodingException
    {
        return CommonEncrypt.encrypt(inputText, "MD5", charset);
    }

    /**
     * sha256加密
     * 
     * @param inputText 待加密字符串
     * @return sha256加密字符串
     */
    public static String sha256Encode(String inputText)
    {
        try
        {
            return sha256Encode(inputText, DEFAULT_ENCODE);
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }

    /**
     * sha256加密
     * 
     * @param inputText 待加密字符串
     * @param charset 字符集
     * @return sha256加密字符串
     * @throws UnsupportedEncodingException
     */
    public static String sha256Encode(String inputText, String charset) throws UnsupportedEncodingException
    {
        return CommonEncrypt.encrypt(inputText, "SHA-256", charset);
    }

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     * @param encryptText 被签名的字符串
     * @param encryptKey 密钥
     * @return
     */
    public static byte[] hmacSHA1Encrypt(String encryptText, String encryptKey)
    {
        try
        {
            return hmacSHA1Encrypt(encryptText, encryptKey, DEFAULT_ENCODE);
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     * @param encryptText 被签名的字符串
     * @param encryptKey 密钥
     * @param charset 字符集
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] hmacSHA1Encrypt(String encryptText, String encryptKey, String charset)
            throws UnsupportedEncodingException
    {
        String MAC_NAME = "HmacSHA1";
        byte[] data = encryptKey.getBytes(charset);
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        Mac mac;
        try
        {
            mac = Mac.getInstance(MAC_NAME);
            mac.init(secretKey);
            byte[] text = encryptText.getBytes(charset);
            return mac.doFinal(text);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sha1加密
     * 
     * @param inputText 待加密字符串
     * @return sha1加密字符串
     */
    public static String sha1Encode(String inputText)
    {
        try
        {
            return sha1Encode(inputText, DEFAULT_ENCODE);
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }

    /**
     * sha1加密
     * 
     * @param inputText 待加密字符串
     * @param charset 字符集
     * @return sha1加密字符串
     * @throws UnsupportedEncodingException
     */
    public static String sha1Encode(String inputText, String charset) throws UnsupportedEncodingException
    {
        return CommonEncrypt.encrypt(inputText, "SHA-1", charset);
    }

    /**
     * 获得对明文进行补位填充的字节.
     * 
     * @param count 需要进行填充补位操作的明文字节个数
     * @return 补齐用的字节数组
     */
    public static byte[] pkcs7Encrypt(int count)
    {
        int BLOCK_SIZE = 32;
        // 计算需要填充的位数
        int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
        if (amountToPad == 0)
        {
            amountToPad = BLOCK_SIZE;
        }
        // 获得补位所用的字符
        char padChr = chr(amountToPad);
        String tmp = new String();
        for (int index = 0; index < amountToPad; index++)
        {
            tmp += padChr;
        }
        try
        {
            return tmp.getBytes(DEFAULT_ENCODE);
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }

    /**
     * pkcs7删除解密后明文的补位字符
     * 
     * @param decrypted 解密后的明文
     * @return 删除补位字符后的明文
     */
    public static byte[] pkcs7Decryptor(byte[] decrypted) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        int pad = (int) decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32)
        {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    /**
     * 将数字转化成ASCII码对应的字符，用于对明文进行补码
     * 
     * @param a 需要转化的数字
     * @return 转化得到的字符
     */
    private static char chr(int a)
    {
        byte target = (byte) (a & 0xFF);
        return (char) target;
    }

    /**
     * base64加密
     * 
     * @param bytes 待加密字符串
     * @return base64加密字符串
     */
    public static String base64Encrypt(byte[] bytes)
    {
        return Base64.encrypt(bytes);
    }

    /**
     * base64解密
     * 
     * @param inputText 待解密字符串
     * @return 解密字符串
     */
    public static byte[] base64Decrypt(String inputText)
    {
        return Base64.decrypt(inputText);
    }

    /**
     * 获取RSA公钥私钥
     * @return String[0]=publicKey,String[1]=privateKey
     */
    public static String[] getRsaKeys()
    {
        String[] keys = new String[2];
        Map<String, Key> map = RSA.genKeyPair();
        keys[0] = RSA.getPublicKey(map);
        keys[1] = RSA.getPrivateKey(map);
        return keys;
    }

    /**
     * RSA加密
     * <p>
     * 注意：由于富士康项目当初采用的rsaEncrypt采用的默认编码时ISO-8859-1，因此当前方法，默认编码不能使用utf-8
     * @param source 待加密字符串
     * @param publicKey 公钥
     * @return 加密字符串
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    public static String rsaEncrypt(String source, String publicKey) throws InvalidKeyException,
            InvalidKeySpecException
    {
        return RSA.encrypt(source, publicKey);
    }

    /**
     * RSA加密
     * @param source 待加密字符串
     * @param publicKey 公钥
     * @return 加密字符串
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public static String rsaEncrypt(String source, String publicKey, String charset) throws InvalidKeyException,
            InvalidKeySpecException, UnsupportedEncodingException
    {
        return RSA.encrypt(source, publicKey, charset);
    }

    /**
     * RSA解密
     * @param source 待解密字符串
     * @param privateKey 私钥
     * @return 解密字符串
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    public static String rsaDecrypt(String cryptograph, String privateKey) throws InvalidKeyException,
            InvalidKeySpecException
    {
        return RSA.decrypt(cryptograph, privateKey);
    }

    /**
     * RSA解密
     * @param source 待解密字符串
     * @param privateKey 私钥
     * @return 解密字符串
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public static String rsaDecrypt(String cryptograph, String privateKey, String charset) throws InvalidKeyException,
            InvalidKeySpecException, UnsupportedEncodingException
    {
        return RSA.decrypt(cryptograph, privateKey, charset);
    }

    /**
     * RSA字符串签名
     * @param data 签名字符串
     * @param privateKey 私钥
     * @return 签名
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    public static String rsaSign(String data, String privateKey) throws InvalidKeyException, InvalidKeySpecException
    {
        try
        {
            return RSA.sign(data.getBytes(DEFAULT_ENCODE), privateKey);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RSA签名认证
     * @param data 签名字符串
     * @param publicKey 公钥
     * @return 认证结果
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    public static boolean rsaVerifySign(String data, String publicKey, String sign) throws InvalidKeyException,
            InvalidKeySpecException
    {
        try
        {
            return RSA.verifySign(data.getBytes(DEFAULT_ENCODE), publicKey, sign);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * RSA字符串签名
     * @param data 签名字符串
     * @param privateKey 私钥
     * @param charset 签名字符集
     * @return 签名
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    public static String rsaSign(String data, String privateKey, String charset) throws InvalidKeyException,
            InvalidKeySpecException
    {
        try
        {
            return RSA.sign(data.getBytes(charset), privateKey);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RSA签名认证
     * @param data 签名字符串
     * @param publicKey 公钥
     * @param charset 签名字符集
     * @return 认证结果
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    public static boolean rsaVerifySign(String data, String publicKey, String sign, String charset)
            throws InvalidKeyException, InvalidKeySpecException
    {
        try
        {
            return RSA.verifySign(data.getBytes(charset), publicKey, sign);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private static class CommonEncrypt
    {
        /**
         * 通用加密
         * 
         * @param inputText 待加密字符串
         * @param algorithmName 加密算法名称：md5或者sha-1，不区分大小写
         * @return
         * @throws UnsupportedEncodingException
         */
        static String encrypt(String inputText, String algorithmName, String charset)
                throws UnsupportedEncodingException
        {
            if (inputText == null || "".equals(inputText.trim()))
            {
                throw new IllegalArgumentException("请输入要加密的内容");
            }

            String encryptText = null;
            try
            {
                MessageDigest m = MessageDigest.getInstance(algorithmName);
                m.update(inputText.getBytes(charset));
                byte s[] = m.digest();
                return hex(s);
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            return encryptText;
        }

        /**
         * 返回十六进制字符串
         * 
         * @param arr
         * @return
         */
        private static String hex(byte[] arr)
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < arr.length; ++i)
            {
                sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        }
    }

    private static class Base64
    {
        static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
                .toCharArray();

        static private byte[] codes = new byte[256];
        static
        {
            for (int i = 0; i < 256; i++)
                codes[i] = -1;
            for (int i = 'A'; i <= 'Z'; i++)
                codes[i] = (byte) (i - 'A');
            for (int i = 'a'; i <= 'z'; i++)
                codes[i] = (byte) (26 + i - 'a');
            for (int i = '0'; i <= '9'; i++)
                codes[i] = (byte) (52 + i - '0');
            codes['+'] = 62;
            codes['/'] = 63;
        }

        /**
         * Encodes hex octects into Base64
         * 
         * @param binaryData Array containing binaryData
         * @return Encoded Base64 array
         */
        static String encrypt(byte[] data)
        {
            char[] out = new char[((data.length + 2) / 3) * 4];
            for (int i = 0, index = 0; i < data.length; i += 3, index += 4)
            {
                boolean quad = false;
                boolean trip = false;
                int val = (0xFF & (int) data[i]);
                val <<= 8;
                if ((i + 1) < data.length)
                {
                    val |= (0xFF & (int) data[i + 1]);
                    trip = true;
                }
                val <<= 8;
                if ((i + 2) < data.length)
                {
                    val |= (0xFF & (int) data[i + 2]);
                    quad = true;
                }
                out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
                val >>= 6;
                out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
                val >>= 6;
                out[index + 1] = alphabet[val & 0x3F];
                val >>= 6;
                out[index + 0] = alphabet[val & 0x3F];
            }
            return new String(out);
        }

        /**
         * Decodes Base64 data into octects
         * 
         * @param encoded string containing Base64 data
         * @return Array containind decoded data.
         */
        static byte[] decrypt(String source)
        {
            if (source == null)
            {
                return null;
            }

            char[] data = source.toCharArray();
            int len = ((data.length + 3) / 4) * 3;
            if (data.length > 0 && data[data.length - 1] == '=')
                --len;
            if (data.length > 1 && data[data.length - 2] == '=')
                --len;
            byte[] out = new byte[len];
            int shift = 0;
            int accum = 0;
            int index = 0;
            for (int ix = 0; ix < data.length; ix++)
            {
                int value = codes[data[ix] & 0xFF];
                if (value >= 0)
                {
                    accum <<= 6;
                    shift += 6;
                    accum |= value;
                    if (shift >= 8)
                    {
                        shift -= 8;
                        out[index++] = (byte) ((accum >> shift) & 0xff);
                    }
                }
            }
            if (index != out.length)
                throw new Error("miscalculated data length!");
            return out;
        }
    }

    private static class RSA
    {
        /** RSA算法开始 */
        /** 指定加密算法为RSA */
        private static final String KEY_ALGORITHM = "RSA";

        /**
         * 签名算法
         */
        public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

        /** 密钥长度，用来初始化 */
        private static final int KEYSIZE = 1024;

        /** 指定公钥存放文件 */
        private static String PUBLIC_KEY = "PublicKey";

        /** 指定私钥存放文件 */
        private static String PRIVATE_KEY = "PrivateKey";

        /**
         * <p>
         * 用私钥对信息生成数字签名
         * </p>
         * 
         * @param data 已加密数据
         * @param privateKey 私钥(BASE64编码)
         * 
         * @return
         * @throws InvalidKeySpecException
         * @throws InvalidKeyException
         * @throws Exception
         */
        static String sign(byte[] data, String privateKey) throws InvalidKeySpecException, InvalidKeyException
        {
            try
            {
                byte[] keyBytes = getPrivateKey(privateKey).getEncoded();
                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
                Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
                signature.initSign(privateK);
                signature.update(data);
                return Base64.encrypt(signature.sign());
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (SignatureException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        /** */
        /**
         * <p>
         * 校验数字签名
         * </p>
         * 
         * @param data 已加密数据
         * @param publicKey 公钥(BASE64编码)
         * @param sign 数字签名
         * 
         * @return
         * @throws InvalidKeySpecException
         * @throws InvalidKeyException
         * 
         */
        static boolean verifySign(byte[] data, String publicKey, String sign) throws InvalidKeySpecException,
                InvalidKeyException
        {
            try
            {
                byte[] keyBytes = getPublicKey(publicKey).getEncoded();
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PublicKey publicK = keyFactory.generatePublic(keySpec);
                Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
                signature.initVerify(publicK);
                signature.update(data);
                return signature.verify(Base64.decrypt(sign));
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (SignatureException e)
            {
                e.printStackTrace();
            }
            return false;
        }

        /** RSA算法开始 */
        /**
         * <p>
         * 生成密钥对(公钥和私钥)
         * </p>
         * 
         * @return
         * @throws Exception
         */
        static Map<String, Key> genKeyPair()
        {
            Map<String, Key> keyMap = new HashMap<String, Key>();
            KeyPairGenerator keyPairGenerator = null;
            try
            {
                keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
                keyPairGenerator.initialize(KEYSIZE);

                /** 生成密匙对 */
                KeyPair keyPair = keyPairGenerator.generateKeyPair();

                /** 得到公钥 */
                Key publicKey = keyPair.getPublic();

                /** 得到私钥 */
                Key privateKey = keyPair.getPrivate();

                keyMap.put(PUBLIC_KEY, publicKey);
                keyMap.put(PRIVATE_KEY, privateKey);
                return keyMap;
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * <p>
         * 获取私钥
         * </p>
         * 
         * @param keyMap 密钥对
         * @return
         * @throws Exception
         */
        static String getPrivateKey(Map<String, Key> keyMap)
        {
            RSAPrivateKey privateKey = (RSAPrivateKey) keyMap.get(PRIVATE_KEY);
            return Base64.encrypt(privateKey.getEncoded());
        }

        /** */
        /**
         * 获取公钥 </p> 加密成字符串
         * @param keyMap 密钥对
         * @return
         * @throws Exception
         */
        static String getPublicKey(Map<String, Key> keyMap)
        {
            RSAPublicKey publicKey = (RSAPublicKey) keyMap.get(PUBLIC_KEY);
            return Base64.encrypt(publicKey.getEncoded());
        }

        private static Key getPublicKey(String publicKeyStr) throws InvalidKeySpecException
        {
            byte[] keyBytes = Base64.decrypt(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory;
            try
            {
                keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PublicKey publicKey = keyFactory.generatePublic(keySpec);
                return publicKey;
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        private static Key getPrivateKey(String privateKeyStr) throws InvalidKeySpecException
        {
            byte[] keyBytes = Base64.decrypt(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory;
            try
            {
                keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
                return privateKey;
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 加密方法
         * @param encryptedData 源数据
         * @return
         * @throws InvalidKeySpecException
         * @throws InvalidKeyException
         */
        static String encrypt(String encryptedData, String publicKey) throws InvalidKeyException,
                InvalidKeySpecException
        {
            /** 得到Cipher对象来实现对源数据的RSA加密 */
            ByteArrayOutputStream out = null;
            try
            {
                Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
                byte[] b = encryptedData.getBytes();
                int inputLen = b.length;
                out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段解密
                while (inputLen - offSet > 0)
                {
                    if (inputLen - offSet > 117)
                    {
                        cache = cipher.doFinal(b, offSet, 117);
                    }
                    else
                    {
                        cache = cipher.doFinal(b, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * 117;
                }
                byte[] decryptedData = out.toByteArray();
                return Base64.encrypt(decryptedData);
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchPaddingException e)
            {
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e)
            {
                e.printStackTrace();
            }
            catch (BadPaddingException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        /**
         * 加密方法
         * @param encryptedData 源数据
         * @return
         * @throws InvalidKeySpecException
         * @throws InvalidKeyException
         * @throws UnsupportedEncodingException
         */
        static String encrypt(String encryptedData, String publicKey, String charset) throws InvalidKeyException,
                InvalidKeySpecException, UnsupportedEncodingException
        {
            /** 得到Cipher对象来实现对源数据的RSA加密 */
            ByteArrayOutputStream out = null;
            try
            {
                Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
                byte[] b = encryptedData.getBytes(charset);
                int inputLen = b.length;
                out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段解密
                while (inputLen - offSet > 0)
                {
                    if (inputLen - offSet > 117)
                    {
                        cache = cipher.doFinal(b, offSet, 117);
                    }
                    else
                    {
                        cache = cipher.doFinal(b, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * 117;
                }
                byte[] decryptedData = out.toByteArray();
                return Base64.encrypt(decryptedData);
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchPaddingException e)
            {
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e)
            {
                e.printStackTrace();
            }
            catch (BadPaddingException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        /**
         * 解密算法
         * @param cryptograph 密文
         * @return
         * @throws InvalidKeyException
         * @throws InvalidKeySpecException
         */
        static String decrypt(String cryptograph, String privateKey) throws InvalidKeyException,
                InvalidKeySpecException
        {
            try
            {
                /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
                Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
                InputStream ins = new ByteArrayInputStream(Base64.decrypt(cryptograph));
                ByteArrayOutputStream writer = new ByteArrayOutputStream();
                // rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
                byte[] buf = new byte[128];
                int bufl;

                while ((bufl = ins.read(buf)) != -1)
                {
                    byte[] block = null;

                    if (buf.length == bufl)
                    {
                        block = buf;
                    }
                    else
                    {
                        block = new byte[bufl];
                        for (int i = 0; i < bufl; i++)
                        {
                            block[i] = buf[i];
                        }
                    }

                    writer.write(cipher.doFinal(block));
                }

                return new String(writer.toByteArray());
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchPaddingException e)
            {
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e)
            {
                e.printStackTrace();
            }
            catch (BadPaddingException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 解密算法
         * @param cryptograph 密文
         * @return
         * @throws InvalidKeyException
         * @throws InvalidKeySpecException
         * @throws UnsupportedEncodingException
         */
        static String decrypt(String cryptograph, String privateKey, String charset) throws InvalidKeyException,
                InvalidKeySpecException, UnsupportedEncodingException
        {
            try
            {
                /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
                Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
                InputStream ins = new ByteArrayInputStream(Base64.decrypt(cryptograph));
                ByteArrayOutputStream writer = new ByteArrayOutputStream();
                // rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
                byte[] buf = new byte[128];
                int bufl;
                try
                {
                    while ((bufl = ins.read(buf)) != -1)
                    {
                        byte[] block = null;

                        if (buf.length == bufl)
                        {
                            block = buf;
                        }
                        else
                        {
                            block = new byte[bufl];
                            for (int i = 0; i < bufl; i++)
                            {
                                block[i] = buf[i];
                            }
                        }

                        writer.write(cipher.doFinal(block));
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    return null;
                }
                return new String(writer.toByteArray(), charset);
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchPaddingException e)
            {
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e)
            {
                e.printStackTrace();
            }
            catch (BadPaddingException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}