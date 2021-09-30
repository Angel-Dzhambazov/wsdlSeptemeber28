import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.impl.wsdl.submit.transports.http.WsdlResponse;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.iface.Response;

import java.util.concurrent.TimeUnit;

public class WSDL_Example {
    private static final String GET_CONTINENTS = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://www.oorsprong.org/websamples.countryinfo\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <web:ListOfContinentsByCode/>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    public static void main(String[] args) throws Exception {
        String urlWsdl = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL";

        WsdlProject project = new WsdlProject();


        WsdlInterface[] wsdls = WsdlImporter.importWsdl(project, urlWsdl);
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
            WsdlSubmit<?> submit = wsdlRequest.submit(wsdlSubmitContext, false);
            wsdlResponse = wsdlRequest.getResponse();
            Response response = submit.getResponse();
            soapResponse = response.getContentAsString();
            System.out.println(soapResponse);
        }
    }
}
