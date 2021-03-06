# twas-certahtu-env

## This is an experimental environment for certificate login on traditional WebSphere. Practices are not necessarily recommended. Use at your own risk.

## Overview

This repository consists of 3 directories. 
- websphere traditional
- cert-login-sampleapp
- custom-mapping
---
### websphere-traditional

This directory contains Dockerfile and configuration file.  On top of tWAS docker image, follwoing configuration is performed to create a container that can be used to test certificate login. 
#### Configuration
- Installs certificate authentication application. (CertLoginSample.war)
- Copies sample custom mapping jar into `{was_install_dir}lib\ext`
- Configures security custom property `WAS_customUserMappingIMpl` 
- Configures `client authentication required` for `NodeDefaultSSLSettings` to make server to require certificate login for certificate authentication application. <!-- In the directory, configire.py. On adminconsole, NodeDefaultSSLSettings, QOP Panel Screenshot -->
#### To build and run
- `docker build -t testwas .` to build the docker image. 
- `docker run -p 9043:9043 -p 9443:9443 -v ~/waslogs:/logs`  to start the container. 
- To get on the container,  `docker exec -it {container_id} /bin/bash` 
- Then install the key.p12 (in this repository) into the browser. This keystore is from the docker image - nodedefault keystore - `WebAS`.
- On the browser, go to the sample page https://localhost:9443/CertLoginSample
- The browser asks whether to send certificate or not. 
- When I say yes, trace will show `localhost` certificate is mapped to `wsadmin`
- TODO: Make UserMapping optional. 
- TODO: screenshots

---
### cert-login-sampleapp 

This directory contains sample certificate auth application. The application contains `web.xml` that has CLIENT_CERT auth method. 

    ```
    <auth-method>CLIENT-CERT</auth-method> 
    ```
Test URL : https://localhost:9443/CertLoginSample

To rebuild the application, 
- update source files in `src` directory. Run `./gradlew libertyPackage`.
- The app is based on https://openliberty.io/guides/gradle-intro.html

---
### custom-mapping
   - Users can place custom user mapping jar in `{was_install_dir}/lib/ext` directory just like customTAI or custom LoginModule. 
        - Copy customUserMapping jar under `/lib/ext`
        - The jar should include user mapping class that implements following UserMapping interface: https://www.ibm.com/support/knowledgecenter/SSEQTJ_8.5.5/com.ibm.websphere.javadoc.doc/web/spidocs/com/ibm/websphere/security/package-summary.html?view=embed 
        - Configure security custom property with the user mapping class name. For example, `WAS_customUserMappingImpl=com.example.CustomUserMapping`.
        - To compile CustomUserMapping.java
            - go to `src` directory and run command below. It requires `com.ibm.ws.runtime.jar` from tWAS plugins directory in the classpath. (TODO for me: make it easy to compile this. )
            ```
            javac -cp /opt/IBM/WebSphere/AppServer/plugins/com.ibm.ws.runtime.jar com/example/CustomUserMapping.java
            ```  
        - To create jar file, go one directory above `src` and run
            ```
          # Any jar name is ok
             jar cMf CustomUserMapping.jar -C src .
             ```
   - Note: Following is similar interface but it does not apply certificate login. It is for SAML user mapping. 
    https://www.ibm.com/support/knowledgecenter/SS7K4U_9.0.5/com.ibm.websphere.javadoc.doc/web/spidocs/com/ibm/wsspi/security/web/saml/UserMapping.html
   

---
### Trace

Following is trace snippet from the container above. 

```
[5/10/20 3:18:26:352 UTC] 000000a1 SystemOut     O   DEBUG: Entering mapCertificateToName
[5/10/20 3:18:26:353 UTC] 000000a1 SystemOut     O   DEBUG: mapCertificateToName: Printing SubjectDN=CN=localhost, OU=DefaultCell01, OU=DefaultNode01, O=IBM, C=US
[5/10/20 3:18:26:354 UTC] 000000a1 SystemOut     O   DEBUG: Mapping CN=localhost, OU=DefaultCell01, OU=DefaultNode01, O=IBM, C=US to wsadmin
[5/10/20 3:18:26:355 UTC] 000000a1 SystemOut     O   DEBUG: Exiting mapCertificateToName()  Returning : wsadmin
```
<!---

[Configure Kerberos in WAS](https://www.ibm.com/support/knowledgecenter/en/SSEQTP_9.0.5/com.ibm.websphere.base.doc/ae/tsec_kerb_setup.html)  
[Configure Kerberos in DB2](https://www.ibm.com/support/knowledgecenter/en/SSEPGG_11.1.0/com.ibm.db2.luw.admin.sec.doc/doc/c0058525.html)

- TODO upload all tWAS files Dropbox\CertificatLogin

-->