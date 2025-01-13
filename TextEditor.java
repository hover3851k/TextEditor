

/**
 * A simple text editor with basic functionality.
 *
 * @author Simhadri
 */



import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
public class TextEditor extends JFrame implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
    private JButton italicButton;
    private JButton boldButton;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu viewMenu;
    private JMenuItem newItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem exitItem;
    private JMenuItem saveAsItem;
    private JMenuItem cutItem;
    private JMenuItem copyItem;
    private JMenuItem printItem;
    private JMenuItem pasteItem;
    JComboBox<String> newFontBox;
    JScrollPane newScrollPane;
    JButton newColorButton ;
    JLabel label;
    JPanel panel;
    private JPanel controlPanel;
    private String currentMode = "dark";
    private HashMap<JTextArea, TextStyle> textAreaStyles = new HashMap<>();
    private class TextStyle {
        Font font;
        Color foreground;
        Color background;
        Color caretColor;

        public TextStyle(Font font, Color foreground, Color background, Color caretColor) {
            this.font = font;
            this.foreground = foreground;
            this.background = background;
            this.caretColor = caretColor;
        }
    }
    // Constructor for TextEditor
    public TextEditor() {
        // JFrame setuping
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Notepad");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
    
        // Initializing JTabbedPane
        tabbedPane = new JTabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);
    
        // Adding an initial tab
        JTextArea initialTextArea = new JTextArea();
        addNewTab("Untitled", initialTextArea);
        
    
        // Menu Bar setup
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File ");
        newItem = new JMenuItem("New ");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");
        saveAsItem=new JMenuItem("SaveAs");
        printItem=new JMenuItem("Print");
        // Adding action listeners for the menu items
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        printItem.addActionListener(this);
        exitItem.addActionListener(this);
    
        // Adding menu items to the File menu
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(printItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(exitItem);
        // Edit menu setup
        // -------------------------------------------
        editMenu = new JMenu("Edit ");
        cutItem = new JMenuItem("Cut ");
        pasteItem = new JMenuItem("Paste");
        copyItem = new JMenuItem("Copy");
        // Adding action listaners for the edit menu items
        cutItem.addActionListener(this);
        copyItem.addActionListener(this);
        pasteItem.addActionListener(this);
    
        // Adding edit menu items to the Edit menu
        editMenu.add(cutItem);
        editMenu.add(pasteItem);
        editMenu.add(copyItem);
        // Adding File and Edit menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
    
        // Setting menu bar properties
        fileMenu.setForeground(Color.WHITE);
        editMenu.setForeground(Color.WHITE);
        menuBar.setForeground(Color.WHITE);
        menuBar.setBackground(new Color(20, 20, 20));
        setJMenuBar(menuBar);
    
        // Adding mouse listeners for menu items
        fileMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                fileMenu.setBackground(Color.LIGHT_GRAY);
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                fileMenu.setBackground(UIManager.getColor("Menu.background"));
            }
        });
        menuBar.setBorder(BorderFactory.createEmptyBorder());
        editMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                editMenu.setBackground(Color.BLUE);
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                editMenu.setBackground(UIManager.getColor("Menu.background"));
            }
        });
    
        // Adding menu items for light mode and dark mode
        viewMenu = new JMenu("View");
        JMenuItem lightModeItem = new JMenuItem("Light Mode");
        JMenuItem darkModeItem = new JMenuItem("Dark Mode");
    
        // Adding action listeners for light mode and dark mode menu items
        lightModeItem.addActionListener(e -> {
            setLightMode();
            //viewMenu.setForeground(Color.BLACK);
        });
        darkModeItem.addActionListener(e -> {
            setDarkMode();
            
        
        });
    
        // Adding menu items to the View menu
        viewMenu.add(lightModeItem);
        viewMenu.add(darkModeItem);
        viewMenu.setForeground(Color.WHITE);
        menuBar.add(viewMenu);
    
        // Setting mnemonics for the menus and menu items
        fileMenu.setMnemonic(KeyEvent.VK_F);
        editMenu.setMnemonic(KeyEvent.VK_E);
        newItem.setMnemonic(KeyEvent.VK_N);
        fileMenu.setDisplayedMnemonicIndex(-1);
        editMenu.setDisplayedMnemonicIndex(-1);
        menuBar.setBorder(BorderFactory.createEmptyBorder());
        this.setBackground(new Color(120, 120, 120));
    
        // Making the JFrame visible
        this.setVisible(true);
        setDarkMode();
    }
    


    private void addNewTab(String title, JTextArea textArea) {
        // Setup the scroll pane
        newScrollPane = new JScrollPane(textArea);
        newScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        newScrollPane.setBorder(BorderFactory.createEmptyBorder());
    
        // Setupping the text area
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.requestFocusInWindow();
        int initialFontSize = 20;
        textArea.setFont(new Font("Arial", Font.PLAIN, initialFontSize));
        if (currentMode.equals("dark")) {
            textArea.setForeground(Color.WHITE);
        } else {
            textArea.setForeground(Color.BLACK);
        }
        
        // Setuping the font size spinner
        int initialSize = 20;
        JSpinner newFontSize = new JSpinner(new SpinnerNumberModel(initialSize, 1, 100, 1));

		newFontSize.addChangeListener(e -> {
			   int newSize = (int) newFontSize.getValue();
			   textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, newSize));
		});

    
        // Setupping the color button
        ImageIcon i = new ImageIcon(".src\\icons8-blue-color-20.png");
        newColorButton = new JButton(i);
        newColorButton.setFocusable(false);
        newColorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(null, "Choose a color", Color.BLACK);
            textArea.setForeground(color);
        });
        newColorButton.setSize(20, 20);
    
        // Setupping the font selection combo box
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        newFontBox = new JComboBox<>(fonts);
        newFontBox.setSelectedItem("Arial");
        newFontBox.addActionListener(e -> textArea.setFont(new Font((String) newFontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize())));
    
        // Setupping the bold and italic buttons
        boldButton = new JButton("B");
        boldButton.setFocusable(false);
        boldButton.setSize(1, 1);
        boldButton.setFont(new Font("Bodoni MT Black", Font.BOLD, 18));
        boldButton.addActionListener(e -> {
            JTextArea selectedTextArea = getSelectedTextArea();
            if (selectedTextArea != null) {
                Font font = selectedTextArea.getFont();
                int style = font.getStyle();
                if ((style & Font.BOLD) != 0) {
                    selectedTextArea.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));
                } else {
                    selectedTextArea.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
                }
            }
        });
    
        italicButton = new JButton("I");
        italicButton.setFont(new Font("Bodoni MT Black", Font.PLAIN, 18));
        italicButton.setSize(1, 1);
        italicButton.setFocusable(false);
        italicButton.addActionListener(e -> {
            JTextArea selectedTextArea = getSelectedTextArea();
            if (selectedTextArea != null) {
                Font font = selectedTextArea.getFont();
                int style = font.getStyle();
                if ((style & Font.ITALIC) != 0) {
                    selectedTextArea.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));
                } else {
                    selectedTextArea.setFont(new Font(font.getName(), Font.ITALIC, font.getSize()));
                }
            }
        });
    
        // Adding components to the control panel
        label = new JLabel("Size:");
        label.setFont(new Font("Ariel",Font.PLAIN,15));
        controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,15,0));
        controlPanel.setBorder(BorderFactory.createLineBorder(new Color(120,120,120), 1));
        controlPanel.add(label);
        controlPanel.add(newFontSize);
        controlPanel.add(newFontBox);
        controlPanel.add(newColorButton);
        controlPanel.add(italicButton);
        controlPanel.add(boldButton);
    
        // Adding control panel and scroll pane to a panel
        panel = new JPanel(new BorderLayout());
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(newScrollPane, BorderLayout.CENTER);
    
        // Adding the panel to the tabbed pane
        tabbedPane.addTab(title, panel);
        tabbedPane.setOpaque(true);
        int tabIndex = tabbedPane.indexOfComponent(panel);
        tabbedPane.setTabComponentAt(tabIndex, createTabTitleComponent(title, panel));
        tabbedPane.setSelectedComponent(panel);
        textArea.requestFocusInWindow();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        panel.setBorder(BorderFactory.createEmptyBorder());
        //mode switching
        textAreaStyles.put(textArea, new TextStyle(textArea.getFont(), textArea.getForeground(), textArea.getBackground(), textArea.getCaretColor()));
        // Add a property change listener to update the textAreaStyles map when the user changes the style of the text area
        textArea.addPropertyChangeListener("font", e -> {
            TextStyle style = textAreaStyles.get(textArea);
            style.font = textArea.getFont();
        });
        textArea.addPropertyChangeListener("foreground", e -> {
            TextStyle style = textAreaStyles.get(textArea);
            style.foreground = textArea.getForeground();
        });
        textArea.addPropertyChangeListener("background", e -> {
            TextStyle style = textAreaStyles.get(textArea);
            style.background = textArea.getBackground();
        });
        textArea.addPropertyChangeListener("caretColor", e -> {
            TextStyle style = textAreaStyles.get(textArea);
            style.caretColor = textArea.getCaretColor();
        });

        
    }
   private JPanel createTabTitleComponent(String title, JPanel tabPanel) {
    // Creating title panel
    JPanel titlePanel = new JPanel(new BorderLayout(10, 0));
    titlePanel.setOpaque(true);

    // creating title label
    JLabel titleLabel = new JLabel(title);
    titlePanel.add(titleLabel, BorderLayout.WEST);

    // Creating close button
    JButton closeButton = new JButton() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.RED);
            int size = Math.min(getWidth(), getHeight());
            int offset = 4;
            g2.drawLine(offset, offset, size - offset, size - offset);
            g2.drawLine(size - offset, offset, offset, size - offset);
            g2.dispose();
        }
    };
    closeButton.setPreferredSize(new Dimension(16, 16));
    closeButton.setContentAreaFilled(false);
    closeButton.setBorderPainted(false);
    closeButton.setFocusPainted(false);
    closeButton.setOpaque(false);
    titlePanel.add(closeButton, BorderLayout.EAST);

    // Adding action listener to remove the tab when the close button is clicked
    closeButton.addActionListener(e -> {
        if (tabbedPane.getTabCount() > 1) {
            tabbedPane.remove(tabPanel);
        } else {
            JOptionPane.showMessageDialog(null, "At least one tab must remain.");
        }
    });
    return titlePanel;
}

