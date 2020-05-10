/**
 *  
 *  This is test code 
 * 
 */

package com.example;

import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.security.auth.x500.X500Principal;

import com.ibm.websphere.security.CertificateMapFailedException;
import com.ibm.websphere.security.CertificateMapNotSupportedException;
import com.ibm.websphere.security.CustomRegistryException;
import com.ibm.websphere.security.NotImplementedException;
import com.ibm.websphere.security.UserMapping;
import com.ibm.websphere.security.UserMappingException;

/**
 * Implementing this implementation enables WebSphere Security to use customized
 * mapping for certificates (Java and Web clients) and for Identity assertion
 * tokens.
 *
 * Implementation of this implementation must provide implementations for:
 * <ul>
 * <li>mapCertificateToName(X509Certificate[])
 * <li>mapDNToName(String)
 * <li>mapPrincipalToName(String)
 * </ul>
**/

public class CustomUserMapping implements UserMapping
{
  /**
   * Maps a Certificate (of X509 format) to a valid user in the Registry.
   * This is used to map the name in the certificate supplied by either a 
   * browser  or a Java client or a Identity assertion token
   * to a valid user in the registry
   *
   * @param	cert the X509 certificate chain
   * @return	the mapped name of the user which should be valid in the registry
   * @exception	UserMappingException if the mapping of the 
   *            certificate fails. 
   * @exception	NotImplementedException if this implementation is not supported.
   *            In this case the default implementation is used. 
   **/
   public String mapCertificateToName(final X509Certificate[] cert)
        throws UserMappingException, NotImplementedException {

     System.out.println("DEBUG: Entering mapCertificateToName");

     String name = null;
     String s = null;

     try {
        s = cert[0].getSubjectDN().getName();
        System.out.println("DEBUG: mapCertificateToName: Printing SubjectDN=" + s);
        final int index = s.indexOf("=");
        if (index == -1) {
           name = s;
        } else {
           final int index1 = s.indexOf(",");
           if (index1 == -1)
              name = s.substring(index + 1);
           else
              name = s.substring(index + 1, index1);
        }
     } /* catch (final CertificateMapNotSupportedException e) {
        throw new UserMappingException(e.getMessage());
     } catch (final CertificateMapFailedException e) {
        throw new UserMappingException(e.getMessage());
     } */ catch (final Exception e) {
        throw new UserMappingException(e.getMessage());
     }

     name = "wsadmin";
     System.out.println("DEBUG: Mapping " + s + " to " + name);
     System.out.println("DEBUG: Exiting mapCertificateToName()  Returning : " + name);
     return name;
  }

  /**
   * Maps a Distinguised Name (DN) to a valid user in the Registry. This is used
   * to map the DN in an Identity Assertion Token to a valid user in the registry
   *
   * @param String the Distinguised Name(DN) in the Identity Assertion Token.
   * @return the mapped name of the user which should be valid in the registry
   * @exception UserMappingException    if the mapping of the DN fails.
   * @exception NotImplementedException if this implementation is not supported.
   *                                    In this case the default implementation is
   *                                    used.
   **/
  public String mapDNToName(final String dn) throws UserMappingException, NotImplementedException {
     System.out.println("DEBUG: mapDNToName input dn=" + dn);
     return dn;
  }

  /**
   * Maps a PrincipalName to a valid user in the Registry. This is used to map the
   * PrincipalName in an Identity Assertion Token to a valid user in the registry
   *
   * @param String the PrincipalName in the Identity Assertion Token.
   * @return the mapped name of the user which should be valid in the registry
   * @exception UserMappingException    if the mapping of the PrincipalName fails.
   * @exception NotImplementedException if this implementation is not supported.
   *                                    In this case the default implementation is
   *                                    used.
   **/
  public String mapPrincipalToName(final String principalName) throws UserMappingException, NotImplementedException {
   System.out.println("DEBUG:mapPrincipalToName input principalName=" + principalName);
   return principalName;
  }

       
}
