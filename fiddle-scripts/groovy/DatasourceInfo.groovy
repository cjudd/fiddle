/* Description:
 * This script provides information about a container configured datasource.
 *
 * Instructions:
 * Change the datasourceName variable to your datasource name.
 */

import javax.naming.*

datasourceName = "jdbc/__default" // change variable to match your datasource

ctx = new InitialContext()
ds = ctx.lookup(datasourceName)
conn = ds.getConnection()

"""
Datasource <b>${datasourceName}</b><br>
<ui>
  <li>Class: ${conn.class.name}</li>
  <li>Jar location:	${conn.class.protectionDomain.codeSource.location}</li>
  <li>Read only: ${conn.isReadOnly()}</li>
  <li>Auto commit: ${conn.autoCommit}</li>
  <li>Catalog: ${conn.catalog}</li>
  <li>Meta Data:</li>
  <ul>
    <li>Product name: ${conn.metaData.databaseProductName}</li>
    <li>Product version: ${conn.metaData.databaseProductVersion}</li>
    <li>Database version number: ${conn.metaData.databaseMajorVersion}.${conn.metaData.databaseMinorVersion}</li>
    <li>Driver name: ${conn.metaData.driverName}</li>
    <li>Driver version number: ${conn.metaData.driverMajorVersion}.${conn.metaData.driverMinorVersion}</li>
    <li>JDBC version: ${conn.metaData.JDBCMajorVersion}.${conn.metaData.JDBCMinorVersion}</li>
  <ul>
</ui>
"""
