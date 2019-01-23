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
	private JLabel nameLabel = new JLabel("幸运者" , JLabel.CENTER);
	private JButton startBtn = new JButton("开始");
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
		nameArray[0] = "张学祥";
		nameArray[1] = "陈会杰";
		nameArray[2] = "葛振宇";
		nameArray[3] = "赵炳丽";
		nameArray[4] = "李腾飞";
		nameArray[5] = "李军波";
		nameArray[6] = "朱永杰";
		nameArray[7] = "刘伟金";
		nameArray[8] = "孟庆伟";
		nameArray[9] = "侯全帅";
		nameArray[10] = "王亚飞";
		nameArray[11] = "张义豪";
		nameArray[12] = "冯子轩";
		nameArray[13] = "张彦昭";
		nameArray[14] = "朱增金";
		nameArray[15] = "孙思宇";
		nameArray[16] = "赫华新";
		nameArray[17] = "张宵";
		nameArray[18] = "李政通";
		nameArray[19] = "崔玮轩";
		nameArray[20] = "郭二静";
		nameArray[21] = "刘河河";
		nameArray[22] = "闫玉龙";
		
	}


	private void initView() {
		this.setBounds(550, 250, 300, 250);
		this.setTitle("1609C班实训第一组点名程序");
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
