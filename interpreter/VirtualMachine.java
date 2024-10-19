package interpreter;

import java.util.List;
import java.util.Stack;

import interpreter.bytecode.ByteCode;
import interpreter.loader.Program;

public class VirtualMachine {
  private int pc;
  protected Stack<Integer> returnAddresses;
  private boolean isRunning;
  protected Program program;
  protected RuntimeStack runStack;

  private String DMPState = "-";

  public VirtualMachine(Program program) {
    this(program, new Stack<>(), new RuntimeStack());
  }

  public VirtualMachine(Program program, Stack<Integer> returnAddresses, RuntimeStack runStack) {
    this.program = program;
    this.returnAddresses = returnAddresses;
    this.runStack = runStack;
  }

  public void setDMPState(String state) {
    this.DMPState = state;
  }

  public RuntimeStack getRuntimeStack() {
    return this.runStack;
  }

  public int getPC() {
    return this.pc;
  }

  public void setPC(int pc) {
    this.pc = pc;
  }

  public boolean getIsRunning() {
    return this.isRunning;
  }

  public void stopRunning() {
    this.returnAddresses.clear();
    this.runStack.clear();
    this.isRunning = false;
  }

  public List<Integer> getArgs() {
    return this.runStack.getCurrentFrame();
  }

  public void pushReturnAddress() {
    this.returnAddresses.push(this.pc);
  }

  public void returnTo () {
    this.pc = this.returnAddresses.pop();
  }

  public void executeProgram() {
    this.pc = 0;
    this.isRunning = true;

    while (this.isRunning) {
      ByteCode code = this.program.getCode(this.pc);
      code.execute(this);

      if(this.DMPState.equals("+") && !code.getCode().equals("DMP")) {
        System.out.println(code.toString());
        System.out.println(this.runStack.toString());
      }

      this.pc++;
    }
  }
}
