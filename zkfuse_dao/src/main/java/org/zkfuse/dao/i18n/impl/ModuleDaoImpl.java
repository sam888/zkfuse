package org.zkfuse.dao.i18n.impl;

import org.springframework.stereotype.Service;
import org.zkfuse.dao.i18n.ModuleDao;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.model.i18n.Module;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

/**
 *
 * @author Samuel Huang
 */
@Service
public class ModuleDaoImpl extends BasicDao<Module, Long> implements ModuleDao {

    /** Constructor that sets the entity to Module.class */
    public ModuleDaoImpl() {
        super(Module.class);
    }
    
    public Module getModuleByName(String name) {
        Object obj = null;
        try {
            obj = findNamedQuerySingle( Module.NAMED_QUERY_GET_MODULE_BY_NAME, new String[] { name } );
        } catch (NoResultException ex) {
            return Module.EMPTY_MODULE;
        }
        return ( obj != null ? (Module)obj : Module.EMPTY_MODULE );
    }

    public void deleteModule(String name) {
        Module module = getModuleByName( name );
        remove(Module.class, module.getModuleId());   
    }

    public void deleteModule(Module module) throws EntityNotFoundException {
        remove(Module.class, module.getModuleId());  
    }

}
