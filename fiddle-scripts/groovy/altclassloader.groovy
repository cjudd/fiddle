import javax.naming.*

ctx = new InitialContext()
ds = ctx.lookup("java:/DefaultDS")
conn = ds.getConnection()

conn.class.protectionDomain.codeSource.location