<?xml version="1.0" encoding="ISO-8859-1" standalone="yes"?>

<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
    
    <xsl:template match="invoice">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master
                 master-name="normal-page"
                 page-height="29.7cm"
                 page-width="21cm"
                 margin-top="7mm"
                 margin-bottom="5mm"
                 margin-left="25mm"
                 margin-right="48mm">
                    
                    <fo:region-body
                     margin-top="50mm"
                     margin-bottom="20mm"/>
                    
                    <fo:region-before extent="20mm"/>
                    <fo:region-after extent="20mm"/>
                    
                </fo:simple-page-master>
            </fo:layout-master-set>
            
            <fo:page-sequence master-reference="normal-page" font-size="10pt">
                <!-- Seitenkopf (wiederholt sich) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
                <!--
                <fo:static-content flow-name="xsl-region-before">
                <fo:block text-align-last="center" font-size="16pt">
                HEADER
                </fo:block>
                <fo:block space-after="1mm">
                <fo:leader leader-pattern="rule"
                leader-length="162mm"
                rule-style="solid"
                rule-thickness="1pt"/>
                </fo:block>
                </fo:static-content>
                <fo:static-content flow-name="xsl-region-after">
                <fo:block space-after="1mm">
                <fo:leader leader-pattern="rule"
                leader-length="162mm"
                rule-style="solid"
                rule-thickness="1pt"/>
                </fo:block>
                <fo:block text-align-last="center" font-size="16pt">
                FOOTER
                </fo:block>
                </fo:static-content>
                -->
                <fo:flow flow-name="xsl-region-body" text-align="justify">
                    <!--
                    <fo:block text-align="center">
                    RECHNUNG
                    </fo:block>
                    -->
                    <fo:block font-size="10pt" font-family="Helvetica">
                        <xsl:apply-templates/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    
    <xsl:template match="receipient">
        <fo:block 
            space-after="20mm" 
            text-align="justify">
            <fo:block>
                <xsl:value-of select="company"/>
            </fo:block>
            <fo:block>
                <xsl:value-of select="forename"/>
				<xsl:text>
                </xsl:text>
                <xsl:value-of select="surname"/>
            </fo:block>
            <fo:block>
                <xsl:apply-templates select="address"/>
            </fo:block>
        </fo:block>
        <fo:block font-size="10pt" space-after="2mm" text-align="justify">
            <xsl:apply-templates select="details"/>
        </fo:block>
    </xsl:template>
    
    <xsl:template match="address">
        <fo:block>
            <xsl:value-of select="street"/>
            <xsl:text>
            </xsl:text>
            <xsl:value-of select="housenumber"/>
        </fo:block>
        <fo:block>
            <xsl:value-of select="postcode"/>
            <xsl:text>
            </xsl:text>
            <xsl:value-of select="city"/>
        </fo:block>
        <fo:block>
        </fo:block>
        <fo:block space-before="10pt">
            <xsl:value-of select="country"/>
        </fo:block>
    </xsl:template>
    
    <xsl:template match="details">
        <fo:block space-after="20pt">
            <xsl:value-of select="date"/>
        </fo:block>
        <fo:block 
            font-weight="bold"
            space-after="30pt">
            <fo:block>
                <xsl:text>
                    Invoice No.:
                </xsl:text>
                <xsl:value-of select="number"/>
            </fo:block>
            <fo:block>
                <xsl:text>
                    Your use of Usability-Methods.com
                </xsl:text>
            </fo:block>
        </fo:block>
        <fo:block space-after="10pt">
            <xsl:text>
                Dear
            </xsl:text>
            <xsl:value-of select="../receipient/forename"/>
            <xsl:text>
            </xsl:text>
            <xsl:value-of select="../receipient/surname"/>
            <xsl:text>
                ,
            </xsl:text>
        </fo:block>
        <fo:block space-after="10pt">
            <xsl:text>
                thank you very much for using Usability-Methods.com. We are glad that
				our online Usability-Lab provided an added value for you and you are hence
				willing to contribute financially to the further development of this service.
            </xsl:text>
        </fo:block>
        <fo:block space-after="10pt">
            <xsl:text>
                The amount of
            </xsl:text>
            <xsl:value-of select="gross"/>
			<xsl:text>
            </xsl:text>
			<xsl:value-of select="currency"/>
			<xsl:text>
                 for the period of service
            </xsl:text>
			<xsl:value-of select="date"/>
            <xsl:text>
               agreed upon has already been payed.
            </xsl:text>
        </fo:block>
        <fo:block space-after="10pt">
            <xsl:text>
                The amount is made up of:
            </xsl:text>
        </fo:block>
        <fo:block space-after="10pt">
            <fo:table>
                <fo:table-body>
                    
                    <fo:table-row>
                        
                        <fo:table-cell>
                            <fo:block>
                                <xsl:text>
                                    Net amount:
                                </xsl:text>
                            </fo:block>
                        </fo:table-cell>
                        
                        <fo:table-cell>
                            <fo:block>
                                <xsl:value-of select="net"/>
                                <xsl:text>
                                </xsl:text>
                                <xsl:value-of select="currency"/>
                            </fo:block>
                        </fo:table-cell>
                        
                    </fo:table-row>
                    
                    <fo:table-row>
                        
                        <fo:table-cell>
                            <fo:block>
                                <xsl:text>
                                    plus 
                                </xsl:text>
                                <xsl:value-of select="taxrate"/>
                                <xsl:text>
                                    % VAT: 
                                </xsl:text>
                            </fo:block>
                        </fo:table-cell>
                        
                        <fo:table-cell>
                            <fo:block>
                                <xsl:value-of select="tax"/>
                                <xsl:text>
                                </xsl:text>
                                <xsl:value-of select="currency"/>
                            </fo:block>
                        </fo:table-cell>
                    
                    </fo:table-row>
                    
                    <fo:table-row>
                        
                        <fo:table-cell padding-top="10pt">
                            <fo:block font-weight="bold">
                                <xsl:text>
                                    Total amount: 
                                </xsl:text>
                            </fo:block>
                        </fo:table-cell>
                        
                        <fo:table-cell padding-top="10pt">
                            <fo:block font-weight="bold">
                                <xsl:value-of select="gross"/>
                                <xsl:value-of select="currency"/>
                             </fo:block>
                        </fo:table-cell>
                    
                    </fo:table-row>
                    
                </fo:table-body>
            </fo:table>

        </fo:block>
        <fo:block space-after="10pt">
            Thank you for the trust you place in us.
        </fo:block>
        <fo:block space-after="10pt">
            With kind regards,
        </fo:block>
        <fo:block space-after="10pt">
            Steffen Eßers
        </fo:block>
        <fo:block space-after="10pt">
            This document was created automatically and is valid even without a signature.
        </fo:block>
    </xsl:template>
    
    <!--
    <xsl:template match="insasse" mode="ul">
    <fo:block space-after="2mm">
    <xsl:choose>
    <xsl:when test="../@fahrer=@kennung">
    - <fo:inline font-weight="bold"><xsl:value-of select="@name"/></fo:inline>
    </xsl:when>
    <xsl:otherwise>
    - <xsl:value-of select="@name"/>
    </xsl:otherwise>
    </xsl:choose>
    </fo:block>
    </xsl:template>
    
    <xsl:template match="typ">
    <fo:block font-size="14pt" space-after="2mm" text-align="center">
    <xsl:value-of select="concat(@schluessel,', ')"/>
    <xsl:value-of select="concat(@hersteller,' ')"/>
    <xsl:value-of select="concat(@name,', ')"/>
    <xsl:value-of select="concat(@sitze,' Sitze, ')"/>
    <xsl:value-of select="concat(@tueren,' Türen, ')"/>
    <xsl:value-of select="@farbe"/>
    </fo:block>
    </xsl:template>-->
</xsl:stylesheet>