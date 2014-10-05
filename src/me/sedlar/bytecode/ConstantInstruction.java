/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode;

import static me.sedlar.bytecode.structure.ConstantPool.*;

import java.io.IOException;

import me.sedlar.bytecode.io.BytecodeInput;
import me.sedlar.bytecode.io.BytecodeOutput;
import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.ConstantPool;
import me.sedlar.bytecode.structure.constants.*;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class ConstantInstruction extends AbstractInstruction {

	private AbstractInstruction instruction;

	/**
	 * Constructor.
	 *
	 * @param ibi
	 *            an ImmediateByteInstruction for namesake an LDC* that pushes a
	 *            byte.
	 */
	public ConstantInstruction(ImmediateByteInstruction ibi) {
		super(ibi.methodInfo(), ibi.opcode());
		this.instruction = ibi;
	}

	/**
	 * Constructor.
	 *
	 * @param isi
	 *            an ImmediateShortInstruction for namesake an LDC* that pushes
	 *            a short
	 */
	public ConstantInstruction(ImmediateShortInstruction isi) {
		super(isi.methodInfo(), isi.opcode());
		this.instruction = isi;
	}

	private int value() {
		if (instruction instanceof ImmediateByteInstruction) {
			return ((ImmediateByteInstruction) instruction).value();
		} else {
			return ((ImmediateShortInstruction) instruction).value();
		}
	}

	/**
	 * Get the constant variable
	 *
	 * @return the constant variable
	 */
	public Object constant() {
		try {
			int value = value();
			ConstantPool pool = classInfo().constantPoolAt(value);
			if (pool.tag() == ConstantPool.CONSTANT_STRING) {
				return pool.verbose();
			} else {
				if (pool instanceof ConstantIntegerInfo) {
					return ((ConstantIntegerInfo) pool).value();
				} else if (pool instanceof ConstantDoubleInfo) {
					return ((ConstantDoubleInfo) pool).value();
				}else if (pool instanceof ConstantFloatInfo) {
					return ((ConstantFloatInfo) pool).value();
				}else {
					return ((ConstantLongInfo) pool).value();
				}
			}
		} catch (Exception e) {
			return null;
		}
	}

	public void setClassInfo(String name, int index) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_CLASS) {
			ConstantClassInfo cci = (ConstantClassInfo) pool;
			cci.setName(name);
			cci.setIndex(index);
		}
	}

	public void setClassInfo(ClassInfo ci) {
		setClassInfo(ci.name(), ci.index());
	}

	public void setFieldrefInfo(int nameAndTypeIndex) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_FIELDREF) {
			ConstantFieldrefInfo cfi = (ConstantFieldrefInfo) pool;
			cfi.setNameAndTypeIndex(nameAndTypeIndex);
		}
	}

	public void setMethodrefInfo(int nameAndTypeIndex) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_METHODREF) {
			ConstantMethodrefInfo cmi = (ConstantMethodrefInfo) pool;
			cmi.setNameAndTypeIndex(nameAndTypeIndex);
		}
	}

	public void setInterfaceMethodrefInfo(int nameAndTypeIndex) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_INTERFACE_METHODREF) {
			ConstantInterfaceMethodrefInfo cimi = (ConstantInterfaceMethodrefInfo) pool;
			cimi.setNameAndTypeIndex(nameAndTypeIndex);
		}
	}

	public void setString(int stringIndex) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_STRING) {
			ConstantStringInfo csi = (ConstantStringInfo) pool;
			csi.setStringIndex(stringIndex);
		}
	}

	public void setInt(int value) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_INTEGER) {
			ConstantIntegerInfo cii = (ConstantIntegerInfo) pool;
			cii.setValue(value);
		}
	}

	public void setFloat(float value) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_FLOAT) {
			ConstantFloatInfo cfi = (ConstantFloatInfo) pool;
			cfi.setValue(value);
		}
	}

	public void setLong(long value) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_LONG) {
			ConstantLongInfo cli = (ConstantLongInfo) pool;
			cli.setValue(value);
		}
	}

	public void setDouble(double value) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_DOUBLE) {
			ConstantDoubleInfo cdi = (ConstantDoubleInfo) pool;
			cdi.setValue(value);
		}
	}

	public void setNameAndType(int nameIndex, int descriptorIndex) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_NAME_AND_TYPE) {
			ConstantNameAndTypeInfo cnati = (ConstantNameAndTypeInfo) pool;
			cnati.setNameIndex(nameIndex);
			cnati.setDescriptorIndex(descriptorIndex);
		}
	}

	public void setMethodType(int descriptorIndex) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_METHOD_TYPE) {
			ConstantMethodTypeInfo cmti = (ConstantMethodTypeInfo) pool;
			cmti.setDescriptorIndex(descriptorIndex);
		}
	}

	public void setMethodHandle(int index, int type) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_METHOD_HANDLE) {
			ConstantMethodHandleInfo cmhi = (ConstantMethodHandleInfo) pool;
			cmhi.setIndex(index);
			cmhi.setType(type);
		}
	}

	public void setInvokeDynamic(int nameAndTypeIndex, int bootstrapMethodAttributeIndex) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_INVOKE_DYNAMIC) {
			ConstantInvokeDynamicInfo cidi = (ConstantInvokeDynamicInfo) pool;
			cidi.setNameAndTypeIndex(nameAndTypeIndex);
			cidi.setBootstrapIndex(bootstrapMethodAttributeIndex);
		}
	}

	public void setUtf(String utf) {
		ConstantPool pool = classInfo().constantPoolAt(value());
		byte tag = pool.tag();
		if (tag == CONSTANT_UTF8) {
			ConstantUtf8Info cui = (ConstantUtf8Info) pool;
			cui.setString(utf);
		}
	}

	public boolean numeric() {
		int value = value();
		ConstantPool pool = classInfo().constantPoolAt(value);
		byte tag = pool.tag();
		return tag == CONSTANT_INTEGER || tag == CONSTANT_FLOAT || tag == CONSTANT_DOUBLE || tag == CONSTANT_LONG;
	}

	@Override
	public void read(BytecodeInput input) throws IOException {
		instruction.read(input);
	}

	@Override
	public void write(BytecodeOutput output) throws IOException {
		instruction.write(output);
	}
}
