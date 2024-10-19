package interpreter.loader;

import java.util.Map;

import java.util.HashMap;

public class CodeTable {
  private static Map<String, String> codes;

  static {
    codes = new HashMap<>();
    codes.put("ARGS", "interpreter.bytecode.ArgsCode");
    codes.put("BOP", "interpreter.bytecode.BopCode");
    codes.put("CALL", "interpreter.bytecode.CallCode");
    codes.put("DMP", "interpreter.bytecode.DmpCode");
    codes.put("FALSEBRANCH", "interpreter.bytecode.FalsebranchCode");
    codes.put("GOTO", "interpreter.bytecode.GotoCode");
    codes.put("HALT", "interpreter.bytecode.HaltCode");
    codes.put("LABEL", "interpreter.bytecode.LabelCode");
    codes.put("LIT", "interpreter.bytecode.LitCode");
    codes.put("LOAD", "interpreter.bytecode.LoadCode");
    codes.put("POP", "interpreter.bytecode.PopCode");
    codes.put("READ", "interpreter.bytecode.ReadCode");
    codes.put("RETURN", "interpreter.bytecode.ReturnCode");
    codes.put("STORE", "interpreter.bytecode.StoreCode");
    codes.put("WRITE", "interpreter.bytecode.WriteCode");
  }

  public static String get(String byteCode) {
    return codes.get(byteCode);
  }
}
