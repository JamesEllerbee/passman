package com.jamesellerbee.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionEngineTest {

    @BeforeEach
    void clean() {
        File file = new File("test.key");
        if(file.exists())
        {
            file.delete();
        }
    }
    /**
     * Verifies that the keyfile gets generated.
     */
    @Test
    void testCreateKeyFile() {
        // Given that the key file does not exist to begin with

        // When the encryption object is created
        EncryptionEngine encryption = new EncryptionEngine("test.key");
        // Then the test file exists.
        File file = new File("test.key");
        assertTrue(file.exists());
        // Then clean up
        file.delete();
    }

    /**
     * Verifies that when a key file exists, encryption object can use it
     */
    @Test
    void testReadExistingKeyFile()
    {
        // Create an encryption object to generate the key file
        EncryptionEngine encryption = new EncryptionEngine("test.key");

        File file = new File("test.key");
        assertTrue(file.exists());

        // Create a second encryption to read the key file.
        EncryptionEngine encryption1 = new EncryptionEngine("test.key");

        assertEquals(encryption.getSecretKey(), encryption1.getSecretKey());

        file.delete();
    }

    @Test
    void testEncryptionAndDecryption()
    {
        // Given an orignal string to encrypt
        String originalContent = "foobar";
        // Given an encryption engine
        EncryptionEngine encryptionEngine = new EncryptionEngine("test.key");
        // When encryption engine encrypts content into a enc file
        encryptionEngine.encrypt(originalContent, "foobar.enc");
        // Then when decrypting the file,
        String decryptedContent = encryptionEngine.decrypt("foobar.enc");
        // We except the decrypted content is the same as the original content
        assertEquals(originalContent, decryptedContent);
    }
}