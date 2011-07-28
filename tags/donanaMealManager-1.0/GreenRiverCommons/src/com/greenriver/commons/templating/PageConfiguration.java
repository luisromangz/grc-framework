

package com.greenriver.commons.templating;

import com.greenriver.commons.Copieable;
import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
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

    private static final float INCH_IN_MM = 25.4f;

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
    @FieldProperties(label = "Orientación del papel", type = FieldType.SELECTION,
    possibleValues={"false","true"},possibleValueLabels={"Vertical","Apaisado"}, getterPrefix="is")
    private boolean landscape;

    @FieldProperties(label="Pie de página (izquierda)", required=false)
    private String footerLeft;
    @FieldProperties(label="Pie de página (centro)", required=false)
    private String footerCenter;
    @FieldProperties(label="Pie de página (derecha)", required=false)
    private String footerRight="&PT";
    // </editor-fold>

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

        copyTarget.setFooterCenter(footerCenter);
        copyTarget.setFooterLeft(footerLeft);
        copyTarget.setFooterRight(footerRight);
    }

    public <T extends TemplateReplacement> PageConfiguration fillTemplate(Map<T, String> replacements) {
        PageConfiguration pageConfiguration = new PageConfiguration();
        this.copyTo(pageConfiguration);

        for(T replacement : replacements.keySet()) {
            pageConfiguration.setFooterLeft(pageConfiguration.getFooterLeft().replace(
                    replacement.getDecoratedPlaceholder(),
                    replacements.get(replacement)));

            pageConfiguration.setFooterCenter(pageConfiguration.getFooterCenter().replace(
                    replacement.getDecoratedPlaceholder(),
                    replacements.get(replacement)));

            pageConfiguration.setFooterRight(pageConfiguration.getFooterRight().replace(
                    replacement.getDecoratedPlaceholder(),
                    replacements.get(replacement)));
        }

        pageConfiguration.setFooterLeft(pageConfiguration.getFooterLeft());
        pageConfiguration.setFooterCenter(pageConfiguration.getFooterCenter());
        pageConfiguration.setFooterRight(pageConfiguration.getFooterRight());

        return pageConfiguration;
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

    /**
     * @return the footerLeft
     */
    public String getFooterLeft() {
        return footerLeft==null?"":footerLeft;
    }

    /**
     * @param footerLeft the footerLeft to set
     */
    public void setFooterLeft(String footerLeft) {
        this.footerLeft = footerLeft;
    }

    /**
     * @return the footerCenter
     */
    public String getFooterCenter() {
        return footerCenter==null?"":footerCenter;
    }

    /**
     * @param footerCenter the footerCenter to set
     */
    public void setFooterCenter(String footerCenter) {
        this.footerCenter = footerCenter;
    }

    /**
     * @return the footerRight
     */
    public String getFooterRight() {
        return footerRight==null?"":footerRight;
    }

    /**
     * @param footerRight the footerRight to set
     */
    public void setFooterRight(String footerRight) {
        this.footerRight = footerRight;
    }

    public boolean hasFooter() {
        return !(Strings.isNullOrEmpty(footerLeft) && Strings.isNullOrEmpty(footerCenter)
                && Strings.isNullOrEmpty(footerRight));
    }
    // </editor-fold>
}