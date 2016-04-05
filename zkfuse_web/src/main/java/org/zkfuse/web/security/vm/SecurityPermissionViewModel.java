package org.zkfuse.web.security.vm;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationContext;
import org.zkfuse.model.security.SecurityPermission;
import org.zkfuse.service.security.SecurityPermissionService;
import org.zkfuse.util.context.AppContext;
import org.zkfuse.web.vm.GenericVM;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;


/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 * Date: 14/07/13
 */
public class SecurityPermissionViewModel extends GenericVM<SecurityPermission> {

    private SecurityPermission newSecurityPermission;

    //@Autowired
    private SecurityPermissionService securityPermissionService;

    // list to display on zul
    private ListModelList<SecurityPermission> securityPermissionList;

    @Getter @Setter
    private Listbox securityPermissionListbox;

    // action commands for CRUD

    @Override
    @Command
    @NotifyChange({"selected","securityPermissionList"})
    public void newEntity() {
        newSecurityPermission();
    }

    public void newSecurityPermission() {
        if ( newSecurityPermission == null ) {
            newSecurityPermission = new SecurityPermission();
            getSecurityPermissionList().add( newSecurityPermission );
        }
        setSelected( newSecurityPermission ); //select the new one
    }

    public ListModelList<SecurityPermission> getEntityList() {
        return getSecurityPermissionList();
    }

    public ListModelList<SecurityPermission> getSecurityPermissionList() {
        if (securityPermissionList == null) {
            // init the list
            securityPermissionList = new ListModelList<SecurityPermission>( getSecurityPermissionService().findAll() );
        }
        return securityPermissionList;
    }

    @Override
    @Command
    @NotifyChange({"selected", "errorMessage"})
    public void saveEntity() {
        saveSecurityPermission();
    }

    public void saveSecurityPermission( ){

        SecurityPermission selectedSecurityPermission = getSelected();
        if ( selectedSecurityPermission.getPermissionId() == null ) {

            SecurityPermission existingSecurityPermissionWithTheSamePermissionName = getSecurityPermissionService().getSecurityPermissionByPermissionName(
               selectedSecurityPermission.getPermissionName() );
            if ( existingSecurityPermissionWithTheSamePermissionName != SecurityPermission.EMPTY_SECURITY_PERMISSION ) {
                setErrorMessage( "This permission name has been taken. Please choose a different one.");
                return;
            }

            getSecurityPermissionService().persist( selectedSecurityPermission );
            newSecurityPermission = null;

        } else {
            getSecurityPermissionService().merge( selectedSecurityPermission );
        }
    }

    // called by messageDialogs.zul
    @Override
    @Command
    @NotifyChange({"selected","securityUserList","deleteMessage","errorMessage"})
    public void deleteEntity() {
        deleteSecurityPermission();
    }

    public void deleteSecurityPermission(){
        SecurityPermission selectedPermission = getSelected();
        boolean isDeleteSuccess = false;

        Long selectedPermissionId = selectedPermission.getPermissionId();
        if ( selectedPermissionId != null ) {
            isDeleteSuccess = deleteSecurityPermissionWithErrorHandler( selectedPermission );
        }

        setDeleteMessage( null );
        if ( !isDeleteSuccess && selectedPermissionId != null ) {
            return;  // exit to show error message box for failing to delete
        }

        getSecurityPermissionList().remove( selectedPermission );
        if ( selectedPermission == newSecurityPermission ) {
            newSecurityPermission = null;
        }
        setSelected( null );
    }

    /** For use by deleteSecurityPermission() only. Return true if delete successfully, false otherwise **/
    private boolean deleteSecurityPermissionWithErrorHandler(SecurityPermission selectedSecurityPermission) {
        try {
            getSecurityPermissionService().deleteSecurityPermissionByPermissionName( selectedSecurityPermission.getPermissionName() );
            return true;
        } catch (Exception ex) {
            setErrorMessage( "Unexpectted error: " + ex.getMessage() );
            // log it...
        }
        return false;
    }

    // confirm delete message

    @NotifyChange("deleteMessage")
    @Command
    public void confirmDelete(){
        //set the message to show user
        setDeleteMessage( "Are you sure you want to delete " + getSelected().toString() + " ?" );
    }

    // getter for service class

    private SecurityPermissionService getSecurityPermissionService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (SecurityPermissionService)ctx.getBean("securityPermissionServiceImpl");
    }

    // listener

}
