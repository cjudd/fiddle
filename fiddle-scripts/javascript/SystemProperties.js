importPackage(java.lang);
importPackage(java.util);

output = ""

properties = System.getProperties();
keys = properties.propertyNames();

while(keys.hasMoreElements()) {
  key = keys .nextElement();
  output = output + key + "=" + properties.getProperty(key) + "<br>"
}

output
