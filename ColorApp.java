// Ethan Mahoney
// CS326 - HW#8
// Date: December 7th, 2021
// Project: Java Color Sampler GUI Application

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;

public class ColorApp extends JFrame 
{
	//Protected Button variables for incrementing RGB values
	protected JButton redPlus;
	protected JButton greenPlus;
	protected JButton bluePlus;
	protected JButton redMinus;
	protected JButton greenMinus;
	protected JButton blueMinus;
	protected JButton save;
	protected JButton reset;
	
	//Protected Text variables for displaying RGB values
	protected JTextField redInput;
	protected JTextField greenInput;
	protected JTextField blueInput;
	
	//Protected Label variables for displaying RGB
	protected JLabel redLabel;
	protected JLabel greenLabel;
	protected JLabel blueLabel;
	
	//Protected variables for storing each individual color read from the text file
	protected JList colors;
	protected Drawing colorPanel;
	protected Colors[] colorList = new Colors[30];	// Array size is arbitrary (large enough)
	protected String[] listColors = new String[30];	// List used to setup JList with color names
	protected int colorCount = 0;
	
	public static void main(String[] args) {
		
		// Try to instantiate a color app and display GUI window
		try {
			new ColorApp("Color Sampler");
		} catch (IOException e) {
			System.out.println("Error Cannot open file: colors.txt");	// An exception is thrown if the text file doesn't open
			System.exit(ERROR);	// Exit with error
		}
		
	}
	
	// Parameterized Constructor
	public ColorApp(String windowName) throws IOException
	{
		super(windowName);	// Inherit Constructor from JFrame to create window
		
		fileIO("colors.txt");	// Read RGB colors from text file
		setSize(575, 450);	// Set window size
		addWindowListener(new WindowDestroyer());	// Event listener for when window closes
		
		// Creates a JList to display different color options in window
		colors = new JList();
		colors.addListSelectionListener(new ListHandler());
		colors.setListData(listColors);		// Set JList to the names of different colors in text file
		
		
		colorPanel = new Drawing();
		redPlus = new JButton("+");
		greenPlus = new JButton("+");
		bluePlus = new JButton("+");
		redMinus = new JButton("-");
		greenMinus = new JButton("-");
		blueMinus = new JButton("-");
		save = new JButton("Save");
		reset = new JButton("Reset");
		
		redInput = new JTextField("0");
		redInput.setEditable(false);	// Red input cannot be changed by text only event listeners
		greenInput = new JTextField("0");
		greenInput.setEditable(false);	// Green input cannot be changed by text only event listeners
		blueInput = new JTextField("0");
		blueInput.setEditable(false);	// BLue input cannot be changed by text only event listeners
			
		redLabel = new JLabel("Red:");
		greenLabel = new JLabel("Green:");
		blueLabel = new JLabel("Blue:");
		
		// Create event listeners for each button to be pressed
		redPlus.addActionListener(new ActionHandler());  
		redMinus.addActionListener(new ActionHandler());
		
		greenPlus.addActionListener(new ActionHandler());  
		greenMinus.addActionListener(new ActionHandler());
		
		bluePlus.addActionListener(new ActionHandler());  
		blueMinus.addActionListener(new ActionHandler());
		
		save.addActionListener(new ActionHandler());  
		reset.addActionListener(new ActionHandler());
		
		
		// Set layout for window to null in order to
		// adjust and move components freely
		getContentPane().setLayout(null);
		getContentPane().add(colors);
		getContentPane().add(colorPanel);
			
		
		colors.setBounds(390, 15, 145, 230);
		colorPanel.setBounds(10, 20, 300, 150);
		
		getContentPane().add(redLabel);
		getContentPane().add(redInput);
		getContentPane().add(redPlus);
		getContentPane().add(redMinus);
		redLabel.setBounds(10, 210, 45, 20);
		redInput.setBounds(60, 210, 45, 20);
		redMinus.setBounds(110, 210, 65, 20);
		redPlus.setBounds(180, 210, 65, 20);
		
		getContentPane().add(greenLabel);
		getContentPane().add(greenInput);
		getContentPane().add(greenPlus);
		getContentPane().add(greenMinus);
		greenLabel.setBounds(10, 260, 45, 20);
		greenInput.setBounds(60, 260, 45, 20);
		greenMinus.setBounds(110, 260, 65, 20);
		greenPlus.setBounds(180, 260, 65, 20);

		getContentPane().add(blueLabel);
		getContentPane().add(blueInput);
		getContentPane().add(bluePlus);
		getContentPane().add(blueMinus);
		blueLabel.setBounds(10, 310, 45, 20);
		blueInput.setBounds(60, 310, 45, 20);
		blueMinus.setBounds(110, 310, 65, 20);
		bluePlus.setBounds(180, 310, 65, 20);
		
		getContentPane().add(save);
		getContentPane().add(reset);
		save.setBounds(55, 360, 80, 20);
		reset.setBounds(140, 360, 80, 20);
		
		colorPanel.setVisible(false);	// Initially set colorPanel to invisible since no color option has been chosen yet

		setVisible(true);	// Set every other component in window to visible
	}
	
