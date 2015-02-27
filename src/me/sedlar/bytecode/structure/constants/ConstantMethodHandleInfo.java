/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.ConstantPool;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class ConstantMethodHandleInfo extends ConstantPool {

	/**
	 * Length of the constant pool data structure in bits.
	 */
	public static final int SIZE = 3;

	public static final int TYPE_GET_FIELD = 1;
	public static final int TYPE_GET_STATIC = 2;
	public static final int TYPE_PUT_FIELD = 3;
	public static final int TYPE_PUT_STATIC = 4;
	public static final int TYPE_INVOKE_VIRTUAL = 5;
	public static final int TYPE_INVOKE_STATIC = 6;
	public static final int TYPE_INVOKE_SPECIAL = 7;
	public static final int TYPE_NEW_INVOKE_SPECIAL = 8;
	public static final int TYPE_INVOKE_INTERFACE = 9;

	private int referenceIndex;
	private int type;

	@Override
	public byte tag() {
		return CONSTANT_METHOD_HANDLE;
	}

	@Override
	public String verboseTag() {
		return CONSTANT_METHOD_HANDLE_VERBOSE;
	}

	@Override
	public String verbose() throws InvalidByteCodeException {
		return name();
	}

	/**
	 * Get the index of the constant pool entry containing the reference.
	 *
	 * @return the index
	 */
	public int index() {
		return referenceIndex;
	}

	/**
	 * Set the index of the constant pool entry containing the reference.
	 *
	 * @param referenceIndex
	 *            the index
	 */
	public void setIndex(int referenceIndex) {
		this.referenceIndex = referenceIndex;
	}

	public int type() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String verboseType() {
		switch (type) {
			case TYPE_GET_FIELD: {
				return "REF_getField";
			}
			case TYPE_GET_STATIC: {
				return "REF_getStatic";
			}
			case TYPE_PUT_FIELD: {
				return "REF_putField";
			}
			case TYPE_PUT_STATIC: {
				return "REF_putStatic";
			}
			case TYPE_INVOKE_VIRTUAL: {
				return "REF_invokeVirtual";
			}
			case TYPE_INVOKE_STATIC: {
				return "REF_invokeStatic";
			}
			case TYPE_INVOKE_SPECIAL: {
				return "REF_invokeSpecial";
			}
			case TYPE_NEW_INVOKE_SPECIAL: {
				return "REF_newInvokeSpecial";
			}
			case TYPE_INVOKE_INTERFACE: {
				return "REF_invokeInterface";
			}
			default: {
				return "unknown variable " + type;
			}
		}
	}

	/**
	 * Get the descriptor.
	 *
	 * @return the descriptor
	 * @throws me.sedlar.bytecode.structure.InvalidByteCodeException
	 *             if the byte code is invalid
	 */
	public String name() throws InvalidByteCodeException {
		return verboseType() + " " + classInfo.constantPoolEntryName(referenceIndex);
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		type = in.readByte();
		referenceIndex = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeByte(CONSTANT_METHOD_HANDLE);
		out.write(type);
		out.writeShort(referenceIndex);
		if (debug)
			debug("wrote ");
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ConstantMethodHandleInfo))
			return false;
		ConstantMethodHandleInfo constantMethodHandleInfo = (ConstantMethodHandleInfo) object;
		return super.equals(object) && constantMethodHandleInfo.referenceIndex == referenceIndex
				&& constantMethodHandleInfo.type == type;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ referenceIndex;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + verboseTag() + " with reference_index " + referenceIndex + " and type " + type);
	}
}
