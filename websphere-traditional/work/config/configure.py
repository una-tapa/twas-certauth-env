# Update hostname to websphere
# AdminConfig.modify('(cells/DefaultCell01/nodes/DefaultNode01|serverindex.xml#ServerIndex_1)',  "[[hostName websphere]]")
# AdminConfig.save()

#Install Application
AdminApp.install('/work/app/CertLoginSample.war', '[ -nopreCompileJSPs -distributeApp -nouseMetaDataFromBinary -appname CertLoginSample_war -createMBeansForResources -noreloadEnabled -nodeployws -validateinstall warn -noprocessEmbeddedConfig -filepermission .*\.dll=755#.*\.so=755#.*\.a=755#.*\.sl=755 -noallowDispatchRemoteInclude -noallowServiceRemoteInclude -asyncRequestDispatchType DISABLED -nouseAutoLink -noenableClientModule -clientMode isolated -novalidateSchema -contextroot /CertLoginSample -MapModulesToServers [[ "Hello Servlet" CertLoginSample.war,WEB-INF/web.xml WebSphere:cell=DefaultCell01,node=DefaultNode01,server=server1 ]] -MapWebModToVH [[ "Hello Servlet" CertLoginSample.war,WEB-INF/web.xml default_host ]]]' ) 
AdminConfig.save()

#Enable App security
AdminTask.applyWizardSettings('[-secureApps true -secureLocalResources false -adminPassword password -userRegistryType WIMUserRegistry -adminName wsadmin ]')  
AdminConfig.save()

#Enable ClientAuth Required
AdminTask.modifySSLConfig('[-alias NodeDefaultSSLSettings -clientAuthentication true ]')
AdminConfig.save()

#Set security custom property
AdminTask.setAdminActiveSecuritySettings('[-customProperties ["WAS_customUserMappingImpl=com.example.CustomUserMapping"]]')
AdminConfig.save()

#Set Trace
serverName = "server1"
print serverName
server = AdminConfig.getid('/Server:'+serverName+'/')
print server
tc = AdminConfig.list('TraceService', server)
print tc
# traceSpec = "*=info"#: WAS.j2c=all: RRA=all :com.ibm.ws.security.*=all:com.ibm.websphere.security.*=all"
traceSpec = "*=info:com.ibm.ws.security.*=all:com.ibm.websphere.security.*=all"
print traceSpec
attrs = [["startupTraceSpecification", traceSpec]]
print attrs
AdminConfig.modify(tc, attrs)
AdminConfig.save()
