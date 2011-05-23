import javax.naming.*

ctx = new InitialContext()
ctx.lookup("java:/jdbc/ticket")