	// Function to read colors.txt file
	public void fileIO(String fileName) throws IOException
	{
		FileInputStream stream = new FileInputStream(fileName);  
		InputStreamReader reader = new InputStreamReader(stream); 
		StreamTokenizer tokens = new StreamTokenizer(reader);
		
		String color;
		int count = 0;
		int r;
		int g;
		int b;
		
		while (tokens.nextToken() != StreamTokenizer.TT_EOF) 	// Loop through each line of the txt file
		{  
			color = (String) tokens.sval; 
			tokens.nextToken(); 
			r = (int) tokens.nval; 
			tokens.nextToken(); 
			g = (int) tokens.nval;
			tokens.nextToken();
			b = (int) tokens.nval;
			colorList[count] = new Colors(color, r, g, b); 	// Save RGB values and color names in color list
			listColors[count] = color;
			count++;
		} 
		
		colorCount = count; // Set global count for how many colors in the color list
		
		stream.close(); 
	}
	
	// Function to rewrite the updated RGB values to colors.txt
	public void writeFile(String file) throws IOException
	{
		FileOutputStream ostream = new FileOutputStream(file);
        PrintWriter writer = new PrintWriter(ostream);

        int i = 0;
        while (i < colorCount)	// Iterate through the number of colors there are
        {
            writer.println(colorList[i].colorName + " " + colorList[i].r + " " + colorList[i].g + " " + colorList[i].b);	// Write updated values back to the text file

            i++;
        }

        writer.flush();	// Flush the buffer for new data
		ostream.close();
	}

