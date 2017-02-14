package com.softcom.npdn.encryption;

/**
 * Created by mayowa on 2/13/17.
 */

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES static function for different key and iv
 * mode: AES/CBC/PKCS5Padding
 * file input encoding: binary
 * file output encoding: binary
 */
public class AES {

    /**
     * Encrypt files(.mp4, .pdf or any)
     * @param srcBytes
     * @param key
     * @param newIv
     *
     * @return encrypted bytes
     * @throws Exception
     */
    public static final byte[] encryptBytes(byte[] srcBytes, byte[] key, byte[] newIv) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(newIv);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] encrypted = cipher.doFinal(srcBytes);
        return encrypted;
    }

    /**
     * Decrypt files(.mp4, .pdf or any)
     * @param srcBytes
     * @param key
     * @param newIv
     *
     * @return decrypted bytes
     * @throws Exception
     */
    public static final byte[] decryptBytes(byte[] srcBytes, byte[] key, byte[] newIv) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(newIv);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        byte[] encrypted = cipher.doFinal(srcBytes);

        return encrypted;
    }


    /**
     * Sample encryption key = [1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6]
     * Sample initialization vector = [1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6]
     * @param encryptionKey
     * @param initializationVector
     * @param inputFilePath : Path to file encrypted with node app
     * @param outputFilePath : Path to decrypted file
     *
     * TODO: call this method to decrypt an ecrypted file
     *
     * @return decrypted file
     */
    public File getDecryptedFile(byte[] encryptionKey, byte[] initializationVector, String inputFilePath, String outputFilePath){

        try {
            byte[] dataToDecrypt = readFile(inputFilePath);

            File file = new File(outputFilePath);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

            byte[] decryptedByte = decryptBytes(dataToDecrypt, encryptionKey, initializationVector);

            bos.write(decryptedByte);
            bos.flush();
            bos.close();

            return file;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param filepath
     *
     * @return bytes read from file
     */
    public byte[] readFile(String filepath) {
        byte[] contents;

        File file = new File(filepath);
        int size = (int) file.length();
        contents = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(
                    new FileInputStream(file));
            try {
                buf.read(contents);
                buf.close();

                Log.d("DEBUG", "Content read");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return contents;
    }
}