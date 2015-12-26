package me.vowed.api.race;

import me.vowed.api.plugin.Vowed;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by JPaul on 12/23/2015.
 */
public class Tutorial extends JPanel
{
    JTable table;
    DefaultTableModel tableModel;

    public Tutorial()
    {
        String[] columns = {"UUID", "Last username", "Value"};

        tableModel = new DefaultTableModel(null, columns);

        table = new JTable(tableModel)
        {
            public boolean isCellEditable(int data, int columns)
            {
                return false;
            }

            public Component prepareRenderer(TableCellRenderer r, int data, int columns)
            {

                return super.prepareRenderer(r, data, columns);
            }
        };


        table.setPreferredScrollableViewportSize(new Dimension(450, 63));
        table.setFillsViewportHeight(true);

        JScrollPane jps = new JScrollPane(table);
        add(jps);

    }

    public boolean contains(Object[] objects)
    {
        int rowCount = this.table.getRowCount();
        int columnCount = this.table.getColumnCount();

        String currentEntry = "";

        for (Object object : objects)
        {
            currentEntry = object.toString();
        }

        for (int rowCounter = 0; rowCounter < rowCount; rowCounter++)
        {
            for (int columnCounter = 0; columnCounter < columnCount; columnCounter++)
            {
                String rowEntry = this.tableModel.getValueAt(rowCounter, columnCounter).toString();

                if (rowEntry.equalsIgnoreCase(currentEntry))
                {
                    Vowed.LOG.debug("wtf");
                    return true;
                }
            }
        }

        return false;
    }

    public boolean contains(Object object)
    {
        return contains(new Object[] {object});
    }

    public DefaultTableModel getTableModel()
    {
        return tableModel;
    }

    public JTable getTable()
    {
        return table;
    }

    public JTableButtonRenderer getButton()
    {
        return new JTableButtonRenderer();
    }

    private static class JTableButtonRenderer implements TableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton)value;
            return button;
        }
    }
}
