package com.jamesellerbee.security;

import com.jamesellerbee.interfaces.IInjector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("An EncryptionEngine should...")
class EncryptionEngineTest
{

    @BeforeEach
    void setup()
    {

    }

    @AfterEach
    void clean()
    {
        File file = new File("test.key");
        if (file.exists())
        {
            file.delete();
        }
    }

    /**
     * Verifies that the keyfile gets generated.
     */
    @Test
    @DisplayName("generate a key file.")
    void testCreateKeyFile()
    {
        // Given that the key file does not already exist

        // When the encryption object is created
        EncryptionEngine encryption = new EncryptionEngine("test.key");
        // Then the test file exists.
        File file = new File("test.key");
        assertTrue(file.exists());
        // Finally, clean up
        file.delete();
    }

    /**
     * Verifies that when a key file exists, encryption object can use it
     */
    @Test
    @DisplayName("read and use existing key file.")
    void testReadExistingKeyFile()
    {
        // Create an encryption object to generate the key file
        EncryptionEngine encryption = new EncryptionEngine("test.key");

        File file = new File("test.key");
        assertTrue(file.exists());

        // Create a second encryption to read the key file.
        EncryptionEngine encryption1 = new EncryptionEngine("test.key");

        assertEquals(encryption.getSecretKey(), encryption1.getSecretKey());

        // Finally, clean up
        file.delete();
    }

    @Test
    @DisplayName("encrypt and decrypt using key file")
    void testEncryptionAndDecryption()
    {
        // Given an original string to encrypt
        String originalContent = "foobar";
        // Given an encryption engine
        EncryptionEngine encryptionEngine = new EncryptionEngine("test.key");
        // When encryption engine encrypts content into a enc file
        encryptionEngine.encrypt(originalContent, "foobar.enc");
        // Then when decrypting the file,
        String decryptedContent = encryptionEngine.decrypt("foobar.enc");
        // We except the decrypted content is the same as the original content
        assertEquals(originalContent, decryptedContent);

        // Finally, clean up
        new File("foobar.enc").delete();
    }
}