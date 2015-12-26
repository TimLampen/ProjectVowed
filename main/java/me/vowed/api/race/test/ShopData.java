package me.vowed.api.race.test;

import me.vowed.api.player.PlayerWrapper;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.race.Race;
import me.vowed.api.race.RaceListener;
import me.vowed.api.trade.Trade;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by JPaul on 12/23/2015.
 */
public class ShopData extends JFrame
{
    JTable table;
    DefaultTableModel tableModel;

    public ShopData()
    {
        super("Shop");
        super.setSize(1000, 900);

        JPanel panel = new JPanel();

        panel.setLayout(null);

        tableModel = new DefaultTableModel()
        {
            public Class getColumnClass(int columnIndex)
            {
                return String.class;
            }

            public boolean isCellEditable(int row, int column)
            {
                switch (column)
                {
                    case 0:
                        return true;

                    default:
                        return false;
                }
            }
        };

        table = new JTable(tableModel);

        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(true);

        tableModel.addColumn("UUID");
        tableModel.addColumn("Current name");
        tableModel.addColumn("Name changes");

        table.getColumnModel().getColumn(1).setMaxWidth(200);
        table.getColumnModel().getColumn(1).setMinWidth(200);

        table.setRowHeight(table.getRowHeight() * 5);
        table.setDefaultRenderer(String.class, new MultiLineCellRenderer());

        table.getColumn("UUID").setCellRenderer(new ButtonRenderer());
        table.getColumn("UUID").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(40, 150, 900, 600);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JTextField searchField = new JTextField();
        searchField.setBounds(10, 50, 200, 25);

        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());

        table.setRowSorter(rowSorter);

