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

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class AbstractAttributeStructure extends AbstractStructure {

	/**
	 * Attributes of this structure.
	 */
	protected AttributeInfo[] attributes;

	/**
	 * Get the attributes of this structure.
	 *
	 * @return the attributes
	 */
	public AttributeInfo[] attributes() {
		return attributes;
	}

	/**
	 * Set the attributes of this structure.
	 *
	 * @param attributes
	 *            the new attributes
	 */
	public void setAttributes(AttributeInfo[] attributes) {
		this.attributes = attributes;
	}

	/**
	 * Find an attribute of a certain class.
	 *
	 * @param attributeClass
	 *            the class of the attribute
	 * @return the found attribute, <tt>null</tt> if not found
	 */
	public AttributeInfo findAttribute(Class<?> attributeClass) {
		AttributeInfo foundAttribute = null;
		for (AttributeInfo attribute : attributes) {
			if (attribute.getClass() == attributeClass) {
				foundAttribute = attribute;
				break;
			}
		}
		return foundAttribute;
	}

	/**
	 * Read the attributes of this structure from the given <tt>DataInput</tt>.
	 * <p>
	 * <p/>
	 * Expects <tt>DataInput</tt> to be in JVM class file format and just before
	 * an attribute length field.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	protected void readAttributes(DataInput in) throws InvalidByteCodeException, IOException {
		int attributesCount = in.readUnsignedShort();
		attributes = new AttributeInfo[attributesCount];
		for (int i = 0; i < attributesCount; i++)
			attributes[i] = AttributeInfo.createOrSkip(in, classInfo);
	}

	/**
	 * Write the attributes of this structure to the given <tt>DataOutput</tt>.
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
	protected void writeAttributes(DataOutput out) throws InvalidByteCodeException, IOException {
		int attributesCount = length(attributes);
		out.writeShort(attributesCount);
		for (int i = 0; i < attributesCount; i++) {
			if (attributes[i] == null)
				throw new InvalidByteCodeException("attribute " + i + " is null");
			attributes[i].write(out);
		}
	}

	/**
	 * Get the length of all attributes as a number of bits.
	 *
	 * @return the length
	 */
	protected int totalAtrributes() {
		int totalLength = 0;
		int attributesCount = length(attributes);
		for (int i = 0; i < attributesCount; i++) {
			if (attributes[i] != null)
				totalLength += attributes[i].length();
		}
		return totalLength;
	}
}
