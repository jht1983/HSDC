package cn.jiyun.controller;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MyRandom extends JFrame { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel nameLabel = new JLabel("幸运�?" , JLabel.CENTER);
	private JButton startBtn = new JButton("�?�?");
	private JButton endBtn = new JButton("结束");
	
	private String[] nameArray = new String[23];
	private boolean flag = true; 
	

	public MyRandom(){

		initView();

		initData();

		initListener();
	}
	

	private void initListener() {

		startBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				flag = true;
				
				new Thread(new Runnable() {
					public void run() {
						while (flag) {
							int index = new Random().nextInt(22);
							String name = nameArray[index];
							nameLabel.setText(name);
							try {
								Thread.sleep(80);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
		});
		endBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				flag = false;
			}
		});
	}

	private void initData() {
		nameArray[0] = "张学�?";
		nameArray[1] = "陈会�?";
		nameArray[2] = "葛振�?";
		nameArray[3] = "赵炳�?";
		nameArray[4] = "李腾�?";
		nameArray[5] = "李军�?";
		nameArray[6] = "朱永�?";
		nameArray[7] = "刘伟�?";
		nameArray[8] = "孟庆�?";
		nameArray[9] = "侯全�?";
		nameArray[10] = "王亚�?";
		nameArray[11] = "张义�?";
		nameArray[12] = "冯子�?";
		nameArray[13] = "张彦�?";
		nameArray[14] = "朱增�?";
		nameArray[15] = "孙�?�宇";
		nameArray[16] = "赫华�?";
		nameArray[17] = "张宵";
		nameArray[18] = "李政�?";
		nameArray[19] = "崔玮�?";
		nameArray[20] = "郭二�?";
		nameArray[21] = "刘河�?";
		nameArray[22] = "闫玉�?";
		
	}


	private void initView() {
		this.setBounds(550, 250, 300, 250);
		this.setTitle("1609C班实训第�?组点名程�?");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Font font = new Font("宋体", Font.BOLD, 32);
		nameLabel.setFont(font);
		this.add(nameLabel);		

		JPanel panel = new JPanel();
		panel.add(startBtn);
		panel.add(endBtn);
		this.add(panel, BorderLayout.SOUTH);
	}
	public static void main(String[] args) {
		new MyRandom().setVisible(true);
	}
}
