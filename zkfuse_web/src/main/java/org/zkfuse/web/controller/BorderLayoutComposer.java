package org.zkfuse.web.controller;

import java.util.Locale;

import org.zkfuse.web.util.i18n.SessionUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 * Code adapted from ZK demo.
 *
 * @author Samuel Huang
 */
public class BorderLayoutComposer extends SelectorComposer<Window> {
    
    private static final long serialVersionUID = 1L;

    private ListModelList<MenuNode> menuModel = new ListModelList<MenuNode>();
    private ListModelList<Locale> localeModel = new ListModelList<Locale>();
    
    private static final String WELCOME_URL = "i18n/welcome_i18n.zul";
    private static final String HELLO_WORLD_URL = "i18n/hello_world.zul";
    private static final String USER_DETAILS_URL = "i18n/userDetails.zul";
    private static final String ORDER_FORM_URL = "order/orderForm.zul";
    
    @Wire
    Listbox menuListbox;
    
    @Wire 
    Listbox localeListbox;
    
    @Wire
    Div contentDiv;
    
    @Wire
    Label localeLabel;
    
    @Wire
    Button resetLocaleButton;
    
    public BorderLayoutComposer(){
        populateMenuModel( );
        populateLocaleModel();
    }

    private void populateMenuModel() {
        menuModel.add(new MenuNode("Welcome", WELCOME_URL ));
        menuModel.add(new MenuNode("Hello World", HELLO_WORLD_URL ));
        menuModel.add(new MenuNode("User Detail", USER_DETAILS_URL ));
        menuModel.add(new MenuNode("Order Form", ORDER_FORM_URL ));
        // menuModel.add(new MenuNode("Graph Demo","borderlayout_fn2.zul"));
        // menuModel.add(new MenuNode("ZK Home","borderlayout_fn3.zul"));        
    }
    
    private void populateLocaleModel() {
        localeModel.add( null );
        localeModel.add( Locale.ENGLISH );
        localeModel.add( Locale.GERMAN );
        localeModel.add( Locale.TRADITIONAL_CHINESE );
        localeModel.add( Locale.SIMPLIFIED_CHINESE );
        localeModel.add( Locale.JAPANESE );
        localeModel.add( Locale.KOREA );
    }
    
    public void doAfterCompose(Window win) throws Exception {
        super.doAfterCompose(win);
        initMenuListbbox();
        initLocaleListbox();
    }

    private void initMenuListbbox() {
        menuListbox.setModel( menuModel );
        menuListbox.setItemRenderer( new MenuNodeItemRenderer() );
        menuListbox.addEventListener( Events.ON_SELECT, new MenuNodeSelectListener() );
    }

    private void initLocaleListbox() {
        localeListbox.setModel( localeModel );
        localeListbox.setSelectedItem(null);
        localeListbox.setItemRenderer( new LocaleListboxRenderer() );
        localeListbox.addEventListener( Events.ON_SELECT, new LocaleListboxSelectListener() );
    }

    class MenuNode {
        String label;
        String link;
        public MenuNode(String label,String link){
            this.label = label;
            this.link = link;
        }
        public String getLabel() {
            return label;
        }
        public void setLabel(String label) {
            this.label = label;
        }
        public String getLink() {
            return link;
        }
        public void setLink(String link) {
            this.link = link;
        }
    }
    
    @SuppressWarnings("rawtypes")
    class MenuNodeItemRenderer implements ListitemRenderer {
        @Override
        public void render(Listitem item, Object data, int index) throws Exception {
            MenuNode node = (MenuNode) data;
            item.setImage("imgs\\icon-24x24.png");
            item.setLabel(node.getLabel());
            item.setValue(node);
        }
    }
    
    class MenuNodeSelectListener implements EventListener<Event>{
        public void onEvent(Event event) throws Exception {
            Listitem item = menuListbox.getSelectedItem();
            contentDiv.getChildren().clear();
            if(item!=null){
                MenuNode node = (MenuNode)item.getValue();
                Executions.createComponents(node.getLink(),contentDiv,null);
            }
        }       
    }
    
    @SuppressWarnings("rawtypes")
    class LocaleListboxRenderer implements ListitemRenderer {
        @Override
        public void render(Listitem item, Object data, int index) throws Exception {
            Locale locale = (Locale)data;
            if ( locale == null ) return;
            item.setLabel( locale.getDisplayName() );
            item.setValue( locale );
        }
    }
    
    class LocaleListboxSelectListener implements EventListener<Event>{
        public void onEvent(Event event) throws Exception {
            Listitem item = localeListbox.getSelectedItem();
            Locale locale = (Locale)item.getValue();
            if ( locale != null ){
                SessionUtil.setLocaleInSession(locale);
                // Locales.setThreadLocal( locale );
                localeLabel.setValue( locale.toString() );
            } else {
                SessionUtil.setLocaleInSession( null );
                // Locales.setThreadLocal(null);
                localeLabel.setValue( "" );
            }
        }       
    }
    
    // global commands

    @Listen("onClick = toolbarbutton")
    public void i18nToolBarButtonListener(Event event) {
        Component comp = event.getTarget();
        if (comp instanceof Toolbarbutton) {
            Toolbarbutton toolBarButton = (Toolbarbutton)comp;
            String url = (String)toolBarButton.getAttribute("url");
            contentDiv.getChildren().clear();
            Executions.createComponents(url, contentDiv, null);
        }
    }
}
