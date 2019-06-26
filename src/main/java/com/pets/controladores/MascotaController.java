package com.pets.controladores;

import com.pets.entidades.Mascota;
import com.pets.controladores.util.JsfUtil;
import com.pets.controladores.util.PaginationHelper;
import com.pets.dao.MascotaFacade;
import com.pets.entidades.TipoMascota;
import java.io.IOException;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import static javax.faces.context.FacesContext.getCurrentInstance;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("mascotaController")
@SessionScoped
public class MascotaController implements Serializable {

    private Mascota current;
    private DataModel items = null;
    @EJB
    private com.pets.dao.MascotaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    private TipoMascota mascotaSeleccionada;
    private List<Mascota> lstMascotas;

    String nombreMascota;
    String edad;
    String raza;
    //boolean mostrarTabla = false;
    boolean mostrarTabla;

    public MascotaController() {
    }

    public List<Mascota> getLstMascotas() {
        if (lstMascotas == null) {
            lstMascotas = ejbFacade.findAll();
        }
        return lstMascotas;
    }

    public void setLstMascotas(List<Mascota> lstMascotas) {
        this.lstMascotas = lstMascotas;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public TipoMascota getMascotaSeleccionada() {
        return mascotaSeleccionada;
    }

    public void setMascotaSeleccionada(TipoMascota mascotaSeleccionada) {
        this.mascotaSeleccionada = mascotaSeleccionada;
    }

    public boolean isMostrarTabla() {
        return mostrarTabla;
    }

    public void setMostrarTabla(boolean mostrarTabla) {
        this.mostrarTabla = mostrarTabla;
    }
    

    public Mascota getSelected() {
        if (current == null) {
            current = new Mascota();
            selectedItemIndex = -1;
        }
        return current;
    }

    private MascotaFacade getFacade() {
        return ejbFacade;
    }

    public void guardarMascota() {
        System.out.println("nombre--->" + nombreMascota);
        System.out.println("tipo mascota--->" + mascotaSeleccionada.getIdTipoMascota());
        System.out.println("Edad-->" + edad);
        System.out.println("raza-->" + raza);

        String idTipoMascota = "";
        boolean fisrtTime = true;

//        if (!mascotaSeleccionada.isEmpty()) {
//            for (TipoMascota objetTipoMas : mascotaSeleccionada) {
//                if (fisrtTime) {
//                    idTipoMascota = objetTipoMas.getIdTipoMascota().toString();
//                    fisrtTime = false;
//                } else {
//                    idTipoMascota = idTipoMascota + "," + objetTipoMas.getIdTipoMascota();
//                }
//            }
        Mascota objetoMascota = new Mascota();
        objetoMascota.setNombreMascota(nombreMascota);
        objetoMascota.setEdad(edad);
        objetoMascota.setRaza(raza);
        objetoMascota.setIdTipoMascota(mascotaSeleccionada);
        ejbFacade.create(objetoMascota);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mascota Registrada con Exito!", ""));
        
        mostrarTabla = true;


    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Mascota) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Mascota();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MascotaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Mascota) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MascotaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Mascota) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MascotaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Mascota getMascota(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Mascota.class)
    public static class MascotaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MascotaController controller = (MascotaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mascotaController");
            return controller.getMascota(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Mascota) {
                Mascota o = (Mascota) object;
                return getStringKey(o.getIdMascota());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Mascota.class.getName());
            }
        }

    }

}
