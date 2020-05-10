# Update hostname to websphere
# AdminConfig.modify('(cells/DefaultCell01/nodes/DefaultNode01|serverindex.xml#ServerIndex_1)',  "[[hostName websphere]]")
# AdminConfig.save()

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
