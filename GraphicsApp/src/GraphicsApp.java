import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class GraphicsApp 
{
	public static void main(String[] args) 
	{
		JFrame f = new AppFrame("Graphics App");
	}
}

class AppFrame extends JFrame
{
	public AppFrame(String title)
	{
		super(title);
	}
	
	public void frameInit()
	{
		super.frameInit();
		
		//adding the panels
		this.setLayout(new BorderLayout());
		DrawingPanel draw = new DrawingPanel();
		BottomPanel btm = new BottomPanel(draw);
		TopPanel top = new TopPanel(draw);
		
		this.add(top, BorderLayout.NORTH);
		this.add(btm, BorderLayout.CENTER);
		
		this.setSize(1000, 1000);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}

class TopPanel extends JPanel
{
	private iShapeDisplay display;
	JButton btnTriangle, btnRectangle, btnCircle;
	JPanel pnlButtons;
	
	public TopPanel(DrawingPanel d)
	{
		super();
		this.display = (iShapeDisplay)d;
		btnTriangle = new JButton("Triangle");
		btnTriangle.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				display.setShape('t');
			}
			
		});
		btnRectangle = new JButton("Rectangle");
		btnRectangle.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				display.setShape('r');
			}
		});
		btnCircle = new JButton("Circle");
		btnCircle.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				display.setShape('c');
			}
		});
		
		pnlButtons = new JPanel();
		pnlButtons.add(btnTriangle);
		pnlButtons.add(btnRectangle);
		pnlButtons.add(btnCircle);
		this.setLayout(new BorderLayout());
		this.add(pnlButtons, BorderLayout.CENTER);
	}
}

class BottomPanel extends JPanel
{
	DrawingPanel pnlDrawing;
	JPanel pnlOptions;
	JTextField txtShapeEditor;
	JButton btnClear;
	String text;
	
	public BottomPanel(DrawingPanel d)
	{
		super();
		this.setBorder(BorderFactory.createTitledBorder("Drawing Pane"));
		pnlDrawing = d;
		pnlOptions = new JPanel();
		txtShapeEditor = new JTextField(20);
		btnClear = new JButton("Clear");
		
		txtShapeEditor.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void changedUpdate(DocumentEvent arg0) 
			{
				sendText();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) 
			{
				sendText();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) 
			{
				sendText();
			}
			
			public void sendText()
			{
				pnlDrawing.setText(txtShapeEditor.getText());
			}
			
		});
		
		btnClear.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				pnlDrawing.clear();
			}
		});
		
		pnlOptions.add(txtShapeEditor);
		pnlOptions.add(btnClear);
		this.setLayout(new BorderLayout());
		this.add(pnlOptions, BorderLayout.SOUTH);
		this.add(pnlDrawing, BorderLayout.CENTER);
	}
}

class DrawingPanel extends JPanel implements iShapeDisplay
{
	java.util.List<Shape> shapes;
	char shapeType;
	String shapeString;
	
	public DrawingPanel()
	{
		super();
		this.addMouseListener(new MsListener());
		shapes = new java.util.ArrayList<Shape>();
		shapeType = ' ';
		shapeString = " ";
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g.create();
		
		for(Shape s : shapes)
		{
			g2.draw(s);
		}
	}
	
	class MsListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			Shape shape;
			switch (shapeType)
			{
			case 'r':
				if(shapeString.equalsIgnoreCase("square"))
					shape = new Rectangle2D.Double(e.getX(), e.getY(), 50, 50);
				else
					shape = new Rectangle2D.Double(e.getX(), e.getY(), 50, 100);
				break;
			case 'c':
				shape = new Ellipse2D.Double(e.getX(), e.getY(), 50, 50);
				break;
			default:
				if(shapeString.equalsIgnoreCase("equilateral"))
				{
					int x1Points[] = {e.getX() - 25, e.getX(), e.getX()-50};
					int y1Points[] = {e.getY() - 25, e.getY() + 25, e.getY() + 25};
					GeneralPath polygon =  new GeneralPath(GeneralPath.WIND_EVEN_ODD, x1Points.length);
					polygon.moveTo(x1Points[0], y1Points[0]);

					for (int index = 1; index < x1Points.length; index++) 
					{
					        polygon.lineTo(x1Points[index], y1Points[index]);
					};

					polygon.closePath();
					shape = polygon;
				}
				else
				{
					int x1Points[] = {e.getX(), e.getX() + 50, e.getX()};
					int y1Points[] = {e.getY(), e.getY() + 25, e.getY() + 25};
					GeneralPath polygon =  new GeneralPath(GeneralPath.WIND_EVEN_ODD, x1Points.length);
					polygon.moveTo(x1Points[0], y1Points[0]);

					for (int index = 1; index < x1Points.length; index++) 
					{
				        polygon.lineTo(x1Points[index], y1Points[index]);
					};

					polygon.closePath();
					shape = polygon;
				}
				break;
			} 
			shapes.add(shape);
			Graphics2D g2 = (Graphics2D) getGraphics().create();
			g2.draw(shape);
		}
	}
	
	public void setText(String s)
	{
		shapeString = s;
	}
	
	public void clear()
	{
		shapes = new java.util.ArrayList<Shape>();
		this.repaint();
	}
	
	@Override
	public void setShape(char shapeType) 
	{
		this.shapeType = shapeType;		
	}
	
}

interface iShapeDisplay
{
	void setShape(char shapeType);
}