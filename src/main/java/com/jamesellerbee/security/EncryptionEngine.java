package com.jamesellerbee.security;

import com.jamesellerbee.interfaces.IEncryptionEngine;
import com.jamesellerbee.interfaces.ILogger;
import com.jamesellerbee.utilities.constants.SystemConstants;
import com.jamesellerbee.utilities.logging.SimpleLogger;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Provides encryption and decryption methods.
 */
public class EncryptionEngine implements IEncryptionEngine
{
    /**
     * Separator between id and username.
     */
    public static final String FILE_NAME_SEPARATOR = " ";

    /**
     * File name extension for login file.
     */
    public static final String LOGIN_FILE_EXTENSION = ".enc";

    /**
     * File name extension for key file.
     */
    public static final String KEY_FILE_EXTENSION = ".key";

    private static final String DEFAULT_KEY_FILE_NAME = "private.key";

    private final ILogger logger = new SimpleLogger(getClass().getName());

    private final static int KEY_LENGTH = 256;
    private final static int LENGTH = 32;
    private final static String ALGORITHM = "AES";
    private final static String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private final static int FILE_IV_SIZE = 16;

    private SecretKey secretKey;
    private Cipher cipher;


    /**
     * Constructs the encryption engine and generates or processes the key file with default name.
     */
    public EncryptionEngine()
    {
        this(DEFAULT_KEY_FILE_NAME);
    }

    /**
     * Constructs the encryption engine and generates or processes the key file
     *
     * @param fileName Key file name.
     */
    public EncryptionEngine(String fileName)
    {
        // TODO: make this configurable
        String secretKeyPath = SystemConstants.DEFAULT_PATH;

        processSecretKey(secretKeyPath + SystemConstants.SYSTEM_FILE_SEPARATOR + fileName);
        createCipher();
    }

    public SecretKey getSecretKey()
    {
        return secretKey;
    }

    private void processSecretKey(String path)
    {
        File secretKeyFile = new File(path);
        if (!secretKeyFile.exists())
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
            catch (NoSuchAlgorithmException e)
            {
                logger.error("There was a problem creating the key generator.");
            }
        }
        else
        {
            readKeyFile(secretKeyFile);
        }
    }

    private void readKeyFile(File secretKeyFile)
    {
        try (FileInputStream fileInputStream = new FileInputStream(secretKeyFile))
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

    private void writeKeyFile(File secretKeyFile, byte[] secretKeyBytes)
    {
        try (FileOutputStream fileOutputStream = new FileOutputStream(secretKeyFile))
        {
            fileOutputStream.write(secretKeyBytes);
        }
        catch (IOException e)
        {
            logger.error("There was a problem writing the key file.");
            logger.debug("Exception: " + e.getMessage());
        }
    }

    /**
     * Encrypts content and outputs encrypted content to the file specified by path
     *
     * @param content Content to encrypt.
     * @param path    Path to write encrypted content.
     */
    public void encrypt(String content, String path)
    {
        initializeCipherForEncryption();
        byte[] iv = cipher.getIV();

        try (FileOutputStream fileOutputStream = new FileOutputStream(path); CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher))
        {
            fileOutputStream.write(iv);
            cipherOutputStream.write(content.getBytes());
        }
        catch (IOException e)
        {
            logger.error("There was an issue with the path.");
        }
    }

    /**
     * Decrypts the content stored in file at path.
     *
     * @param path Path to the file containing encrypted content.
     * @return Decrypted content.
     */
    public String decrypt(String path)
    {
        String content = "";

        try (FileInputStream fileInputStream = new FileInputStream(path))
        {
            byte[] fileIv = new byte[FILE_IV_SIZE];
            fileInputStream.read(fileIv);
            initializeCipherForDecryption(fileIv);

            try (CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher); InputStreamReader inputStreamReader = new InputStreamReader(cipherInputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader);)
            {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }
                content = stringBuilder.toString();
            }
        }
        catch (IOException e)
        {
            logger.error("There was an issue with the path.");
            logger.debug("Exception: " + e.getMessage());
        }

        return content;
    }

    private void createCipher()
    {
        try
        {
            cipher = Cipher.getInstance(TRANSFORMATION);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            logger.error("There was a problem generating the encryption engine.");
        }
    }

    private void initializeCipherForEncryption()
    {
        try
        {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        }
        catch (InvalidKeyException e)
        {
            logger.error("There was a problem initializing the cipher for encryption.");
        }
    }

    private void initializeCipherForDecryption(byte[] fileIV)
    {
        try
        {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIV));
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException e)
        {
            logger.error("There was a problem initializing the cipher for decryption");
        }
    }
}
