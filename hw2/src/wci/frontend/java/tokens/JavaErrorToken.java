package wci.frontend.java.tokens;

import wci.frontend.Source;
import wci.frontend.java.JavaErrorCode;
import wci.frontend.java.JavaToken;

import static wci.frontend.java.JavaTokenType.ERROR;

/**
 *
 *
 * <h1>JavaErrorToken</h1>
 *
 * <p>Java error token.
 *
 * <p>Copyright (c) 2009 by Ronald Mak
 *
 * <p>For instructional purposes only. No warranties.
 */
public class JavaErrorToken extends JavaToken {
  /**
   * Constructor.
   *
   * @param source the source from where to fetch subsequent characters.
   * @param errorCode the error code.
   * @param tokenText the text of the erroneous token.
   * @throws Exception if an error occurred.
   */
  public JavaErrorToken(Source source, JavaErrorCode errorCode, String tokenText) throws Exception {
    super(source);

    this.text = tokenText;
    this.type = ERROR;
    this.value = errorCode;
  }

  /**
   * Do nothing. Do not consume any source characters.
   *
   * @throws Exception if an error occurred.
   */
  protected void extract() throws Exception {}
}
