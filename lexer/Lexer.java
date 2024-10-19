package lexer;

import java.nio.file.Paths;

import lexer.daos.Symbol;
import lexer.daos.Token;
import lexer.daos.TokenKind;
import lexer.readers.IReader;
import lexer.readers.SourceFileReader;

public class Lexer implements ILexer, AutoCloseable {
  private IReader reader;

  private char ch;
  private int startPosition, endPosition;

  public Lexer(IReader reader) {
    this.reader = reader;
    this.ch = ' ';
  }

  public Lexer(String sourceFilePath) {
    this(new SourceFileReader(sourceFilePath));
  }

  @Override
  public void close() throws Exception {
    this.reader.close();
  }

  @Override
  public Token nextToken() throws Lexception {
    ignoreWhitespace();

    beginNewToken();

    if (Character.isJavaIdentifierStart(this.ch)) {
      return identifierOrKeyword();
    }

    if (Character.isDigit(this.ch)) {
      return digit();
    }

    if (this.ch == '\'') {  //if ch is single quote, check for character literal
      return character();
    }

    return operatorOrSeparator();
  }

  private void ignoreWhitespace() {
    while (Character.isWhitespace(this.ch)) {
      advance();
    }
  }

  private void beginNewToken() {
    this.startPosition = this.reader.getColumn();
    this.endPosition = this.startPosition;
  }

  private Token identifierOrKeyword() {
    String lexeme = "";

    do {
      lexeme += this.ch;
      advance();
    } while (Character.isJavaIdentifierPart(this.ch) && !atEof());

    return new Token(
        SymbolTable.recordSymbol(lexeme, TokenKind.Identifier),
        this.startPosition,
        this.endPosition - 1,
        this.reader.getLineNumber());
  }

  private Token character() throws Lexception {
    String lexeme = "";


    lexeme += this.ch;
    advance();


    if (this.ch == ' ' || this.ch == '\n' || this.ch == '\t') {
      throw new Lexception(
          this.ch + "",
          this.reader.getLineNumber(),
          this.reader.getColumn());
    }


    lexeme += this.ch;
    advance();


    if (this.ch != '\'') {
      throw new Lexception(
          this.ch + "",
          this.reader.getLineNumber(),
          this.reader.getColumn());
    }

    lexeme += this.ch;
    advance();

    return new Token(
        SymbolTable.recordSymbol(lexeme, TokenKind.CharLit),
        this.startPosition,
        this.endPosition - 1,
        this.reader.getLineNumber());
  }

  private Token digit() throws Lexception{
    String lexeme = "";
    boolean isBinary = true;

    do {
      if(this.ch != '0' && this.ch != '1') { 
        isBinary = false;
      }
      lexeme += this.ch;
      advance();
    } while (Character.isDigit(this.ch) && !atEof());
    

    if ((this.ch == 'b' || this.ch == 'B') && isBinary) {
      lexeme += this.ch;
      advance();
      return new Token(
          SymbolTable.recordSymbol(lexeme, TokenKind.BinaryLit),
          this.startPosition,
          this.endPosition - 1,
          this.reader.getLineNumber());
    } 
    else { 
      return new Token(
          SymbolTable.recordSymbol(lexeme, TokenKind.IntLit),
          this.startPosition,
          this.endPosition - 1,
          this.reader.getLineNumber());
    }
  }

  private Token operatorOrSeparator() throws Lexception {
    String singleCharacter = "" + this.ch;

    if (atEof()) {
      return new Token(
          SymbolTable.recordSymbol(singleCharacter, TokenKind.EOF),
          this.startPosition,
          this.endPosition,
          this.reader.getLineNumber());
    }

    advance();

    String doubleCharacter = singleCharacter + this.ch;
    Symbol symbol = SymbolTable.recordSymbol(doubleCharacter, TokenKind.BogusToken);

    if (symbol == null) {
      return singleCharacterOperatorOrSeparator(singleCharacter);
    } else if (symbol.getTokenKind() == TokenKind.Comment) {
      ignoreComment();
      return nextToken();
    } else {
      advance();

      return new Token(
          symbol,
          this.startPosition,
          this.endPosition - 1,
          this.reader.getLineNumber());
    }
  }

  private Token singleCharacterOperatorOrSeparator(String lexeme) throws Lexception {
    Symbol symbol = SymbolTable.recordSymbol(lexeme, TokenKind.BogusToken);

    if (symbol == null) {
      throw new Lexception(lexeme, this.reader.getLineNumber(), this.reader.getColumn());
    } else {
      return new Token(
          symbol,
          this.startPosition,
          this.endPosition - 1,
          this.reader.getLineNumber());
    }
  }

  private void ignoreComment() {
    int currentLine = this.reader.getLineNumber();

    while (currentLine == this.reader.getLineNumber() && !atEof()) {
      advance();
    }
  }

  private void advance() {
    this.ch = this.reader.read();
    this.endPosition++;
  }

  private boolean atEof() {
    return this.ch == '\0';
  }

  @Override
  public String toString() {
    return this.reader.toString();
  }


  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Usage: java lexer.Lexer filename.x");
      System.exit(1);
    }

    String sourceFilePath = Paths.get(args[0]).toString();

    try (Lexer lexer = new Lexer(sourceFilePath)) {
      Token token;

      while ((token = lexer.nextToken()).getTokenKind() != TokenKind.EOF) {
        System.out.println(token.Print());
      }

    } catch (Lexception lexception) {
      System.err.println(lexception.getMessage());
      System.exit(1);
    } catch (Exception exception) {
      System.err.println("Failed to close the Lexer");
      System.exit(1);
    }

    System.out.println();

    try (IReader reader = new SourceFileReader(sourceFilePath)) {
      int lineNumber = 1;
      char ch;

      System.out.printf("%3d: ", lineNumber++);

      while ((ch = reader.read()) != '\0') {
        System.out.print(ch);

        if (ch == '\n') {
          System.out.printf("%3d: ", lineNumber++);
        }
      }
      System.out.println();
    } 
    catch (Exception exception) {
      System.err.println("Failed to close the Reader");
      System.exit(1);
    }
  }
}
