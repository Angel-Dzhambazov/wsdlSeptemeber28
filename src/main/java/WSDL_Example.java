import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.impl.wsdl.submit.transports.http.WsdlResponse;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.iface.Request;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;
import org.apache.xmlbeans.XmlException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WSDL_Example {
    private static final String GET_CONTINENTS = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://www.oorsprong.org/websamples.countryinfo\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <web:ListOfContinentsByCode/>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    public static void main(String[] args){
        String urlWsdl = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL";

        WsdlProject project = null;
        try {
            project = new WsdlProject();
        } catch (XmlException e) {
            System.out.println("XmlException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        } catch (SoapUIException e) {
            System.out.println("SoapUIException");
            e.printStackTrace();
        }
        System.out.println(project.toString());

        WsdlInterface[] wsdls = new WsdlInterface[0];
        try {
            wsdls = WsdlImporter.importWsdl(project, urlWsdl);
        } catch (Exception e) {
            System.out.println("Exception e");
            e.printStackTrace();
        }
        WsdlInterface wsdl = wsdls[0];

        String soapResponse;
        String currentOperation = "currentOperation";

        WsdlResponse wsdlResponse;
        for (Operation operation : wsdl.getOperationList()) {
            WsdlOperation op = (WsdlOperation) operation;
            WsdlRequest wsdlRequest = op.addNewRequest(currentOperation);
            /* This public API does not need authorisation therefore I am not using credentials
            wsdlRequest.setUsername(userName);
            wsdlRequest.setPassword();
            wsdlRequest.setWssPasswordType();
            */

            wsdlRequest.setTimeout(String.valueOf(TimeUnit.HOURS.toMillis(3L)));
            String requestXML = GET_CONTINENTS;
            wsdlRequest.setRequestContent(requestXML);
            WsdlSubmitContext wsdlSubmitContext = new WsdlSubmitContext(wsdlRequest);
            WsdlSubmit<?> submit = null;
            try {
                submit = wsdlRequest.submit(wsdlSubmitContext, false);
            } catch (Request.SubmitException e) {
                e.printStackTrace();
            }
            wsdlResponse = wsdlRequest.getResponse();
            Response response = submit.getResponse();
            soapResponse = response.getContentAsString();
            System.out.println(soapResponse);
        }
    }
}
