package parser;

import java.util.EnumSet;

import ast.AST;
import ast.trees.*;
import lexer.ILexer;
import lexer.Lexception;
import lexer.Lexer;
import lexer.daos.Token;
import lexer.daos.TokenKind;

public class Parser {
  private Token currentToken;
  private ILexer lexer;

  private EnumSet<TokenKind> relationalOperators = EnumSet.of(
      TokenKind.Equal,
      TokenKind.NotEqual,
      TokenKind.Less,
      TokenKind.LessEqual,
      TokenKind.Greater,
      TokenKind.GreaterEqual);
  private EnumSet<TokenKind> additionOperators = EnumSet.of(
      TokenKind.Plus,
      TokenKind.Minus,
      TokenKind.Or,
      TokenKind.BoolOr,
      TokenKind.BoolXor);
  private EnumSet<TokenKind> multiplicationOperators = EnumSet.of(
      TokenKind.Multiply,
      TokenKind.Divide,
      TokenKind.And,
      TokenKind.BoolAnd);

  public Parser(String sourceProgramPath) throws Lexception {
    this(new Lexer(sourceProgramPath));
  }

  public Parser(ILexer lexer) throws Lexception {
    this.lexer = lexer;
    scan();
  }

  private void expect(TokenKind expected) throws SyntaxErrorException, Lexception {
    if (this.currentToken.getTokenKind() == expected) {
      scan();
    } else {
      error(currentToken.getTokenKind(), expected);
    }
  }

  private void error(TokenKind actual, TokenKind... expected) throws SyntaxErrorException {
    throw new SyntaxErrorException(actual, expected);
  }

  private void scan() throws Lexception {
    this.currentToken = lexer.nextToken();
  }

  private boolean match(TokenKind... kinds) {
    for (TokenKind tokenKind : kinds) {
      if (this.currentToken.getTokenKind() == tokenKind) {
        return true;
      }
    }

    return false;
  }

  public AST execute() throws SyntaxErrorException, Lexception {
    return program();
  }

  /**
   * PROGRAM → 'program' BLOCK
   *
   * @throws Lexception
   * @throws SyntaxErrorException
   */
  private AST program() throws SyntaxErrorException, Lexception {
    AST node = new ProgramTree();

    expect(TokenKind.Program);
    node.addChild(block());

    return node;
  }

  /**
   * BLOCK → '{' DECLARATIONS* STATEMENTS* '}'
   *
   * @throws Lexception
   * @throws SyntaxErrorException
   */
  private AST block() throws SyntaxErrorException, Lexception {
    AST node = new BlockTree();

    expect(TokenKind.LeftBrace);

    while (startingDeclaration()) {
      node.addChild(declaration());
    }

    while (startingStatement()) {
      node.addChild(statement());
    }

    expect(TokenKind.RightBrace);

    return node;
  }

  private boolean startingDeclaration() {
    return match(
        TokenKind.IntType,
        TokenKind.BooleanType,
        TokenKind.BinaryType,
        TokenKind.CharType);
  }

  private boolean startingStatement() {
    return match(
        TokenKind.If,
        TokenKind.While,
        TokenKind.Iterate,
        TokenKind.Return,
        TokenKind.LeftBrace,
        TokenKind.Identifier);
  }

  /**
   * DECLARATION → TYPE NAME
   * DECLARATION → TYPE NAME '(' FORMALS ')' BLOCK
   */
  private AST declaration() throws SyntaxErrorException, Lexception {
    AST type = type();
    AST name = name();

    if (match(TokenKind.LeftParen)) {
      AST function = new FunctionDeclarationTree().addChild(type).addChild(name);

      expect(TokenKind.LeftParen);
      function.addChild(formals());
      expect(TokenKind.RightParen);

      return function.addChild(block());
    } else {
      return new DeclarationTree().addChild(type).addChild(name);
    }
  }

