<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
	<html>
  		<body>
			<xsl:apply-templates/>
   		</body>
	</html>
</xsl:template>

<xsl:template match="order">

	<h1>Purchasing Order</h1>

	<xsl:apply-templates select="customer"/>

	<p>
		<strong>Order ID: </strong> <xsl:value-of select="@id"/><br></br>
		<strong>Submitted on: </strong> <xsl:value-of select="@submitted"/>
	</p>

	<xsl:apply-templates select="items"/>

        <h2>Charges</h2>
<table border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td><strong>Sub-Total:</strong></td>
    <td><xsl:value-of select="total"/></td>
  </tr>
  <tr>
    <td><strong>Shipping Fees:</strong></td>
    <td><xsl:value-of select="shipping"/></td>
  </tr>
  <tr>
    <td><strong>GST:</strong></td>
    <td><xsl:value-of select="GST"/></td>
  </tr>
  <tr>
    <td><strong>PST:</strong></td>
    <td><xsl:value-of select="PST"/></td>
  </tr>
  <tr>
    <td><strong>Grand Total:</strong></td>
    <td><xsl:value-of select="grandTotal"/></td>
  </tr>
</table>
	
</xsl:template>

<xsl:template match="customer">
	<h2>Customer Information</h2>
	<p>
		<strong>Name: </strong> <xsl:value-of select="name"/><br></br>
		<strong>Account: </strong> <xsl:value-of select="@account"/>
	</p>
	<xsl:apply-templates select="address"/>
</xsl:template>

<xsl:template match="address">
	<p>	
		<strong>Address:</strong> <br></br>
		<xsl:value-of select="street"/><br></br>
		<xsl:value-of select="city"/><br></br>
		<xsl:value-of select="postal"/><br></br>
	</p>
</xsl:template>

<xsl:template match="items">

<h2>Ordered Items</h2>
<p>
<table border="0">
	<tr>
		<th align="left">Product Name</th>
		<th align="left">Product ID</th>
		<th align="left">Unit Price</th>
		<th align="left">Quantity</th>
	</tr>

	<xsl:for-each select="item">

		<tr>
			<td><xsl:value-of select="name"/></td>
			<td><xsl:value-of select="@number"/></td>
			<td><xsl:value-of select="price"/></td>
			<td><xsl:value-of select="quantity"/></td>
		</tr>
        
	</xsl:for-each>
 
</table>
</p>
</xsl:template>

</xsl:stylesheet>