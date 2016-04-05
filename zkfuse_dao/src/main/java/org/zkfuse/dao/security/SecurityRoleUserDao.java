package org.zkfuse.dao.security;

import org.zkfuse.dao.IBasicDao;
import org.zkfuse.model.security.SecurityRoleUser;
import org.zkfuse.model.security.SecurityRoleUserPK;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 */
public interface SecurityRoleUserDao extends IBasicDao<SecurityRoleUser, SecurityRoleUserPK> {

    /**
     * Delete a record form the link table security_role_user by user Id and role Id
     *
     * @param userId
     * @param roleId
     */
    void delete(Long userId, Long roleId);
}
