/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pets.converter;

import com.pets.controladores.TipoMascotaController;
import com.pets.entidades.TipoMascota;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author npena
 */
@FacesConverter(value = "tipoMascotaConverter")
public class TipoMascotaConverter  implements Converter {
    
    @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TipoMascotaController controller = (TipoMascotaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tipoMascotaController");
            return controller.getTipoMascota(getKey(value));
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
            if (object instanceof TipoMascota) {
                TipoMascota o = (TipoMascota) object;
                return getStringKey(o.getIdTipoMascota());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TipoMascota.class.getName());
            }
        }
}
