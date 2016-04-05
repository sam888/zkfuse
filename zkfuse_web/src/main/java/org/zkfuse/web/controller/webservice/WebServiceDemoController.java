package org.zkfuse.web.controller.webservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkfuse.web.util.HttpClientUtil;
import org.zkfuse.webservice.WebServiceNameEnum;
import org.zkfuse.webservice.client.util.SoapRequestResponse;
import org.zkfuse.webservice.client.util.WebServiceClientUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;

/**
 * Created with IntelliJ IDEA.
 * User: Sam
 * Date: 27/08/13
 */
@Slf4j
@Controller
@Scope("prototype")
public class WebServiceDemoController extends SelectorComposer<Component> {

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Autowired
    private WebServiceClientUtil webServiceClientUtil;

    private String port;

    @Wire
    private Combobox webServiceCombo;

    @Wire
    private Button sendWebServiceButton;

    @Wire
    private Intbox inputIntbox;

    @Wire
    private Textbox descriptionTextbox;

    @Wire
    private Textbox wsdlTextbox;

    @Wire
    private Textbox soapRequestTextbox;

    @Wire
    private Textbox soapResponseTextbox;

    @Wire
    private A refLink;

    private String webServiceId;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose( comp );
        port = Integer.toString( Executions.getCurrent().getLocalPort() );
        webServiceCombo.addEventListener(Events.ON_SELECT, new WebServiceComboListener());
        sendWebServiceButton.addEventListener(Events.ON_CLICK, new OnClickSendWebServiceButtonEventListener());
    }

    private class WebServiceComboListener implements EventListener<Event>{
        public void onEvent(Event event) throws Exception {
            String wsdl = null;
            webServiceId = webServiceCombo.getSelectedItem().getId();

            if ( webServiceId == null ) return; // Avoid NPE

            if ( webServiceId.equals( "ut-hash" ) ) {

                descriptionTextbox.setValue(Labels.getLabel("usernameToken.hash"));
                wsdl = httpClientUtil.getWsdlForWebService(WebServiceNameEnum.USERNAME_TOKEN_HASHED_PASSWORD_WS.value(), port );
                refLink.setLabel("");

            } else if ( webServiceId.equals( "x509-asym" ) ) {

                descriptionTextbox.setValue(Labels.getLabel("X509.asymmetric.binding") );
                refLink.setLabel("X509 asymmetric message protection");
                wsdl = httpClientUtil.getWsdlForWebService(WebServiceNameEnum.X509_ASYMMETRIC_WS.value(), port);
                refLink.setLabel("");

            } else if ( webServiceId.equals( "ut-x509-sym" ) ) {

                descriptionTextbox.setValue(Labels.getLabel("usernameToken.X509.symmetric.binding"));
                refLink.setHref("http://pic.dhe.ibm.com/infocenter/wasinfo/v8r5/index.jsp?topic=%2Fcom.ibm.websphere.wlp.express.doc%2Fae%2Fcwlp_wssec_templates_scenario6.html");
                refLink.setLabel("UsernameToken with X509 symmetric message protection");
                wsdl = httpClientUtil.getWsdlForWebService(WebServiceNameEnum.USERNAME_TOKEN_X509_SYMMETRIC_WS.value(), port);

            } else if ( webServiceId.equals( "ut-x509-asym" ) ) {

                descriptionTextbox.setValue( Labels.getLabel("usernameToken.X509.asymmetric.binding") );
                refLink.setHref("http://pic.dhe.ibm.com/infocenter/wasinfo/v8r5/index.jsp?topic=%2Fcom.ibm.websphere.wlp.express.doc%2Fae%2Fcwlp_wssec_templates_scenario4.html");
                refLink.setLabel( "UsernameToken with X509 asymmetric message protection" );
                wsdl = httpClientUtil.getWsdlForWebService(WebServiceNameEnum.USERNAME_TOKEN_X509_ASYMMETRIC_WS.value(), port);
            }
            wsdlTextbox.setText( wsdl );

            inputIntbox.setValue(null);
            soapRequestTextbox.setValue("");
            soapResponseTextbox.setValue("");
        }
    }

    private class OnClickSendWebServiceButtonEventListener implements EventListener<Event> {

        @Override
        public void onEvent(Event event) throws Exception {
            if ( StringUtils.isNotBlank(webServiceId) ) {
                Integer input = inputIntbox.getValue();

                if ( input == null ) {
                   inputIntbox.setErrorMessage("Please enter a number in Input field before clicking Send button");
                   return;
                }

                System.out.println("OnClickSendWebServiceButtonEventListener: input:" + input);

                if ( webServiceCombo.getSelectedItem() != null ) {
                    String webServiceId = webServiceCombo.getSelectedItem().getId();
                    SoapRequestResponse soapRequestResponse = webServiceClientUtil.callWebService(input, webServiceId);
                    soapRequestTextbox.setText( soapRequestResponse.getSoapRequest() );
                    soapResponseTextbox.setText( soapRequestResponse.getSoapResponse() );
                }

            } else {
                inputIntbox.setErrorMessage("Please select a Web Service from drop down list above before clicking Send button");
            }
        }
    }
}
