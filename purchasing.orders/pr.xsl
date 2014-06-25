<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
    <h1> Procurement Report </h1>
    <body>
    <table border="2">
      <tr>
       <th>Item Name</th><th>Price</th><th>Quantity</th><th>Extended</th><th>Wholesaler</th>
      </tr>

      <xsl:for-each select="/procurementList/procurements">        
      	  <tr>            	      		  			
  			<td><xsl:value-of select="item/name"/></td>
  			<td><xsl:value-of select="item/price"/></td>
  			<td><xsl:value-of select="item/quantity"/></td>
  			<td><xsl:value-of select="item/extended"/></td>      			           	
      		<td><xsl:value-of select="wholesaler"/></td>
          </tr>
       </xsl:for-each>             
    </table>            
    
    <h2> Grand Total = $<xsl:value-of select="/procurementList/grandTotal"/> </h2>
    
    </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
