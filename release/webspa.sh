#!/bin/sh

JAVA=`which java`
WEB_SPA="webspa-0.8.jar"
JAVA_PARAMS="-Xms64m -Xmx256m"

umask 077

# launch webspa
if [ -r ./$WEB_SPA ]; then
    $JAVA $JAVA_PARAMS -jar ./$WEB_SPA $@
    exit 0;
fi

echo ""
echo "WebSpa - Single HTTP/S Request Authorisation"
echo "version 0.8 (webspa@seleucus.net)"
echo ""
echo "Unable to find $WEB_SPA file."
echo "$0 needs to be in the same directory as $WEB_SPA"
echo ""
