/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 **/
public class Streams {

	/**
	 * Writes the given InputStream to the given OutputStream.
	 * 
	 * @param in
	 *            the InputStream to transfer from.
	 * @param out
	 *            the OutputStream to transfer to.
	 * @return <t>true</t> if the transfer was successful, otherwise
	 *         <t>false</t>.
	 */
	public static boolean transfer(InputStream in, OutputStream out) {
		try {
			out.write(binary(in));
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Converts an InputStream to a byte array.
	 * 
	 * @param in
	 *            the InputStream to convert.
	 * @return the converted byte array.
	 */
	public static byte[] binary(InputStream in) {
		byte[] output = {};
		int length = Integer.MAX_VALUE;
		int pos = 0;
		while (pos < length) {
			int readable;
			if (pos >= output.length) {
				readable = Math.min(length - pos, output.length + 1024);
				if (output.length < pos + readable)
					output = Arrays.copyOf(output, pos + readable);
			} else {
				readable = output.length - pos;
			}
			int cc;
			try {
				cc = in.read(output, pos, readable);
			} catch (IOException e1) {
				throw new RuntimeException("Failed to read bytes");
			}
			if (cc < 0) {
				if (output.length != pos)
					output = Arrays.copyOf(output, pos);
				break;
			}
			pos += cc;
		}
		return output;
	}
	
	/**
     * Converts the given class to a byte[]
     *
     * @param clazz The class to convert
     * @return the byte[] of the given class
     */
    public static byte[] binaryClass(String clazz) {
        try {
            String path = clazz.replace('.', '/') + ".class";
            return binary(ClassLoader.getSystemResourceAsStream(path));
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * Converts the given class to a byte[]
     *
     * @param clazz The class to convert
     * @return the byte[] of the given class
     */
    public static byte[] binaryClass(Class<?> clazz) {
        return binaryClass(clazz.getCanonicalName());
    }
}