	// Event listener for when a window is closed
	private class WindowDestroyer extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			try {
				writeFile("colors.txt");	// Try to write new colors to file before closing window
			} catch (IOException e1) {
				System.out.println("Error cannot open file: colors.txt");
			}
			System.exit(0);
		}
	}
	
	// Event listener for clicking on the JList color picker
	private class ListHandler implements ListSelectionListener 
	{      
		public void valueChanged(ListSelectionEvent e)
		{
			setTitle("Color Sampler");
			
			if ( e.getSource() == colors )
				if ( !e.getValueIsAdjusting() )
				{
					colorPanel.setVisible(true);
					String s = (String) colors.getSelectedValue();
					int i = 0;
					
					while(colorList[i].colorName != s)
					{
						i++;
					}
					
					// Sets the colorPanel to the desired color that was chosen from the JList
					colorPanel.paint = new Color(colorList[i].r, colorList[i].g, colorList[i].b);
					colorPanel.repaint();
					
					// Updates the RGB values of the color when the JList is clicked on
					new String();
					redInput.setText(String.valueOf(colorList[i].r));
					greenInput.setText(String.valueOf(colorList[i].g));
					blueInput.setText(String.valueOf(colorList[i].b));
					
				}
		}
	}
	
	// Event listener for changing RGB values of each of the colors
	private class ActionHandler implements ActionListener 
	{      
		public void actionPerformed(ActionEvent e)
		{
			setTitle("Color Sampler*");	// Shows that the current color is being updated at the moment
			
			if ( e.getSource() == redPlus )
			{
				int integer = Integer.parseInt(redInput.getText());
				int newSum = integer+5;
				
				if(newSum > 255)
				{
					newSum = 255;
				}
				else
				{
					Colors newColor = getColor((String) colors.getSelectedValue());
					colorPanel.paint = new Color(newSum, newColor.g, newColor.b);
					colorPanel.repaint();
					new String();
					redInput.setText(String.valueOf(newSum));
				}
			}
			else if ( e.getSource() == redMinus )
			{
				int integer = Integer.parseInt(redInput.getText());
				int newSum = integer-5;
				
				if (newSum < 0)
				{
					newSum = 0;
				}
				else
				{
					Colors newColor = getColor((String) colors.getSelectedValue());
					colorPanel.paint = new Color(newSum, newColor.g, newColor.b);
					colorPanel.repaint();
					new String();
					redInput.setText(String.valueOf(newSum));
				}
			}
			else if ( e.getSource() == greenPlus )
			{
				int integer = Integer.parseInt(greenInput.getText());
				int newSum = integer+5;
				
				if (newSum > 255)
				{
					newSum = 255;
				}
				else
				{
					Colors newColor = getColor((String) colors.getSelectedValue());
					colorPanel.paint = new Color(newColor.r, newSum, newColor.b);
					colorPanel.repaint();
					new String();
					greenInput.setText(String.valueOf(newSum));
				}
			}
			else if ( e.getSource() == greenMinus )
			{
				int integer = Integer.parseInt(greenInput.getText());
				int newSum = integer-5;
				
				if (newSum < 0)
				{
					newSum = 0;
				}
				else
				{
					Colors newColor = getColor((String) colors.getSelectedValue());
					colorPanel.paint = new Color(newColor.r, newSum, newColor.b);
					colorPanel.repaint();
					new String();
					greenInput.setText(String.valueOf(newSum));
				}
			}
			else if ( e.getSource() == bluePlus )
			{
				int integer = Integer.parseInt(blueInput.getText());
				int newSum = integer+5;

				if(newSum > 255)
				{
					newSum = 255;
				}
				else
				{
					Colors newColor = getColor((String) colors.getSelectedValue());
					colorPanel.paint = new Color(newColor.r, newColor.g, newSum);
					colorPanel.repaint();
					new String();
					blueInput.setText(String.valueOf(newSum));
				}
			}
			else if ( e.getSource() == blueMinus )
			{
				int integer = Integer.parseInt(blueInput.getText());
				int newSum = integer-5;

				if (newSum < 0)
				{
					newSum = 0;
				}
				else
				{
					Colors newColor = getColor((String) colors.getSelectedValue());
					colorPanel.paint = new Color(newColor.r, newColor.g, newSum);
					colorPanel.repaint();
					new String();
					blueInput.setText(String.valueOf(newSum));
				}
			}
			else if ( e.getSource() == save )
			{
				setTitle("Color Sampler");
				int red = Integer.parseInt(redInput.getText());
				int green = Integer.parseInt(greenInput.getText());
				int blue = Integer.parseInt(blueInput.getText());
				
				String c = (String) colors.getSelectedValue();
				int i = 0;
				
				while(colorList[i].colorName != c)
				{
					i++;
				}
				
				colorList[i] = new Colors(colorList[i].colorName, red, green, blue);
			}
			else if ( e.getSource() == reset )
			{
				setTitle("Color Sampler");
				String c = (String) colors.getSelectedValue();
				int i = 0;
				
				while(colorList[i].colorName != c)
				{
					i++;
				}
				
				Colors newColor = getColor((String) colors.getSelectedValue());
				colorPanel.paint = new Color(newColor.r, newColor.g, newColor.b);
				colorPanel.repaint();
				redInput.setText(String.valueOf(newColor.r));
				greenInput.setText(String.valueOf(newColor.g));
				blueInput.setText(String.valueOf(newColor.b));
				
				
			}
			
		}
	}    
	
	// Function to get the specific color from the color list and return it
	final public Colors getColor(String target)
	{
		String c = (String) colors.getSelectedValue();
		int i = 0;
		
		while(colorList[i].colorName != c)
		{
			i++;
		}
		
		return colorList[i];
	}
	
	// Custom class to use to create a color panel for the program
	class Drawing extends JComponent
	{
		Color paint = Color.red;
		public void paint(Graphics g)
		{
			Dimension d = getSize();
			
			g.setColor(paint);
			g.fillRect(1, 1, d.width-2, d.height-2);
		}
	}
}

// Custom class to hold information about each color
// such as RGB value and color name
class Colors {
    String colorName;
    int	r;
    int	g;
    int	b;

    public Colors(String name, int red, int green, int blue) {
        this.colorName = name;
        this.r = red;
        this.g = green;
        this.b = blue;
    }
}