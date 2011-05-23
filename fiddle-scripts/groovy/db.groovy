import javax.naming.*

ctx = new InitialContext()
ds = ctx.lookup("java:/jdbc/ticket")
conn = ds.getConnection()
stmt = conn.createStatement()
rs = stmt.executeQuery("select * from SYS.SYSTABLES")

out = ""

while(rs.next()) {
  out = out + rs.getString(2) + "<br>"
}

out