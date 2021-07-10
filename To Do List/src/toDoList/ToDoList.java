package toDoList;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class ToDoList implements ActionListener{
	static Connection con;
	Statement smt;
	PreparedStatement ps,deletesmt,pst,updatesmt,searchsmt;
	public
	JFrame frame;
	JPanel panel,panel_1;
	JLabel listLabel,priorityLabel,note;
	JTextField listField, priorityField,textFieldL,textFieldP,SrField;
	JTable table;
	JButton add,update,show,delete,exit;
	JLabel heading;
	JScrollPane scrollPane;
	DefaultTableModel model;
	String lf, pf;
	ResultSet rst,rlast,rs,updaters,searchSrNo;
	//Driver code
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ToDoList window = new ToDoList();
					window.frame.setVisible(true);
			//	con.close();
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//constructor 
	public ToDoList() {
		initialize();
		addRows();
		}

		
		public void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 255, 153));
		frame.setBounds(100, 100, 799, 780);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		this.jdbcCon();
		
		heading = new JLabel("Plan out your day with To do List");
		heading.setForeground(new Color(139, 69, 19));
		heading.setFont(new Font("Castellar", Font.BOLD, 24));
		heading.setBounds(78, 29, 600, 40);
		frame.getContentPane().add(heading);
		
	    panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBackground(new Color(204, 204, 204));
		panel.setBounds(46, 82, 697, 253);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		listLabel = new JLabel("ENTER TASK");
		listLabel.setForeground(new Color(51, 102, 102));
		listLabel.setFont(new Font("Castellar", Font.BOLD, 16));
		listLabel.setBounds(12, 36, 132, 59);
		panel.add(listLabel);
		
		listField = new JTextField();
		listField.setBackground(new Color(255, 255, 255));
		listField.setBounds(170, 49, 435, 32);
		panel.add(listField);
		listField.setColumns(40);
		
		
		add = new JButton("Add");
		add.setForeground(new Color(139, 69, 19));
	
		add.setBackground( new Color(255, 255, 153));
		add.setFont(new Font("Castellar", Font.BOLD, 18));
		add.setBounds(32, 186, 146, 43);
		panel.add(add);
		
	    update= new JButton("Update"); 
	    update.setForeground(new Color(139, 69, 19));
		update.setBackground(new Color(255, 255, 153));
		update.setFont(new Font("Castellar", Font.BOLD, 18));
		update.setBounds(238, 186, 146, 43);
		panel.add(update);
		
		priorityLabel = new JLabel("Set Priority");
		priorityLabel.setForeground(new Color(51, 102, 102));
		priorityLabel.setFont(new Font("Castellar", Font.BOLD, 16));
		priorityLabel.setBounds(12, 113, 146, 16);
		panel.add(priorityLabel);
		
		priorityField = new JTextField();
		priorityField.setBounds(170, 105, 132, 32);
		panel.add(priorityField);
		priorityField.setColumns(10);
		
		show = new JButton("Show");
		show.setFont(new Font("Castellar", Font.BOLD, 18));
		show.setBackground(new Color(255, 255, 153));
		show.setForeground(new Color(139, 69, 19));
		show.setBounds(439, 186, 146, 43);
		panel.add(show);
		
	    panel_1 = new JPanel();
		panel_1.setBounds(46, 348, 697, 283);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		 scrollPane = 
				new JScrollPane();
 
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(12, 13, 673, 257);
		panel_1.add(scrollPane);
		
		
		model=new DefaultTableModel();
		table = new JTable(model);
		scrollPane.setViewportView(table);
		model.addColumn("Sr. No.");
		model.addColumn("Task");
		model.addColumn("Priority");

		table.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(0, 102, 102), null, null, null));
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		note = new JLabel("(Provide Sr. No. below to delete/update a row. For update provide values in both the fields)");
		note.setBounds(56, 642, 550, 16);
		frame.getContentPane().add(note);
		
		JLabel SrLabel = new JLabel("Sr. No.");
		SrLabel.setForeground(new Color(139, 69, 19));
		SrLabel.setFont(new Font("Castellar", Font.BOLD, 18));
		SrLabel.setBounds(78, 672, 81, 34);
		frame.getContentPane().add(SrLabel);
		
		SrField = new JTextField();
		SrField.setBounds(158, 669, 73, 43);
		frame.getContentPane().add(SrField);
		SrField.setColumns(10);
		
		delete = new JButton("Delete");
		delete.setFont(new Font("Castellar", Font.BOLD, 18));
		delete.setBackground(new Color(204, 204, 204));
		delete.setForeground(new Color(139, 69, 19));
		delete.setBounds(285, 668, 146, 43);
		frame.getContentPane().add(delete);
		
		 exit = new JButton("Exit");
		
		exit.setForeground(new Color(139, 69, 19));
		exit.setFont(new Font("Castellar", Font.BOLD, 18));
		exit.setBackground(new Color(204, 204, 204));
		exit.setBounds(494, 668, 146, 43);
		frame.getContentPane().add(exit);
		

		//action performed
		add.addActionListener(this);
		update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					if(SrField.getText().equals("")) {
						JOptionPane.showMessageDialog(frame, "Please enter Sr. No.","Error", JOptionPane.ERROR_MESSAGE, null);
					}
					else {
						int sr=Integer.parseInt(SrField.getText());
						String task=listField.getText();
						String prior=priorityField.getText();
						updatesmt=con.prepareStatement("update list set taskName=?,priority=? where taskId=?");
						updatesmt.setString(1, task);
						updatesmt.setString(2, prior);
						updatesmt.setInt(3, sr);
						updatesmt.executeUpdate();
						model.setRowCount(0);
						addRows();	
					}
			
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				listField.setText("");	
				priorityField.setText("");
				listField.requestFocus();
				
			}
		});
		show.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setRowCount(0);
				addRows();
				
			}
		});
		delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					if(SrField.getText().equals("")) {
						JOptionPane.showMessageDialog(frame,"Enter Sr. No.!!","Error", JOptionPane.ERROR_MESSAGE, null);
					}
					else {
						int sr=Integer.parseInt(SrField.getText());
						deletesmt=con.prepareStatement("delete from list where taskId=?");
						deletesmt.setInt(1,sr);
						deletesmt.executeUpdate();
						JOptionPane.showMessageDialog(frame,"Deleted the record!!");
						model.setRowCount(0);
						addRows();
					}
					
				} catch (SQLException e1) {
					
					System.out.println("Exception: Delete exception occured!");
				}
				
				
				SrField.setText("");
				listField.requestFocus();
			}
		});
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					con.close();
					if(con.isClosed()) {
						System.out.println("Connection Closed");
					}
					else {
						System.out.println("Connection not closed");
					}
					System.exit(0);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("Exception:Connection closing exception "+e1.getMessage());
					
				}
			}
		});
	}
	public void jdbcCon() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist","root","root");
			if(con.isClosed()==false)
				System.out.println("Connected to database");
		    smt=con.createStatement(); 
			ps=con.prepareStatement("insert into list(taskName,priority) values(?,?)");
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	void addRows(){

        try{

        Object[] row = null;
        rst = smt.executeQuery("select * from list");

        while(rst.next()){ 

              String string = String.valueOf(rst.getString(1)+","+rst.getString(2)+","+rst.getString(3));
              row = string.split(",");
              model.addRow(row);

        }

        panel.revalidate();

        }catch(Exception ex){ System.out.println("Exception:add rows "+ex.getMessage()); }

  }


	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			if(e.getSource()==add) {
				if((!(listField.getText().equals("")) && priorityField.getText().equals("")) || (listField.getText().equals("") && !(priorityField.getText().equals(""))) ||  (listField.getText().equals("") && priorityField.getText().equals("")) ) {
				
				JOptionPane.showMessageDialog(frame, "Don't keep fields blank!!","Error", JOptionPane.ERROR_MESSAGE, null);
				}
				else {
				ps.setString(1,listField.getText());
				ps.setString(2, priorityField.getText());
			int i=ps.executeUpdate();
			if(i>0) {
				System.out.println("Record entered!");
				JOptionPane.showMessageDialog(frame,"Record Entered!!");	
			}
			}
				
				listField.setText("");	
				priorityField.setText("");
				listField.requestFocus();	
				
				}
			
		}
		catch(SQLException ep) {
			System.out.println("Exception: On add button "+ep.getMessage());
		}
		catch(Exception ep) {
			System.out.println("Exception: On add button "+ep.getMessage());
		}
		
	}
		
}
	



