package interpreter.loader;

import interpreter.bytecode.ByteCode;

import java.util.ArrayList;
import java.util.List;

public class Program {

  List<ByteCode> byteCodes;

  public Program() {
    this.byteCodes = new ArrayList<>();
  }

  public int getSize() {
    return this.byteCodes.size();
  }

  public void addCode(ByteCode code) {
    this.byteCodes.add(code);
  }

  public ByteCode getCode(int pc) {
    return byteCodes.get(pc);
  }

  public void resolveSymbolicAddresses() {
    for (int i = 0; i < byteCodes.size() - 1; i++) {
      if (byteCodes.get(i) instanceof interpreter.bytecode.CallCode
          || byteCodes.get(i) instanceof interpreter.bytecode.GotoCode
          || byteCodes.get(i) instanceof interpreter.bytecode.FalsebranchCode) {
        String label = "";
        if (byteCodes.get(i) instanceof interpreter.bytecode.CallCode) {
          label = ((interpreter.bytecode.CallCode) byteCodes.get(i)).getLabel();
        } else if (byteCodes.get(i) instanceof interpreter.bytecode.GotoCode) {
          label = ((interpreter.bytecode.GotoCode) byteCodes.get(i)).getLabel();
        } else if (byteCodes.get(i) instanceof interpreter.bytecode.FalsebranchCode) {
          label = ((interpreter.bytecode.FalsebranchCode) byteCodes.get(i)).getLabel();
        }
        boolean found = false;
        int j = 0;
        int result = 0;
        while (!found) {
          ByteCode code = getCode(j);
          
          if (code.getCode().equals("LABEL")) {
            if(((interpreter.bytecode.LabelCode) code).getLabel().equals(label)) {
              found = true;
              result = j;
            }
          }
          j++;
        }
        if (byteCodes.get(i) instanceof interpreter.bytecode.CallCode) {
          ((interpreter.bytecode.CallCode) byteCodes.get(i)).setAddress(result);
        } else if (byteCodes.get(i) instanceof interpreter.bytecode.GotoCode) {
          ((interpreter.bytecode.GotoCode) byteCodes.get(i)).setAddress(result);
        } else if (byteCodes.get(i) instanceof interpreter.bytecode.FalsebranchCode) {
          ((interpreter.bytecode.FalsebranchCode) byteCodes.get(i)).setAddress(result);
        }
      }
    }
  }
}
