/* ========================================================================= *
 *        Licensed under The Apache Software License, Version 2.0            *
 *        Copyright (c) 2007 Christopher M. Judd, All rights reserved.       *
 * ========================================================================= */

package net.java.dev.fiddle;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet executes Java Scripts in order to debug/fiddle with an application
 * deployed in a web container or Java EE application server.
 * 
 * @author Christopher M. Judd (cjudd@juddsolutions.com)
 * @since 1.0
 */
public class FiddleServlet extends HttpServlet {
  
  private static final long serialVersionUID = 1L;

  private static final String BINDING_ENGINES = "engines";

  /**
   * one more line of comment.
   */
  private static final String BINDING_ENGINE = "engine";

  /**
   * yet more comments.
   */
  private static final String BINDING_SESSION = "session";

  private static final String BINDING_RESPONSE = "response";

  private static final String BINDING_REQUEST = "request";

  private static final String BINDING_EXCEPTION = "exception";

  private static final String BINDING_RESULT = "result";

  private static final String BINDING_SCRIPT = "script";

  private static final String BINDING_SCRIPT_ENGINE = "scriptEngine";

  private static final String SCRIPT = "SCRIPT";

  private static final String SCRIPT_PARAM = BINDING_SCRIPT;

  private static final String SCRIPT_ENGINE_PARAM = "scriptEngine";

  private static final String DEFAULT_SCRIPT_ENGINE = "JavaScript";

  private static final String DEFAULT_VIEW_SCRIPT = "/view.js";

  private ScriptEngineManager manager;

  private Map<String, String> engines;

  private CompiledScript viewScript;

  /**
   * The doGet method of the servlet. <br>
   * 
   * This method is called when a form has its tag value method equals to get.
   * 
   * @param request
   *          the request send by the client to the server
   * @param response
   *          the response send by the server to the client
   * @throws ServletException
   *           if an error occurred
   * @throws IOException
   *           if an error occurred
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * The doPost method of the servlet. <br>
   * 
   * This method is called when a form has its tag value method equals to post.
   * 
   * @param request
   *          the request send by the client to the server
   * @param response
   *          the response send by the server to the client
   * @throws ServletException
   *           if an error occurred
   * @throws IOException
   *           if an error occurred
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    
    response.setContentType("text/html");
    
    String script = request.getParameter(SCRIPT_PARAM);
    if (script != null) {
      request.setAttribute(SCRIPT, script);
    }

    String scriptEngine = request.getParameter(SCRIPT_ENGINE_PARAM);
    if (scriptEngine == null) {
      scriptEngine = DEFAULT_SCRIPT_ENGINE;
    }

    Writer out = response.getWriter();

    Object result = null;
    Exception caughtException = null;

    try {
      if (script != null && !"".equals(script)) {
        result = execute(script, scriptEngine, request, response);
      }
    } catch (Exception e) {
      caughtException = e;
    }

    try {
      String output = generateView(script, result, caughtException,
          scriptEngine, request, response);
      out.write(output);
    } catch (ScriptException ex) {
      String message = "Unable to generate view from view script.";
      log(message, ex);
      new ServletException(message, ex);
    }
  }

  /**
   * Generates the HTML to be rendered. It uses a scripting engine to process
   * the view.
   * 
   * @param script
   *          User requested script.
   * @param result
   *          Result of user requested script.
   * @param exception
   *          Exception caused by executing user requested script.
   * @param scriptEngine
   *          Name of user requested script engine.
   * @param request
   *          Http request.
   * @param response
   *          Http response.
   * @return HTML page to return.
   * @throws ScriptException
   *           Exception while evaluating the user view.
   * @throws IOException
   *           IO error.
   */
  private String generateView(String script, Object result,
      Exception exception, String scriptEngine, HttpServletRequest request,
      HttpServletResponse response) throws ScriptException, IOException {
    Bindings bindings = new SimpleBindings();
    bindings.put(BINDING_SCRIPT, script);
    bindings.put(BINDING_RESULT, result);
    bindings.put(BINDING_EXCEPTION, exception);
    bindings.put(BINDING_REQUEST, request);
    bindings.put(BINDING_RESPONSE, response);
    bindings.put(BINDING_SESSION, request.getSession());
    bindings.put(BINDING_ENGINES, engines);
    bindings.put(BINDING_SCRIPT_ENGINE, scriptEngine);
    return (String) viewScript.eval(bindings);
  }

  /**
   * Executes the user requested script.
   * 
   * @param script
   *          User requested script to execute.
   * @param engineName
   *          Name of script engine to use while evaluating.
   * @param request
   *          Http request.
   * @param response
   *          Http response.
   * @return Result of script executing.
   * @throws ScriptException
   */
  private Object execute(String script, String engineName,
      HttpServletRequest request, HttpServletResponse response)
      throws ScriptException {
    ScriptEngine engine = manager.getEngineByName(engineName);
    Bindings bindings = engine.createBindings();
    bindings.put(BINDING_REQUEST, request);
    bindings.put(BINDING_SESSION, request.getSession());
    bindings.put(BINDING_RESPONSE, response);
    bindings.put(BINDING_ENGINE, engine);
    return engine.eval(script, bindings);
  }

  /**
   * Initialize the servlet.
   */
  public void init(ServletConfig config) throws ServletException {
    initScriptEngines();
    Reader script = loadViewScript();
    try {
      compileViewScript(script);
    } catch (ScriptException ex) {
      throw new ServletException("Unable to compile view script.", ex);
    }
    super.init(config);
  }

  /**
   * Load view script.
   * 
   * @return View Script.
   */
  private Reader loadViewScript() {
    InputStream inputStream = this.getClass().getResourceAsStream(
        DEFAULT_VIEW_SCRIPT);
    return new InputStreamReader(inputStream);
  }

  /**
   * Compile the view script. Rather then embedding the view in the servlet
   * code, the compiles a script and uses that as a template technology. This
   * insures there is not dependency on anything other than the regular
   * Scripting stuff.
   * 
   * @throws ScriptException
   */
  private void compileViewScript(Reader script) throws ScriptException {
    ScriptEngine engine = manager.getEngineByName(DEFAULT_SCRIPT_ENGINE);
    Compilable compilable = (Compilable) engine;
    viewScript = compilable.compile(script);
  }

  /**
   * Determine the script engines and cache the data so it does not need to be
   * redetermined each time the page is rendered.
   */
  private void initScriptEngines() {
    manager = new ScriptEngineManager();

    // cache the engines details to insure order and consistant names.
    engines = new TreeMap<String, String>();
    for (ScriptEngineFactory factory : manager.getEngineFactories()) {
      engines.put(factory.getNames().get(0), factory.getLanguageName());
    }
  }

}
