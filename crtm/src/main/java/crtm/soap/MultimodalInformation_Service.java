
package crtm.soap;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.WebServiceFeature;


/**
 * This class was generated by the XML-WS Tools.
 * XML-WS Tools 4.0.0
 * Generated source version: 3.0
 * 
 */
@WebServiceClient(name = "MultimodalInformation", targetNamespace = "http://tempuri.org/", wsdlLocation = "http://www.citram.es:8080/WSMultimodalInformation/MultimodalInformation.svc?wsdl")
public class MultimodalInformation_Service
    extends Service
{

    private final static URL MULTIMODALINFORMATION_WSDL_LOCATION;
    private final static WebServiceException MULTIMODALINFORMATION_EXCEPTION;
    private final static QName MULTIMODALINFORMATION_QNAME = new QName("http://tempuri.org/", "MultimodalInformation");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://www.citram.es:8080/WSMultimodalInformation/MultimodalInformation.svc?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        MULTIMODALINFORMATION_WSDL_LOCATION = url;
        MULTIMODALINFORMATION_EXCEPTION = e;
    }

    public MultimodalInformation_Service() {
        super(__getWsdlLocation(), MULTIMODALINFORMATION_QNAME);
    }

    public MultimodalInformation_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), MULTIMODALINFORMATION_QNAME, features);
    }

    public MultimodalInformation_Service(URL wsdlLocation) {
        super(wsdlLocation, MULTIMODALINFORMATION_QNAME);
    }

    public MultimodalInformation_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, MULTIMODALINFORMATION_QNAME, features);
    }

    public MultimodalInformation_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MultimodalInformation_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns MultimodalInformation
     */
    @WebEndpoint(name = "BasicHttp")
    public MultimodalInformation getBasicHttp() {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttp"), MultimodalInformation.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link jakarta.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns MultimodalInformation
     */
    @WebEndpoint(name = "BasicHttp")
    public MultimodalInformation getBasicHttp(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttp"), MultimodalInformation.class, features);
    }

    private static URL __getWsdlLocation() {
        if (MULTIMODALINFORMATION_EXCEPTION!= null) {
            throw MULTIMODALINFORMATION_EXCEPTION;
        }
        return MULTIMODALINFORMATION_WSDL_LOCATION;
    }

}
