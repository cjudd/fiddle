// print out session variables

output = ''

for (n in session.attributeNames)
{
  output = output + "${n} ${session.getAttribute(n)} <br>"
}

output

// or

// print out session variables

output = ""
sessionVars = [:]

for (n in session.attributeNames)
{
  sessionVars[n] = session.getAttribute(n)
}

sessionVars.each { s | 
  output = output + "${s.key} = ${s.value} <br>"
}

output
