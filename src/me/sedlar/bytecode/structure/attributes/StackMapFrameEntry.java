/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.AbstractStructure;
import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class StackMapFrameEntry extends AbstractStructure {

	private static final VerificationTypeInfoEntry[] NO_ENTRIES = new VerificationTypeInfoEntry[0];

	/**
	 * Factory method for creating <tt>StackMapFrameEntry</tt> structure.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>StackMapFrameEntry</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @param previousOffset
	 *            the offset of the previous stack map frame
	 * @return the new <tt>StackMapFrameEntry</tt> structure
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static StackMapFrameEntry create(DataInput in, ClassInfo classInfo, int previousOffset)
			throws InvalidByteCodeException, IOException {
		StackMapFrameEntry entry = new StackMapFrameEntry();
		entry.setClassInfo(classInfo);
		entry.read(in);
		entry.offset = previousOffset + entry.deltaOffset();
		return entry;
	}

	private int tag;
	private StackFrameType frameType;
	private int deltaOffset;
	private int offset;
	private VerificationTypeInfoEntry localItems[] = NO_ENTRIES;
	private VerificationTypeInfoEntry stackItems[] = NO_ENTRIES;

	/**
	 * Returns the frame tag
	 */
	public int tag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	/**
	 * Returns the frame type category
	 */
	public StackFrameType type() {
		return frameType;
	}

	/**
	 * Sets the frame type
	 */
	public void setFrameType(StackFrameType frameType) {
		this.frameType = frameType;
	}

	/**
	 * Returns the offset delta
	 */
	public int deltaOffset() {
		return deltaOffset;
	}

	/**
	 * Sets the offset delta.
	 */
	public void setDeltaOffset(int deltaOffset) {
		this.deltaOffset = deltaOffset;
	}

	/**
	 * Returns the offset
	 */
	public int offset() {
		return offset;
	}

	/**
	 * Sets the offset.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Returns the local variable verifications
	 */
	public VerificationTypeInfoEntry[] localItems() {
		return localItems;
	}

	/**
	 * Sets the local verification items. No consistency check will be
	 * performed.
	 */
	public void setLocalItems(VerificationTypeInfoEntry[] localItems) {
		this.localItems = localItems;
	}

	/**
	 * Returns the stack variable verifications
	 */
	public VerificationTypeInfoEntry[] stackItems() {
		return stackItems;
	}

	/**
	 * Sets the stack verification items. No consistency check will be
	 * performed.
	 */
	public void setStackItems(VerificationTypeInfoEntry[] stackItems) {
		this.stackItems = stackItems;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		tag = in.readUnsignedByte();
		frameType = StackFrameType.getFromTag(tag);
		switch (frameType) {
			case SAME: {
				deltaOffset = tag;
				break;
			}
			case SAME_LOCALS_1_STACK_ITEM: {
				readOneStackItem(in);
				break;
			}
			case SAME_LOCALS_1_STACK_ITEM_EXT: {
				readOneStackItemExt(in);
				break;
			}
			case CHOP: {
				readChop(in);
				break;
			}
			case SAME_EXT: {
				readSameExt(in);
				break;
			}
			case APPEND: {
				readAppend(in);
				break;
			}
			case FULL: {
				readFull(in);
				break;
			}
			default: {
				throw new IllegalStateException(frameType.toString());
			}
		}
		if (debug)
			debug("read ");
	}

	private void readOneStackItem(DataInput in) throws InvalidByteCodeException, IOException {
		deltaOffset = tag - 64;
		stackItems = new VerificationTypeInfoEntry[1];
		stackItems[0] = VerificationTypeInfoEntry.create(in, classInfo);
	}

	private void readOneStackItemExt(DataInput in) throws InvalidByteCodeException, IOException {
		deltaOffset = in.readUnsignedShort();
		stackItems = new VerificationTypeInfoEntry[1];
		stackItems[0] = VerificationTypeInfoEntry.create(in, classInfo);
	}

	private void readChop(DataInput in) throws InvalidByteCodeException, IOException {
		deltaOffset = in.readUnsignedShort();
	}

	private void readSameExt(DataInput in) throws InvalidByteCodeException, IOException {
		deltaOffset = in.readUnsignedShort();
	}

	private void readAppend(DataInput in) throws InvalidByteCodeException, IOException {
		deltaOffset = in.readUnsignedShort();
		int numLocals = tag - 251;
		localItems = new VerificationTypeInfoEntry[numLocals];
		for (int i = 0; i < numLocals; i++) {
			localItems[i] = VerificationTypeInfoEntry.create(in, classInfo);
		}
	}

	private void readFull(DataInput in) throws InvalidByteCodeException, IOException {
		deltaOffset = in.readUnsignedShort();
		int numLocals = in.readUnsignedShort();
		localItems = new VerificationTypeInfoEntry[numLocals];
		for (int i = 0; i < numLocals; i++)
			localItems[i] = VerificationTypeInfoEntry.create(in, classInfo);
		int numStacks = in.readUnsignedShort();
		stackItems = new VerificationTypeInfoEntry[numStacks];
		for (int i = 0; i < numStacks; i++)
			stackItems[i] = VerificationTypeInfoEntry.create(in, classInfo);
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeByte(tag);
		switch (frameType) {
			case SAME: {
				break;
			}
			case SAME_LOCALS_1_STACK_ITEM: {
				writeOneStackItem(out);
				break;
			}
			case SAME_LOCALS_1_STACK_ITEM_EXT: {
				writeOneStackItemExt(out);
				break;
			}
			case CHOP: {
				writeChop(out);
				break;
			}
			case SAME_EXT: {
				writeSameExt(out);
				break;
			}
			case APPEND: {
				writeAppend(out);
				break;
			}
			case FULL: {
				writeFull(out);
				break;
			}
			default: {
				throw new IllegalStateException(frameType.name());
			}
		}
		if (debug)
			debug("wrote ");
	}

	private void writeOneStackItem(DataOutput out) throws IOException, InvalidByteCodeException {
		stackItems[0].write(out);
	}

	private void writeOneStackItemExt(DataOutput out) throws IOException, InvalidByteCodeException {
		out.writeShort(deltaOffset);
		stackItems[0].write(out);
	}

	private void writeChop(DataOutput out) throws IOException {
		out.writeShort(deltaOffset);
	}

	private void writeSameExt(DataOutput out) throws IOException {
		out.writeShort(deltaOffset);
	}

	private void writeAppend(DataOutput out) throws IOException, InvalidByteCodeException {
		out.writeShort(deltaOffset);
		for (VerificationTypeInfoEntry localItem : localItems)
			localItem.write(out);
	}

	private void writeFull(DataOutput out) throws IOException, InvalidByteCodeException {
		out.writeShort(deltaOffset);
		out.writeShort(localItems.length);
		for (VerificationTypeInfoEntry localItem : localItems)
			localItem.write(out);
		out.writeShort(stackItems.length);
		for (VerificationTypeInfoEntry stackItem : stackItems)
			stackItem.write(out);
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "StackMapFrame entry of type " + frameType);
	}

	/**
	 * Returns the verbose representation for display in the UI
	 */
	public String getVerbose() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("<b>").append(frameType).append("</b> (").append(tag).append(')');
		appendOffset(buffer);
		switch (frameType) {
			case SAME:
			case CHOP:
			case SAME_EXT: {
				break;
			}
			case SAME_LOCALS_1_STACK_ITEM:
			case SAME_LOCALS_1_STACK_ITEM_EXT: {
				appendStack(buffer);
				break;
			}
			case APPEND: {
				appendLocals(buffer);
				break;
			}
			case FULL: {
				appendLocals(buffer);
				appendStack(buffer);
				break;
			}
		}
		return buffer.toString().replace("\n", "<br>").replace(" ", "&nbsp;");
	}

	private void appendOffset(StringBuilder buffer) {
		buffer.append(", Offset: ").append(offset).append(" (+").append(deltaOffset).append(")");
	}

	private void appendStack(StringBuilder buffer) {
		buffer.append("\n    Stack verifications:\n");
		for (int i = 0; i < stackItems.length; i++) {
			buffer.append("        ");
			stackItems[i].appendTo(buffer);
			if (i < stackItems.length - 1)
				buffer.append("\n");
		}
	}

	private void appendLocals(StringBuilder buffer) {
		buffer.append("\n    Local verifications:\n");
		for (int i = 0; i < localItems.length; i++) {
			buffer.append("        ");
			localItems[i].appendTo(buffer);
			if (i < localItems.length - 1)
				buffer.append("\n");
		}
	}

	/**
	 * Returns the bytecode length of the entry
	 */
	public int length() {
		switch (frameType) {
			case SAME: {
				return 1;
			}
			case SAME_LOCALS_1_STACK_ITEM: {
				return 1 + vertificationInfoLength(stackItems);
			}
			case SAME_LOCALS_1_STACK_ITEM_EXT: {
				return 3 + vertificationInfoLength(stackItems);
			}
			case CHOP:
			case SAME_EXT: {
				return 3;
			}
			case APPEND: {
				return 3 + vertificationInfoLength(localItems);
			}
			case FULL: {
				return 7 + vertificationInfoLength(localItems) + vertificationInfoLength(stackItems);
			}
		}
		return 0;
	}

	private int vertificationInfoLength(VerificationTypeInfoEntry entries[]) {
		int size = 0;
		for (VerificationTypeInfoEntry entry : entries) {
			size += entry.length();
		}
		return size;
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		if (accessFlags != 0) {
			throw new RuntimeException("Access flags should be zero: " + Integer.toHexString(accessFlags));
		}
		return "";
	}
}
