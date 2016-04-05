package org.zkfuse.service.i18n.impl;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.zkfuse.dao.i18n.ModuleDao;
import org.zkfuse.model.i18n.Module;
import org.zkfuse.service.i18n.ModuleService;
import org.zkfuse.service.impl.GenericService;

/**
 *
 * @author Samuel Huang
 */
@Service
public class ModuleServiceImpl extends GenericService<Module, Long> implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;
    
    public ModuleServiceImpl() {
        super(Module.class);
    }
    
    public Module getModuleByName(String name) {
        return moduleDao.getModuleByName(name);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteModule(String name) throws EntityNotFoundException {
        moduleDao.deleteModule(name);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteModule(Module module) {
        moduleDao.deleteModule(module);
    }

}