  /**
   * TYPE → 'int'
   * TYPE → 'boolean'
   * TYPE → 'binary'
   * TYPE → 'char'
   */
  private AST type() throws Lexception, SyntaxErrorException {
    AST node = null;

    if (match(TokenKind.IntType)) {
      node = new IntTypeTree();
    } else if (match(TokenKind.BooleanType)) {
      node = new BoolTypeTree();
    } else if (match(TokenKind.BinaryType)){
      node = new BinaryTypeTree();
    } else if (match(TokenKind.CharType)){
      node = new CharTypeTree();
    } else {
      error(
          currentToken.getTokenKind(),
          TokenKind.IntType,
          TokenKind.BooleanType,
          TokenKind.BinaryType,
          TokenKind.CharType);
    }

    scan();
    return node;
  }

  /*
   * NAME → <id>
   */
  private AST name() throws Lexception, SyntaxErrorException {
    AST node = null;

    if (match(TokenKind.Identifier)) {
      node = new IdentifierTree(currentToken);

      expect(TokenKind.Identifier);
    }

    return node;
  }

  /**
   * FORMALS → 
   * FORMALS → DECLARATION (',' DECLARATION)*
   */
  private AST formals() throws SyntaxErrorException, Lexception {
    AST formals = new FormalsTree();

    if (match(TokenKind.RightParen)) {
      return formals;
    } else {
      do {
        formals.addChild(declaration());

        if (match(TokenKind.Comma)) {
          expect(TokenKind.Comma);
        }
      } while (!match(TokenKind.RightParen));

      return formals;
    }
  }

  /**
   * STATEMENT → 'if' E 'then' BLOCK 'else' BLOCK
   * STATEMENT → 'while' E BLOCK
   * STATEMENT → 'iter' '|-' <int> '~' <int> BLOCK
   * STATEMENT → 'return' E
   * STATEMENT → BLOCK
   * STATEMENT → NAME '=' E
   */
  private AST statement() throws SyntaxErrorException, Lexception {

    switch (currentToken.getTokenKind()) {
      case If:
        return ifStatement();
      case While:
        return whileStatement();
      case Iterate:
        return iterationStatement();
      case Return:
        return returnStatement();
      case LeftBrace:
        return block();
      case Identifier:
        return assignStatement();
      default:
        error(
            currentToken.getTokenKind(),
            TokenKind.If,
            TokenKind.While,
            TokenKind.Return,
            TokenKind.LeftBrace,
            TokenKind.Identifier);
        return null;
    }
  }

  /**
   * STATEMENT → 'if' E 'then' BLOCK 
   *             if "else" -> 'else' BLOCK
   */
  private AST ifStatement() throws SyntaxErrorException, Lexception {
    AST node = new IfTree();

    expect(TokenKind.If);
    node.addChild(expression());
    expect(TokenKind.Then);
    node.addChild(block());
    if (match(TokenKind.Else)) {
      expect(TokenKind.Else);
      node.addChild(block());
    }

    return node;
  }

  /**
   * STATEMENT → 'while' E BLOCK
   */
  private AST whileStatement() throws SyntaxErrorException, Lexception {
    AST node = new WhileTree();

    expect(TokenKind.While);
    node.addChild(expression()).addChild(block());

    return node;
  }

    /*
   * STATEMENT → 'iter' '|-' <int> '~' <int> BLOCK
   */
  private AST iterationStatement() throws SyntaxErrorException, Lexception {
    AST node = new IterationTree();

    expect(TokenKind.Iterate);
    node.addChild(range());
    node.addChild(block());

    return node;
  }

  private AST range() throws SyntaxErrorException, Lexception {
    AST node = new RangeTree();

    expect(TokenKind.Pipette);
    node.addChild(new IntTree(currentToken));
    expect(TokenKind.IntLit);
    expect(TokenKind.Tilde);
    node.addChild(new IntTree(currentToken));
    expect(TokenKind.IntLit);

    return node;
  }

  /**
   * STATEMENT → 'return' E
   */
  private AST returnStatement() throws SyntaxErrorException, Lexception {
    AST node = new ReturnTree();

    expect(TokenKind.Return);

    node.addChild(expression());
    return node;
  }

