package org.zkfuse.web.security.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.zkfuse.model.security.SecurityRole;
import org.zkfuse.model.security.SecurityUser;
import org.zkfuse.service.security.SecurityRoleService;
import org.zkfuse.service.security.SecurityUserService;
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
 * Date: 14/07/13
 */
public class SecurityUserViewModel extends GenericVM<SecurityUser> {

    private SecurityUser newSecurityUser;

    private SecurityUserService securityUserService;
    private SecurityRoleService securityRoleService;

    // list to display on zul
    private ListModelList<SecurityUser> securityUserList;

    // contains all security roles not yet owned by selected user
    private ListModelList<SecurityRole> availableSecurityRoleList = new ListModelList<SecurityRole>();

    // contains security roles of selected user
    private ListModelList<SecurityRole> ownedSecurityRoleList = new ListModelList<SecurityRole>();

    private Listbox securityUserListbox;

    // action commands for CRUD

    @Override
    @Command
    @NotifyChange({"selected","securityUserList"})
    public void newEntity() {
        newSecurityUser();
    }

    public void newSecurityUser() {
        if ( newSecurityUser == null ) {
            newSecurityUser = new SecurityUser();
            getSecurityUserList().add( newSecurityUser );
        }
        setSelected( newSecurityUser ); //select the new one
    }

    public ListModelList<SecurityUser> getEntityList() {
        return getSecurityUserList();
    }



    @Override
    @Command
    @NotifyChange({"selected", "errorMessage"})
    public void saveEntity() {
        saveSecurityUser();
    }

    public void saveSecurityUser( ){

        SecurityUser selectedSecurityUser = getSelected();
        if ( selectedSecurityUser.getUserId() == null ) {

            SecurityUser existingSecurityUserWithTheSameLoginName = getSecurityUserService().getUserByLoginName( selectedSecurityUser.getUserLoginName() );
            if ( existingSecurityUserWithTheSameLoginName != SecurityUser.EMPTY_SECURITY_USER ) {
                setErrorMessage( "This login name has been taken. Please choose a different one.");
                return;
            }

            getSecurityUserService().persist( selectedSecurityUser );
            newSecurityUser = null;

        } else {
            getSecurityUserService().merge(selectedSecurityUser);
        }
    }

    // called by messageDialogs.zul
    @Override
    @Command
    @NotifyChange({"selected","securityUserList","deleteMessage","errorMessage"})
    public void deleteEntity() {
        deleteSecurityUser();
    }

    public void deleteSecurityUser(){
        SecurityUser selectedUser = getSelected();
        boolean isDeleteSuccess = false;

        Long selectedUserId = selectedUser.getUserId();
        if ( selectedUserId != null ) {
            isDeleteSuccess = deleteSecurityUserWithErrorHandler(selectedUser);
        }

        setDeleteMessage( null );
        if ( !isDeleteSuccess && selectedUserId != null ) {
            return;  // exit to show error message box for failing to delete
        }

        getSecurityUserList().remove(selectedUser);
        if ( selectedUser == newSecurityUser ) {
            newSecurityUser = null;
        }
        setSelected( null );
    }

    /** For use by deleteSecurityUser() only. Return true if delete successfully, false otherwise **/
    private boolean deleteSecurityUserWithErrorHandler(SecurityUser selectedSecurityUser ) {
        try {
            getSecurityUserService().deleteUserByLoginName(selectedSecurityUser.getUserLoginName());
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

    // listener

    // methods for assigning roles to user

    @Command
    @NotifyChange({"availableSecurityRoleList", "ownedSecurityRoleList"})
    public void reloadAvailableAndAssignedRoleLists() {
        reloadAvailableSecurityRoleList();
        reloadOwnedSecurityRoleList();
        removeAssignedRolesFromAvailableRoleList();
    }

    @Command
    public void addSecurityRoles() {
        moveSecurityRolesSelection( getAvailableSecurityRoleList(), getOwnedSecurityRoleList(), "Please select at least one Role to add.");
    }

    @Command
    public void removeSecurityRoles() {
        moveSecurityRolesSelection( ownedSecurityRoleList, availableSecurityRoleList, "Please select at least one Role to remove.");
    }

    @NotifyChange({"availableSecurityRoleList", "ownedSecurityRoleList"})
    public void moveSecurityRolesSelection(ListModelList<SecurityRole> origin, ListModelList<SecurityRole> destination, String failMessage) {
        Set<SecurityRole> selection = origin.getSelection();
        if (selection.isEmpty()) {
            Clients.showNotification(failMessage, "info", null, null, 2000, true);
        } else {
            destination.addAll(selection);
            origin.removeAll(selection);
        }
    }

    @Command
    @NotifyChange({"availableSecurityRoleList", "ownedSecurityRoleList"})
    public void saveSecurityRolesToUser() {
        List<SecurityRole> assignedSecurityRoleList = new ArrayList<SecurityRole>( getOwnedSecurityRoleList().getInnerList() );
        getSecurityUserService().updateAssignedRoles( assignedSecurityRoleList, getSelected());
    }

    // getters for service classes

    private SecurityUserService getSecurityUserService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (SecurityUserService)ctx.getBean("securityUserServiceImpl");
    }

    private SecurityRoleService getSecurityRoleService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (SecurityRoleService)ctx.getBean("securityRoleServiceImpl");
    }

    // getters for all list models

    public ListModelList<SecurityUser> getSecurityUserList() {
        if (securityUserList == null) {
            // init the list
            securityUserList = new ListModelList<SecurityUser>( getSecurityUserService().findAll() );
        }
        return securityUserList;
    }

    // reload list models for available and assigned roles

    private void reloadAvailableSecurityRoleList() {
        if (getSelected() != null) {
            // init the list
            availableSecurityRoleList = new ListModelList<SecurityRole>( getSecurityRoleService().findAll() );
            availableSecurityRoleList.setMultiple( true );
        }
    }

    private void reloadOwnedSecurityRoleList() {
        if ( getSelected() != null ) {
            // init the list
            ownedSecurityRoleList= new ListModelList<SecurityRole>( getSelected().getSecurityRoles() );
            ownedSecurityRoleList.setMultiple( true );
        }
    }

    private void removeAssignedRolesFromAvailableRoleList() {
        if (getSelected() != null) {
            // init the list
            availableSecurityRoleList.removeAll( ownedSecurityRoleList.getInnerList() );
        }
    }

    // Getters & setters

    public ListModelList<SecurityRole> getAvailableSecurityRoleList() {
        return availableSecurityRoleList;
    }

    public ListModelList<SecurityRole> getOwnedSecurityRoleList() {
        return ownedSecurityRoleList;
    }

    public Listbox getSecurityUserListbox() {
        return securityUserListbox;
    }

    public void setSecurityUserListbox(Listbox securityUserListbox) {
        this.securityUserListbox = securityUserListbox;
    }
}
