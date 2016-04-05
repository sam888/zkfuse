package org.zkfuse.web.security.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationContext;
import org.zkfuse.model.security.SecurityPermission;
import org.zkfuse.model.security.SecurityRole;
import org.zkfuse.service.security.SecurityPermissionService;
import org.zkfuse.service.security.SecurityRoleService;
import org.zkfuse.util.context.AppContext;
import org.zkfuse.web.vm.GenericVM;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 */
public class SecurityRoleViewModel extends GenericVM<SecurityRole> {

    private SecurityRole newSecurityRole;

    //@Autowired
    private SecurityRoleService securityRoleService;

    // list to display on zul
    private ListModelList<SecurityRole> securityRoleList;

    // contains all security permissions not yet assigned to selected role
    // @Getter
    private ListModelList<SecurityPermission> availableSecurityPermissionModelList = new ListModelList<SecurityPermission>();

    // contains security permissions of selected role
    // @Getter
    private ListModelList<SecurityPermission> assignedSecurityPermissionModelList = new ListModelList<SecurityPermission>();

    @Getter @Setter
    private Listbox securityRoleListbox;

    // action commands for CRUD

    @Override
    @Command
    @NotifyChange({"selected","securityRoleList"})
    public void newEntity() {
        newSecurityRole();
    }

    public void newSecurityRole() {
        if ( newSecurityRole == null ) {
            newSecurityRole = new SecurityRole();
            getSecurityRoleList().add( newSecurityRole );
        }
        setSelected( newSecurityRole ); // select the new one
    }

    public ListModelList<SecurityRole> getEntityList() {
        return getSecurityRoleList();
    }

    public ListModelList<SecurityRole> getSecurityRoleList() {
        if (securityRoleList == null) {
            // init the list
            securityRoleList = new ListModelList<SecurityRole>( getSecurityRoleService().findAll() );
        }
        return securityRoleList;
    }

    @Override
    @Command
    @NotifyChange({"selected", "errorMessage"})
    public void saveEntity() {
        saveSecurityRole();
    }

    public void saveSecurityRole( ){

        SecurityRole selectedSecurityRole = getSelected();
        if ( selectedSecurityRole.getRoleId() == null ) {

            SecurityRole existingSecurityRoleWithTheSameRoleName = getSecurityRoleService().getRoleByRoleName( selectedSecurityRole.getRoleName() );
            if ( existingSecurityRoleWithTheSameRoleName != SecurityRole.EMPTY_SECURITY_ROLE ) {
                setErrorMessage( "This role name has been taken already. Please choose a different one.");
                return;
            }

            getSecurityRoleService().persist( selectedSecurityRole );
            newSecurityRole = null;

        } else {
            getSecurityRoleService().merge( selectedSecurityRole );
        }
    }

    // called by messageDialogs.zul
    @Override
    @Command
    @NotifyChange({"selected","securityUserList","deleteMessage","errorMessage"})
    public void deleteEntity() {
        deleteSecurityRole();
    }

    public void deleteSecurityRole(){
        SecurityRole selectedRole = getSelected();
        boolean isDeleteSuccess = false;

        Long selectedRoleId = selectedRole.getRoleId();
        if ( selectedRoleId != null ) {
            isDeleteSuccess = deleteSecurityRoleWithErrorHandler( selectedRole );
        }

        setDeleteMessage( null );
        if ( !isDeleteSuccess && selectedRoleId != null ) {
            return;  // exit to show error message box for failing to delete
        }

        getSecurityRoleList().remove( selectedRole );
        if ( selectedRole == newSecurityRole ) {
            newSecurityRole = null;
        }
        setSelected( null );
    }

    /** For use by deleteSecurityRole() only. Return true if delete successfully, false otherwise **/
    private boolean deleteSecurityRoleWithErrorHandler(SecurityRole selectedSecurityRole ) {
        try {
            getSecurityRoleService().deleteRoleByRoleName( selectedSecurityRole.getRoleName() );
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

    // getters for service classes

    private SecurityRoleService getSecurityRoleService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (SecurityRoleService)ctx.getBean("securityRoleServiceImpl");
    }

    private SecurityPermissionService getSecurityPermissionService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (SecurityPermissionService)ctx.getBean("securityPermissionServiceImpl");
    }

    // listener

    // methods for assigning permissions to role

    @Command
    @NotifyChange({"availableSecurityPermissionModelList", "assignedSecurityPermissionModelList"})
    public void reloadAvailableAndAssignedPermissionLists() {
        reloadAvailableSecurityPermissionList();
        reloadAssignedSecurityPermissionList();
        removeAssignedPermissionFromAvailablePermissionList();
    }

    @Command
    public void addSecurityPermissions() {
        moveSecurityPermissionsSelection( availableSecurityPermissionModelList, assignedSecurityPermissionModelList, "Please select at least one Permission to add.");
    }

    @Command
    public void removeSecurityPermissions() {
        moveSecurityPermissionsSelection( assignedSecurityPermissionModelList, availableSecurityPermissionModelList, "Please select at least one Permission to remove.");
    }

    @NotifyChange({"availableSecurityPermissionModelList", "assignedSecurityPermissionModelList"})
    public void moveSecurityPermissionsSelection(ListModelList<SecurityPermission> origin, ListModelList<SecurityPermission> destination, String failMessage) {
        Set<SecurityPermission> selection = origin.getSelection();
        if (selection.isEmpty()) {
            Clients.showNotification(failMessage, "info", null, null, 2000, true);
        } else {
            destination.addAll(selection);
            origin.removeAll(selection);
        }
    }

    @Command
    @NotifyChange({"availableSecurityPermissionModelList", "assignedSecurityPermissionModelList"})
    public void saveSecurityPermissionsToRole() {
        List<SecurityPermission> assignedSecurityPermissionList = new ArrayList<SecurityPermission>( getAssignedSecurityPermissionModelList().getInnerList() );

        getSecurityRoleService().updateAssignedPermissions( assignedSecurityPermissionList, getSelected() );
    }

    // reload list models for available and assigned permissions

    private void reloadAvailableSecurityPermissionList() {
        if (getSelected() != null) {
            // init the list
            availableSecurityPermissionModelList = new ListModelList<SecurityPermission>( getSecurityPermissionService().findAll() );
            availableSecurityPermissionModelList.setMultiple(true);
        }
    }

    private void reloadAssignedSecurityPermissionList() {
        if ( getSelected() != null ) {
            // init the list
            assignedSecurityPermissionModelList = new ListModelList<SecurityPermission>( getSelected().getSecurityPermissions() );
            assignedSecurityPermissionModelList.setMultiple(true);
        }
    }

    private void removeAssignedPermissionFromAvailablePermissionList() {
        if (getSelected() != null) {
            // init the list
            availableSecurityPermissionModelList.removeAll(assignedSecurityPermissionModelList.getInnerList());
        }
    }

    // getters for list models

    public ListModelList<SecurityPermission> getAvailableSecurityPermissionModelList() {
        return availableSecurityPermissionModelList;
    }

    public ListModelList<SecurityPermission> getAssignedSecurityPermissionModelList() {
        return assignedSecurityPermissionModelList;
    }
}