// Set the application to light mode
    private void setLightMode() {
        // Set the background color of the frame to white
        this.setBackground(Color.WHITE);

        // Set the background and foreground colors of the tabbed pane
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(Color.BLACK);

        // Update the current mode
        currentMode = "light";

        // Iterate over each tab and update its components
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component component = tabbedPane.getComponentAt(i);
            
            // Check if the component is a JPanel
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                controlPanel = (JPanel) panel.getComponent(0);

                // Update the control panel's background color
                controlPanel.setBackground(Color.WHITE);

                // Update the background colors of the bold, italic, and color buttons
                boldButton = (JButton) controlPanel.getComponent(5);
                italicButton = (JButton) controlPanel.getComponent(4);
                newColorButton = (JButton) controlPanel.getComponent(3);

                boldButton.setBackground(Color.WHITE);
                italicButton.setBackground(Color.WHITE);
                newColorButton.setBackground(Color.WHITE);
                newColorButton.setBorder(BorderFactory.createLineBorder(Color.white,0));
                boldButton.setBorder(BorderFactory.createLineBorder(Color.white, 1));
                italicButton.setBorder(BorderFactory.createLineBorder(Color.white, 1));
                // Get the scroll pane and text area
                JScrollPane scrollPane = (JScrollPane) panel.getComponent(1);
                JTextArea textArea = (JTextArea) scrollPane.getViewport().getView();
                
                // Get the text style for the text area
                TextStyle style = textAreaStyles.get(textArea);
                
                // Update the background colors of the text area and scroll pane
                textArea.setBackground(Color.WHITE);
                scrollPane.setBackground(Color.WHITE);
                scrollPane.setOpaque(true);
                newColorButton.setBackground(Color.white);
                italicButton.setBackground(Color.white);
                boldButton.setBackground(Color.white);
                // Update the foreground color of the text area if necessary
                if (style.foreground == null || style.foreground == Color.WHITE) {
                    textArea.setForeground(Color.BLACK);
                }
                
                // Update the caret color of the text area
                textArea.setCaretColor(Color.BLACK);
            }
        }

        // Update the colors of the menu bar and its components
       
        menuBar.setBackground(Color.WHITE);
        menuBar.setForeground(Color.BLACK);
        menuBar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        fileMenu.setBackground(Color.WHITE);
        fileMenu.setForeground(Color.BLACK);
        editMenu.setBackground(Color.WHITE);
        editMenu.setForeground(Color.BLACK);
        italicButton.setBackground(Color.white);
        boldButton.setBackground(Color.white);
        boldButton.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        italicButton.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        boldButton.setForeground(Color.BLACK);
        italicButton.setForeground(Color.BLACK);
        label.setForeground(Color.BLACK);
        newColorButton.setBorder(BorderFactory.createLineBorder(Color.white,0));
        newColorButton.setBackground(Color.white);
        controlPanel.setBackground(Color.WHITE);
        viewMenu.setForeground(Color.BLACK);

        // Repaint the frame to apply the changes
        this.revalidate();
        this.repaint();

    }
