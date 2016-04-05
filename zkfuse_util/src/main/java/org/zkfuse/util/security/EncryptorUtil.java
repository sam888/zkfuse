package org.zkfuse.util.security;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Base64;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Random;


/**
 * Created with IntelliJ IDEA.
 * Date: 5/03/13
 */
public final class EncryptorUtil {

    private static BufferedBlockCipher cipher = null;

    public static final String UTF8 = "UTF-8";

    // A convenient method to return encrypted bytes as Base64 encoded string
    public static final String encrypt( String base64EncodedKey, String plainText, CipherEngineEnum cipherEngineEnum ) throws UnsupportedEncodingException, InvalidCipherTextException {
        byte[] keyBytes = Base64.decode( base64EncodedKey );
        byte[] encryptedBytes = encrypt( keyBytes, plainText, cipherEngineEnum );
        return new String( Base64.encode( encryptedBytes ), UTF8 );
    }

    public static final byte[] encrypt(byte[] key, String plainText, CipherEngineEnum cipherEngineEnum) throws InvalidCipherTextException {

        byte[] ptBytes = plainText.getBytes();

        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher( getEngineInstance(cipherEngineEnum) ));

        // String name = cipher.getUnderlyingCipher().getAlgorithmName();
        // System.out.println("Using algorithm: " + name);

        cipher.init(true, new KeyParameter(key));

        byte[] rv = new byte[cipher.getOutputSize(ptBytes.length)];

        int oLen = cipher.processBytes(ptBytes, 0, ptBytes.length, rv, 0);
        cipher.doFinal(rv, oLen);
        return rv;
    }

    // A convenient method that acts as a wrapper for decrypt(byte[] base64EncodedKey, byte[] cipherText) by encoding string to bytes
    public static final String decrypt(String base64EncodedKey, String encryptedData, CipherEngineEnum cipherEngineEnum) throws UnsupportedEncodingException, InvalidCipherTextException {
        byte[] keyBytes = Base64.decode(base64EncodedKey);
        byte[] encryptedDataBytes = Base64.decode( encryptedData );
        return decrypt( keyBytes, encryptedDataBytes, cipherEngineEnum );
    }

    public static final String decrypt(byte[] key, byte[] cipherText, CipherEngineEnum cipherEngineEnum) throws InvalidCipherTextException, UnsupportedEncodingException {

        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher( getEngineInstance(cipherEngineEnum) ));

        cipher.init(false, new KeyParameter(key));

        byte[] rv = new byte[cipher.getOutputSize(cipherText.length)];

        int oLen = cipher.processBytes(cipherText, 0, cipherText.length, rv, 0);
        cipher.doFinal(rv, oLen);
        return new String(rv).trim();
    }

    public static int whichCipher() {
        return 4;
    }

    public  static final BlockCipher getEngineInstance( CipherEngineEnum cipherEngineEnum ) {
        BlockCipher rv = null;
        switch ( cipherEngineEnum ) {
            case AES:
                rv = new AESEngine();
                break;
            case TWO_FISH:
                rv = new TwofishEngine();
                break;
            case RIJNDAEL:
                rv = new RijndaelEngine();
        }
        return rv;
    }

    public static void status(String s) {
        System.out.println("Status:" + s);
    }

    /**
     * Generate a random salt value for creating the security base64EncodedKey.
     *
     * @param keySize, 	a int value for AES base64EncodedKey size, should be one of the 128, 192, 256
     * @return			based64 encoded random value
     */
    public static final String generateBase64EncodedKey(int keySize) throws UnsupportedEncodingException {
        if ( keySize != 128 && keySize != 192 && keySize != 256 ) {
            throw new IllegalStateException( "Key size should be one of the following values: 128, 192, 256" );
        }
        // create salt using secure random
        Random r = new SecureRandom();
        byte[] salt = new byte[keySize / 8];
        r.nextBytes(salt);
        return new String(Base64.encode(salt), "UTF8");
    }

    public static final byte[] generateKey(int keySize) {
        if ( keySize != 128 && keySize != 192 && keySize != 256 ) {
            throw new IllegalStateException( "Key size should be one of the following values: 128, 192, 256" );
        }
        // create salt using secure random
        Random r = new SecureRandom();
        byte[] salt = new byte[keySize / 8];
        r.nextBytes(salt);
        return salt;
    }

    public static final int validateKey( String saltStr )  {
        byte[] salt = Base64.decode(saltStr); // 128, 196, 256 bit sized salt
        if (salt == null || salt.length < 1) {
            return -1; // invalid base64EncodedKey size
        }
        int keySize = salt.length * 8;
        if ( keySize != 128 && keySize != 192 && keySize != 256 ) {
            return -1;
        }
        return keySize;
    }

    public static CipherEngineEnum getCipherEngineByAlgorithmId(String algorithmId) {
        CipherEngineEnum cipherEngineEnum = null;
        if ( "aes".equals( algorithmId ) ) {
            cipherEngineEnum = CipherEngineEnum.AES;
        } else if ( "two-fish".equals( algorithmId )) {
            cipherEngineEnum = CipherEngineEnum.TWO_FISH;
        } else if ( "rijndael".equals( algorithmId ) ) {
            cipherEngineEnum = CipherEngineEnum.RIJNDAEL;
        }
        return cipherEngineEnum;
    }

    // delete latter
    public static void main(String[] args) throws Exception {

        String data = "secretData";
        String key = generateBase64EncodedKey(256);
        System.out.println("Encryption key: " + key);
        String encryptedData = encrypt( key, data, CipherEngineEnum.AES);
        System.out.println("Encrypted data: " + encryptedData);
        String decryptedData = decrypt( key, encryptedData, CipherEngineEnum.AES);
        System.out.println( "AES OK: " + decryptedData.equals( data ));

        System.out.println();
        key = generateBase64EncodedKey(256);
        System.out.println("Encryption key: " + key);
        encryptedData = encrypt( key, data, CipherEngineEnum.TWO_FISH);
        System.out.println("Encrypted data: " + encryptedData);
        decryptedData = decrypt( key, encryptedData, CipherEngineEnum.TWO_FISH);
        System.out.println( "TWO_FISH OK: " + decryptedData.equals( data ));

        System.out.println();
        key = generateBase64EncodedKey(256);
        System.out.println("Encryption key: " + key);
        encryptedData = encrypt( key, data, CipherEngineEnum.RIJNDAEL);
        System.out.println("Encrypted data: " + encryptedData);
        decryptedData = decrypt( key, encryptedData, CipherEngineEnum.RIJNDAEL);
        System.out.println( "RIJNDAEL OK: " + decryptedData.equals( data ));

    }

}