        searchField.getDocument().addDocumentListener(new DocumentListener()
        {

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                String text = searchField.getText();

                if (text.trim().length() == 0)
                {
                    rowSorter.setRowFilter(null);
                } else
                {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String text = searchField.getText();

                if (text.trim().length() == 0)
                {
                    rowSorter.setRowFilter(null);
                } else
                {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });

        File nameList = new File("C:\\Users\\JPaul\\Desktop\\Server\\plugins\\VowedCore\\Transactions\\DATA\\names.dataList");

        List<String> namesofFile = new ArrayList<>();

        Scanner scanner;
        try
        {
            scanner = new Scanner(nameList);
            while (scanner.hasNextLine())
            {
                namesofFile.add(scanner.nextLine());
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        UUIDFetcher fetcher = new UUIDFetcher(namesofFile);

        Map<String, UUID> response = new HashMap<String, UUID>();

        try
        {
            response = fetcher.call();
        } catch (Exception e1)
        {
            Vowed.LOG.warning("Exception while fetching UUID's");
            e1.printStackTrace();
        }

        System.out.println(namesofFile);
        System.out.println(response);

        for (String name : response.keySet())
        {
            StringBuilder stringBuilder = new StringBuilder();
            for (String changes : TransactionUtil.sendNames(response.get(name).toString()))
            {
                stringBuilder.append(changes);
            }

            tableModel.addRow(new Object[]{response.get(name).toString(), name, stringBuilder.toString()});
        }

        JTextField playerName = new JTextField();
        JTextField UUID = new JTextField();


        JButton nameToUUID = new JButton();
        nameToUUID.setText("Name to UUID");

        nameToUUID.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String input = playerName.getText();

                UUIDFetcher fetcher = new UUIDFetcher(Collections.singletonList(input));

                Map<String, UUID> response = new HashMap<String, java.util.UUID>();

                try
                {
                    response = fetcher.call();
                } catch (Exception e1)
                {
                    Vowed.LOG.warning("Exception while fetching UUID's");
                    e1.printStackTrace();
                }

                if (response != null)
                {
                    if (!response.values().isEmpty())
                    {
                        for (UUID uuidFinder : response.values())
                        {
                            UUID.setText(uuidFinder.toString());
                            response.remove(input);
                        }
                    } else
                    {
                        UUID.setText("NULL");

                    }
                }
            }
        });

        playerName.setBounds(250, 50, 150, 25);
        UUID.setBounds(600, 50, 300, 25);
        nameToUUID.setBounds(425, 50, 140, 25);

        panel.add(scroll);
        panel.add(searchField);
        panel.add(playerName);
        panel.add(UUID);
        panel.add(nameToUUID);

        getContentPane().add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public ShopData(String name, String uuid)
    {
        super(name + " - " + uuid);

        tableModel = new DefaultTableModel()
        {
            public Class getColumnClass(int columnIndex)
            {
                return String.class;
            }

            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        JPanel panel = new JPanel();

        panel.setLayout(null);

        table = new JTable(tableModel);

        table.setRowSelectionAllowed(false);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tableModel.addColumn("Date");
        tableModel.addColumn("Transactions");

        table.getColumnModel().getColumn(0).setMinWidth(200);
        table.getColumnModel().getColumn(0).setMaxWidth(200);
        table.getColumnModel().getColumn(1).setMaxWidth(400);
        table.getColumnModel().getColumn(1).setMinWidth(400);

        table.setRowHeight(table.getRowHeight() * 15);
        table.setDefaultRenderer(String.class, new MultiLineCellRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(40, 150, 900, 600);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JTextField searchField = new JTextField();
        searchField.setBounds(10, 50, 200, 30);

        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());

        table.setRowSorter(rowSorter);

        searchField.getDocument().addDocumentListener(new DocumentListener()
        {

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                String text = searchField.getText();

                if (text.trim().length() == 0)
                {
                    rowSorter.setRowFilter(null);
                } else
                {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String text = searchField.getText();

                if (text.trim().length() == 0)
                {
                    rowSorter.setRowFilter(null);
                } else
                {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });

        JTextField playerName = new JTextField();
        JTextField UUID = new JTextField();

        JButton nameToUUID = new JButton();
        nameToUUID.setText("Name to UUID");

        nameToUUID.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String input = playerName.getText();

                UUIDFetcher fetcher = new UUIDFetcher(Collections.singletonList(input));

                Map<String, UUID> response = new HashMap<String, java.util.UUID>();

                try
                {
                    response = fetcher.call();
                } catch (Exception e1)
                {
                    Vowed.LOG.warning("Exception while fetching UUID's");
                    e1.printStackTrace();
                }

                if (response != null)
                {
                    if (!response.values().isEmpty())
                    {
                        for (UUID uuidFinder : response.values())
                        {
                            UUID.setText(uuidFinder.toString());
                            response.remove(input);
                        }
                    } else
                    {
                        UUID.setText("NULL");

                    }
                }
            }
        });

        playerName.setBounds(250, 50, 150, 25);
        UUID.setBounds(600, 50, 300, 25);
        nameToUUID.setBounds(425, 50, 140, 25);

        panel.add(scroll);
        panel.add(searchField);
        panel.add(playerName);
        panel.add(UUID);
        panel.add(nameToUUID);

        getContentPane().add(panel);
        setSize(1000, 900);
        setVisible(true);
    }

    public JTable getTable()
    {
        return table;
    }

    public DefaultTableModel getTableModel()
    {
        return tableModel;
    }

    public boolean contains(Object[] objects)
    {
        String currentEntry = "";
        for (Object object : objects)
        {
            currentEntry = object.toString();
        }

        int rowCount = getTable().getRowCount();
        int columnCount = getTable().getColumnCount();

        for (int rCounter = 0; rCounter < rowCount; rCounter++)
        {
            for (int cCounter = 0; cCounter < columnCount; cCounter++)
            {
                String rowEntry = getTable().getValueAt(rCounter, cCounter).toString();
                if (rowEntry.equalsIgnoreCase(currentEntry))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean contains(Object object)
    {
        return contains(new Object[]{object});
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer
    {

        public ButtonRenderer()
        {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column)
        {
            if (isSelected)
            {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else
            {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor
    {
        protected JButton button;

        private String label;

        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox)
        {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column)
        {
            if (isSelected)
            {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else
            {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue()
        {
            if (isPushed)
            {
                UUID playerUUID = UUID.fromString(button.getText());

                ShopData frame = new ShopData(TransactionUtil.getName(playerUUID.toString()), playerUUID.toString());

                try
                {

                    for (List<String> lines : TransactionUtil.getTransactions(playerUUID))
                    {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < lines.size() - 2; i++)
                        {
                            stringBuilder.append(lines.get(i));
                        }

                        String date = lines.get(lines.size() - 2);

                        int monthINT = Integer.parseInt(date.substring(3, 5)) - 1;
                        int dayINTNumber = Integer.parseInt(date.substring(0, 2));
                        int dayINTName = Integer.parseInt(date.substring(0, 2)) - 1;
                        int yearINT = Integer.parseInt(date.substring(6, 10));

                        Date realDate = new Date(yearINT, monthINT, dayINTName);

                        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(realDate);
                        String month = new SimpleDateFormat("MMMM", Locale.ENGLISH).format(realDate);

                        String time = date.substring(11, date.length()).replaceAll("-", ":");

                        StringBuilder timeBuilder = new StringBuilder(time);
                        timeBuilder.replace(8, 9, " ");

                        String newTime = timeBuilder.toString();

                        String namedDate = month + " " + dayINTNumber + getDateSuffix(dayINTName) + " " + yearINT + " - " + day + "\n@ " + newTime + " - PST\n\n" + date.substring(0, 10).replaceAll("-", "/");

                        frame.getTableModel().addRow(new Object[]{namedDate, stringBuilder.toString()});
                    }

                } catch (IOException e)
                {
                    e.printStackTrace();
                }

                frame.setVisible(true);
            }
            isPushed = false;
            return label;
        }

        public String getDateSuffix( int day)
        {
            switch (day)
            {
                case 1 - 1:
                case 21 - 1:
                case 31 - 1:
                    return ("st");

                case 2 - 1:
                case 22 - 1:
                    return ("nd");

                case 3 - 1:
                case 23 - 1:
                    return ("rd");

                default:
                    return ("th");
            }
        }

        public boolean stopCellEditing()
        {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped()
        {
            super.fireEditingStopped();
        }
    }

    class MultiLineCellRenderer extends JTextArea implements TableCellRenderer
    {

        public MultiLineCellRenderer()
        {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column)
        {
            if (isSelected)
            {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else
            {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setFont(table.getFont());
            if (hasFocus)
            {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
                if (table.isCellEditable(row, column))
                {
                    setForeground(UIManager.getColor("Table.focusCellForeground"));
                    setBackground(UIManager.getColor("Table.focusCellBackground"));
                }
            } else
            {
                setBorder(new EmptyBorder(1, 2, 1, 2));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    public static void main(String[] args)
    {
        new ShopData();
    }
}