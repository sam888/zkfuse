package org.zkfuse.web.controller.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkfuse.util.security.CipherEngineEnum;
import org.zkfuse.util.security.EncryptorUtil;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: Sam
 * Date: 14/09/13
 */
@Slf4j
@Controller
@Scope("prototype")
public class EncryptionDemoController extends SelectorComposer<Component> {

    @Wire
    private Combobox symKeyAlgorithmCombo;

    @Wire
    private Combobox encryptKeySizeCombo;

    @Wire
    private Textbox encryptKeyTextbox;

    @Wire
    private Button generateEncryptKeyButton;

    // UI componnets for 'Encrypt/Decrypt Data' panel

    @Wire
    private Radiogroup encryptRadioGroup;

    @Wire
    private Textbox processDataTextbox;

    @Wire
    private Textbox processedDataTextbox;

    @Wire
    private Button processDataButton;

    @Listen("onClick=#generateEncryptKeyButton")
    public void clickGenerateEncryptKeyButton()  {

        if ( !validAlgorithmAndKeySize() ) {
            return;
        }

        String algorithmId = symKeyAlgorithmCombo.getSelectedItem().getId();

        int keySize = new Integer( encryptKeySizeCombo.getSelectedItem().getLabel() );
        try {
            encryptKeyTextbox.setValue( EncryptorUtil.generateBase64EncodedKey( keySize ) );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    @Listen("onClick=#processDataButton")
    public void clickProcessDataButton()  {

        if ( !validAlgorithmAndKeySize() ) {
            return;
        }

        if ( StringUtils.isBlank(encryptKeyTextbox.getValue()) ) {
            Clients.showNotification("Please generate the Encryption key first", "error", generateEncryptKeyButton, "end_center", -1);
            return;
        }

        if ( StringUtils.isBlank( processDataTextbox.getValue() ) ) {
            Clients.showNotification("Please enter data", "error", processDataTextbox, "end_center", -1);
            return;
        }

        if ( encryptRadioGroup.getSelectedItem() == null ) {
            Clients.showNotification("Please select an option", "error", encryptRadioGroup, "before_center", -1);
            return;
        }

        CipherEngineEnum cipherEngineEnum = EncryptorUtil.getCipherEngineByAlgorithmId( symKeyAlgorithmCombo.getSelectedItem().getId() );
        String encryptKey = encryptKeyTextbox.getValue();
        String data = processDataTextbox.getValue();
        String radioId = encryptRadioGroup.getSelectedItem().getId();
        if ( "encryptRadio".equals( radioId ) ) {

            try {
                processedDataTextbox.setValue( EncryptorUtil.encrypt( encryptKey, data, cipherEngineEnum ) );
            } catch (Exception e) {
                e.printStackTrace();
                Messagebox.show( e.getMessage(), "Failed encryption", Messagebox.OK, Messagebox.ERROR);
            }

        } else if ( "decryptRadio".equals( radioId ) ) {

            try {
                processedDataTextbox.setValue( EncryptorUtil.decrypt(encryptKey, data, cipherEngineEnum) );
            } catch (Exception e) {
                e.printStackTrace();
                Messagebox.show( e.getMessage(), "Failed decryption", Messagebox.OK, Messagebox.ERROR);
            }
        }
    }

    private boolean validAlgorithmAndKeySize() {

        if ( symKeyAlgorithmCombo.getSelectedItem() == null ) {
            Clients.showNotification("Please select an algorithm", "error", symKeyAlgorithmCombo, "end_center", -1);
            return false;
        }

        if ( encryptKeySizeCombo.getSelectedItem() == null ) {
            Clients.showNotification("Please select key size", "error", encryptKeySizeCombo, "before_end", -1);
            return false;
        }
        return true;
    }

}
