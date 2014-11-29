/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */

package me.sedlar.bytecode.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import me.sedlar.bytecode.*;
import me.sedlar.bytecode.structure.MethodInfo;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class BytecodeReader {

	/**
	 * Converts the code to a list of instructions.
	 *
	 * @param code
	 *            the code as an array of bits from which to read the
	 *            instructions
	 * @return the <tt>java.util.List</tt> with the instructions
	 * @throws IOException
	 *             if an exception occurs with the code
	 */
	public static ArrayList<AbstractInstruction> readBytecode(MethodInfo methodInfo, byte[] code)
			throws IOException {
		BytecodeInputStream bcis = new BytecodeInputStream(new ByteArrayInputStream(code));
		ArrayList<AbstractInstruction> instructions = new ArrayList<>();
		boolean wide = false;
		AbstractInstruction currentInstruction = null;
		AbstractInstruction previous;
		while (bcis.count() < code.length) {
			previous = currentInstruction;
			currentInstruction = readNextInstruction(methodInfo, bcis, wide);
			wide = (currentInstruction.opcode() == Opcode.WIDE);
			currentInstruction.setPrevious(previous);
			instructions.add(currentInstruction);
		}
		for (int i = 0; i + 1 < instructions.size(); i++)
			instructions.get(i).setNext(instructions.get(i + 1));
		return instructions;
	}

	private static AbstractInstruction readNextInstruction(MethodInfo methodInfo, BytecodeInputStream bcis,
			boolean wide) throws IOException {
		AbstractInstruction instruction;
		int bytecode = bcis.readUnsignedByte();
		Opcode opcode = Opcode.fromBytecode(bytecode);
		if (opcode == null)
			throw new IOException("invalid opcode 0x" + Integer.toHexString(bytecode));
		switch (opcode) {
			case ICONST_M1:
			case ICONST_0:
			case ICONST_1:
			case ICONST_2:
			case ICONST_3:
			case ICONST_4:
			case ICONST_5:
			case LCONST_0:
			case LCONST_1:
			case FCONST_0:
			case FCONST_1:
			case FCONST_2:
			case DCONST_0:
			case DCONST_1:
			case ILOAD_0:
			case ILOAD_1:
			case ILOAD_2:
			case ILOAD_3:
			case LLOAD_0:
			case LLOAD_1:
			case LLOAD_2:
			case LLOAD_3:
			case FLOAD_0:
			case FLOAD_1:
			case FLOAD_2:
			case FLOAD_3:
			case DLOAD_0:
			case DLOAD_1:
			case DLOAD_2:
			case DLOAD_3:
			case ALOAD_0:
			case ALOAD_1:
			case ALOAD_2:
			case ALOAD_3:
			case IALOAD:
			case LALOAD:
			case FALOAD:
			case DALOAD:
			case AALOAD:
			case BALOAD:
			case CALOAD:
			case SALOAD:
			case ISTORE_0:
			case ISTORE_1:
			case ISTORE_2:
			case ISTORE_3:
			case LSTORE_0:
			case LSTORE_1:
			case LSTORE_2:
			case LSTORE_3:
			case FSTORE_0:
			case FSTORE_1:
			case FSTORE_2:
			case FSTORE_3:
			case DSTORE_0:
			case DSTORE_1:
			case DSTORE_2:
			case DSTORE_3:
			case ASTORE_0:
			case ASTORE_1:
			case ASTORE_2:
			case ASTORE_3:
			case IASTORE:
			case LASTORE:
			case FASTORE:
			case DASTORE:
			case AASTORE:
			case BASTORE:
			case CASTORE:
			case SASTORE: {
				instruction = new SimpleInstruction(methodInfo, opcode);
				instruction = new VariableInstruction((SimpleInstruction) instruction);
				break;
			}
			case WIDE:
			case NOP:
			case ACONST_NULL:
			case POP:
			case POP2:
			case DUP:
			case DUP_X1:
			case DUP_X2:
			case DUP2:
			case DUP2_X1:
			case DUP2_X2:
			case SWAP:
			case IADD:
			case LADD:
			case FADD:
			case DADD:
			case ISUB:
			case LSUB:
			case FSUB:
			case DSUB:
			case IMUL:
			case LMUL:
			case FMUL:
			case DMUL:
			case IDIV:
			case LDIV:
			case FDIV:
			case DDIV:
			case IREM:
			case LREM:
			case FREM:
			case DREM:
			case INEG:
			case LNEG:
			case FNEG:
			case DNEG:
			case ISHL:
			case LSHL:
			case ISHR:
			case LSHR:
			case IUSHR:
			case LUSHR:
			case IAND:
			case LAND:
			case IOR:
			case LOR:
			case IXOR:
			case LXOR:
			case I2L:
			case I2F:
			case I2D:
			case L2I:
			case L2F:
			case L2D:
			case F2I:
			case F2L:
			case F2D:
			case D2I:
			case D2L:
			case D2F:
			case I2B:
			case I2C:
			case I2S:
			case LCMP:
			case FCMPL:
			case FCMPG:
			case DCMPL:
			case DCMPG:
			case IRETURN:
			case LRETURN:
			case FRETURN:
			case DRETURN:
			case ARETURN:
			case RETURN:
			case ARRAYLENGTH:
			case ATHROW:
			case MONITORENTER:
			case MONITOREXIT:
			case BREAKPOINT:
			case IMPDEP1:
			case IMPDEP2: {
				instruction = new SimpleInstruction(methodInfo, opcode);
				break;
			}
			case LDC: {
				instruction = new ImmediateByteInstruction(methodInfo, opcode, wide);
				instruction = new ConstantInstruction((ImmediateByteInstruction) instruction);
				break;
			}
			case BIPUSH: {
				instruction = new ImmediateByteInstruction(methodInfo, opcode, wide);
				instruction = new PushInstruction((ImmediateByteInstruction) instruction);
				break;
			}
			case ILOAD: // subject to wide
			case LLOAD: // subject to wide
			case FLOAD: // subject to wide
			case DLOAD: // subject to wide
			case ALOAD: // subject to wide
			case ISTORE: // subject to wide
			case LSTORE: // subject to wide
			case FSTORE: // subject to wide
			case DSTORE: // subject to wide
			case ASTORE: {// subject to wide
				instruction = new ImmediateByteInstruction(methodInfo, opcode, wide);
				instruction = new VariableInstruction((ImmediateByteInstruction) instruction);
				break;
			}
			case RET: // subject to wide
			case NEWARRAY: {
				instruction = new ImmediateByteInstruction(methodInfo, opcode, wide);
				break;
			}
			case INVOKEVIRTUAL:
			case INVOKESPECIAL:
			case INVOKESTATIC: {
				instruction = new MethodInstruction(methodInfo, opcode);
				break;
			}
			case LDC_W:
			case LDC2_W: {
				instruction = new ImmediateShortInstruction(methodInfo, opcode);
				instruction = new ConstantInstruction((ImmediateShortInstruction) instruction);
				break;
			}
			case GETSTATIC:
			case PUTSTATIC:
			case GETFIELD:
			case PUTFIELD: {
				instruction = new FieldInstruction(methodInfo, opcode);
				break;
			}
			case SIPUSH: {
				instruction = new ImmediateShortInstruction(methodInfo, opcode);
				instruction = new PushInstruction((ImmediateShortInstruction) instruction);
				break;
			}
			case NEW:
			case ANEWARRAY:
			case CHECKCAST:
			case INSTANCEOF: {
				instruction = new TypeInstruction(methodInfo, opcode);
				break;
			}
			case IFEQ:
			case IFNE:
			case IFLT:
			case IFGE:
			case IFGT:
			case IFLE:
			case IF_ICMPEQ:
			case IF_ICMPNE:
			case IF_ICMPLT:
			case IF_ICMPGE:
			case IF_ICMPGT:
			case IF_ICMPLE:
			case IF_ACMPEQ:
			case IF_ACMPNE:
			case GOTO:
			case JSR:
			case IFNULL:
			case IFNONNULL: {
				instruction = new BranchInstruction(methodInfo, opcode);
				break;
			}
			case GOTO_W:
			case JSR_W: {
				instruction = new WideBranchInstruction(methodInfo, opcode);
				break;
			}
			case IINC: {// subject to wide
				instruction = new IncrementInstruction(methodInfo, opcode, wide);
				break;
			}
			case TABLESWITCH: {
				instruction = new TableSwitchInstruction(methodInfo, opcode);
				break;

			}
			case LOOKUPSWITCH: {
				instruction = new LookupSwitchInstruction(methodInfo, opcode);
				break;
			}
			case INVOKEINTERFACE: {
				instruction = new InvokeInterfaceInstruction(methodInfo, opcode);
				break;
			}
			case INVOKEDYNAMIC: {
				instruction = new InvokeDynamicInstruction(methodInfo, opcode);
				break;
			}
			case MULTIANEWARRAY: {
				instruction = new MultianewarrayInstruction(methodInfo, opcode);
				break;
			}
			default: {
				throw new IOException("unhandled opcode " + opcode);
			}
		}
		instruction.read(bcis);
		return instruction;
	}
}
