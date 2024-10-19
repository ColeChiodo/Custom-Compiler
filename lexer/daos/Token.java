package lexer.daos;

public class Token {
  private int leftPosition, rightPosition, lineNumber;
  private Symbol symbol;

  //legacy constructor
  public Token(Symbol symbol, int leftPosition, int rightPosition) {
    this.symbol = symbol;
    this.leftPosition = leftPosition;
    this.rightPosition = rightPosition;
  }

  //new constructor
  public Token(Symbol symbol, int leftPosition, int rightPosition, int lineNumber) {
    this.symbol = symbol;
    this.leftPosition = leftPosition;
    this.rightPosition = rightPosition;
    this.lineNumber = lineNumber;
  }

  public int getRightPosition() {
    return rightPosition;
  }

  public int getLeftPosition() {
    return leftPosition;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public String getLexeme() {
    return this.symbol.getLexeme();
  }

  public TokenKind getTokenKind() {
    return this.symbol.getTokenKind();
  }

  /**
   * This is not the correct way to create a String representation of an object!
   * 
   * @return String
   */
  public String testPrint() {
    return String.format(
        "L: %d, R: %d, %s", 
        this.getLeftPosition(), this.getRightPosition(), this.getLexeme());
  }

  public String Print() {
    return String.format(
        "%-20s left: %-8d right: %-8d line: %-8d %s", 
        this.getLexeme(), this.getLeftPosition(), this.getRightPosition(), this.getLineNumber(), this.getTokenKind());
  }
}
