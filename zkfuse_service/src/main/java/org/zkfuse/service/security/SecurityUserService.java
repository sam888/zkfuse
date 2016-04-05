package org.zkfuse.service.security;

import org.zkfuse.model.security.SecurityRole;
import org.zkfuse.model.security.SecurityUser;
import org.zkfuse.service.IGenericService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 * Date: 13/07/13
 */
public interface SecurityUserService extends IGenericService<SecurityUser, Long> {

    SecurityUser getUserByLoginName(String loginName);

    void deleteUserByLoginName(String loginName);

    void updateAssignedRoles(List<SecurityRole> assignedSecurityRoleList, SecurityUser securityUser);
}

