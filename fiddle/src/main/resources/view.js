var o = "<html><head><title>Fiddler</title></head><body>"
o += "<div align=\"center\">"
o += "  <div align=\"left\">"
o += "    <form name=\"script\" method=\"post\" action=\"\">"
o += "      <table width=\"800\" border=\"0\">"
o += "        <tr><td><div align=\"center\"><h2>Fiddle</h2></div></td></tr>"
o += "        <tr><td><h3>Script:</h3></td></tr>"
o += "        <tr><td>"
o += "          <div align=\"center\">"
o += "<textarea name=\"script\" cols=\"75\" rows=\"15\">"

if (script != null) {
  o += script
}

o += "</textarea>"
o += "          </div>"
o += "        </td></tr>"
o += "        <tr><td><div align=\"center\">"
o += "<select name=\"scriptEngine\">"

for (var iter = engines.keySet().iterator(); iter.hasNext();) {
  var engineName = iter.next()
  o += "  <option value=\"" + engineName + "\" "
  if (scriptEngine.equals(engineName)) {
    o += "selected"
  }
  o += ">" + engines.get(engineName) + "  </option>"
}


//for each (var engineName in engines.keySet()) {
//  o += "  <option value=\"" + engineName + "\" "
//  if (scriptEngine.equals(engineName)) {
//    o += "selected"
//  }
//  o += ">" + engines.get("js") + "  </option>"
//}

o += "</select>"

o += "          <input type=\"submit\" name=\"Submit\" value=\"Submit\">"
o += "        </div></td></tr>"
o += "        </td></tr>"
o += "      </table>"
o += "    </form>"

if (result != null) {
  o += "  <h3>Results:</h3>"
  o += result.toString()
}

o += "  </div>"
o += "</div>"

if (exception != null) {
  o += "<h2>Exception</h2>"
  o += exception.getClass().getName()
  o += "<h3>Message:</h3>"
  if (exception.getMessage() != null) {
    o += exception.getMessage()
  } else {
    o += "No Message!!!"
  }

  if (exception.getStackTrace() != null) {
    o += "<h3>Stack Trace:</h3>"
//    o += stackTrace = exception.getStackTrace()
    for each (var stackTrace in exception.getStackTrace()) {
      o += stackTrace.toString() + "<br>"
    }
//    for (int i = 0; i < stackTrace.length; i++) {
//      o += stackTrace[i].toString()
//      o += "<br>"
//    }
  } else {
    o += "No Stack Trace!!!"
  }
}

o += "</body></html>"

o