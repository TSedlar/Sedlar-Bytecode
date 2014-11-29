/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.values;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.AbstractStructure;
import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public abstract class Value extends AbstractStructure {

	public final static char BYTE_TAG = 'B';
	public final static char CHAR_TAG = 'C';
	public final static char DOUBLE_TAG = 'D';
	public final static char FLOAT_TAG = 'F';
	public final static char INT_TAG = 'I';
	public final static char LONG_TAG = 'J';
	public final static char SHORT_TAG = 'S';
	public final static char BOOL_TAG = 'Z';
	public final static char STRING_TAG = 's';
	public final static char ENUM_TAG = 'e';
	public final static char CLASS_TAG = 'c';
	public final static char ARRAY_TAG = '[';
	public final static char ANNOTATION_TAG = '@';

	public final static String BYTE_TAG_VERBOSE = "byte";
	public final static String CHAR_TAG_VERBOSE = "String";
	public final static String DOUBLE_TAG_VERBOSE = "double";
	public final static String FLOAT_TAG_VERBOSE = "float";
	public final static String INT_TAG_VERBOSE = "int";
	public final static String LONG_TAG_VERBOSE = "long";
	public final static String SHORT_TAG_VERBOSE = "short";
	public final static String BOOL_TAG_VERBOSE = "boolean";
	public final static String STRING_TAG_VERBOSE = "String";
	public final static String ENUM_TAG_VERBOSE = "Enum";
	public final static String CLASS_TAG_VERBOSE = "Class";
	public final static String ARRAY_TAG_VERBOSE = "Array";
	public final static String ANNOTATION_TAG_VERBOSE = "Annotation";

	protected static final int INITIAL_LENGTH = 1;
	private int tag;

	public Value(int tag) {
		this.tag = tag;
	}

	/**
	 * Factory for creating <tt>Value</tt> structure.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the <tt>Value</tt>
	 *            structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>Value</tt> structure
	 * @throws me.sedlar.bytecode.structure.InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws java.io.IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static Value create(DataInput in, ClassInfo classInfo) throws InvalidByteCodeException, IOException {
		int tagType = in.readUnsignedByte();
		Value entry;
		switch (tagType) {
			case BOOL_TAG:
			case BYTE_TAG:
			case CHAR_TAG:
			case DOUBLE_TAG:
			case FLOAT_TAG:
			case SHORT_TAG:
			case INT_TAG:
			case LONG_TAG:
			case STRING_TAG: {
				entry = new ConstValue(tagType);
				break;
			}
			case ENUM_TAG: {
				entry = new EnumValue();
				break;
			}
			case CLASS_TAG: {
				entry = new ClassValue();
				break;
			}
			case ANNOTATION_TAG: {
				entry = new AnnotationValue();
				break;
			}
			case ARRAY_TAG: {
				entry = new ArrayValue();
				break;
			}
			default: {
				throw new InvalidByteCodeException("Unknown tag " + (char) tagType);
			}
		}
		entry.setClassInfo(classInfo);
		entry.read(in);
		return entry;
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeByte(tag);
		if (debug)
			debug("wrote ");
	}

	protected abstract int specificLength();

	public final int length() {
		return INITIAL_LENGTH + specificLength();
	}

	public abstract String entryName();

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		if (accessFlags != 0)
			throw new RuntimeException("Access flags should be zero: " + Integer.toHexString(accessFlags));
		return "";
	}

	public int tag() {
		return tag;
	}

	public static String getTagDescription(int tag) {
		switch (tag) {
			case BOOL_TAG: {
				return BOOL_TAG_VERBOSE;
			}
			case BYTE_TAG: {
				return BYTE_TAG_VERBOSE;
			}
			case CHAR_TAG: {
				return CHAR_TAG_VERBOSE;
			}
			case DOUBLE_TAG: {
				return DOUBLE_TAG_VERBOSE;
			}
			case FLOAT_TAG: {
				return FLOAT_TAG_VERBOSE;
			}
			case SHORT_TAG: {
				return SHORT_TAG_VERBOSE;
			}
			case INT_TAG: {
				return INT_TAG_VERBOSE;
			}
			case LONG_TAG: {
				return LONG_TAG_VERBOSE;
			}
			case STRING_TAG: {
				return STRING_TAG_VERBOSE;
			}
			case ENUM_TAG: {
				return ENUM_TAG_VERBOSE;
			}
			case CLASS_TAG: {
				return CLASS_TAG_VERBOSE;
			}
			case ANNOTATION_TAG: {
				return ANNOTATION_TAG_VERBOSE;
			}
			case ARRAY_TAG: {
				return ARRAY_TAG_VERBOSE;
			}
			default: {
				return "Unknown";
			}
		}
	}
}
