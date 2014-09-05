/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure;

import java.io.DataInput;
import java.io.IOException;

import me.sedlar.bytecode.structure.constants.*;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class ConstantPool extends AbstractStructure {

	public static final byte CONSTANT_CLASS = 7;
	public static final byte CONSTANT_FIELDREF = 9;
	public static final byte CONSTANT_METHODREF = 10;
	public static final byte CONSTANT_INTERFACE_METHODREF = 11;
	public static final byte CONSTANT_STRING = 8;
	public static final byte CONSTANT_INTEGER = 3;
	public static final byte CONSTANT_FLOAT = 4;
	public static final byte CONSTANT_LONG = 5;
	public static final byte CONSTANT_DOUBLE = 6;
	public static final byte CONSTANT_NAME_AND_TYPE = 12;
	public static final byte CONSTANT_METHOD_HANDLE = 15;
	public static final byte CONSTANT_METHOD_TYPE = 16;
	public static final byte CONSTANT_INVOKE_DYNAMIC = 18;
	public static final byte CONSTANT_UTF8 = 1;

	public static final String CONSTANT_CLASS_VERBOSE = "CONSTANT_Class_info";
	public static final String CONSTANT_FIELDREF_VERBOSE = "CONSTANT_Fieldref_info";
	public static final String CONSTANT_METHODREF_VERBOSE = "CONSTANT_Methodref_info";
	public static final String CONSTANT_INTERFACE_METHODREF_VERBOSE = "CONSTANT_InterfaceMethodref_info";
	public static final String CONSTANT_STRING_VERBOSE = "CONSTANT_String_info";
	public static final String CONSTANT_INTEGER_VERBOSE = "CONSTANT_Integer_info";
	public static final String CONSTANT_FLOAT_VERBOSE = "CONSTANT_Float_info";
	public static final String CONSTANT_LONG_VERBOSE = "CONSTANT_Long_info";
	public static final String CONSTANT_DOUBLE_VERBOSE = "CONSTANT_Double_info";
	public static final String CONSTANT_NAME_AND_TYPE_VERBOSE = "CONSTANT_NameAndType_info";
	public static final String CONSTANT_METHOD_HANDLE_VERBOSE = "CONSTANT_MethodHandle_info";
	public static final String CONSTANT_METHOD_TYPE_VERBOSE = "CONSTANT_MethodType_info";
	public static final String CONSTANT_INVOKE_DYNAMIC_VERBOSE = "CONSTANT_InvokeDynamic_info";
	public static final String CONSTANT_UTF8_VERBOSE = "CONSTANT_Utf8_info";

	/**
	 * Factory method for creating <tt>ConstantPool</tt> structure.
	 * <p>
	 * A <tt>ConstantPool</tt> of the appropriate subtype from the
	 * <tt>constants</tt> package is created.
	 * <p>
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>ConstantPool</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>ConstantPool</tt> structure
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static ConstantPool create(DataInput in, ClassInfo classInfo) throws InvalidByteCodeException,
			IOException {
		ConstantPool pool;
		byte tag = in.readByte();
		switch (tag) {
			case CONSTANT_CLASS: {
				pool = new ConstantClassInfo();
				break;
			}
			case CONSTANT_FIELDREF: {
				pool = new ConstantFieldrefInfo();
				break;
			}
			case CONSTANT_METHODREF: {
				pool = new ConstantMethodrefInfo();
				break;
			}
			case CONSTANT_INTERFACE_METHODREF: {
				pool = new ConstantInterfaceMethodrefInfo();
				break;
			}
			case CONSTANT_STRING: {
				pool = new ConstantStringInfo();
				break;
			}
			case CONSTANT_INTEGER: {
				pool = new ConstantIntegerInfo();
				break;
			}
			case CONSTANT_FLOAT: {
				pool = new ConstantFloatInfo();
				break;
			}
			case CONSTANT_LONG: {
				pool = new ConstantLongInfo();
				break;
			}
			case CONSTANT_DOUBLE: {
				pool = new ConstantDoubleInfo();
				break;
			}
			case CONSTANT_NAME_AND_TYPE: {
				pool = new ConstantNameAndTypeInfo();
				break;
			}
			case CONSTANT_METHOD_TYPE: {
				pool = new ConstantMethodTypeInfo();
				break;
			}
			case CONSTANT_METHOD_HANDLE: {
				pool = new ConstantMethodHandleInfo();
				break;
			}
			case CONSTANT_INVOKE_DYNAMIC: {
				pool = new ConstantInvokeDynamicInfo();
				break;
			}
			case CONSTANT_UTF8: {
				pool = new ConstantUtf8Info();
				break;
			}
			default: {
				throw new InvalidByteCodeException("invalid constant pool entry with unknown tag " + tag);
			}
		}
		pool.setClassInfo(classInfo);
		pool.read(in);
		return pool;
	}

	/**
	 * Get the variable of the <tt>tag</tt> field of the <tt>cp_info</tt>
	 * structure.
	 *
	 * @return the tag
	 */
	public abstract byte tag();

	/**
	 * Get the verbose description of the <tt>tag</tt> field of the
	 * <tt>cp_info</tt> structure.
	 *
	 * @return the verbose description
	 */
	public abstract String verboseTag();

	/**
	 * Get the verbose description of the content of the constant pool entry.
	 *
	 * @return the verbose description
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 */
	public String verbose() throws InvalidByteCodeException {
		return "";
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof ConstantPool;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		if (accessFlags != 0)
			throw new RuntimeException("Access flags should be zero: " + Integer.toHexString(accessFlags));
		return "";
	}

	/**
	 * Skip a <tt>ConstantPool</tt> structure in a <tt>DataInput</tt>.
	 * <p>
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>ConstantPool</tt> structure
	 * @return the number of bits skipped
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static int skip(DataInput in) throws InvalidByteCodeException, IOException {
		int offset = 0;
		byte tag = in.readByte();
		switch (tag) {
			case CONSTANT_CLASS: {
				in.skipBytes(ConstantClassInfo.SIZE);
				break;
			}
			case CONSTANT_FIELDREF:
			case CONSTANT_METHODREF:
			case CONSTANT_INTERFACE_METHODREF: {
				in.skipBytes(ConstantReference.SIZE);
				break;
			}
			case CONSTANT_STRING: {
				in.skipBytes(ConstantStringInfo.SIZE);
				break;
			}
			case CONSTANT_INTEGER:
			case CONSTANT_FLOAT: {
				in.skipBytes(ConstantNumeric.SIZE);
				break;
			}
			case CONSTANT_LONG:
			case CONSTANT_DOUBLE: {
				in.skipBytes(ConstantLargeNumeric.SIZE);
				offset = 1;
				break;
			}
			case CONSTANT_NAME_AND_TYPE: {
				in.skipBytes(ConstantNameAndTypeInfo.SIZE);
				break;
			}
			case CONSTANT_METHOD_TYPE: {
				in.skipBytes(ConstantMethodTypeInfo.SIZE);
				break;
			}
			case CONSTANT_METHOD_HANDLE: {
				in.skipBytes(ConstantMethodHandleInfo.SIZE);
				break;
			}
			case CONSTANT_INVOKE_DYNAMIC: {
				in.skipBytes(ConstantInvokeDynamicInfo.SIZE);
				break;
			}
			case CONSTANT_UTF8: {
				// Length of the constant is determined by the length of the
				// byte
				// array
				in.skipBytes(in.readUnsignedShort());
				break;
			}
			default: {
				throw new InvalidByteCodeException("invalid constant pool entry with unknown tag " + tag);
			}
		}
		return offset;
	}

	/**
	 * Add a <tt>ConstantMethodRef</tt> constant pool entry to the constant pool
	 * of a <tt>ClassInfo</tt>.
	 *
	 * @param classInfo
	 *            the class file whose constant pool is to be edited
	 * @param className
	 *            the name of the referenced class
	 * @param methodName
	 *            the name of the referenced method
	 * @param methodSignature
	 *            the signature of the referenced method
	 * @param sizeDelta
	 *            the minimum increment by which the array holding the constant
	 *            pool is to be enlarged. Set to 0 if unsure.
	 * @return the constant pool index of the added <tt>ConstantMethodRef</tt>
	 */
	public static int addConstantMethodrefInfo(ClassInfo classInfo, String className, String methodName,
			String methodSignature, int sizeDelta) {
		sizeDelta = Math.max(sizeDelta, 6);
		int classIndex = addConstantClassInfo(classInfo, className, sizeDelta);
		int nameAndTypeIndex = addConstantNameAndTypeInfo(classInfo, methodName, methodSignature, sizeDelta);
		ConstantMethodrefInfo methodrefInfo = new ConstantMethodrefInfo();
		methodrefInfo.setClassInfo(classInfo);
		methodrefInfo.setClassIndex(classIndex);
		methodrefInfo.setNameAndTypeIndex(nameAndTypeIndex);
		return addConstantPoolEntry(classInfo, methodrefInfo, sizeDelta);
	}

	/**
	 * Add a <tt>ConstantFieldRef</tt> constant pool entry to the constant pool
	 * of a <tt>ClassInfo</tt>.
	 *
	 * @param classInfo
	 *            the class file whose constant pool is to be edited
	 * @param className
	 *            the name of the referenced class
	 * @param fieldName
	 *            the name of the referenced field
	 * @param fieldType
	 *            the type of the referenced field
	 * @param sizeDelta
	 *            the minimum increment by which the array holding the constant
	 *            pool is to be enlarged. Set to 0 if unsure.
	 * @return the constant pool index of the added <tt>ConstantMethodRef</tt>
	 */
	public static int addConstantFieldrefInfo(ClassInfo classInfo, String className, String fieldName,
			String fieldType, int sizeDelta) {
		sizeDelta = Math.max(sizeDelta, 6);
		int classIndex = addConstantClassInfo(classInfo, className, sizeDelta);
		int nameAndTypeIndex = addConstantNameAndTypeInfo(classInfo, fieldName, fieldType, sizeDelta);
		ConstantFieldrefInfo fieldrefInfo = new ConstantFieldrefInfo();
		fieldrefInfo.setClassInfo(classInfo);
		fieldrefInfo.setClassIndex(classIndex);
		fieldrefInfo.setNameAndTypeIndex(nameAndTypeIndex);
		return addConstantPoolEntry(classInfo, fieldrefInfo, sizeDelta);
	}

	/**
	 * Add a <tt>ConstantNameAndTypeInfo</tt> constant pool entry to the
	 * constant pool of a <tt>ClassInfo</tt>.
	 *
	 * @param classInfo
	 *            the class file whose constant pool is to be edited
	 * @param name
	 *            the name
	 * @param descriptor
	 *            the descriptor
	 * @param sizeDelta
	 *            the minimum increment by which the array holding the constant
	 *            pool is to be enlarged. Set to 0 if unsure.
	 * @return the constant pool index of the added
	 *         <tt>ConstantNameAndTypeInfo</tt>
	 */
	public static int addConstantNameAndTypeInfo(ClassInfo classInfo, String name, String descriptor, int sizeDelta) {
		sizeDelta = Math.max(sizeDelta, 3);
		int nameIndex = addConstantUTF8Info(classInfo, name, sizeDelta);
		int descriptorIndex = addConstantUTF8Info(classInfo, descriptor, sizeDelta);
		ConstantNameAndTypeInfo nameAndTypeInfo = new ConstantNameAndTypeInfo();
		nameAndTypeInfo.setClassInfo(classInfo);
		nameAndTypeInfo.setNameIndex(nameIndex);
		nameAndTypeInfo.setDescriptorIndex(descriptorIndex);
		return addConstantPoolEntry(classInfo, nameAndTypeInfo, sizeDelta);
	}

	/**
	 * Add a <tt>ConstantClassInfo</tt> constant pool entry to the constant pool
	 * of a <tt>ClassInfo</tt>.
	 *
	 * @param classInfo
	 *            the class file whose constant pool is to be edited
	 * @param className
	 *            the name of the referenced class
	 * @param sizeDelta
	 *            the minimum increment by which the array holding the constant
	 *            pool is to be enlarged. Set to 0 if unsure.
	 * @return the constant pool index of the added <tt>ConstantClassInfo</tt>
	 */
	public static int addConstantClassInfo(ClassInfo classInfo, String className, int sizeDelta) {
		sizeDelta = Math.max(sizeDelta, 2);
		int nameIndex = addConstantUTF8Info(classInfo, className, sizeDelta);
		ConstantClassInfo cci = new ConstantClassInfo();
		cci.setClassInfo(classInfo);
		cci.setIndex(nameIndex);
		return addConstantPoolEntry(classInfo, cci, sizeDelta);
	}

	/**
	 * Add a <tt>ConstantUTF8Info</tt> constant pool entry to the constant pool
	 * of a <tt>ClassInfo</tt>.
	 *
	 * @param classInfo
	 *            the class file whose constant pool is to be edited
	 * @param string
	 *            the string
	 * @param sizeDelta
	 *            the minimum increment by which the array holding the constant
	 *            pool is to be enlarged. Set to 0 if unsure.
	 * @return the constant pool index of the added <tt>ConstantUTF8Info</tt>
	 */
	public static int addConstantUTF8Info(ClassInfo classInfo, String string, int sizeDelta) {
		ConstantUtf8Info utf8Info = new ConstantUtf8Info();
		utf8Info.setClassInfo(classInfo);
		utf8Info.setString(string);
		return addConstantPoolEntry(classInfo, utf8Info, sizeDelta);
	}

	/**
	 * Add a constant pool entry to the constant pool of a <tt>ClassInfo</tt>.
	 *
	 * @param classInfo
	 *            the class file whose constant pool is to be edited
	 * @param newEntry
	 *            the new constant pool entry
	 * @param sizeDelta
	 *            the minimum increment by which the array holding the constant
	 *            pool is to be enlarged. Set to 0 if unsure.
	 * @return the constant pool index of the added constant pool entry
	 */
	public static int addConstantPoolEntry(ClassInfo classInfo, ConstantPool newEntry, int sizeDelta) {
		ConstantPool[] constantPool = classInfo.constantPool();
		int index = classInfo.constantPoolIndex(newEntry);
		if (index > -1)
			return index;
		int lastFreeIndex;
		lastFreeIndex = constantPool.length - 1;
		while (lastFreeIndex >= 0 && constantPool[lastFreeIndex] == null)
			lastFreeIndex--;
		if (lastFreeIndex == constantPool.length - 1) {
			ConstantPool[] newConstantPool = new ConstantPool[constantPool.length + Math.max(1, sizeDelta)];
			System.arraycopy(constantPool, 0, newConstantPool, 0, constantPool.length);
			classInfo.enlargeConstantPool(newConstantPool);
			constantPool = newConstantPool;
		}
		int newIndex = lastFreeIndex + 1;
		constantPool[newIndex] = newEntry;
		classInfo.registerConstantPoolEntry(newIndex);
		return newIndex;
	}
}
