package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.WidgetAction;
import com.greenriver.commons.data.fieldProperties.WidgetProps;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
import java.util.ArrayList;
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
public abstract class TextRepeaterSubtemplate<T extends TemplateReplacement, K>
        extends RepeaterSubtemplate<T, K>
        implements Serializable, Subtemplateable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @WidgetProps(label = "Texto que se repite", type = FieldType.RICHTEXT)
    @Column(length = 10240)
    private String body;
    @WidgetProps(label = "Tamaño de fila", type = FieldType.NUMBER, minValue = 1)
    private int rowSize = 1;
    @WidgetProps(label = "Bordes de la tabla", type = FieldType.SELECTION, externalValues=false,enumLabelMethod = "getLabel")
    @WidgetAction(triggerField = "horizontalRepetitions", triggerValue = "1", newValue = "'NONE'", deactivate=true)
    @Enumerated(EnumType.STRING)
    private BorderType borderType = BorderType.NONE;
    @WidgetProps(label = "Página nueva tras el texto", type = FieldType.CHECKBOX)
    private boolean newPageAfterText = false;
    @WidgetProps(label = "Añadir nueva línea tras el texto", type = FieldType.CHECKBOX)        
    @WidgetAction(triggerField = "newPageAfterText", triggerValue = "'on'", deactivate=true)
    private boolean newLineAfterText = true;

    @Override
    protected String fillTemplatesInternal(List<Map<T, String>> replacements) {
        
        // We create the text inside each repetition.
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

        if (rowSize > 1) {
            List<String> tables = new ArrayList<String>();

            int counter = 0;
            List<String> currentTableCells = new ArrayList<String>();
            
            int rows = (int) Math.ceil((texts.size()+0.0)/rowSize);
            int rowIndex = 0;
            for (String text : texts) {
                currentTableCells.add(text);
                counter++;                

                if (counter == rowSize) {
                    // We reached the end of a row, so we create a table...
                    
                    String rowClass="";
                    if(rowIndex==0) {
                        rowClass="firstRow";
                    } else if(rowIndex == rows-1) {
                        rowClass="lastRow";
                    }
                    String formattedTable = String.format(
                            "<table class=\"%s\" style=\"%s;\"><tbody><tr>",                            
                            rowClass,                            
                            this.borderType.getTableStyle());

                    int cellIndex =0;
                    for (String cell : currentTableCells) {
                        
                        String cellClass = rowClass;
                        if(cellIndex ==0) {
                            cellClass+=" firstCell";
                        } else if(cellIndex == rowSize-1) {
                            cellClass+=" lastCell";
                        }
                        
                        formattedTable += String.format(
                                "<td class=\"repeaterCell %s\" style=\"text-align:left\">%s</td>",
                                cellClass,
                                cell);
                        
                        cellIndex ++;
                    }

                    formattedTable += "</tr></tbody></table>";

                    tables.add(formattedTable);

                    // And reset the row.
                    currentTableCells = new ArrayList<String>();
                    counter = 0;
                    rowIndex++;
                }
            }

            if (counter > 0) {
                // We have an extra row with cellNumber < rowSize.
                String tableWidth = 100.0 * currentTableCells.size() / rowSize + "%";
                String formattedTable = String.format("<table class=\"lastRow\" style=\"%s;width:%s\"><tbody><tr>",
                        this.borderType.getTableStyle(),
                        tableWidth);

                int cellIndex =0;
                for (String cell : currentTableCells) {
                    formattedTable += String.format(
                            "<td class=\"lastRow repeaterCell %s\" style=\"text-align:left\">%s</td>",
                            cellIndex==0?"firstCell":(cellIndex==rowSize-1?"lastCell":""),                            
                            cell);
                    cellIndex++;
                }

                formattedTable += "</tr></tbody></table>";

                tables.add(formattedTable);
            }

            texts = tables;
        }




        String glue = "";
        if (newPageAfterText) {
            glue = PrintingTemplate.PAGE_BREAK;
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
        templateTarget.rowSize = rowSize;
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
    public int getRowSize() {
        return rowSize;
    }

    /**
     * @param horizontalRepetitions the horizontalRepetitions to set
     */
    public void setRowSize(int horizontalRepetitions) {
        this.rowSize = horizontalRepetitions;
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
