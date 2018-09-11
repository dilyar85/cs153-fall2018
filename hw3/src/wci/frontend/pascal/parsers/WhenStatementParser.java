package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.frontend.*;
import wci.frontend.pascal.*;
import wci.intermediate.*;
import wci.intermediate.icodeimpl.*;

import static wci.frontend.pascal.PascalTokenType.*;
import static wci.frontend.pascal.PascalErrorCode.*;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;

/**
 *
 *
 * <h1>IfStatementParser</h1>
 *
 * <p>Parse a Pascal IF statement.
 *
 * <p>Copyright (c) 2009 by Ronald Mak
 *
 * <p>For instructional purposes only. No warranties.
 */
public class WhenStatementParser extends StatementParser {
  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public WhenStatementParser(PascalParserTD parent) {
    super(parent);
  }

  // Synchronization set for THEN.
  private static final EnumSet<PascalTokenType> THEN_SET = StatementParser.STMT_START_SET.clone();

  static {
    THEN_SET.add(THEN);
    THEN_SET.addAll(StatementParser.STMT_FOLLOW_SET);
  }

  /**
   * Parse an IF statement.
   *
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurred.
   */
  public ICodeNode parse(Token token) throws Exception {
    token = nextToken(); // consume the WHEN

    // Create an IF node.
    ICodeNode ifNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.IF);

    // Parse the expression.
    // The IF node adopts the expression subtree as its first child.
    ExpressionParser expressionParser = new ExpressionParser(this);
    StatementParser statementParser = new StatementParser(this);

    do {

      ifNode.addChild(expressionParser.parse(token));

      // Synchronize at the THEN.
      token = synchronize(THEN_SET);
      if (token.getType() == THEN) {
        token = nextToken(); // consume the THEN
      } else {
        errorHandler.flag(token, MISSING_THEN, this);
      }

      // Parse the THEN statement.
      // The IF node adopts the statement subtree as its second child.

      ifNode.addChild(statementParser.parse(token));
      token = currentToken();

      if (token.getType() != SEMICOLON) {
        errorHandler.flag(token, MISSING_SEMICOLON, this);
      }

      token = nextToken();
    } while (token.getType() != OTHERWISE);

    // Look for an OTHERWISE.
    if (token.getType() == OTHERWISE) {
      token = nextToken(); // consume the OTHERWISE

      // Parse the ELSE statement.
      // The IF node adopts the statement subtree as its third child.
      ifNode.addChild(statementParser.parse(token));
    }
    
    token = currentToken();

    if (token.getType() == END) {
      token = nextToken(); // consume the END
    } else {
      errorHandler.flag(token, MISSING_END, this);
    }

    if (token.getType() == SEMICOLON) {
      token = nextToken(); // consume semicolon
    } else {
      errorHandler.flag(token, MISSING_SEMICOLON, this);
    }

    return ifNode;
  }
}