  /**
   * STATEMENT → NAME '=' E
   */
  private AST assignStatement() throws SyntaxErrorException, Lexception {
    AST node = new AssignmentTree();

    node.addChild(name());
    expect(TokenKind.Assign);
    node.addChild(expression());

    return node;
  }

  /**
   * E → SE
   * E → SE '==' SE
   * E → SE '!=' SE
   * E → SE '<' SE
   * E → SE '<=' SE
   */
  private AST expression() throws Lexception, SyntaxErrorException {
    AST tree, child = simpleExpression();

    tree = getRelopTree();
    if (tree == null) {
      return child;
    }

    tree.addChild(child);
    tree.addChild(simpleExpression());

    return tree;
  }

  private AST getRelopTree() throws Lexception {
    if (relationalOperators.contains(currentToken.getTokenKind())) {
      AST tree = new RelOpTree(currentToken);
      scan();

      return tree;
    } else {
      return null;
    }
  }

  /**
   * SE → T
   * SE → SE '+' T
   * SE → SE '-' T
   * SE → SE '|' T
   */
  private AST simpleExpression() throws Lexception, SyntaxErrorException {
    AST tree, child = term();

    while ((tree = getAddOpTree()) != null) {
      tree.addChild(child);
      tree.addChild(term());

      child = tree;
    }

    return child;
  }

  private AST getAddOpTree() throws Lexception {
    if (additionOperators.contains(currentToken.getTokenKind())) {
      AST tree = new AddOpTree(currentToken);
      scan();

      return tree;
    } else {
      return null;
    }
  }

  /**
   * T → F
   * T → T '*' F
   * T → T '/' F
   * T → T '&' F
   */
  private AST term() throws SyntaxErrorException, Lexception {
    AST tree, child = factor();

    while ((tree = getMultOpTree()) != null) {
      tree.addChild(child);
      tree.addChild(factor());

      child = tree;
    }

    return child;
  }

  private AST getMultOpTree() throws Lexception {
    if (multiplicationOperators.contains(currentToken.getTokenKind())) {
      AST tree = new MultOpTree(currentToken);
      scan();

      return tree;
    } else {
      return null;
    }
  }

  /**
   * F → '(' E ')'
   * F → <int>
   * F → <binary>
   * F → <char>
   * F → NAME
   * F → NAME '(' E_LIST ')'
   */
  private AST factor() throws SyntaxErrorException, Lexception {
    switch (currentToken.getTokenKind()) {
      case LeftParen: {
        expect(TokenKind.LeftParen);
        AST node = expression();
        expect(TokenKind.RightParen);

        return node;
      }
      case IntLit: {
        AST node = new IntTree(currentToken);
        expect(TokenKind.IntLit);

        return node;
      }
      case BinaryLit: {
        AST node = new BinaryLitTree(currentToken);
        expect(TokenKind.BinaryLit);

        return node;
      }
      case CharLit: {
        AST node = new CharLitTree(currentToken);
        expect(TokenKind.CharLit);

        return node;
      }
      case Identifier: {
        AST node = new IdentifierTree(currentToken);
        expect(TokenKind.Identifier);

        if (match(TokenKind.LeftParen)) {
          node = new CallTree().addChild(node);
          expect(TokenKind.LeftParen);
          node.addChild(actualArguments());
          expect(TokenKind.RightParen);
        }

        return node;
      }
      default:
        error(
            currentToken.getTokenKind(),
            TokenKind.LeftParen);
        return null;
    }
  }

  /**
   * ACTUAL_ARGUMENTS → 
   * ACTUAL_ARGUMENTS → E (',' E)*
   */
  private AST actualArguments() throws SyntaxErrorException, Lexception {
    AST node = new ActualArgumentsTree();

    while (!match(TokenKind.RightParen)) {
      node.addChild(expression());

      if (match(TokenKind.Comma)) {
        expect(TokenKind.Comma);
      }
    }

    return node;
  }
}
