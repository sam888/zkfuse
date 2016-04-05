package org.zkfuse.service.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.dao.security.SecurityRoleUserDao;
import org.zkfuse.dao.security.SecurityUserDao;
import org.zkfuse.model.security.SecurityRole;
import org.zkfuse.model.security.SecurityRoleUser;
import org.zkfuse.model.security.SecurityRoleUserPK;
import org.zkfuse.model.security.SecurityUser;
import org.zkfuse.service.impl.GenericService;
import org.zkfuse.service.security.SecurityUserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 * Date: 13/07/13
 */
@Service
public class SecurityUserServiceImpl extends GenericService<SecurityUser, Long> implements SecurityUserService{

    @Autowired
    private SecurityUserDao securityUserDao;

    @Autowired
    private SecurityRoleUserDao securityRoleUserDao;


    public SecurityUserServiceImpl() {
        super(SecurityUser.class);
    }

    @Override
    public SecurityUser getUserByLoginName(String loginName) {
        return securityUserDao.getUserByLoginName( loginName );
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteUserByLoginName(String loginName) {
        securityUserDao.deleteUserByLoginName( loginName );
    }

    /**
     * Update the list of roles assigned to the user to the DB.
     *
     * @param assignedSecurityRoleList  a list of roles to be assigned to the 'securityUser' argument
     * @param securityUser              the SecurityUser object to be assigned the list of roles from 'assignedSecurityRoleList' argument
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateAssignedRoles(List<SecurityRole> assignedSecurityRoleList, SecurityUser securityUser) {

        if ( securityUser == null ) {
            return;
        }

        // remove roles
        List<SecurityRoleUser> existingSecurityRoleUserList = new ArrayList<SecurityRoleUser>( securityUser.getSecurityRoleUsers() );
        for ( SecurityRoleUser securityRoleUser: existingSecurityRoleUserList ) {
            SecurityRole securityRole = securityRoleUser.getSecurityRole();
            if ( !assignedSecurityRoleList.contains( securityRole ) ) {

                SecurityRoleUserPK pk = securityRoleUser.getId();
                securityRoleUserDao.delete( pk.getSecurityUser().getUserId(), pk.getSecurityRole().getRoleId() );

                securityUser.removeSecurityRoleUser( securityRoleUser );
            }
        }

        assignedSecurityRoleList.removeAll( securityUser.getSecurityRoles() ); // what remains will be new role(s)

        // add new roles
        for ( SecurityRole securityRole: assignedSecurityRoleList ) {

            SecurityRoleUser securityRoleUser = new SecurityRoleUser();
            securityRoleUser.setSecurityRole(securityRole);
            securityRoleUser.setSecurityUser( securityUser );
            securityRoleUser.setCreatedDate(new Date());
            securityRoleUser.setCreatedBy( "DEMO_USER" );

            securityRoleUserDao.persist( securityRoleUser );

            securityUser.addSecurityRoleUser( securityRoleUser );
            // securityRole.addSecurityRoleUser( securityRoleUser );
        }

        // merge(..) not used as it is problematic even though unit tests showed it can be used to add or delete SecurityRoleUser objects
        // merge( securityUser );
    }

}
