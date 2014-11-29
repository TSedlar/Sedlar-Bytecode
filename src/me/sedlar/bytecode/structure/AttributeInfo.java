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

import me.sedlar.bytecode.structure.attributes.*;
import me.sedlar.bytecode.structure.constants.ConstantUtf8Info;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class AttributeInfo extends AbstractAttributeStructure {

	private int attributeNameIndex;
	private int attributeLength;
	private byte[] info;

	/**
	 * Factory method for creating <tt>AttributeInfo</tt> structure.
	 * <p>
	 * An <tt>AttributeInfo</tt> of the appropriate subtype from the
	 * <tt>attributes</tt> package is created unless the type of the attribute
	 * is unknown in which case an instance of <tt>AttributeInfo</tt> is
	 * returned.
	 * <p>
	 * <p/>
	 * Attributes are skipped if the environment variable
	 * <tt>SYSTEM_PROPERTY_SKIP_ATTRIBUTES</tt> is set to true.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>AttributeInfo</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>AttributeInfo</tt> structure
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static AttributeInfo createOrSkip(DataInput in, ClassInfo classInfo) throws InvalidByteCodeException,
			IOException {
		AttributeInfo attributeInfo = null;
		int attributeNameIndex = in.readUnsignedShort();
		int attributeLength = in.readInt();
		ConstantUtf8Info cpInfoName = classInfo.constantPoolUtf8Entry(attributeNameIndex);
		String attributeName;
		if (cpInfoName == null)
			return null;
		attributeName = cpInfoName.string();
		switch (attributeName) {
			case ConstantValueAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new ConstantValueAttribute();
				break;
			}
			case CodeAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new CodeAttribute();
				break;
			}
			case ExceptionsAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new ExceptionsAttribute();
				break;
			}
			case InnerClassesAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new InnerClassesAttribute();
				break;
			}
			case SyntheticAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new SyntheticAttribute();
				break;
			}
			case SourceFileAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new SourceFileAttribute();
				break;
			}
			case LineNumberTableAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new LineNumberTableAttribute();
				break;
			}
			case LocalVariableTableAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new LocalVariableTableAttribute();
				break;
			}
			case DeprecatedAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new DeprecatedAttribute();
				break;
			}
			case EnclosingMethodAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new EnclosingMethodAttribute();
				break;
			}
			case SignatureAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new SignatureAttribute();
				break;
			}
			case LocalVariableTypeTableAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new LocalVariableTypeTableAttribute();
				break;
			}
			case RuntimeVisibleAnnotationsAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new RuntimeVisibleAnnotationsAttribute();
				break;
			}
			case RuntimeInvisibleAnnotationsAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new RuntimeInvisibleAnnotationsAttribute();
				break;
			}
			case RuntimeVisibleParameterAnnotationsAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new RuntimeVisibleParameterAnnotationsAttribute();
				break;
			}
			case RuntimeInvisibleParameterAnnotationsAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new RuntimeInvisibleParameterAnnotationsAttribute();
				break;
			}
			case RuntimeVisibleTypeAnnotationsAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new RuntimeVisibleTypeAnnotationsAttribute();
				break;
			}
			case RuntimeInvisibleTypeAnnotationsAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new RuntimeInvisibleTypeAnnotationsAttribute();
				break;
			}
			case AnnotationDefaultAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new AnnotationDefaultAttribute();
				break;
			}
			case BootstrapMethodsAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new BootstrapMethodsAttribute();
				break;
			}
			case StackMapTableAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new StackMapTableAttribute();
				break;
			}
			case MethodParametersAttribute.ATTRIBUTE_NAME: {
				attributeInfo = new MethodParametersAttribute();
				break;
			}
			default: {
				attributeInfo = new AttributeInfo(attributeLength);
				break;
			}
		}
		attributeInfo.setNameIndex(attributeNameIndex);
		attributeInfo.setClassInfo(classInfo);
		attributeInfo.read(in);
		return attributeInfo;
	}

	/**
	 * Constructor.
	 */
	protected AttributeInfo() {
	}

	private AttributeInfo(int attributeLength) {
		this.attributeLength = attributeLength;
	}

	/**
	 * Get the constant pool index for the name of the attribute.
	 *
	 * @return the index
	 */
	public int nameIndex() {
		return attributeNameIndex;
	}

	/**
	 * Set the constant pool index for the name of the attribute.
	 *
	 * @param attributeNameIndex
	 *            the new index
	 */
	public void setNameIndex(int attributeNameIndex) {
		this.attributeNameIndex = attributeNameIndex;
	}

	/**
	 * Get the raw bits of the attribute.
	 * <p>
	 * <p/>
	 * Is non-null only if attribute is of unknown type.
	 *
	 * @return the byte array
	 */
	public byte[] info() {
		return info;
	}

	/**
	 * Set the raw bits of the attribute.
	 * <p>
	 * <p/>
	 * Works only if attribute is an instance of <tt>AttributeInfo</tt>.
	 *
	 * @param info
	 *            the new byte array
	 */
	public void setInfo(byte[] info) {
		this.info = info;
	}

	/**
	 * Get the name of the attribute.
	 *
	 * @return the name
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 */
	public String name() throws InvalidByteCodeException {
		return classInfo.constantPoolUtf8Entry(attributeNameIndex).string();
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		info = new byte[attributeLength];
		in.readFully(info);
		if (debug)
			debug("read " + message());
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeShort(attributeNameIndex);
		out.writeInt(length());
		if (getClass().equals(AttributeInfo.class)) {
			out.write(info);
			if (debug)
				debug("wrote " + message());
		}
	}

	/**
	 * Get the length of this attribute as a number of bits.
	 *
	 * @return the length
	 */
	public int length() {
		return length(info);
	}

	// cannot override debug because subclasses will call super.debug
	// and expect to call the implementation in AbstractStructure
	private String message() {
		String type;
		try {
			type = classInfo.constantPoolUtf8Entry(attributeNameIndex).string();
		} catch (InvalidByteCodeException ex) {
			type = "(unknown)";
		}
		return "uninterpreted attribute of reported type " + type;
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		if (accessFlags != 0)
			throw new RuntimeException("Access flags should be zero: " + Integer.toHexString(accessFlags));
		return "";
	}
}
