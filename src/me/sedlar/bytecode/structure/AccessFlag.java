/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure;

import java.util.EnumSet;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public enum AccessFlag {

	PUBLIC(0x0001, "public"),
	PRIVATE(0x0002, "private"),
	PROTECTED(0x0004, "protected"),
	STATIC(0x0008, "static"),
	FINAL(0x0010, "final"),
	SYNCHRONIZED(0x0020, "synchronized"),
	/**
	 * For ClassInfo structure, 0x0020 is ACC_SUPER, which has historical
	 * significance only
	 */
	SUPER(0x0020, ""),
	VOLATILE(0x0040, "volatile"),
	TRANSIENT(0x0080, "transient"),

	NATIVE(0x0100, "native"),
	INTERFACE(0x0200, "interface"),
	ABSTRACT(0x0400, "abstract"),
	STRICT(0x0800, "strict"),

	/**
	 * new in Java 1.4
	 */
	SYNTHETIC(0x1000, "synthetic"),
	/**
	 * new in Java 1.5
	 */
	ANNOTATION(0x2000, "annotation"),
	/**
	 * new in Java 1.5
	 */
	ENUM(0x4000, "enum"),
	/**
	 * new in Java 1.5
	 */
	BRIDGE(0x0040, "bridge"),
	/**
	 * new in Java 1.5
	 */
	VARARGS(0x0080, "varargs");

	/**
	 * Class accessFlags flags
	 */
	public static final EnumSet<AccessFlag> CLASS_ACCESS_FLAGS = EnumSet.of(PUBLIC, FINAL, SUPER, INTERFACE,
			ABSTRACT, SYNTHETIC, ANNOTATION, ENUM);

	/**
	 * Inner class accessFlags flags
	 */
	public static final EnumSet<AccessFlag> INNER_CLASS_ACCESS_FLAGS = EnumSet.of(PUBLIC, PRIVATE, PROTECTED,
			STATIC, FINAL, INTERFACE, ABSTRACT, SYNTHETIC, ANNOTATION, ENUM);

	/**
	 * Field accessFlags flags
	 */
	public static final EnumSet<AccessFlag> FIELD_ACCESS_FLAGS = EnumSet.of(PUBLIC, PRIVATE, PROTECTED, STATIC,
			FINAL, VOLATILE, TRANSIENT, SYNTHETIC, ENUM);

	/**
	 * Method accessFlags flags
	 */
	public static final EnumSet<AccessFlag> METHOD_ACCESS_FLAGS = EnumSet.of(PUBLIC, PRIVATE, PROTECTED, STATIC,
			FINAL, SYNCHRONIZED, BRIDGE, VARARGS, NATIVE, ABSTRACT, STRICT, SYNTHETIC);

	private int flag;
	private String verbose;

	AccessFlag(int flag, String verbose) {
		this.flag = flag;
		this.verbose = verbose;
	}

	/**
	 * Returns the flag
	 *
	 * @return the flag
	 */
	public int flag() {
		return flag;
	}

	/**
	 * Returns the verbose form of the flag suitable for printing a list of
	 * accessFlags flags
	 *
	 * @return the text
	 */
	public String verbose() {
		return verbose;
	}

	/**
	 * Checks if this accessFlags flag is set in the supplied accessFlags flags.
	 *
	 * @param accessFlags
	 *            the accessFlags flags
	 * @return if this flag is set
	 */
	public boolean is(int accessFlags) {
		return (accessFlags & flag) == flag;
	}
}
