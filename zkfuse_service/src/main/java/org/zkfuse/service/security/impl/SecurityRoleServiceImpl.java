package org.zkfuse.service.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.dao.security.SecurityPermissionDao;
import org.zkfuse.dao.security.SecurityPermissionRoleDao;
import org.zkfuse.dao.security.SecurityRoleDao;
import org.zkfuse.dao.security.SecurityRoleUserDao;
import org.zkfuse.model.security.SecurityPermission;
import org.zkfuse.model.security.SecurityPermissionRole;
import org.zkfuse.model.security.SecurityPermissionRolePK;
import org.zkfuse.model.security.SecurityRole;
import org.zkfuse.service.impl.GenericService;
import org.zkfuse.service.security.SecurityRoleService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 * Date: 13/07/13
 */
@Service
public class SecurityRoleServiceImpl extends GenericService<SecurityRole, Long> implements SecurityRoleService {

    @Autowired
    private SecurityRoleDao securityRoleDao;

    @Autowired
    private SecurityPermissionRoleDao securityPermissionRoleDao;

    public SecurityRoleServiceImpl() {
        super(SecurityRole.class);
    }

    @Override
    public SecurityRole getRoleByRoleName(String roleName) {
        return securityRoleDao.getRoleByRoleName( roleName );
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteRoleByRoleName(String roleName) {
        securityRoleDao.deleteRoleByRoleName( roleName );
    }

    /**
     * Update the list of permissions assigned to the role to the DB.
     *
     * @param assignedSecurityPermissionList  a list of permissions to be assigned to the 'securityRole' argument
     * @param securityRole                    the SecurityRole object to be assigned the list of roles from 'assignedSecurityRoleList' argument
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateAssignedPermissions(List<SecurityPermission> assignedSecurityPermissionList, SecurityRole securityRole) {

        if ( securityRole == null ) {
            return;
        }

        // remove permissions
        List<SecurityPermissionRole> existingSecurityPermissionRoleList = new ArrayList<SecurityPermissionRole>( securityRole.getSecurityPermissionRoles() );
        for ( SecurityPermissionRole securityPermissionRole: existingSecurityPermissionRoleList ) {
            SecurityPermission securityPermission = securityPermissionRole.getSecurityPermission();
            if ( !assignedSecurityPermissionList.contains( securityPermission ) ) {

                SecurityPermissionRolePK pk = securityPermissionRole.getId();
                securityPermissionRoleDao.delete( pk.getSecurityRole().getRoleId(), pk.getSecurityPermission().getPermissionId() );

                securityRole.removeSecurityPermissionRole( securityPermissionRole );
            }
        }

        assignedSecurityPermissionList.removeAll( securityRole.getSecurityPermissions() ); // what remains will be new permission(s)

        // add new permission
        for ( SecurityPermission securityPermission: assignedSecurityPermissionList ) {

            SecurityPermissionRole securityPermissionRole = new SecurityPermissionRole();
            securityPermissionRole.setSecurityRole(securityRole);
            securityPermissionRole.setSecurityPermission(securityPermission);
            securityPermissionRole.setCreatedDate( new Date() );
            securityPermissionRole.setCreatedBy( "DEMO_USER" );

            securityPermissionRoleDao.persist( securityPermissionRole );

            securityRole.addSecurityPermissionRole( securityPermissionRole );
            securityPermission.addSecurityPermissionRole( securityPermissionRole );
        }

        // merge(..) not used as it is problematic even though unit tests showed it can be used to add or delete SecurityPermissionRole records
        // merge( securityRole );
    }
}
