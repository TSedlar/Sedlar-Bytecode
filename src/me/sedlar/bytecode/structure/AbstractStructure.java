/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class AbstractStructure {

	/**
	 * Set this JVM System property to true to switch on debugging for reading
	 * and writing class files.
	 */
	public static final String SYSTEM_PROPERTY_DEBUG = "me.sedlar.bytecode.debug";

	/**
	 * Parent class file for this structure. <tt>Null</tt> for a
	 * <tt>ClassInfo</tt> structure.
	 */
	protected ClassInfo classInfo;

	/**
	 * Flag for debugging while reading and writing class files.
	 */
	protected boolean debug;

	/**
	 * Constructor.
	 */
	protected AbstractStructure() {
		debug = Boolean.getBoolean(SYSTEM_PROPERTY_DEBUG);
	}

	/**
	 * Get parent class file.
	 *
	 * @return the class file
	 */
	public ClassInfo classInfo() {
		return classInfo;
	}

	/**
	 * Set parent class file.
	 * <p>
	 * <p/>
	 * Has to be called at least once on a structure.
	 *
	 * @param classInfo
	 *            the new parent class file
	 */
	public void setClassInfo(ClassInfo classInfo) {
		this.classInfo = classInfo;
	}

	/**
	 * Read this structure from the given <tt>DataInput</tt>.
	 * <p>
	 * <p/>
	 * Expects <tt>DataInput</tt> to be in JVM class file format and just before
	 * a structure of this kind. No look ahead parsing since the class file
	 * format is deterministic.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
	}

	/**
	 * Write this structure to the given <tt>DataOutput</tt>.
	 * <p>
	 * <p/>
	 * The bytesWritten bits are in JVM class file format.
	 *
	 * @param out
	 *            the <tt>DataOutput</tt> to which to write
	 * @throws InvalidByteCodeException
	 *             if the structure is internally inconsistent
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataOutput</tt>
	 */
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
	}

	/**
	 * Get the debug mode for this structure.
	 *
	 * @return whether debug is on or not
	 */
	public boolean debug() {
		return debug;
	}

	/**
	 * Set the debug mode for this structure.
	 *
	 * @param debug
	 *            the new debug mode
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Utility method for derived structure. Get the length of an arbitrary
	 * array which may hold primitive types or reference types or may be null.
	 *
	 * @param array
	 *            the array for which the length is requested
	 * @return the length
	 */
	protected int length(Object array) {
		if (array instanceof List) {
			return ((List<?>) array).size();
		} else if (array instanceof Set) {
			return ((Set<?>) array).size();
		} else if (array instanceof Map) {
			return ((Map<?, ?>) array).size();
		}
		return array != null && array.getClass().isArray() ? Array.getLength(array) : 0;
	}

	/**
	 * Utility method for derived structure. Dump a specific debug message.
	 *
	 * @param message
	 *            the debug message
	 */
	protected void debug(String message) {
		if (debug)
			System.err.println(message);
	}

	/**
	 * Utility method for derived structure. Print an int variable as a hex
	 * string.
	 *
	 * @param bytes
	 *            the int variable to print as a hex string
	 * @return the hex string
	 */
	protected String bytes(int bytes) {
		return padHexString(Integer.toHexString(bytes), 8);
	}

	/**
	 * Utility method for derived structure. Print an accessFlags flag or an
	 * unsigned short variable as a hex string.
	 *
	 * @param accessFlags
	 *            the unsigned short variable to print as a hex string
	 * @return the hex string
	 */
	protected String accessFlags(int accessFlags) {
		return padHexString(Integer.toHexString(accessFlags), 4);
	}

	private String padHexString(String hexString, int length) {
		StringBuilder buffer = new StringBuilder("0x");
		for (int i = hexString.length(); i < length; i++)
			buffer.append('0');
		buffer.append(hexString);
		return buffer.toString();
	}

	/**
	 * Utility method for derived structure. Print an accessFlags flag as a
	 * space separated list of verbose java accessFlags modifiers.
	 *
	 * @param accessFlags
	 *            the unsigned short variable to print as a hex string
	 * @return the hex string
	 */
	protected abstract String verboseAccessFlags(int accessFlags);

	/**
	 * Utility method for derived structure. Print an accessFlags flag as a
	 * space separated list of verbose java accessFlags modifiers.
	 *
	 * @param availableAccessFlags
	 *            array with the accessFlags flags available for the derived
	 *            structure
	 * @param accessFlags
	 *            the unsigned short variable to print as a hex string
	 * @return the accessFlags flags verbose description
	 */
	protected String verboseAccessFlags(EnumSet<AccessFlag> availableAccessFlags, int accessFlags) {
		StringBuilder buffer = new StringBuilder();
		int all = 0;
		for (AccessFlag accessFlag : availableAccessFlags) {
			int flags = accessFlag.flag();
			all |= flags;
			if ((accessFlags & flags) != 0) {
				if (buffer.length() > 0)
					buffer.append(' ');
				buffer.append(accessFlag.verbose()).append(' ');
			}
		}
		// Check if every possible flag has been processed
		if ((all | accessFlags) != all)
			buffer.append('?');
		return buffer.toString();
	}
}
