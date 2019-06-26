/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pets.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author npena
 */
@Entity
@Table(name = "tipo_mascota")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoMascota.findAll", query = "SELECT t FROM TipoMascota t")
    , @NamedQuery(name = "TipoMascota.findByIdTipoMascota", query = "SELECT t FROM TipoMascota t WHERE t.idTipoMascota = :idTipoMascota")
    , @NamedQuery(name = "TipoMascota.findByDescripcion", query = "SELECT t FROM TipoMascota t WHERE t.descripcion = :descripcion")})
public class TipoMascota implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_mascota")
    private Integer idTipoMascota;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoMascota")
    private List<Mascota> mascotaList;

    public TipoMascota() {
    }

    public TipoMascota(Integer idTipoMascota) {
        this.idTipoMascota = idTipoMascota;
    }

    public TipoMascota(Integer idTipoMascota, String descripcion) {
        this.idTipoMascota = idTipoMascota;
        this.descripcion = descripcion;
    }

    public Integer getIdTipoMascota() {
        return idTipoMascota;
    }

    public void setIdTipoMascota(Integer idTipoMascota) {
        this.idTipoMascota = idTipoMascota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Mascota> getMascotaList() {
        return mascotaList;
    }

    public void setMascotaList(List<Mascota> mascotaList) {
        this.mascotaList = mascotaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoMascota != null ? idTipoMascota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoMascota)) {
            return false;
        }
        TipoMascota other = (TipoMascota) object;
        if ((this.idTipoMascota == null && other.idTipoMascota != null) || (this.idTipoMascota != null && !this.idTipoMascota.equals(other.idTipoMascota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pets.entidades.TipoMascota[ idTipoMascota=" + idTipoMascota + " ]";
    }
    
}