// Set the application to dark mode
private void setDarkMode() {
    // Set the background color of the frame
    this.setBackground(new Color(40, 40, 40));
    
    // Set the background and foreground colors of the tabbed pane
    tabbedPane.setBackground(new Color(80, 80, 80));
    tabbedPane.setForeground(Color.WHITE);
    
    // Update the current mode
    currentMode = "dark";
    
    // Iterate over each tab and update its components
    for (int i = 0; i < tabbedPane.getTabCount(); i++) {
        Component component = tabbedPane.getComponentAt(i);
        
        // Check if the component is a JPanel
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            controlPanel = (JPanel) panel.getComponent(0);

            // Update the control panel's background color
            controlPanel.setBackground(new Color(120, 120, 120));

            // Update the background colors of the bold, italic, and color buttons
            boldButton = (JButton) controlPanel.getComponent(5);
            italicButton = (JButton) controlPanel.getComponent(4);
            newColorButton = (JButton) controlPanel.getComponent(3);

            boldButton.setBackground(new Color(120, 120, 120));
            italicButton.setBackground(new Color(120, 120, 120));
            newColorButton.setBackground(new Color(120, 120, 120));
            newColorButton.setBorder(BorderFactory.createLineBorder(new Color(120,120,120)));
            boldButton.setBorder(BorderFactory.createLineBorder(new Color(120,120,120)));
            italicButton.setBorder(BorderFactory.createLineBorder(new Color(120,120,120)));


            // Get the scroll pane and text area
            JScrollPane scrollPane = (JScrollPane) panel.getComponent(1);
            JTextArea textArea = (JTextArea) scrollPane.getViewport().getView();
            
            // Get the text style for the text area
            TextStyle style = textAreaStyles.get(textArea);
            
            // Update the background colors of the text area and scroll pane
            textArea.setBackground(new Color(70, 70, 70));
            scrollPane.setBackground(new Color(70, 70, 70));
            scrollPane.setOpaque(true);
            
            // Update the foreground color of the text area if necessary
            if (style.foreground == null || style.foreground == Color.BLACK) {
                textArea.setForeground(Color.WHITE);
            }
            
            // Update the caret color of the text area
            textArea.setCaretColor(Color.WHITE);
        }
    }
    
    // Update the colors of the menu bar and its components
    menuBar.setBackground(new Color(20, 20, 20));
    menuBar.setForeground(Color.WHITE);
    menuBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    fileMenu.setBackground(new Color(20, 20, 20));
    fileMenu.setForeground(Color.WHITE);
    editMenu.setBackground(new Color(20, 20, 20));
    editMenu.setForeground(Color.WHITE);
    viewMenu.setForeground(Color.WHITE);
    boldButton.setBackground(new Color(120,120,120));
    boldButton.setBorder(BorderFactory.createLineBorder(new Color(120,120,120), 1));
    italicButton.setBorder(BorderFactory.createLineBorder(new Color(120,120,120), 1));
    boldButton.setForeground(Color.BLACK);
    italicButton.setForeground(Color.BLACK);
    italicButton.setBackground(new Color(120,120,120));
    label.setForeground(Color.BLACK);
    newColorButton.setBorder(BorderFactory.createLineBorder(new Color(120,120,120)));
    newColorButton.setBackground(new Color(120,120,120));
    controlPanel.setBackground(new Color(120, 120, 120));
    // Repaint the frame to apply the changes
    this.revalidate();
    this.repaint();
}

