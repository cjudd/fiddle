output = "";

keys = session.getAttributeNames();

while(keys.hasMoreElements()) {
  key = keys .nextElement();
  output = output + key + "=" + session.getAttribute(key) + "<br>";
}

output;
