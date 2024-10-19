package interpreter.loader;

public class ByteCodeLoaderException extends Exception {

  private String byteCodeFileLine;

  public ByteCodeLoaderException(String byteCodeFileLine) {
    this.byteCodeFileLine = byteCodeFileLine;
  }

  @Override
  public String getMessage() {
    return String.format("Error loading byte code [%s]", this.byteCodeFileLine);
  }
}
