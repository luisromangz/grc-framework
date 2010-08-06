package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.EntityFieldsProperties;
import com.greenriver.commons.data.fieldProperties.FieldDeactivationCondition;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author luis
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(length = 255)
@EntityFieldsProperties(appendSuperClassFields = true)
public abstract class TextRepeaterSubtemplate<T extends TemplateReplacement, K extends Collection<?>>
            extends RepeaterSubtemplate<T, K>
        implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @FieldProperties(label = "Texto que se repite", type = FieldType.RICHTEXT)
    @Column(length = 10240)
    private String body;
    @FieldProperties(label = "Número de repeticiones horizontales", type = FieldType.NUMBER, minValue = 1)
    private int horizontalRepetitions = 1;
    @FieldProperties(label = "Bordes de la tabla", type = FieldType.SELECTION, enumLabelMethod = "getLabel",
    deactivationConditions = {
        @FieldDeactivationCondition(triggerField = "horizontalRepetitions", equals = "1", newValue = "'NONE'")})
    @Enumerated(EnumType.STRING)
    private BorderType borderType = BorderType.NONE;
    @FieldProperties(label = "Página nueva tras el texto", type = FieldType.BOOLEAN)
    private boolean newPageAfterText = false;
    @FieldProperties(label = "Añadir nueva línea tras el texto", type = FieldType.BOOLEAN,
    deactivationConditions = {
        @FieldDeactivationCondition(triggerField = "newPageAfterText", equals = "'on'")
    })
    private boolean newLineAfterText = true;

    @Override
    protected String fillTemplatesInternal(List<Map<T, String>> replacements) {
        List<String> texts = new ArrayList<String>();

        for (Map<T, String> elementReplacements : replacements) {
            String elementResult = body;

            for (T replacement : elementReplacements.keySet()) {
                elementResult = elementResult.replace(
                        replacement.getDecoratedPlaceholder(), TemplatingUtils.formatTemplateReplacement(
                        replacement,
                        elementReplacements.get(replacement)));
            }


            texts.add(elementResult);
        }

        if (horizontalRepetitions > 1) {
            List<String> tables = new ArrayList<String>();

            int counter = 0;
            List<String> currentTableCells = new ArrayList<String>();

            
            for (String text : texts) {
                currentTableCells.add(text);
                counter++;

                if (counter == horizontalRepetitions) {
                    // We reached the end of a row, so we format a table...
                    String formattedTable = String.format("<table style=\"%s;\"><tbody><tr>",
                            this.borderType.getTableStyle());

                    for (String cell : currentTableCells) {
                        formattedTable += String.format(
                                "<td style=\"%s;text-align:left\">%s</td>",
                                this.borderType.getCellStyle(),
                                cell);
                    }

                    formattedTable += "</tr></tbody></table>";

                    tables.add(formattedTable);

                    // And reset the row.
                    currentTableCells = new ArrayList<String>();
                    counter = 0;
                }
            }

            if (counter > 0) {
                // We have an extra row with cellNumber < horizontalrepetitions.
                String tableWidth = 100.0* currentTableCells.size()/horizontalRepetitions+"%";
                String formattedTable = String.format("<table style=\"%s;width:%s\"><tbody><tr>",
                        this.borderType.getTableStyle(),
                        tableWidth);

                for (String cell : currentTableCells) {
                    formattedTable += String.format(
                            "<td style=\"%s;text-align:left\">%s</td>",
                            this.borderType.getCellStyle(),
                            cell);
                }

                formattedTable += "</tr></tbody></table>";

                tables.add(formattedTable);
            }

            texts = tables;
        }




        String glue = "";
        if (newPageAfterText) {
            glue = "<div style=\"page-break-after:always\"><!--Non empty--></div>";
        } else if (newLineAfterText) {
            glue = "<br/>";
        }

        return Strings.join(texts, glue);
    }

    @Override
    public void copyTo(Subtemplate copyTarget) {
        super.copyTo(copyTarget);

        TextRepeaterSubtemplate templateTarget = ((TextRepeaterSubtemplate) copyTarget);
        templateTarget.body = this.body;
        templateTarget.newLineAfterText = this.newLineAfterText;
        templateTarget.newPageAfterText = this.newPageAfterText;
        templateTarget.horizontalRepetitions = horizontalRepetitions;
        templateTarget.borderType = borderType;

    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the newLineAfterText
     */
    public boolean getNewLineAfterText() {
        return newLineAfterText;
    }

    /**
     * @param newLineAfterText the newLineAfterText to set
     */
    public void setNewLineAfterText(boolean newLineAfterText) {
        this.newLineAfterText = newLineAfterText;
    }

    /**
     * @return the newPageAfterText
     */
    public boolean getNewPageAfterText() {
        return newPageAfterText;
    }

    /**
     * @param newPageAfterText the newPageAfterText to set
     */
    public void setNewPageAfterText(boolean newPageAfterText) {
        this.newPageAfterText = newPageAfterText;
    }

    /**
     * @return the horizontalRepetitions
     */
    public int getHorizontalRepetitions() {
        return horizontalRepetitions;
    }

    /**
     * @param horizontalRepetitions the horizontalRepetitions to set
     */
    public void setHorizontalRepetitions(int horizontalRepetitions) {
        this.horizontalRepetitions = horizontalRepetitions;
    }

    /**
     * @return the borderType
     */
    public BorderType getBorderType() {
        return borderType;
    }

    /**
     * @param borderType the borderType to set
     */
    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }
    // </editor-fold>
}
