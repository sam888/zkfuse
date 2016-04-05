package org.zkfuse.service.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.dao.i18n.KeyValueDao;
import org.zkfuse.dao.security.SecurityPermissionDao;
import org.zkfuse.model.i18n.KeyValue;
import org.zkfuse.model.security.SecurityPermission;
import org.zkfuse.service.impl.GenericService;
import org.zkfuse.service.security.SecurityPermissionService;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 * Date: 13/07/13
 */
@Service
public class SecurityPermissionServiceImpl extends GenericService<SecurityPermission, Long> implements SecurityPermissionService {

    @Autowired
    private SecurityPermissionDao securityPermissionDao;

    public SecurityPermissionServiceImpl() {
        super(SecurityPermission.class);
    }

    @Override
    public SecurityPermission getSecurityPermissionByPermissionName(String permissionName) {
        return securityPermissionDao.getSecurityPermissionByPermissionName( permissionName );
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteSecurityPermissionByPermissionName(String permissionName) {
        securityPermissionDao.deleteSecurityPermissionByPermissionName( permissionName );
    }
}
