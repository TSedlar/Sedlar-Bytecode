package me.sedlar.bytecode;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @since Sep 12, 2014 - 3:17 PM
 */
public interface InstructionVisitor {

	public void visitBranch(BranchInstruction bi);
	public void visitConstant(ConstantInstruction ci);
	public void visitField(FieldInstruction fi);
	public void visitPush(PushInstruction pi);
	public void visitIncrement(IncrementInstruction ii);
	public void visitInvokeDynamic(InvokeDynamicInstruction idi);
	public void visitInvokeInterface(InvokeInterfaceInstruction ifi);
	public void visitLookupSwitch(LookupSwitchInstruction lsi);
	public void visitMethod(MethodInstruction mi);
	public void visitMultianewarray(MultianewarrayInstruction mi);
	public void visitTableSwitch(TableSwitchInstruction tsi);
	public void visitType(TypeInstruction ti);
	public void visitVariable(VariableInstruction vi);
	public void visitWideBranch(WideBranchInstruction wbi);
	public void visitInstruction(AbstractInstruction ai);
	public void visitAny(AbstractInstruction ai);
}
