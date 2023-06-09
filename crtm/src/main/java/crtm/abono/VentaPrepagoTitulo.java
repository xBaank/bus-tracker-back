
package crtm.abono;

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
@WebServiceClient(name = "VentaPrepagoTitulo", targetNamespace = "http://tempuri.org/", wsdlLocation = "http://www.citram.es:50081/VENTAPREPAGOTITULO/VentaPrepagoTitulo.svc?wsdl")
public class VentaPrepagoTitulo
    extends Service
{

    private final static URL VENTAPREPAGOTITULO_WSDL_LOCATION;
    private final static WebServiceException VENTAPREPAGOTITULO_EXCEPTION;
    private final static QName VENTAPREPAGOTITULO_QNAME = new QName("http://tempuri.org/", "VentaPrepagoTitulo");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://www.citram.es:50081/VENTAPREPAGOTITULO/VentaPrepagoTitulo.svc?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        VENTAPREPAGOTITULO_WSDL_LOCATION = url;
        VENTAPREPAGOTITULO_EXCEPTION = e;
    }

    public VentaPrepagoTitulo() {
        super(__getWsdlLocation(), VENTAPREPAGOTITULO_QNAME);
    }

    public VentaPrepagoTitulo(WebServiceFeature... features) {
        super(__getWsdlLocation(), VENTAPREPAGOTITULO_QNAME, features);
    }

    public VentaPrepagoTitulo(URL wsdlLocation) {
        super(wsdlLocation, VENTAPREPAGOTITULO_QNAME);
    }

    public VentaPrepagoTitulo(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, VENTAPREPAGOTITULO_QNAME, features);
    }

    public VentaPrepagoTitulo(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public VentaPrepagoTitulo(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns IVentaPrepagoTitulo
     */
    @WebEndpoint(name = "BasicHttpBinding_IVentaPrepagoTitulo")
    public IVentaPrepagoTitulo getBasicHttpBindingIVentaPrepagoTitulo() {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_IVentaPrepagoTitulo"), IVentaPrepagoTitulo.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link jakarta.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IVentaPrepagoTitulo
     */
    @WebEndpoint(name = "BasicHttpBinding_IVentaPrepagoTitulo")
    public IVentaPrepagoTitulo getBasicHttpBindingIVentaPrepagoTitulo(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_IVentaPrepagoTitulo"), IVentaPrepagoTitulo.class, features);
    }

    private static URL __getWsdlLocation() {
        if (VENTAPREPAGOTITULO_EXCEPTION!= null) {
            throw VENTAPREPAGOTITULO_EXCEPTION;
        }
        return VENTAPREPAGOTITULO_WSDL_LOCATION;
    }

}
