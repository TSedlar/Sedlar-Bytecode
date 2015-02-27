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

import me.sedlar.bytecode.structure.constants.ConstantUtf8Info;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public abstract class ClassMember extends AbstractAttributeStructure {

	/**
	 * The accessFlags flags of this class member.
	 */
	protected int accessFlags;
	/**
	 * the constant pool index of the name of this class member.
	 */
	protected int nameIndex;
	/**
	 * the constant pool index of the descriptor of this class member.
	 */
	protected int descriptorIndex;

	private String name, descriptor;

	/**
	 * Get the accessFlags flags of this class member.
	 *
	 * @return the accessFlags flags
	 */
	public int accessFlags() {
		return accessFlags;
	}

	/**
	 * Set the accessFlags flags of this class member.
	 *
	 * @param accessFlags
	 *            the accessFlags flags
	 */
	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}

	/**
	 * Get the constant pool index of the name of this class member.
	 *
	 * @return the index
	 */
	public int nameIndex() {
		return nameIndex;
	}

	/**
	 * Set the constant pool index of the name of this class member.
	 *
	 * @param nameIndex
	 *            the index
	 */
	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	/**
	 * Set the constant pool entry string data.
	 *
	 * @param name
	 *            The data to set the constant pool entry to.
	 */
	public void setName(String name) {
		ConstantUtf8Info cpinfo;
		try {
			cpinfo = classInfo.constantPoolUtf8Entry(nameIndex);
			cpinfo.setString((this.name = name));
		} catch (InvalidByteCodeException e) {
			throw new RuntimeException("invalid constant pool index");
		}
	}

	/**
	 * Get the constant pool index of the descriptor of this class member.
	 *
	 * @return the index
	 */
	public int descriptorIndex() {
		return descriptorIndex;
	}

	/**
	 * Set the constant pool index of the descriptor of this class member.
	 *
	 * @param descriptorIndex
	 *            the index
	 */
	public void setDescriptorIndex(int descriptorIndex) {
		this.descriptorIndex = descriptorIndex;
	}

	/**
	 * Set the constant pool entry string data.
	 *
	 * @param descriptor
	 *            The data to set the constant pool entry to.
	 */
	public void setDescriptor(String descriptor) {
		ConstantUtf8Info cpinfo;
		try {
			cpinfo = classInfo.constantPoolUtf8Entry(descriptorIndex);
			cpinfo.setString((this.descriptor = descriptor));
		} catch (InvalidByteCodeException e) {
			throw new RuntimeException("invalid constant pool index");
		}
	}

	/**
	 * Get the name of the class member.
	 *
	 * @return the name
	 */
	public String name() {
		if (name != null)
			return name;
		ConstantUtf8Info cpinfo;
		try {
			cpinfo = classInfo.constantPoolUtf8Entry(nameIndex);
		} catch (InvalidByteCodeException e) {
			return "invalid constant pool index";
		}
		return cpinfo != null ? (name = cpinfo.string()) : "invalid constant pool index";
	}

	/**
	 * Get the verbose descriptor of the class member.
	 *
	 * @return the descriptor
	 */
	public String descriptor() {
		if (descriptor != null)
			return descriptor;
		ConstantUtf8Info cpinfo;
		try {
			cpinfo = classInfo.constantPoolUtf8Entry(descriptorIndex);
		} catch (InvalidByteCodeException e) {
			return "invalid constant pool index";
		}
		return cpinfo != null ? (descriptor = cpinfo.string()) : "invalid constant pool index";
	}

	/**
	 * Get the the accessFlags flags of this class as a hex string.
	 *
	 * @return the hex string
	 */
	public String formattedAccessFlags() {
		return accessFlags(accessFlags);
	}

	/**
	 * Get the verbose description of the accessFlags flags of this class.
	 *
	 * @return the description
	 */
	public String verboseAccessFlags() {
		return verboseAccessFlags(accessFlags);
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		accessFlags = in.readUnsignedShort();
		nameIndex = in.readUnsignedShort();
		descriptorIndex = in.readUnsignedShort();
		readAttributes(in);
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeShort(accessFlags);
		out.writeShort(nameIndex);
		out.writeShort(descriptorIndex);
		writeAttributes(out);
	}

    @Override
    public String toString() {
        return classInfo.toString() + "." + name() + " " + descriptor();
    }
}
