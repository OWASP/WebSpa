#!/bin/sh

JAVA=`which java`
WEB_SPA="web-spa-0.6.jar"
JAVA_PARAMS="-Xms64m -Xmx256m"

# launch web-spa
if [ -r ./$WEB_SPA ]; then
    $JAVA $JAVA_PARAMS -jar ./$WEB_SPA $@
    exit 0;
fi

echo ""
echo "Web-Spa - Single HTTP/S Request Authorisation"
echo "version 0.6 (web-spa@seleucus.net)"
echo ""
echo "Unable to find $WEB_SPA file."
echo "$0 needs to be in the same directory as $WEB_SPA"
echo ""
