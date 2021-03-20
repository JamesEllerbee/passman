package com.jamesellerbee.security;

import com.jamesellerbee.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;

public class EncryptionEngine {

    private final Logger logger = new Logger(getClass().getName());

    private final static int KEY_LENGTH = 256;
    private final static int LENGTH = 32;
    private final static String ALGORITHM = "AES";
    private final static String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    private SecretKey secretKey;
    private Cipher cipher;

    public EncryptionEngine(String secretKeyPath) {
       processSecretKey(secretKeyPath);
       try
       {
           cipher = Cipher.getInstance(TRANSFORMATION);
       }
       catch (NoSuchAlgorithmException | NoSuchPaddingException e)
       {
           logger.error("There was a problem generating the encryption engine.");
       }
    }

    public SecretKey getSecretKey()
    {
        return secretKey;
    }

    private void processSecretKey(String path)
    {
        File secretKeyFile = new File(path);
        if(! secretKeyFile.exists())
        {
            byte[] secretKeyBytes;
            try
            {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
                keyGenerator.init(KEY_LENGTH);
                secretKey = keyGenerator.generateKey();
                secretKeyBytes = secretKey.getEncoded();
                writeKeyFile(secretKeyFile, secretKeyBytes);
            }
            catch(NoSuchAlgorithmException e)
            {
                logger.error("There was a problem creating the key generator.");
            }
        }
        else
        {
            readKeyFile(secretKeyFile);
        }
    }

    private void readKeyFile(File secretKeyFile) {
        try(FileInputStream fileInputStream = new FileInputStream(secretKeyFile))
        {
            byte[] secretKeyBytes = new byte[LENGTH];
            fileInputStream.read(secretKeyBytes);
            secretKey = new SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.length, ALGORITHM);
        }
        catch (IOException e)
        {
            logger.error("Could not read from key file.");
        }
    }

    private void writeKeyFile(File secretKeyFile, byte[] secretKeyBytes) {
        try(FileOutputStream fileOutputStream = new FileOutputStream(secretKeyFile))
        {
            fileOutputStream.write(secretKeyBytes);
        }
        catch (IOException e)
        {
            logger.error("There was a problem writing the key file.");
        }
    }

    public void encrypt(String content, String path)
    {

    }

    public String decrypt(String path)
    {
        return "";
    }
}
