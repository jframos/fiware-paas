#!/bin/bash
# $1: key
# $2: cert
# $3: chain
if [ $# -ne 3 ] ; then
  echo "Use $0 <keyfile> <certfile> <chainfile>"
  exit
fi
exit
cat $2 $3 > fullchain.crt
openssl pkcs12 -inkey $1 -in fullchain.crt -export -out jetty.pkcs12
keytool -importkeystore -srckeystore jetty.pkcs12 -srcstoretype PKCS12 -destkeystore /etc/keystorejetty -srcstorepass password -deststorepass password -destalias jetty -alias 1
chmod 640 /etc/keystorejetty
rm fullchain.crt jetty.pkcs12
