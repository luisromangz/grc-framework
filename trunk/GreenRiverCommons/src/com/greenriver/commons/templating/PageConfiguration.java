/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.templating;

import com.greenriver.commons.Copieable;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author luis
 */
@Entity
public class PageConfiguration implements Serializable, Copieable<PageConfiguration> {
    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static long serialVersionUID = 1L;

    private static final float INCH_IN_MM = 245;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // All margins are measured in mm.
    @FieldProperties(label = "Margen superior", minValue = 0, type = FieldType.DECIMAL, unit = "cm")
    private float topMargin = 2;
    @FieldProperties(label = "Margen inferior", minValue = 0, type = FieldType.DECIMAL, unit = "cm")
    private float bottomMargin = 2;
    @FieldProperties(label = "Margen izquierdo", minValue = 0, type = FieldType.DECIMAL, unit = "cm")
    private float leftMargin = 2;
    @FieldProperties(label = "Margen derecho", minValue = 0, type = FieldType.DECIMAL, unit = "cm")
    private float rightMargin = 2;
    @FieldProperties(label = "Anchura del papel", minValue = 0, type = FieldType.DECIMAL, unit = "mm")
    private float pageWidth = 210;
    @FieldProperties(label = "Altura del papel", minValue = 0, type = FieldType.DECIMAL, unit = "mm")
    private float pageHeight = 297;
    @FieldProperties(label = "Papel apaisado", type = FieldType.BOOLEAN)
    private boolean landscape;
    // </editor-fold>

    public Map<String,String> getCustomJsPrintConfiguration() {
        Map<String,String> configuration = new HashMap<String,String>();

        configuration.put("print_margin_top", String.valueOf(topMargin*10/INCH_IN_MM));
        configuration.put("print_margin_bottom",String.valueOf(bottomMargin*10/INCH_IN_MM));
        configuration.put("print_margin_left", String.valueOf(leftMargin*10/INCH_IN_MM));
        configuration.put("print_margin_right", String.valueOf(rightMargin*10/INCH_IN_MM));

        configuration.put("print_orientation", String.valueOf(landscape?1:0));
        configuration.put("print_paper_size_unit",String.valueOf(1));//in mm, please.

        configuration.put("print_paper_width", String.valueOf(pageWidth));
        configuration.put("print_paper_height", String.valueOf(pageHeight));
        
        return configuration;
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void copyTo(PageConfiguration copyTarget) {
        copyTarget.setBottomMargin(bottomMargin);
        copyTarget.setLandscape(landscape);
        copyTarget.setLeftMargin(leftMargin);
        copyTarget.setRightMargin(rightMargin);
        copyTarget.setPageHeight(pageHeight);
        copyTarget.setTopMargin(topMargin);
        copyTarget.setPageWidth(pageWidth);
    }



    // <editor-fold defaultstate="collapsed" desc="Auto-generated methods">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PageConfiguration)) {
            return false;
        }
        PageConfiguration other = (PageConfiguration) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.greenriver.commons.templating.PageConfiguration[id=" + getId() + "]";
    }

    /**
     * @return the topMargin
     */
    public float getTopMargin() {
        return topMargin;
    }

    /**
     * @param topMargin the topMargin to set
     */
    public void setTopMargin(float topMargin) {
        this.topMargin = topMargin;
    }

    /**
     * @return the bottomMargin
     */
    public float getBottomMargin() {
        return bottomMargin;
    }

    /**
     * @param bottomMargin the bottomMargin to set
     */
    public void setBottomMargin(float bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    /**
     * @return the leftMargin
     */
    public float getLeftMargin() {
        return leftMargin;
    }

    /**
     * @param leftMargin the leftMargin to set
     */
    public void setLeftMargin(float leftMargin) {
        this.leftMargin = leftMargin;
    }

    /**
     * @return the rightMargin
     */
    public float getRightMargin() {
        return rightMargin;
    }

    /**
     * @param rightMargin the rightMargin to set
     */
    public void setRightMargin(float rightMargin) {
        this.rightMargin = rightMargin;
    }

    /**
     * @return the pageWidth
     */
    public float getPageWidth() {
        return pageWidth;
    }

    /**
     * @param pageWidth the pageWidth to set
     */
    public void setPageWidth(float pageWidth) {
        this.pageWidth = pageWidth;
    }

    /**
     * @return the pageHeight
     */
    public float getPageHeight() {
        return pageHeight;
    }

    /**
     * @param pageHeight the pageHeight to set
     */
    public void setPageHeight(float pageHeight) {
        this.pageHeight = pageHeight;
    }

    /**
     * @return the landscape
     */
    public boolean isLandscape() {
        return landscape;
    }

    /**
     * @param landscape the landscape to set
     */
    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }
    // </editor-fold>
}
