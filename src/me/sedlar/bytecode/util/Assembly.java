/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.util;

import me.sedlar.bytecode.*;
import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.FieldInfo;
import me.sedlar.bytecode.structure.MethodInfo;

import java.util.Collection;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class Assembly {

	/**
	 * Gets the argument and return sizes for the given descriptor.
	 *
	 * @param desc
	 *            The descriptor to get the argument and return sizes for.
	 * @return the argument and return sizes for the given descriptor.
	 */
	public static int getArgumentsAndReturnSizes(String desc) {
		int n = 1;
		int c = 1;
		while (c < desc.length()) {
			char at = desc.charAt(c++);
			if (at == ')') {
				at = desc.charAt(c);
				return n << 2 | (at == 'V' ? 0 : (at == 'D' || at == 'J' ? 2 : 1));
			} else if (at == 'L') {
				while (desc.charAt(c) != ';')
					c++;
				n += 1;
			} else if (at == '[') {
				while ((at = desc.charAt(c)) == '[')
					++c;
				if (at == 'D' || at == 'J')
					n -= 1;
			} else if (at == 'D' || at == 'J') {
				n += 2;
			} else {
				n += 1;
			}
		}
		return 0;
	}

	/**
	 * Gets the display string for the given instruction.
	 *
	 * @param insn
	 *            The instruction to get the display string for.
	 * @return the display string for the given instruction.
	 */
	public static String toString(AbstractInstruction insn) {
		if (insn == null)
			return "null";
		Opcode op = insn.opcode();
		if (op == null)
			return insn.toString();
		StringBuilder sb = new StringBuilder();
		sb.append(op.verbose());
		if (insn instanceof PushInstruction) {
			sb.append(' ');
			sb.append(((PushInstruction) insn).value());
		} else if (insn instanceof VariableInstruction) {
			sb.append(" #");
			sb.append(((VariableInstruction) insn).variable());
		} else if (insn instanceof TypeInstruction) {
			sb.append(' ');
			sb.append(((TypeInstruction) insn).type());
		} else if (insn instanceof FieldInstruction) {
			FieldInstruction field = (FieldInstruction) insn;
			sb.append(' ');
			sb.append(field.owner());
			sb.append('.');
			sb.append(field.name());
			sb.append(' ');
			sb.append(field.descriptor());
		} else if (insn instanceof MethodInstruction) {
			MethodInstruction method = (MethodInstruction) insn;
			sb.append(' ');
			sb.append(method.owner());
			sb.append('.');
			sb.append(method.name());
			sb.append(' ');
			sb.append(method.descriptor());
		} else if (insn instanceof ConstantInstruction) {
			ConstantInstruction constant = (ConstantInstruction) insn;
			Object value = constant.constant();
			sb.append(' ');
			sb.append(value != null ? value.getClass().getName() : "null");
			sb.append(' ');
			sb.append(value);
		} else if (insn instanceof IncrementInstruction) {
			IncrementInstruction increment = (IncrementInstruction) insn;
			sb.append(" #");
			sb.append(increment.value());
			sb.append(' ');
			sb.append(increment.increment());
		} else if (insn instanceof MultianewarrayInstruction) {
			MultianewarrayInstruction mi = (MultianewarrayInstruction) insn;
			sb.append(' ');
			sb.append(mi.descriptor());
			sb.append(' ');
			sb.append(mi.dimensions());
		} else if (insn instanceof BranchInstruction) {
			BranchInstruction bi = (BranchInstruction) insn;
			sb.append(' ');
			sb.append(bi.totalOffset());
		}
		return sb.toString();
	}

	public static String key(AbstractInstruction instruction) {
		if (instruction instanceof FieldInstruction) {
			FieldInstruction fi = (FieldInstruction) instruction;
			return fi.owner() + "." + fi.name();
		} else if (instruction instanceof MethodInstruction) {
			MethodInstruction mi = (MethodInstruction) instruction;
			return mi.owner() + "." + mi.name() + mi.descriptor();
		} else {
			return toString(instruction);
		}
	}

	/**
	 * Checks the equality of the two numbers with the given string.
	 * 
	 * @param number
	 *            the number to check against
	 * @param check
	 *            the number to check
	 * @param comparison
	 *            ifeq ==, ifne !=, ifgt >, iflt <, ifge >=, ifle <=
	 * @return the equality of the two numbers with the given string.
	 */
	public static boolean equalCheck(int number, int check, String comparison) {
		switch (comparison) {
			case "ifeq": {
				return number == check;
			}
			case "ifne": {
				return number != check;
			}
			case "ifgt": {
				return number > check;
			}
			case "iflt": {
				return number < check;
			}
			case "ifge": {
				return number >= check;
			}
			case "ifle": {
				return number <= check;
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * Rename all instances of the FieldInfo to the given name.
	 *
	 * @param classes the classes to query.
	 * @param fi the FieldInfo to rename.
	 * @param newName the name to rename it to.
	 */
	public static void rename(Collection<ClassInfo> classes, FieldInfo fi, String newName) {
		for (ClassInfo ci : classes) {
			for (MethodInfo mn : ci.methods()) {
				mn.instructions().stream().filter(ai -> ai instanceof FieldInstruction).forEach(ai -> {
					FieldInstruction field = (FieldInstruction) ai;
					if (field.owner().equals(fi.classInfo().name()) && field.name().equals(fi.name()))
						field.setName(newName);
				});
			}
		}
		fi.setName(newName);
	}

	/**
	 * Rename all instances of the ClassInfo to the given name.
	 *
	 * @param classes the classes to query.
	 * @param ci the ClassInfo to rename.
	 * @param newName the name to rename it to.
	 */
	public static void rename(Collection<ClassInfo> classes, ClassInfo ci, String newName) {
		for (ClassInfo classInfo : classes) {
			if (classInfo.superName().equals(ci.name()))
				classInfo.setSuperName(newName);
			if (classInfo.interfaces().contains(ci.name())) {
				classInfo.interfaces().remove(ci.name());
				classInfo.interfaces().add(newName);
			}
			classInfo.fields().stream().filter(fn -> fn.descriptor().endsWith("L" + ci.name() + ";")).forEach(fn ->
					fn.setDescriptor(fn.descriptor().replace("L" + ci.name() + ";", "L" + newName + ";")));
			for (MethodInfo mn : classInfo.methods()) {
				if (mn.descriptor().contains("L" + ci.name() + ";"))
					mn.setDescriptor(mn.descriptor().replaceAll("L" + ci.name() + ";", "L" + newName + ";"));
				for (AbstractInstruction ain : mn.instructions()) {
					if (ain instanceof FieldInstruction) {
						FieldInstruction field = (FieldInstruction) ain;
						if (field.owner().equals(ci.name()))
							field.setOwner(newName);
					} else if (ain instanceof MethodInstruction) {
						MethodInstruction method = (MethodInstruction) ain;
						if (method.owner().equals(ci.name()))
							method.setOwner(newName);
					}
				}
			}
		}
		ci.setName(newName);
	}
}
