#!/bin/bash

HOST_XSLT="$TMP/$$.host.xsl"
HOST_FILE="$TMP/$$.host.xml"
FINGER_FILE="$TMP/$$.finger.xml"
FINGER_XSLT="$TMP/$$.finger.xsl"
FINGER_NONXML="$TMP/$$.finger.txt"

if [ -z "$1" ]; then 
	echo "Usage: webfinger email@address"
	exit
fi

HOST=`echo $1 | cut -f2 -d@`

HOST_META="http://${HOST}/.well-known/host-meta"
curl -s $HOST_META > $HOST_FILE 

echo '<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:xrd="http://docs.oasis-open.org/ns/xri/xrd-1.0">
<xsl:output method="text"/>
<xsl:template match="/">
<xsl:value-of select="//xrd:Link[@rel='"'"'lrdd'"'"']/@template"/>

</xsl:template>
</xsl:stylesheet>
' >> $HOST_XSLT

FINGER=`xsltproc $HOST_XSLT $HOST_FILE`

rm $HOST_XSLT
rm $HOST_FILE

FINGER=`ECHO $FINGER | sed "s/{uri}/$1/" `

curl -s $FINGER > $FINGER_FILE


echo '<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:xrd="http://docs.oasis-open.org/ns/xri/xrd-1.0">
<xsl:output method="text"/>
<xsl:template match="/">
Subject: <xsl:value-of select="//xrd:Subject"/>
Alias: <xsl:value-of select="//xrd:Alias"/>
Profile Page: <xsl:value-of select="//xrd:Link[@rel='"'"'http://webfinger.net/rel/profile-page'"'"']/@href"/>

</xsl:template>
</xsl:stylesheet>
' >> $FINGER_XSLT 

#### 
#### *
#### <xsl:for-each select="//xrd:Link">#<xsl:value-of select="@rel"/>|<xsl:value-of select="@href"/>#
#### 

xsltproc $FINGER_XSLT $FINGER > $FINGER_NONXML

rm $FINGER_FILE
rm $FINGER_XSLT


cat $FINGER_NONXML

rm $FINGER_NONXML