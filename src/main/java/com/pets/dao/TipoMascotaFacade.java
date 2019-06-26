/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pets.dao;

import com.pets.entidades.TipoMascota;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author npena
 */
@Stateless
public class TipoMascotaFacade extends AbstractFacade<TipoMascota> {

    @PersistenceContext(unitName = "org.primefaces_barcelona_war_1.0.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoMascotaFacade() {
        super(TipoMascota.class);
    }
    
}
