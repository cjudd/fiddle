// determines which xml parser is being loaded

import javax.xml.parsers.DocumentBuilderFactory as dbf

clazz = dbf.newInstance().newDocumentBuilder().class

clazz.protectionDomain.codeSource.location
