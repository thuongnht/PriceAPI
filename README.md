# PriceAPI
A demo to integrate to Price API, using Javascript-SOAP-Servlet 

<br><hr><br>
- Tomcat 7.x, JDK 1.8.x on Windows (Opensdk-8 on Ubuntu)
- Original Source: folders src/, web/
- Deployed Source: folders deployment/
- Test Images: folder blob/
- External library: folder libs/

<br><hr><br>
- clone resource and copy file 'PriceAPI.war' in folder 'deployment' to 'tomcat/webapps/'
- in tomcat/conf/context.xml, tag 'context', add this tag
<br>
<p align="center">
<img width="377" heigh="54" src="https://github.com/thuongnht/PriceAPI/blob/master/blob/context-environment.png" /> 
</p>
<br>
- turn on tomcat server (I use xampp/tomcat)
- open localhost:[port]/PriceAPI/index.html and do some tests

<br><hr><br>
- In case to deploy from original source again, the extenal lib need to be added to project

<br><hr><br>
- client sends soap message to servlet using httpRequest
- servlet parses eanID and do a request to priceapi through BulkRequest class
- the result is mapped to List<<class> Price>
- class Message is used to make soap message response from the List<<class> Price> and is responsed to client
- client parse xml response and show the details in table