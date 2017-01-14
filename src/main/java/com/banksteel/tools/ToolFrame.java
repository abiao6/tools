package com.banksteel.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.banksteel.tools.utils.Tools;

/**
 * 代码自动生成器启动类
 * @author bin.yin 2015.08.14
 */
public class ToolFrame extends JFrame {
	private static final long serialVersionUID = -952834999734555086L;

	public ToolFrame() {
		this.init();
	}

	private void init() {
		super.setTitle("V3.0 Auto Code Generator - chenbiao@1yd.me");
		super.setBounds(300, 200, 500, 300);
		super.setResizable(false);
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel pane = new JPanel();
		pane.setLayout(null);
		this.setContentPane(pane);

		JButton generatorBtn = new JButton("Generator Code");
		generatorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath:application.xml");
				((Tools)ac.getBean("tools")).generator();
				ac.close();
			}
		});
		pane.add(generatorBtn);
		generatorBtn.setBounds(100, 50, 100, 30);
		
		StringBuffer buf = new StringBuffer("");
		JLabel generatorLabel = new JLabel(buf.toString());
		pane.add(generatorLabel);
		generatorLabel.setBounds(100, 60, 200, 100);
	}

	public static void main(String[] args) {
		ToolFrame frame = new ToolFrame();
		frame.setVisible(true);
	}
}
