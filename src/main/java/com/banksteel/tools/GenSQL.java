package com.banksteel.tools;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.banksteel.tools.utils.StringUtils;
import com.banksteel.tools.utils.Tools;

/**
 * GenSQL
 * @author bin.yin 2013.07.15
 */
public class GenSQL {

	private JFrame frmAutoRobotV;

	private JTextField table_name_sfield;
	private JTextField query_condition_field;
	private JTextField table_name_cfield;
	private JTextField package_name_field;

	private JTextArea sql_result_field;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GenSQL window = new GenSQL();
					window.frmAutoRobotV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GenSQL() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frmAutoRobotV = new JFrame();
		frmAutoRobotV.setTitle("Auto Robot V1.0 - Powered by yinbin@1yd.me");
		frmAutoRobotV.setResizable(false);
		frmAutoRobotV.setBounds(100, 100, 800, 600);
		frmAutoRobotV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmAutoRobotV.setJMenuBar(menuBar);
		
		JMenu mainMenu = new JMenu("Menu");
		menuBar.add(mainMenu);
		
		JMenuItem mntmConfigration = new JMenuItem("Configuration");
		mainMenu.add(mntmConfigration);
		
		JMenu mnNewMenu = new JMenu("Help");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Usage");
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("About US");
		mnNewMenu.add(mntmNewMenuItem);
		frmAutoRobotV.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 30, 796, 522);
		frmAutoRobotV.getContentPane().add(tabbedPane);
		
		JPanel panel_sql = new JPanel();
		tabbedPane.addTab("Tab SQL", null, panel_sql, null);
		panel_sql.setLayout(null);
		
		JLabel table_name_slabel = new JLabel("Table Name:");
		table_name_slabel.setHorizontalAlignment(SwingConstants.RIGHT);
		table_name_slabel.setBounds(10, 10, 100, 20);
		panel_sql.add(table_name_slabel);
		
		table_name_sfield = new JTextField();
		table_name_sfield.setToolTipText("If you have more than one table name, please use comma (,) separated.");
		table_name_sfield.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String tableName = table_name_sfield.getText();
				Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z_]*(,[a-zA-Z_]*)*");
				if(!pattern.matcher(tableName).matches()){
					table_name_sfield.setText(tableName.replaceAll(".$", ""));
				}
				table_name_sfield.setText(table_name_sfield.getText().replaceAll("_,|,_", ","));
				
				pattern = Pattern.compile("^[a-zA-Z][a-zA-Z_]*[a-zA-Z](,[a-zA-Z_]*)+");
				if(pattern.matcher(table_name_sfield.getText()).matches()){
					query_condition_field.setText("");
					query_condition_field.setEditable(false);
				}else{
					query_condition_field.setEditable(true);
				}
			}
		});
		table_name_sfield.setBounds(120, 10, 540, 20);
		panel_sql.add(table_name_sfield);
		table_name_sfield.setColumns(10);
		
		JLabel query_condition_label = new JLabel("Query Condition:");
		query_condition_label.setHorizontalAlignment(SwingConstants.RIGHT);
		query_condition_label.setBounds(10, 40, 100, 20);
		panel_sql.add(query_condition_label);
		
		query_condition_field = new JTextField();
		query_condition_field.setBounds(120, 40, 540, 20);
		panel_sql.add(query_condition_field);
		query_condition_field.setColumns(10);
		
		JButton genertor_sql_btn = new JButton("Gen SQL");
		genertor_sql_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tableName = table_name_sfield.getText();
				String queryCondition = query_condition_field.getText();
				if(StringUtils.isEmpty(tableName)){
					JOptionPane.showMessageDialog(null, "You must enter the table name!");
					return;
				}
				try{
					ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath:application.xml");
					String result = ((Tools)ac.getBean("tools")).autosql(tableName, queryCondition);
					ac.close();
					
					sql_result_field.setText(result);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		genertor_sql_btn.setBounds(680, 40, 90, 20);
		panel_sql.add(genertor_sql_btn);
		
		JLabel sql_result_label = new JLabel("SQL Result:");
		sql_result_label.setHorizontalAlignment(SwingConstants.RIGHT);
		sql_result_label.setBounds(10, 70, 100, 20);
		panel_sql.add(sql_result_label);
		
		JScrollPane sql_scroll_pane = new JScrollPane();
		sql_scroll_pane.setBounds(120, 70, 540, 400);
		panel_sql.add(sql_scroll_pane);
		
		sql_result_field = new JTextArea();
		sql_scroll_pane.setViewportView(sql_result_field);
		
		JPanel panel_code = new JPanel();
		tabbedPane.addTab("Tab Code", null, panel_code, null);
		panel_code.setLayout(null);
		
		JLabel table_name_clabel = new JLabel("Table Name:");
		table_name_clabel.setHorizontalAlignment(SwingConstants.RIGHT);
		table_name_clabel.setBounds(10, 40, 100, 20);
		panel_code.add(table_name_clabel);
		
		table_name_cfield = new JTextField();
		table_name_cfield.setBounds(120, 40, 540, 20);
		panel_code.add(table_name_cfield);
		table_name_cfield.setColumns(10);
		
		JButton generator_code_btn = new JButton("Gen Code");
		generator_code_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
			}
		});
		
		JScrollPane code_scroll_pane = new JScrollPane();
		code_scroll_pane.setBounds(120, 100, 540, 370);
		panel_code.add(code_scroll_pane);
		
		JTextArea code_result_field = new JTextArea();
		code_scroll_pane.setViewportView(code_result_field);
		generator_code_btn.setBounds(680, 40, 90, 20);
		panel_code.add(generator_code_btn);
		
		JCheckBox dto_cbox = new JCheckBox("DTO");
		dto_cbox.setEnabled(false);
		dto_cbox.setSelected(true);
		dto_cbox.setBounds(120, 70, 50, 20);
		panel_code.add(dto_cbox);
		
		JCheckBox sql_mapping_cbox = new JCheckBox("SQL Mapping");
		sql_mapping_cbox.setEnabled(false);
		sql_mapping_cbox.setSelected(true);
		sql_mapping_cbox.setBounds(170, 70, 100, 20);
		panel_code.add(sql_mapping_cbox);
		
		JCheckBox service_cbox = new JCheckBox("Service&Impl");
		service_cbox.setBounds(270, 70, 110, 20);
		panel_code.add(service_cbox);
		
		JCheckBox dao_cbox = new JCheckBox("DAO&Impl");
		dao_cbox.setBounds(380, 70, 90, 20);
		panel_code.add(dao_cbox);
		
		JLabel file_type_label = new JLabel("File Type:");
		file_type_label.setHorizontalAlignment(SwingConstants.RIGHT);
		file_type_label.setBounds(10, 70, 100, 20);
		panel_code.add(file_type_label);
		
		JLabel package_name_label = new JLabel("Package Name:");
		package_name_label.setHorizontalAlignment(SwingConstants.RIGHT);
		package_name_label.setBounds(10, 10, 100, 20);
		panel_code.add(package_name_label);
		
		package_name_field = new JTextField();
		package_name_field.setBounds(120, 10, 540, 20);
		panel_code.add(package_name_field);
		package_name_field.setColumns(10);
		
		JLabel code_result_label = new JLabel("Code Result:");
		code_result_label.setHorizontalAlignment(SwingConstants.RIGHT);
		code_result_label.setBounds(10, 100, 100, 20);
		panel_code.add(code_result_label);
		
		JPanel panel_html = new JPanel();
		tabbedPane.addTab("Tab Html", null, panel_html, null);
		
		JPanel panel_common = new JPanel();
		panel_common.setBounds(0, 0, 800, 30);
		frmAutoRobotV.getContentPane().add(panel_common);
		panel_common.setLayout(null);
		
		JLabel source_db_label = new JLabel("Source Database :");
		source_db_label.setHorizontalAlignment(SwingConstants.RIGHT);
		source_db_label.setBounds(10, 5, 104, 20);
		panel_common.add(source_db_label);
		
		JComboBox source_db_sbox = new JComboBox();
		source_db_sbox.setModel(new DefaultComboBoxModel(new String[] {"flsdata", "flsopr"}));
		source_db_sbox.setBounds(124, 5, 90, 21);
		panel_common.add(source_db_sbox);
		
		JCheckBox encode_cbox = new JCheckBox("UTF-8");
		encode_cbox.setBounds(720, 5, 61, 20);
		panel_common.add(encode_cbox);
	}
}