//Action listener methods for menu bar items
 @Override
public void actionPerformed(ActionEvent e) {
    // Check which menu item was clicked
    if (e.getSource() == newItem) {
        // Create a new text area and add it to a new tab
        JTextArea newTextArea = new JTextArea();
        addNewTab("Untitled", newTextArea);
        
        // Update the mode (light or dark) for the new tab
        if(currentMode.equals("dark")) { 
            setDarkMode(); 
        } else{ 
            setLightMode(); 
        }
    } 
    // Save the current file
    else if (e.getSource() == saveItem) {
        // Create a file chooser to select the save location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Documents"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);
        
        // Show the save dialog and get the selected file
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Add .txt extension if not already present
            if (!file.getName().endsWith(".txt")) {
                file = new File(file.getParent(), file.getName() + ".txt");
            }
            
            // Write the text area content to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                JTextArea textArea = getSelectedTextArea();
                textArea.write(writer);
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            
            // Update the tab title with the file name
            updateTabTitle(file.getName());
        }
    } 
    // Save the current file with a different name
    else if (e.getSource() == saveAsItem) {
        // Create a file chooser to select the save location
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);
        
        // Show the save dialog and get the selected file
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                // Get the selected file
                File file = fileChooser.getSelectedFile();
                
                // Add .txt extension if not already present
                if (!file.getName().endsWith(".txt")) {
                    file = new File(file.getParent(), file.getName() + ".txt");
                }
                
                // Check if the file already exists
                if (file.exists()) {
                    // Ask the user if they want to overwrite the file
                    int confirm = JOptionPane.showConfirmDialog(this, "File already exists. Do you want to overwrite it?", "Confirm dialog", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                
                // Write the text area content to the file
                JTextArea textArea = getSelectedTextArea();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                textArea.write(writer);
                writer.close();
                
                // Update the tab title with the file name
                updateTabTitle(file.getName());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    } 
    // Open an existing file
    else if (e.getSource() == openItem) {
        // Create a file chooser to select the file to open
        JFileChooser fileChooser = new JFileChooser();
        
        // Show the open dialog and get the selected file
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                // Read the file content into a new text area
                JTextArea newTextArea = new JTextArea();
                try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                    newTextArea.read(reader, null);
                    reader.close();
                }
                
                // Set up the new text area
                newTextArea.setLineWrap(true);
                newTextArea.setWrapStyleWord(true);
                newTextArea.setFont(new Font("Arial", Font.PLAIN, 20));
                newTextArea.setBackground(new Color(70, 70, 70));
                newTextArea.setForeground(Color.WHITE);
                newTextArea.setCaretColor(Color.WHITE);
                
                // Add the new text area to a new tab
                String fileName = fileChooser.getSelectedFile().getName();
                addNewTab(fileName, newTextArea);
                
                // Update the mode (light or dark) for the new tab
                if(currentMode=="dark") { 
                    setDarkMode(); 
                } else{ 
                    setLightMode(); 
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    } 
    else if (e.getSource() == saveAsItem) {
        // Saving current file with different name
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                // Checking if file already exists
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".txt")) {
                    file = new File(file.getParent(), file.getName() + ".txt");
                }
                if (file.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(this, "File already exists. Do you want to overwrite it?", "Confirm dialog", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                // Writing content into file
                JTextArea textArea = getSelectedTextArea();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                textArea.write(writer);
                writer.close();
                updateTabTitle(file.getName()); // Update the tab title with the file name
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    //opening saved file
    else if (e.getSource() == openItem) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                JTextArea newTextArea = new JTextArea();
                newTextArea.read(reader, null);
                reader.close();
                newTextArea.setLineWrap(true);
                newTextArea.setWrapStyleWord(true);
                newTextArea.setFont(new Font("Arial", Font.PLAIN, 20));
                newTextArea.setBackground(new Color(70, 70, 70));
                newTextArea.setForeground(Color.WHITE);
                newTextArea.setCaretColor(Color.WHITE);
                if(currentMode=="dark") {
                    setDarkMode();
                } else{
                    setLightMode();
                }
                // Call the addNewTab method
                String fileName = fileChooser.getSelectedFile().getName();
                addNewTab(fileName, newTextArea);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
           
    // Exit the application
    else if (e.getSource() == exitItem) {
        // Close the application
        System.exit(0);
        }

        // Cut the selected text
        else if (e.getSource() == cutItem) {
        // Get the currently selected text area
        JTextArea selectedTextArea = getSelectedTextArea();
        if (selectedTextArea != null) {
        // Cut the selected text
        selectedTextArea.cut();
        }
    }

        // Copy the selected text
    else if (e.getSource() == copyItem) {
        // Get the currently selected text area
        JTextArea selectedTextArea = getSelectedTextArea();
        if (selectedTextArea != null) {
        // Copy the selected text
        selectedTextArea.copy();
        }
    }

        // Paste the copied text
    else if (e.getSource() == pasteItem) {
        // Get the currently selected text area
        JTextArea selectedTextArea = getSelectedTextArea();
        if (selectedTextArea != null) {
        // Paste the copied text
        selectedTextArea.paste();
        }
    }

        // Print the text
        else if (e.getSource() == printItem) {
        // Get the currently selected text area
        JTextArea selectedTextArea = getSelectedTextArea();
        if (selectedTextArea != null) {
        try {
        // Print the text
        selectedTextArea.print();
        } catch (PrinterException ex) {
        // Show an error message if printing fails
        JOptionPane.showMessageDialog(this, "Error printing: " + ex.getMessage());
           }
        }
    }
} 
    private void updateTabTitle(String title) {
        int tabIndex = tabbedPane.indexOfComponent(tabbedPane.getSelectedComponent());
        JPanel titlePanel = (JPanel) tabbedPane.getTabComponentAt(tabIndex);
        JLabel titleLabel = (JLabel) titlePanel.getComponent(0);
        titleLabel.setText(title);
    }    
    private JTextArea getSelectedTextArea() {
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent instanceof JPanel) {
            JPanel panel = (JPanel) selectedComponent;
            JScrollPane scrollPane = (JScrollPane) panel.getComponent(1);
            return (JTextArea) scrollPane.getViewport().getView();
        }
        return null;
    }

}
