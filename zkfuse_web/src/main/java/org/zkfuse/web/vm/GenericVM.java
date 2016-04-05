package org.zkfuse.web.vm;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.zkfuse.web.util.i18n.SessionUtil;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zul.ListModelList;

/**
 * @author Samuel Huang
 * @param <E>
 */
public abstract class GenericVM<E> implements Serializable {

    private E selected;

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    
    private String moduleName;
    private String deleteMessage;
    private String errorMessage;
    
    public GenericVM() {
        SessionUtil.setWindowLabelKeyInSession( "" );
    }
    
    //    Empty implementations of CRUD operations for a DB table

    /** Subclass can override this to provide 'Create' operation(i.e. create a record in DB table) for zul to call **/
    @Command
    public void newEntity() {}

    /** Subclass can override this to provide 'READ' operation(i.e. reads a list records from DB table) for zul to call **/
    @Command
    public ListModelList<E> getEntityList() { return null; }

    /** Subclass can override this to provide 'Delete' operation(i.e. delete a record in DB table) for zul to call **/
    @Command
    public void deleteEntity() {};

    /** Subclass can override this to provide 'Update' operation(i.e. update an existing record in DB table) for zul to call **/
    @Command
    public void saveEntity() {};

    //  Methods for validator and its' error message

    public org.zkoss.bind.Validator getJsr303Validator() {
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {

                Property property = ctx.getProperty();
                String propertyName = property.getProperty();
                Object propertyValue = property.getValue();

                Class clazz = selected.getClass();

                // Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
                Set<ConstraintViolation<E>> violationSet = validator.validateValue(clazz, propertyName, propertyValue);

                String errorMsg = getErrorMessage(violationSet);
                if (errorMsg.equals("")) {
                    clearValidationErrorMessage(ctx);
                } else {
                    addInvalidMessage(ctx, errorMsg);
                }
            }
        };
    }
    
    /**
     * Work around for validating non-null entity class(e.g. Locale in ResourceBundle) that has been
     * annotated with @NotNull.
     */
    //    private Object getPropertyValueByReflection(String propertyName, Class clazz) throws Exception {
    //        Method method = clazz.getMethod( "get" + StringUtils.capitalize(propertyName));
    //        return method.invoke(selected);
    //    }

    public String getErrorMessage(Set<ConstraintViolation<E>> violationSet) {
        return violationSet.isEmpty() ? "" : violationSet.iterator().next().getMessage();
    }

    public void clearValidationErrorMessage(ValidationContext ctx) {
        ValidationMessages vmsgs = ((BinderCtrl) ctx.getBindContext().getBinder()).getValidationMessages();
        vmsgs.clearMessages(ctx.getBindContext().getComponent() );
    }

    public void clearValidationErrorMessage(ValidationContext ctx, String msgKey) {
        ValidationMessages vmsgs = ((BinderCtrl) ctx.getBindContext().getBinder()).getValidationMessages();
        vmsgs.clearMessages(ctx.getBindContext().getComponent(), msgKey );
    }

    //  Reusable action commands for all subclasses

    @Command
    @NotifyChange("deleteMessage")
    public void cancelDelete() {
        // clear delete message
        setDeleteMessage(null);
    }

    @NotifyChange("errorMessage")
    @Command
    public void clearErrorMessage() {
        // clear error message
        setErrorMessage(null);
    }
    
    // getters and setters for instance variables

    public String getDeleteMessage() {
        return deleteMessage;
    }
    public void setDeleteMessage(String deleteMessage) {
        this.deleteMessage = deleteMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getModuleName() {
        return moduleName;
    }
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    
    public E getSelected() {
        return selected;
    }
    public void setSelected(E selected) {
        this.selected = selected;
    }

    /*
    protected void showError(UiException e) {
        showError(getLabel(e.getMessage()), getLabel(e.getTitle()));
    }
    
    protected void showError(String errorText, String title) {
        Messagebox.setTemplate("~./messagebox.zul");
        Messagebox.show(errorText, title, Messagebox.OK, Messagebox.ERROR);
    }

    protected void showInfo(String infoText, String title) {
        Messagebox.setTemplate("~./messagebox.zul");
        Messagebox.show(infoText, title, Messagebox.OK, Messagebox.INFORMATION, new EventListener() {

            @Override
            public void onEvent(Event evt) {
                doOK();
            }
        });
    }
    */
}
