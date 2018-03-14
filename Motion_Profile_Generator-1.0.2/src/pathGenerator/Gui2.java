package pathGenerator;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.modifiers.TankModifier;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class Gui2 {

	private JFrame frmMotionProfileGenerator;
	
	private JTextField txtTime;
	private JTextField txtVelocity;
	private JTextField txtAcceleration;
	private JTextField txtJerk;
	private JTextField txtWheelBase;
	private JTextField txtAngle;
	private JTextField txtXValue;
	private JTextField txtYValue;
	private JTextField txtFileName;
	
	private JTabbedPane tabbedPane;
	
	FalconLinePlot blueAllianceGraph = new FalconLinePlot(new double[][]{{0.0,0.0}});
	FalconLinePlot velocityGraph = new FalconLinePlot(new double[][]{{0.0,0.0}});
			
	private JTextArea txtAreaWaypoints;
	
	private JFileChooser fileChooser;
	private File directory;
	
	// Path Waypoints 
	//private Waypoint[] points;
	private List<Waypoint> points = new ArrayList<Waypoint>(); // can be variable length after creation
	
	Trajectory left;
	Trajectory right;
	
	File lFile;
	File rFile;
	
	String fileName;
		
	/**
	 * Create the application.
	 */
	public Gui2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMotionProfileGenerator = new JFrame();
		frmMotionProfileGenerator.setResizable(false);
		frmMotionProfileGenerator.setTitle("Motion Profile Generator");
		frmMotionProfileGenerator.setLocation(150, 100);
		frmMotionProfileGenerator.setSize(1075, 677);
		frmMotionProfileGenerator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMotionProfileGenerator.getContentPane().setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(460, 22, 600, 617);
		frmMotionProfileGenerator.getContentPane().add(tabbedPane);
		
		velocityGraph.setSize(600, 600);
		velocityGraph.setLocation(1070, 0);
		
		JPanel trajecPanel = new JPanel();
		trajecPanel.setBounds(0, 22, 450, 617);
		frmMotionProfileGenerator.getContentPane().add(trajecPanel);
		trajecPanel.setLayout(null);
		
		JLabel lblTimeStep = new JLabel("Time Step");
		lblTimeStep.setBounds(142, 60, 80, 20);
		trajecPanel.add(lblTimeStep);
		
		JLabel lblVelocity = new JLabel("Velocity");
		lblVelocity.setBounds(142, 90, 80, 20);
		trajecPanel.add(lblVelocity);
		
		JLabel lblAcceleration = new JLabel("Acceleration");
		lblAcceleration.setBounds(142, 120, 80, 20);
		trajecPanel.add(lblAcceleration);
		
		JLabel lblJerk = new JLabel("Jerk");
		lblJerk.setBounds(142, 150, 80, 20);
		trajecPanel.add(lblJerk);
		
		txtTime = new JTextField();
		txtTime.setText("0.05");
		txtTime.setBounds(222, 60, 86, 20);
		trajecPanel.add(txtTime);
		txtTime.setColumns(10);
		
		txtVelocity = new JTextField();
		txtVelocity.setText("4");
		txtVelocity.setBounds(222, 90, 86, 20);
		trajecPanel.add(txtVelocity);
		txtVelocity.setColumns(10);
		
		txtAcceleration = new JTextField();
		txtAcceleration.setText("3");
		txtAcceleration.setBounds(222, 120, 86, 20);
		trajecPanel.add(txtAcceleration);
		txtAcceleration.setColumns(10);
		
		txtJerk = new JTextField();
		txtJerk.setText("60");
		txtJerk.setBounds(222, 150, 86, 20);
		trajecPanel.add(txtJerk);
		txtJerk.setColumns(10);
		
		JButton btnGeneratePath = new JButton("Generate Path");
		btnGeneratePath.setBounds(90, 566, 130, 24);
		trajecPanel.add(btnGeneratePath);
		
		btnGeneratePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnGeneratePathActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
		
		JButton btnAddPoint = new JButton("Add Point");
		btnAddPoint.setBounds(130, 329, 90, 20);
		trajecPanel.add(btnAddPoint);
		
		btnAddPoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btnAddPointActionPerformed(evt);
            }
        });
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(230, 328, 90, 20);
		trajecPanel.add(btnClear);
		
		btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btnClearActionPerformed(evt);
            }
        });
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(334, 522, 89, 24);
		trajecPanel.add(btnBrowse);
		
		btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btnBrowseActionPerformed(evt);
            }
        });
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(230, 566, 130, 24);
		trajecPanel.add(btnSave);
		
		btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	try {
					btnSaveActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
		
		JLabel lblMotionVariables = new JLabel("Motion Variables");
		lblMotionVariables.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblMotionVariables.setBounds(137, 11, 176, 40);
		trajecPanel.add(lblMotionVariables);
		
		JLabel lblWaypoints = new JLabel("Waypoints");
		lblWaypoints.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblWaypoints.setBounds(170, 230, 110, 40);
		trajecPanel.add(lblWaypoints);
		
		txtWheelBase = new JTextField();
		txtWheelBase.setText("12");
		txtWheelBase.setBounds(222, 180, 86, 20);
		trajecPanel.add(txtWheelBase);
		txtWheelBase.setColumns(10);
		
		JLabel lblWheelBase = new JLabel(" Wheel Base       ");
		lblWheelBase.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblWheelBase.setBounds(142, 180, 80, 20);
		trajecPanel.add(lblWheelBase);
		
		txtAngle = new JTextField();
		txtAngle.setBounds(257, 298, 63, 20);
		trajecPanel.add(txtAngle);
		txtAngle.setColumns(10);
		
		txtAreaWaypoints = new JTextArea();
		txtAreaWaypoints.setEditable(false);
		txtAreaWaypoints.setFont(new Font("Monospaced", Font.PLAIN, 14));
		String format = "%1$4s %2$6s %3$9s";
    	String line = String.format(format, "X", "Y", "Angle");
		txtAreaWaypoints.append(line + "\n");
		txtAreaWaypoints.append("_______________________" + "\n");
		txtAreaWaypoints.setBounds(131, 363, 188, 176);
		//trajecPanel.add(txtAreaWaypoints);
					
		txtXValue = new JTextField();
		txtXValue.setBounds(130, 298, 63, 20);
		trajecPanel.add(txtXValue);
		txtXValue.setColumns(10);
		
		txtYValue = new JTextField();
		txtYValue.setBounds(193, 298, 64, 20);
		trajecPanel.add(txtYValue);
		txtYValue.setColumns(10);
		
		JLabel lblX = new JLabel("X");
		lblX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblX.setBounds(160, 275, 10, 20);
		trajecPanel.add(lblX);
		
		JLabel lblY = new JLabel("Y");
		lblY.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblY.setBounds(220, 275, 10, 20);
		trajecPanel.add(lblY);
		
		JLabel lblAngle = new JLabel("Angle");
		lblAngle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAngle.setBounds(270, 275, 34, 20);
		trajecPanel.add(lblAngle);
		
		JScrollPane scrollPane = new JScrollPane(txtAreaWaypoints, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(130, 360, 190, 134);
		trajecPanel.add(scrollPane);
		
		txtFileName = new JTextField();
		txtFileName.setBounds(117, 524, 216, 20);
		trajecPanel.add(txtFileName);
		txtFileName.setColumns(10);
		
		JLabel lblLeftFileName = new JLabel("File Name");
		lblLeftFileName.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeftFileName.setBounds(27, 524, 90, 20);
		trajecPanel.add(lblLeftFileName);
								
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1075, 21);
		frmMotionProfileGenerator.getContentPane().add(menuBar);
		menuBar.setBackground(UIManager.getColor("Menu.background"));
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewProfile = new JMenuItem("New Profile");
		mnFile.add(mntmNewProfile);
		
		mntmNewProfile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnClearActionPerformed(evt);
            }
		});
		
		JMenuItem mntmSaveFile = new JMenuItem("Save Profile");
		mnFile.add(mntmSaveFile);
		
		mntmSaveFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
            	try {
					btnMenuSaveActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
		});
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		mntmExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
            }
		});
		
		
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);
		
		mntmHelp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				  if (Desktop.isDesktopSupported()) {
					  Desktop desktop = Desktop.getDesktop();
		              try {
		            	  URI uri = new URI("https://github.com/vannaka/Motion_Profile_Generator");
		                  desktop.browse(uri);
		              } catch (IOException ex) {
		                  return;
		              } catch (URISyntaxException ex) {
		            	  return;
		              }
				  } 
				  else {
					  return;
				  }
		    }
		});
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		mntmAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutPage();
            }
		});
								
		motionGraphBlue();
		velocityGraph();
	};
	
	private void aboutPage()
	{
		JFrame about = new JFrame();
        about.setLocationByPlatform(true);
        about.setVisible(true);
		about.setTitle("About");
		about.setBounds(100, 100, 600, 400);
		about.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		about.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 584, 361);
		about.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblMotionProfileGenerator = new JLabel("Motion Profile Generator");
		lblMotionProfileGenerator.setFont(new Font("Arial", Font.PLAIN, 34));
		lblMotionProfileGenerator.setBounds(109, 29, 365, 64);
		panel.add(lblMotionProfileGenerator);
		
		JLabel lblVersion = new JLabel("Version 1.0.2");
		lblVersion.setFont(new Font("Arial", Font.PLAIN, 14));
		lblVersion.setBounds(82, 104, 85, 14);
		panel.add(lblVersion);
		
		JLabel lblThisProductIs = new JLabel("This product is licensed under the MIT license");
		lblThisProductIs.setFont(new Font("Arial", Font.PLAIN, 14));
		lblThisProductIs.setBounds(82, 128, 296, 14);
		panel.add(lblThisProductIs);
		
		JLabel lblDevelopers = new JLabel("Developers");
		lblDevelopers.setFont(new Font("Arial", Font.PLAIN, 14));
		lblDevelopers.setBounds(82, 152, 85, 14);
		panel.add(lblDevelopers);
		
		JLabel lblLukeMammen = new JLabel("Luke Mammen");
		lblLukeMammen.setFont(new Font("Arial", Font.PLAIN, 14));
		lblLukeMammen.setBounds(109, 176, 110, 14);
		panel.add(lblLukeMammen);
		
		JLabel lblBlakeMammen = new JLabel("Blake Mammen");
		lblBlakeMammen.setFont(new Font("Arial", Font.PLAIN, 14));
		lblBlakeMammen.setBounds(109, 200, 110, 14);
		panel.add(lblBlakeMammen);
		
		JLabel lblAcknowedgements = new JLabel("Acknowledgments");
		lblAcknowedgements.setFont(new Font("Arial", Font.PLAIN, 14));
		lblAcknowedgements.setBounds(82, 224, 150, 14);
		panel.add(lblAcknowedgements);
		
		JLabel lblJaci = new JLabel("Jaci for the path generation code");
		lblJaci.setFont(new Font("Arial", Font.PLAIN, 14));
		lblJaci.setBounds(109, 248, 250, 14);
		panel.add(lblJaci);
		
		JLabel lblJH = new JLabel("KHEngineering for the graph code");
		lblJH.setFont(new Font("Arial", Font.PLAIN, 14));
		lblJH.setBounds(109, 272, 250, 14);
		panel.add(lblJH);
	}
	
	private void motionGraphBlue()
	{
		tabbedPane.insertTab("Blue Alliance", null, blueAllianceGraph, null, 0);
		// Create a blank grid for the field graph
		blueAllianceGraph.yGridOn();
		blueAllianceGraph.xGridOn();
		blueAllianceGraph.setYLabel("Y (in)");
		blueAllianceGraph.setXLabel("X (in)");
		blueAllianceGraph.setTitle("Top Down View of FRC Field - Blue Alliance (30ft x 27ft) \n shows global position of robot path with left and right wheel trajectories");
			
					
		//force graph to show field dimensions of 30ft x 27 feet
		double fieldWidth = 324.0;
		blueAllianceGraph.setXTic(0, 360, 12);
		blueAllianceGraph.setYTic(0, fieldWidth, 12);
					
					
		//lets add field markers to help visual
		double[][] switchOutline = new double[][]{
				{140, 85.25},
				{196, 85.25},
				{196, 238.75},
				{140, 238.75},
				{140, 85.25},
			};
		blueAllianceGraph.addData(switchOutline, Color.black);
		
		double[][] switchTopOutline = new double[][]{
			{144, 234.75},
			{192, 234.75},
			{192, 198.75},
			{144, 198.75},
			{144, 234.75},
		};
		blueAllianceGraph.addData(switchTopOutline, Color.red);
		
		double[][] switchBotOutline = new double[][]{
			{144, 89.25},
			{192, 89.25},
			{192, 125.25},
			{144, 125.25},
			{144, 89.25},
		};
		blueAllianceGraph.addData(switchBotOutline, Color.blue);
		
		double[][] driverSideCubeMat = new double[][]{
			{140, 139.5},
			{98, 139.5},
			{98, 184.5},
			{140, 184.5},
			{140, 139.5},
		};
		blueAllianceGraph.addData(driverSideCubeMat, Color.black);
		
		double[][] driverSideCubePile = new double[][]{
			{140, 142.5},
			{127, 142.5},
			{127, 149},
			{114, 149},
			{114, 155.5},
			{101, 155.5},
			{101, 168.5},
			{114, 168.5},
			{114, 175},
			{127, 175},
			{127, 181.5},
			{140, 181.5},
		};
		blueAllianceGraph.addData(driverSideCubePile, Color.orange);
		
		double[][] scaleMat = new double[][]{
			{300, 228.75},
			{261.5, 228.75},
			{261.5, 95.25},
			{300, 95.25},
		};
		blueAllianceGraph.addData(scaleMat, Color.blue);
		
		double[][] scalePlateTop = new double[][]{
			{300, 72},
			{348, 72},
			{348, 108},
			{300, 108},
			{300, 72},
		};
		blueAllianceGraph.addData(scalePlateTop, Color.blue);
		
		double[][] scalePlateBot = new double[][]{
			{300, 252},
			{348, 252},
			{348, 216},
			{300, 216},
			{300, 252},
		};
		blueAllianceGraph.addData(scalePlateBot, Color.red);
		
		double[][] scaleArm = new double[][]{
			{330, 108},
			{318, 108},
			{318, 216},
			{330, 216},
			{330, 108},
		};
		blueAllianceGraph.addData(scaleArm, Color.black);
		
		double[][] nullZoneTop = new double[][]{
			{348, 228.75},
			{360, 228.75},
			{360, 324},
			{324, 324},
			{324, 252},
			{324, 324},
			{288, 324},
			{288, 228.75},
			{300, 228.75},
		};
		blueAllianceGraph.addData(nullZoneTop, Color.black);
		
		double[][] nullZoneBot = new double[][]{
			{348, 95.25},
			{360, 95.25},
			{360, 0},
			{324, 0},
			{324, 72},
			{324, 0},
			{288, 0},
			{288, 95.25},
			{300, 95.25},
		};
		blueAllianceGraph.addData(nullZoneBot, Color.black);
		
		double[][] borderWall = new double[][]{
			{360, 0},
			{36, 0},
			{0, 30},
			{0, 294},
			{36, 324},
			{360, 324},
		};
		blueAllianceGraph.addData(borderWall, Color.black);
		
		// Auto Line
		//double[][] baseLine = new double[][] {{7.77,0}, {7.77, fieldWidth}};
		//blueAllianceGraph.addData(baseLine, Color.black);
						
	}
	
	private void velocityGraph()
	{
		tabbedPane.insertTab("Velocity", null, velocityGraph, null, 2);
		velocityGraph.yGridOn();
      	velocityGraph.xGridOn();
      	velocityGraph.setYLabel("Velocity (ft/sec)");
      	velocityGraph.setXLabel("time (seconds)");
      	velocityGraph.setTitle("Velocity Profile for Left and Right Wheels \n Left = Cyan, Right = Magenta");
	}
		
	private void btnGeneratePathActionPerformed(java.awt.event.ActionEvent evt) throws IOException
    {
		double timeStep = Double.parseDouble(txtTime.getText()); //default 0.05 
		double velocity = Double.parseDouble(txtVelocity.getText()); //default 4
		double acceleration = Double.parseDouble(txtAcceleration.getText()); // default 3
		double jerk = Double.parseDouble(txtJerk.getText()); // default 60
		double wheelBase = Double.parseDouble(txtWheelBase.getText()); //default 1.464
		
		// clear graphs
    	velocityGraph.clearGraph();
    	velocityGraph.repaint();
    	blueAllianceGraph.clearGraph();
    	blueAllianceGraph.repaint();    	
    	
    	motionGraphBlue();
		velocityGraph();
		
		if(timeStep > 0)
		{
			if(velocity > 0)
			{
				if(acceleration > 0)
				{
					if(jerk > 0)
					{
						if(wheelBase > 0)
						{
							// If waypoints exist
							if( points.size() > 1 )
							{
								Waypoint tmp[] = new Waypoint[ points.size() ];
								points.toArray( tmp );
								try
								{
								trajectory( timeStep, velocity, acceleration, jerk, wheelBase, tmp );
								}
								catch ( Exception e )
								{
									JOptionPane.showMessageDialog(null, "The trajectory provided was invalid! Invalid trajectory could not be generated", "Invalid Points.", JOptionPane.INFORMATION_MESSAGE);
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, "We need at least two points to generate a profile.", "Insufficient Points.", JOptionPane.INFORMATION_MESSAGE);
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "The Wheel Base value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "The Jerk value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "The Acceleration value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "The Velocity value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "The Time Step value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
		}
		
    }
    
    private void btnAddPointActionPerformed(java.awt.event.ActionEvent evt)
    {
    	double xValue = 0;
    	double yValue = 0;
    	double angle = 0;
    	
    	// get x value
    	try
    	{
    		xValue = Double.parseDouble(txtXValue.getText());   		
    	}
    	catch ( Exception e )
    	{
    		JOptionPane.showMessageDialog(null, "The X value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
    		return;
    	}
    	
    	// get y value
    	try
    	{
			yValue = Double.parseDouble(txtYValue.getText());
	    }
		catch ( Exception e )
		{
			JOptionPane.showMessageDialog(null, "The Y value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		// get angle value
    	try
    	{
			angle = Double.parseDouble(txtAngle.getText());
	    }
		catch ( Exception e )
		{
			JOptionPane.showMessageDialog(null, "The Angle value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
    	String format = "%1$6.2f %2$6.2f %3$7.2f";
    	String line = String.format(format, xValue, yValue, angle);
    	txtAreaWaypoints.append(line + "\n");
		
		// add new point to points list
		points.add( new Waypoint(xValue, yValue, Pathfinder.d2r(angle)));
		
		txtXValue.setText("");
		txtYValue.setText("");
		txtAngle.setText("");
		        
    }
    
    private void btnMenuSaveActionPerformed(java.awt.event.ActionEvent evt) throws IOException
    {
    	if(txtFileName.getText().equals("") == false)
    	{
    		if(directory != null)
    		{
    			if(left != null)
    			{
			    	lFile = new File(directory, fileName + "_left.csv");
			        rFile = new File(directory, fileName + "_right.csv");    	
			    	FileWriter lfw = new FileWriter( lFile );
					FileWriter rfw = new FileWriter( rFile );
					PrintWriter lpw = new PrintWriter( lfw );
					PrintWriter rpw = new PrintWriter( rfw );
					
			    	// Detailed CSV with dt, x, y, position, velocity, acceleration, jerk, and heading
			        File leftFile = new File(directory, fileName + "_left_detailed.csv");
			        Pathfinder.writeToCSV(leftFile, left);
			        
			        File rightFile = new File(directory, fileName + "_right_detailed.csv");
			        Pathfinder.writeToCSV(rightFile, right);
			        
			    	// CSV with position and velocity. To be used with your robot. 
			    	// save left path to CSV
			    	for (int i = 0; i < left.length(); i++) 
			    	{			
			    		Segment seg = left.get(i);
			    		lpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
			    	}
			    			
			    	// save right path to CSV
			    	for (int i = 0; i < right.length(); i++) 
			    	{			
			    		Segment seg = right.get(i);
			    		rpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
			    	}
			    			
			    	lpw.close();
			    	rpw.close();
    			}
    			else
    			{
    				JOptionPane.showMessageDialog(null, "No Trajectory has been generated!", "Trajectory Not Generated", JOptionPane.INFORMATION_MESSAGE);
        			return;
    			}
    		}
    		else
    		{
    			JOptionPane.showMessageDialog(null, "No file destination chosen! \nClick the Browse button to choose a directory!", "File Destination Empty", JOptionPane.INFORMATION_MESSAGE);
    			return;
    		}
    	
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(null, "The File Name/directory field is empty! \nPlease enter a file name and click Browse for a destination!", "File Name Empty", JOptionPane.INFORMATION_MESSAGE);
        	return;
    	}	
    }
    
    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt)
    {
    	if(txtFileName.getText().equals("") == false)
    	{
    		fileName = txtFileName.getText();
	    	
	    	fileChooser = new JFileChooser(); 
	        fileChooser.setCurrentDirectory(new java.io.File("."));
	        fileChooser.setDialogTitle("Choose a Directory to Save Files In");
	        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        fileChooser.setAcceptAllFileFilterUsed(false);
	        
	        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	        	//directory = fileChooser.getCurrentDirectory();
	        	directory = fileChooser.getSelectedFile();
	        }
	        
	        else
	        {
	        	return;
	        }
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(null, "The File Name field is empty! \nPlease enter a file name!", "File Name Empty", JOptionPane.INFORMATION_MESSAGE);
        	return;
    	}
        
        txtFileName.setText(directory + "\\" + fileName);
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) throws IOException
    {
    	if(txtFileName.getText().equals("") == false)
    	{
    		if(directory != null)
    		{
    			if(left != null)
    			{
    				lFile = new File(directory, fileName + "_left.csv");
			        rFile = new File(directory, fileName + "_right.csv");    	
			        
			        if( lFile.exists() || rFile.exists() )
			        {
			        	int n = JOptionPane.showConfirmDialog(null, "File already exist. Would you like to replace it?", "File Exists", JOptionPane.YES_NO_OPTION);
			        	
			        	switch( n )
			        	{
			        	case JOptionPane.YES_OPTION:
			        		break;		// Continue with method
			        		
			        	case JOptionPane.NO_OPTION:
			        		return;		// Stop Saving
			        		
			        	default:
			        		return;
			        	}
			        }
			        	
			        	
			    	FileWriter lfw = new FileWriter( lFile );
					FileWriter rfw = new FileWriter( rFile );
					PrintWriter lpw = new PrintWriter( lfw );
					PrintWriter rpw = new PrintWriter( rfw );
					
			    	// Detailed CSV with dt, x, y, position, velocity, acceleration, jerk, and heading
			        File leftFile = new File(directory, fileName + "_left_detailed.csv");
			        Pathfinder.writeToCSV(leftFile, left);
			        
			        File rightFile = new File(directory, fileName + "_right_detailed.csv");
			        Pathfinder.writeToCSV(rightFile, right);
			        
			    	// CSV with position and velocity. To be used with your robot.
			    	// save left path to CSV
			    	for (int i = 0; i < left.length(); i++) 
			    	{			
			    		Segment seg = left.get(i);
			    		lpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
			    	}
			    			
			    	// save right path to CSV
			    	for (int i = 0; i < right.length(); i++) 
			    	{			
			    		Segment seg = right.get(i);
			    		rpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
			    	}
			    			
			    	lpw.close();
			    	rpw.close();
    			}
    			else
    			{
    				JOptionPane.showMessageDialog(null, "No Trajectory has been generated!", "Trajectory Not Generated", JOptionPane.INFORMATION_MESSAGE);
        			return;
    			}
    		}
    		else
    		{
    			JOptionPane.showMessageDialog(null, "No file destination chosen! \nClick the Browse button to choose a directory!", "File Destination Empty", JOptionPane.INFORMATION_MESSAGE);
    			return;
    		}
    	
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(null, "The File Name/directory field is empty! \nPlease enter a file name and click Browse for a destination!", "File Name Empty", JOptionPane.INFORMATION_MESSAGE);
        	return;
    	}	
    }
    
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt)
    {
    	// clear graphs
    	blueAllianceGraph.clearGraph();
    	blueAllianceGraph.repaint();
    	velocityGraph.clearGraph();
    	velocityGraph.repaint();
    	    	
    	motionGraphBlue();
		velocityGraph();  
		
    	points.clear();
    	
    	txtAreaWaypoints.setText(null);
    	String format = "%1$4s %2$6s %3$9s";
    	String line = String.format(format, "X", "Y", "Angle");
		txtAreaWaypoints.append(line + "\n");
		txtAreaWaypoints.append("_______________________" + "\n");
    }
    
    private void trajectory(double timeStep, double velocity, double acceleration, double jerk, double wheelBase, Waypoint[] points) throws IOException
    {
    	
		// Configure the trajectory with the time step, velocity, acceleration, jerk
		Trajectory.Config config = new Trajectory.Config( Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, timeStep, velocity, acceleration, jerk );
             
		// Generate the path
		Trajectory trajectory = Pathfinder.generate(points, config);
		
        // Tank drive modifier with the wheel base
        TankModifier modifier = new TankModifier(trajectory).modify(wheelBase);

        // Separate the trajectory into left and right
        left = modifier.getLeftTrajectory();
        right = modifier.getRightTrajectory();
        
        // Left and Right paths to display on the Field Graph
        double[][] leftPath = new double[left.length()][2];
        double[][] rightPath = new double[right.length()][2];
        
        for(int i = 0; i < left.length(); i++)
        {
        	leftPath[i][0] = left.get(i).x;
        	leftPath[i][1] = left.get(i).y;
        	rightPath[i][0] = right.get(i).x;
        	rightPath[i][1] = right.get(i).y;
        }
        blueAllianceGraph.addData(leftPath, Color.magenta);
       	blueAllianceGraph.addData(rightPath, Color.magenta);
       	blueAllianceGraph.repaint();
        
     	// Velocity to be used in the Velocity graph
     	double[][] leftVelocity = new double[left.length()][2];
     	double[][] rightVelocity = new double[right.length()][2];
     	double[][] middleVelocity = new double[trajectory.length()][2];
     	
     	for(int i = 0; i < left.length(); i++)
     	{
     		leftVelocity[i][0] = left.segments[i].dt * i;
     		leftVelocity[i][1] = left.segments[i].velocity;
     		rightVelocity[i][0] = right.segments[i].dt * i;
     		rightVelocity[i][1] = right.segments[i].velocity;
     		middleVelocity[i][0] = trajectory.segments[i].dt * i;
     		middleVelocity[i][1] = trajectory.segments[i].velocity;
     	}
     	
      	// Velocity Graph
       	velocityGraph.addData(leftVelocity, Color.magenta);
      	velocityGraph.addData(rightVelocity, Color.cyan);
      	velocityGraph.addData(middleVelocity, Color.blue);
      	velocityGraph.repaint();    
		
    }
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui2 window = new Gui2();
					window.frmMotionProfileGenerator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
