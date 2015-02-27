/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure;

import static me.sedlar.bytecode.structure.AccessFlag.STATIC;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import me.sedlar.bytecode.structure.constants.ConstantClassInfo;
import me.sedlar.bytecode.structure.constants.ConstantLargeNumeric;
import me.sedlar.bytecode.structure.constants.ConstantUtf8Info;
import me.sedlar.util.Filter;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class ClassInfo extends AbstractAttributeStructure {

	private static final int MAGIC_NUMBER = 0xcafebabe;

	private int minorVersion;
	private int majorVersion;
	private ConstantPool[] constantPool;
	private HashMap<ConstantPool, Integer> constantPoolEntryToIndex = new HashMap<ConstantPool, Integer>();
	private int accessFlags;
	private int self;
	private int superClass;
	private String name, superName;
	private List<Integer> interfaces = new LinkedList<>();
	private List<FieldInfo> fields = new LinkedList<>();
	private List<MethodInfo> methods = new LinkedList<>();

	/**
	 * Constructor.
	 */
	public ClassInfo(byte[] bytes) throws IOException, InvalidByteCodeException {
		setClassInfo(this);
		read(new DataInputStream(new ByteArrayInputStream(bytes)));
	}

	public ClassInfo(InputStream input) throws IOException, InvalidByteCodeException {
		setClassInfo(this);
		read(new DataInputStream(input));
	}

	/**
	 * Get the minor version of the class file format.
	 *
	 * @return the minor version
	 */
	public int minor() {
		return minorVersion;
	}

	/**
	 * Set the minor version of the class file format.
	 *
	 * @param minorVersion
	 *            the minor version
	 */
	public void setMinor(int minorVersion) {
		this.minorVersion = minorVersion;
	}

	/**
	 * Get the major version of the class file format.
	 *
	 * @return the major version
	 */
	public int major() {
		return majorVersion;
	}

	/**
	 * Get the verbose major version of the class file format.
	 *
	 * @return the major version as text
	 */
	public String verboseMajor() {
		switch (majorVersion) {
			case 45:
				return "1.1";
			case 46:
				return "1.2";
			case 47:
				return "1.3";
			case 48:
				return "1.4";
			case 49:
				return "1.5";
			case 50:
				return "1.6";
			case 51:
				return "1.7";
			case 52:
				return "1.8";
			case 53:
				return "1.9";
			default:
				return "unknown value " + majorVersion;
		}
	}

	/**
	 * Set the major version of the class file format.
	 *
	 * @param majorVersion
	 *            the major version
	 */
	public void setMajor(int majorVersion) {
		this.majorVersion = majorVersion;
	}

	/**
	 * Get the array with all constant pool entries.
	 *
	 * @return the array
	 */
	public ConstantPool[] constantPool() {
		return constantPool;
	}

	/**
	 * Get the index of an equivalent constant pool entry.
	 *
	 * @param pool
	 *            the constant pool entry
	 * @return the index, -1 if no equivalent constant pool entry can be found
	 */
	public int constantPoolIndex(ConstantPool pool) {
		Integer index = constantPoolEntryToIndex.get(pool);
		return index != null ? index : -1;
	}

	/**
	 * Set the array with all constant pool entries. An internal hash map will
	 * need to be recalulated. If you add to the end of the constant pool, use
	 * <tt>enlargeConstantPool</tt>.
	 *
	 * @param constantPool
	 *            the array
	 */
	public void setConstantPool(ConstantPool[] constantPool) {
		this.constantPool = constantPool;
		for (int i = 0; i < constantPool.length; i++)
			constantPoolEntryToIndex.put(constantPool[i], i);
	}

	/**
	 * Set the array with all constant pool entries where the new array of
	 * constant pool entries starts with the old constant pool. If you delete
	 * entries, use <tt>setConstantPool</tt>.
	 *
	 * @param enlargedConstantPool
	 *            the array
	 */
	public void enlargeConstantPool(ConstantPool[] enlargedConstantPool) {
		int startIndex = constantPool == null ? 0 : constantPool.length;
		this.constantPool = enlargedConstantPool;
		for (int i = startIndex; i < constantPool.length; i++) {
			if (constantPool[i] != null)
				constantPoolEntryToIndex.put(constantPool[i], i);
		}
	}

	/**
	 * Register the constant pool entry at a given index, so that it can be
	 * found through the <tt>constantPoolIndex</tt> method.
	 *
	 * @param index
	 *            the index
	 */
	public void registerConstantPoolEntry(int index) {
		constantPoolEntryToIndex.put(constantPool[index], index);
	}

	/**
	 * Unregister the constant pool entry at a given index, so that it can no
	 * longer be found through the <tt>constantPoolIndex</tt> method.
	 *
	 * @param index
	 *            the index
	 */
	public void unregisterConstantPoolEntry(int index) {
		constantPoolEntryToIndex.remove(constantPool[index]);
	}

	/**
	 * Get the accessFlags flags of this class.
	 *
	 * @return the accessFlags flags
	 */
	public int accessFlags() {
		return accessFlags;
	}

	/**
	 * Set the accessFlags flags of this class.
	 *
	 * @param accessFlags
	 *            the accessFlags flags
	 */
	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}

	/**
	 * Get the constant pool index of this class.
	 *
	 * @return the index
	 */
	public int index() {
		return self;
	}

	/**
	 * Set the constant pool index of this class.
	 *
	 * @param self
	 *            the index
	 */
	public void setIndex(int self) {
		this.self = self;
	}

	/**
	 * Get the name of this class.
	 *
	 * @return the name
	 */
	public String name() {
		try {
			if (name != null)
				return name;
			return (name = constantPoolEntryName(index()));
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}

	/**
	 * Set the constant pool entry string data.
	 *
	 * @param name
	 *            The data to set the constant pool entry to.
	 */
	public void setName(String name) {
		try {
			ConstantClassInfo pool = (ConstantClassInfo) constantPoolAt(index());
			pool.setName((this.name = name));
		} catch (Exception e) {
			throw new RuntimeException("invalid constant pool index");
		}
	}

	/**
	 * Get the constant pool index of the super class of this class.
	 *
	 * @return the index
	 */
	public int superIndex() {
		return superClass;
	}

	/**
	 * Set the constant pool index of the super class of this class.
	 *
	 * @param superClass
	 *            the index
	 */
	public void setSuperIndex(int superClass) {
		this.superClass = superClass;
	}

	/**
	 * Get the name of the super class.
	 *
	 * @return the name
	 */
	public String superName() {
		try {
			if (superName != null)
				return superName;
			return (superName = constantPoolEntryName(superIndex()));
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}

	/**
	 * Set the constant pool entry string data.
	 *
	 * @param name
	 *            The data to set the constant pool entry to.
	 */
	public void setSuperName(String name) {
		try {
			ConstantClassInfo pool = (ConstantClassInfo) constantPoolAt(superIndex());
			pool.setName((this.superName = name));
		} catch (Exception e) {
			throw new RuntimeException("invalid constant pool index");
		}
	}

	/**
	 * Get the array with the constant pool entries of all interfaces.
	 *
	 * @return the array
	 */
	public List<String> interfaces() {
		List<String> list = new LinkedList<>();
		for (int iface : interfaces) {
			try {
				list.add(((ConstantClassInfo) constantPoolAt(iface)).name());
			} catch (InvalidByteCodeException ignored) {
			}
		}
		return list;
	}

	/**
	 * Adds the given interface to the classes implemented interfaces.
	 *
	 * @param iface
	 *            the interface to implement
	 */
	public void addInterface(String iface) {
		interfaces.add(ConstantPool.addConstantClassInfo(this, iface, 2));
	}

	/**
	 * Get the list with the <tt>FieldInfo</tt> structure for the fields of this
	 * class.
	 *
	 * @return the list
	 */
	public List<FieldInfo> fields() {
		return fields;
	}

	/**
	 * Get the list with the <tt>MethodInfo</tt> structure for the methods of
	 * this class.
	 *
	 * @return the list
	 */
	public List<MethodInfo> methods() {
		return methods;
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

	/**
	 * Get the constant pool entry at the specified index and cast it to a
	 * specified class.
	 *
	 * @param index
	 *            the index
	 *
	 * @return the constant pool entry.
	 */
	public ConstantPool constantPoolAt(int index) {
		if (!checkValidConstantPoolIndex(index))
			return null;
		ConstantPool pool = constantPool[index];
		if (pool == null)
			return null;
		return pool;
	}

	/**
	 * Get the <tt>ConstantUtf8Info</tt> constant pool entry at the specified
	 * index.
	 *
	 * @param index
	 *            the index
	 * @return the constant pool entry
	 * @throws InvalidByteCodeException
	 *             if the entry is not a <tt>ConstantUtf8Info</tt>
	 */
	public ConstantUtf8Info constantPoolUtf8Entry(int index) throws InvalidByteCodeException {
		return (ConstantUtf8Info) constantPoolAt(index, ConstantUtf8Info.class);
	}

	/**
	 * Get the constant pool entry at the specified index and cast it to a
	 * specified class.
	 *
	 * @param index
	 *            the index
	 * @param entryClass
	 *            the required subtype of <tt>ConstantPool</tt>
	 * @return the constant pool entry
	 * @throws InvalidByteCodeException
	 *             if the entry is of a different class than expected
	 */
	public ConstantPool constantPoolAt(int index, Class<? extends ConstantPool> entryClass)
			throws InvalidByteCodeException {
		if (!checkValidConstantPoolIndex(index))
			return null;
		ConstantPool pool = constantPool[index];
		if (pool == null)
			return null;
		if (entryClass.isAssignableFrom(pool.getClass())) {
			return pool;
		} else {
			throw new InvalidByteCodeException("constant pool entry at " + index + " is not assignable to "
					+ entryClass.getName());
		}
	}

	/**
	 * Get an approximate verbose description of the content of the constant
	 * pool entry at the specified index.
	 *
	 * @param index
	 *            the index
	 * @return the description
	 * @throws InvalidByteCodeException
	 *             if the entry is invalid
	 */
	public String constantPoolEntryName(int index) throws InvalidByteCodeException {
		if (!checkValidConstantPoolIndex(index))
			return null;
		ConstantPool pool = constantPool[index];
		return pool != null ? pool.verbose() : "invalid constant pool index";
	}

	/**
	 * Get the <tt>FieldInfo</tt> for given field name and signature.
	 *
	 * @param name
	 *            the field name.
	 * @param descriptor
	 *            the signature.
	 * @return the <tt>FieldInfo</tt> or <tt>null</tt> if not found.
	 */
	public FieldInfo field(String name, String descriptor) {
		for (FieldInfo field : fields) {
			if (name == null || field.name().equals(name)) {
				if (descriptor == null || field.descriptor().equals(descriptor))
					return field;
			}
		}
		return null;
	}

	/**
	 * Get the <tt>FieldInfo</tt> for given field name
	 *
	 * @param name
	 *            the field name.
	 * @return the <tt>FieldInfo</tt> or <tt>null</tt> if not found.
	 */
	public FieldInfo fieldByName(String name) {
		return field(name, null);
	}

	/**
	 * Get the <tt>FieldInfo</tt> for the given field descriptor
	 *
	 * @param descriptor
	 *            the field descriptor
	 * @return the <tt>FieldInfo</tt> or <tt>null</tt> if not found.
	 */
	public FieldInfo fieldByDesc(String descriptor) {
		return field(null, descriptor);
	}

	/**
	 * Get the <tt>MethodInfo</tt> for given method name and signature.
	 *
	 * @param name
	 *            the method name.
	 * @param descriptor
	 *            the signature.
	 * @return the <tt>MethodInfo</tt> or <tt>null</tt> if not found.
	 */
	public MethodInfo method(String name, String descriptor) {
		for (MethodInfo method : methods) {
			if (name == null || method.name().equals(name)) {
				if (descriptor == null || method.descriptor().equals(descriptor))
					return method;
			}
		}
		return null;
	}

	/**
	 * Get the <tt>MethodInfo</tt> for the given method name.
	 *
	 * @param name
	 *            the method name.
	 * @return the <tt>MethodInfo</tt> or <tt>null</tt> if not found.
	 */
	public MethodInfo methodByName(String name) {
		return method(name, null);
	}

	/**
	 * Get the <tt>MethodInfo</tt> for the given method descriptor.
	 *
	 * @param descriptor
	 *            the method descriptor.
	 * @return the <tt>MethodInfo</tt> or <tt>null</tt> if not found.
	 */
	public MethodInfo methodByDesc(String descriptor) {
		for (MethodInfo method : methods) {
			if (method.descriptor().equals(descriptor))
				return method;
		}
		return null;
	}

	/**
	 * Get the <tt>MethodInfo</tt> for the given filter.
	 *
	 * @param filter
	 *            the filter to match.
	 * @return the <tt>MethodInfo</tt> or <tt>null</tt> if not found.
	 */
	public MethodInfo methodByFilter(Filter<MethodInfo> filter) {
		for (MethodInfo mi : methods()) {
			if (filter.accept(mi))
				return mi;
		}
		return null;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		readMagicNumber(in);
		readVersion(in);
		readConstantPool(in);
		readAccessFlags(in);
		readSelf(in);
		readSuper(in);
		readInterfaces(in);
		readFields(in);
		readMethods(in);
		readAttributes(in);
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		writeMagicNumber(out);
		writeVersion(out);
		writeConstantPool(out);
		writeAccessFlags(out);
		writeSelf(out);
		writeSuper(out);
		writeInterfaces(out);
		writeFields(out);
		writeMethods(out);
		writeAttributes(out);
	}

	private boolean checkValidConstantPoolIndex(int index) {
		return !(index < 1 || index >= constantPool.length);
	}

	private void readMagicNumber(DataInput in) throws InvalidByteCodeException, IOException {
		int magicNumber = in.readInt();
		if (magicNumber != MAGIC_NUMBER) {
			throw new InvalidByteCodeException("Invalid magic number 0x" + Integer.toHexString(magicNumber)
					+ " instead of 0x" + Integer.toHexString(MAGIC_NUMBER));
		}
		if (debug)
			debug("read magic number");
	}

	private void writeMagicNumber(DataOutput out) throws IOException {
		out.writeInt(MAGIC_NUMBER);
		if (debug)
			debug("wrote magic number");
	}

	private void readVersion(DataInput in) throws IOException {
		minorVersion = in.readUnsignedShort();
		if (debug)
			debug("read minor version " + minorVersion);
		majorVersion = in.readUnsignedShort();
		if (debug)
			debug("read major version " + majorVersion);
		checkMajor(majorVersion);
	}

	private void writeVersion(DataOutput out) throws IOException {
		out.writeShort(minorVersion);
		if (debug)
			debug("wrote minor version " + minorVersion);
		out.writeShort(majorVersion);
		if (debug)
			debug("wrote major version " + majorVersion);
		checkMajor(majorVersion);
	}

	private void readConstantPool(DataInput in) throws InvalidByteCodeException, IOException {
		constantPoolEntryToIndex.clear();
		int constantPoolCount = in.readUnsignedShort();
		if (debug)
			debug("read constant pool count " + constantPoolCount);
		constantPool = new ConstantPool[constantPoolCount];
		// constantPool has effective length constantPoolCount - 1
		// constantPool[0] defaults to null
		for (int i = 1; i < constantPoolCount; i++) {
			// create CPInfos via factory method since the actual type
			// of the constant is not yet known
			if (debug)
				debug("reading constant pool entry " + i);
			constantPool[i] = ConstantPool.create(in, this);
			constantPoolEntryToIndex.put(constantPool[i], i);
			if (constantPool[i] instanceof ConstantLargeNumeric) {
				// CONSTANT_Double_info and CONSTANT_Long_info take 2 constant
				// pool entries, the second entry is unusable (design mistake)
				i++;
			}
		}
	}

	private void writeConstantPool(DataOutput out) throws InvalidByteCodeException, IOException {
		int lastFreeIndex;
		lastFreeIndex = length(constantPool) - 1;
		while (lastFreeIndex >= 0 && constantPool[lastFreeIndex] == null)
			lastFreeIndex--;
		out.writeShort(lastFreeIndex + 1);
		if (debug)
			debug("wrote constant pool count " + (lastFreeIndex + 1));
		// constantPool[0] defaults to null and is not bytesWritten into the
		// class file
		for (int i = 1; i <= lastFreeIndex; i++) {
			if (constantPool[i] == null)
				throw new InvalidByteCodeException("constant pool entry " + i + " is null");
			if (debug)
				debug("writing constant pool entry " + i);
			constantPool[i].write(out);
			if (constantPool[i] instanceof ConstantLargeNumeric) {
				// CONSTANT_Double_info and CONSTANT_Long_info take 2 constant
				// pool entries, the second entry is unusable (design mistake)
				i++;
			}
		}
	}

	private void readAccessFlags(DataInput in) throws IOException {
		accessFlags = in.readUnsignedShort();
		if (debug)
			debug("read access flags " + accessFlags(accessFlags));
	}

	private void writeAccessFlags(DataOutput out) throws IOException {
		out.writeShort(accessFlags);
		if (debug)
			debug("wrote access flags " + accessFlags(accessFlags));
	}

	private void readSelf(DataInput in) throws IOException {
		self = in.readUnsignedShort();
		if (debug)
			debug("read this_class index " + self);
	}

	private void writeSelf(DataOutput out) throws IOException {
		out.writeShort(self);
		if (debug)
			debug("wrote index " + self);
	}

	private void readSuper(DataInput in) throws IOException {
		superClass = in.readUnsignedShort();
		if (debug)
			debug("read super_class index " + superClass);
	}

	private void writeSuper(DataOutput out) throws IOException {
		out.writeShort(superClass);
		if (debug)
			debug("wrote super_class index " + superClass);
	}

	private void readInterfaces(DataInput in) throws IOException {
		int interfacesCount = in.readUnsignedShort();
		if (debug)
			debug("read interfaces count " + interfacesCount);
		for (int i = 0; i < interfacesCount; i++) {
			interfaces.add(in.readUnsignedShort());
			if (debug)
				debug("read interface index " + interfaces.get(i));
		}

	}

	private void writeInterfaces(DataOutput out) throws IOException {
		int interfacesCount = length(interfaces);
		out.writeShort(interfacesCount);
		if (debug)
			debug("wrote interfaces count " + interfacesCount);
		for (int i = 0; i < interfacesCount; i++) {
			out.writeShort(interfaces.get(i));
			if (debug)
				debug("wrote interface index " + interfaces.get(i));
		}
	}

	private void readFields(DataInput in) throws InvalidByteCodeException, IOException {
		int fieldsCount = in.readUnsignedShort();
		if (debug)
			debug("read fields count " + fieldsCount);
		for (int i = 0; i < fieldsCount; i++)
			fields.add(FieldInfo.create(in, this));
	}

	private void writeFields(DataOutput out) throws InvalidByteCodeException, IOException {
		int fieldsCount = length(fields);
		out.writeShort(fieldsCount);
		if (debug)
			debug("wrote fields count " + fieldsCount);
		for (int i = 0; i < fieldsCount; i++) {
			if (fields.get(i) == null)
				throw new InvalidByteCodeException("field " + i + " is null");
			fields.get(i).write(out);
		}
	}

	private void readMethods(DataInput in) throws InvalidByteCodeException, IOException {
		int methodsCount = in.readUnsignedShort();
		if (debug)
			debug("read methods count " + methodsCount);
		for (int i = 0; i < methodsCount; i++)
			methods.add(MethodInfo.create(in, this));
	}

	private void writeMethods(DataOutput out) throws InvalidByteCodeException, IOException {
		int methodsCount = length(methods);
		out.writeShort(methodsCount);
		if (debug)
			debug("wrote methods count " + methodsCount);
		for (int i = 0; i < methodsCount; i++) {
			if (methods.get(i) == null)
				throw new InvalidByteCodeException("method " + i + " is null");
			methods.get(i).write(out);
		}
	}

	@Override
	protected void readAttributes(DataInput in) throws InvalidByteCodeException, IOException {
		super.readAttributes(in);
		if (debug)
			debug("read " + length(attributes) + " attributes for the ClassInfo structure");
	}

	@Override
	protected void writeAttributes(DataOutput out) throws InvalidByteCodeException, IOException {
		super.writeAttributes(out);
		if (debug)
			debug("wrote " + length(attributes) + " attributes for the ClassInfo structure");
	}

	private void checkMajor(int majorVersion) {
		if (majorVersion < 45 || majorVersion > 52)
			System.err.println("major version should be between 45 and 51 for JDK <= 1.8, was " + majorVersion);
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		return verboseAccessFlags(AccessFlag.CLASS_ACCESS_FLAGS, accessFlags);
	}

	public boolean ownerless() {
		return superName().equals("java/lang/Object");
	}

	public int methodCount(String desc, boolean ignoreStatic) {
		int count = 0;
		for (MethodInfo method : methods) {
			if (ignoreStatic && STATIC.is(method.accessFlags()))
				continue;
			if (method.descriptor().equals(desc))
				count++;
		}
		return count;
	}

	public int methodCount(String desc) {
		return methodCount(desc, true);
	}

	public int fieldCount(String desc, boolean ignoreStatic) {
		int count = 0;
		for (FieldInfo field : fields) {
			if (ignoreStatic && STATIC.is(field.accessFlags()))
				continue;
			if (field.descriptor().equals(desc))
				count++;
		}
		return count;
	}

	public int fieldCount(String desc) {
		return fieldCount(desc, true);
	}

	public int abnormalFieldCount(boolean ignoreStatic) {
		int count = 0;
		for (FieldInfo field : fields) {
			if (ignoreStatic && STATIC.is(field.accessFlags()))
				continue;
			String desc = field.descriptor();
			if (desc.contains("L") && desc.endsWith(";") && !desc.contains("java"))
				count++;
		}
		return count;
	}

	public int abnormalFieldCount() {
		return abnormalFieldCount(true);
	}

	public int fieldTypeCount(boolean ignoreStatic) {
		List<String> types = new ArrayList<>();
		for (FieldInfo field : fields) {
			if (ignoreStatic && STATIC.is(field.accessFlags()))
				continue;
			String desc = field.descriptor();
			if (!types.contains(desc))
				types.add(desc);
		}
		return types.size();
	}

	public int fieldTypeCount() {
		return fieldTypeCount(true);
	}

    @Override
    public String toString() {
        return name;
    }
}
