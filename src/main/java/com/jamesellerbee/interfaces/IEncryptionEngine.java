package com.jamesellerbee.interfaces;

public interface IEncryptionEngine
{
    void encrypt(String content, String path);

    String decrypt(String path);
}
