package org.zkfuse.dao.security;

import org.zkfuse.dao.IBasicDao;
import org.zkfuse.model.security.SecurityUser;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 */
public interface SecurityUserDao extends IBasicDao<SecurityUser, Long> {

    SecurityUser getUserByLoginName(String loginName);

    void deleteUserByLoginName(String loginName);

}
