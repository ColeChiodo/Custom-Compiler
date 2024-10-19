package compiler;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ast.trees.ProgramTree;
import lexer.ILexer;
import lexer.Lexception;
import lexer.Lexer;
import parser.Parser;
import parser.SyntaxErrorException;
import visitor.PrintVisitor;
import visitor.images.DrawOffsetVisitor;
import visitor.images.OffsetVisitor;

public class Compiler {

  private String sourceFile;

  public Compiler(String sourceFile) {
    this.sourceFile = sourceFile;
  }

  public void compileProgram() {
    try {
      ILexer lexer = new Lexer(sourceFile);
      Parser parser = new Parser(lexer);

      ProgramTree ast = (ProgramTree) parser.execute();

      System.out.println(lexer);
      System.out.println();

      PrintVisitor printVisitor = new PrintVisitor();
      printVisitor.visit(ast);

    } catch (Lexception e) {
      e.printStackTrace();
    } catch (SyntaxErrorException e) {
      e.printStackTrace();
    }
  }

  public void generateImage() {
    try {
      ILexer lexer = new Lexer(sourceFile);
      Parser parser = new Parser(lexer);

      ProgramTree ast = (ProgramTree) parser.execute();

      PrintVisitor printVisitor = new PrintVisitor();
      printVisitor.visit(ast);

      OffsetVisitor offsetVisitor = new OffsetVisitor();
      offsetVisitor.visit(ast);

      DrawOffsetVisitor drawVisitor = new DrawOffsetVisitor(
          offsetVisitor.getOffsets(),
          offsetVisitor.getUnitWidth(),
          offsetVisitor.getUnitHeight());
      drawVisitor.visit(ast);

      File imageFile = new File(sourceFile.replace(".x", ".png"));
      ImageIO.write(drawVisitor.getImage(), "png", imageFile);
    } catch (Lexception e) {
      e.printStackTrace();
    } catch (SyntaxErrorException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    if (args.length == 0 || args.length > 2) {
      System.err.println("usage: java compiler.Compiler <file> [-image]");
      System.exit(1);
    }

    Compiler compiler = new Compiler(args[0]);

    if (args.length == 1) {
      compiler.compileProgram();
    } else if (args[1].equals("-image")) {
      compiler.generateImage();
    } else {
      System.err.println(String.format("Unrecognized flag %s", args[1]));
      System.exit(1);
    }
  }
}
