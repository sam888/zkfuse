package org.zkfuse.dao.security.impl;

import org.springframework.stereotype.Service;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.dao.security.SecurityUserDao;
import org.zkfuse.model.security.SecurityUser;

import javax.persistence.NoResultException;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 */
@Service
public class SecurityUserDaoImpl extends BasicDao<SecurityUser, Long> implements SecurityUserDao {

    public SecurityUserDaoImpl() {
        super( SecurityUser.class );
    }

    @Override
    public void deleteUserByLoginName(String loginName) {
        SecurityUser securityUser = getUserByLoginName( loginName );
        if ( securityUser != SecurityUser.EMPTY_SECURITY_USER ) {
            remove( SecurityUser.class, securityUser.getUserId() );
        }
    }

    @Override
    public SecurityUser getUserByLoginName(String loginName) {

        Object obj = null;
        Object[] namedQueryData = new Object[] { loginName };

        try {
            obj = findNamedQuerySingle( SecurityUser.NAMED_QUERY_GET_USER_BY_LOGIN_NAME, namedQueryData );
        } catch (NoResultException ex) {
            return SecurityUser.EMPTY_SECURITY_USER;
        }

        return ( obj != null ? (SecurityUser)obj : SecurityUser.EMPTY_SECURITY_USER );
    }
}
