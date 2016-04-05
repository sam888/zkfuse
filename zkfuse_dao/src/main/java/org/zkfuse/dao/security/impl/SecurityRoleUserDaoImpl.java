package org.zkfuse.dao.security.impl;

import org.springframework.stereotype.Service;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.dao.security.SecurityRoleUserDao;
import org.zkfuse.model.security.SecurityRoleUser;
import org.zkfuse.model.security.SecurityRoleUserPK;

/**
 * Created with IntelliJ IDEA.
 * Author: Sam
 */
@Service
public class SecurityRoleUserDaoImpl extends BasicDao<SecurityRoleUser, SecurityRoleUserPK> implements SecurityRoleUserDao {

    public SecurityRoleUserDaoImpl() {
        super( SecurityRoleUser.class );
    }

    @Override
    public void delete(Long userId, Long roleId) {
        Object[] paramsArray = new Object[] { userId, roleId };
        executeSingleNamedQuery( SecurityRoleUser.NAMED_QUERY_DELETE_BY_USER_ID_AND_ROLE_ID, paramsArray );
    }
}
