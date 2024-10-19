package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public abstract class ByteCode {
  private String code;

  public ByteCode(List<String> byteCodeLine) {
    this.code = byteCodeLine.get(0);
  }

  public String getCode() {
    return this.code;
  }

  @Override
  public String toString() {
    return this.code;
  }

  public abstract void execute(VirtualMachine vm);
}
