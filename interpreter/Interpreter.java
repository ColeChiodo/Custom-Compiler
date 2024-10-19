package interpreter;

import java.io.IOException;

import interpreter.loader.ByteCodeLoaderException;
import interpreter.loader.ByteCodeLoader;
import interpreter.loader.Program;

public class Interpreter {
  private String byteCodeFilePath;

  public Interpreter(String codeFilePath) {
    this.byteCodeFilePath = codeFilePath;
  }

  public void run() {
    try {
      ByteCodeLoader loader = new ByteCodeLoader(this.byteCodeFilePath);
      Program program = loader.loadCodes();

      (new VirtualMachine(program)).executeProgram();
    } catch (IOException e) {
      System.err.println(String.format("Failed to read from byte code file %s", this.byteCodeFilePath));
      System.exit(1);
    } catch (ByteCodeLoaderException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("usage: java interpreter.Interpreter <codefilepath>");
      System.exit(1);
    }

    (new Interpreter(args[0])).run();
  }
}