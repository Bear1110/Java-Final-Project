import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main {
	static JFrame jf = new JFrame("節拍器");
	static JPanel[] p = new JPanel[3];
	static JPanel[] pp = new JPanel[4];

	static JPanel smallgame = new JPanel(new BorderLayout());
	static JButton smallgame_button = new JButton("特別小遊戲!?");
	static JTextArea smallgame_show = new JTextArea("快點上面鈕 有遊戲");
	static JLabel[] beat = new JLabel[4];
	static JLabel show = new JLabel("請輸入 BPM(整數值)→→");
	static JLabel unit = new JLabel(" BPM (beats per minute)");
	static JButton start = new JButton("開始");
	static JButton stop = new JButton("暫停");
	static JLabel clickBPM_show = new JLabel("請點擊 4下 下方按鈕 將會以您的點擊速度算出BPM");
	static JLabel clickBPM_showBPM = new JLabel("<<<<");
	static JButton clickBPM_button = new JButton("點我四下");
	static JPanel chord_Panel = new JPanel();
	static JButton chord_button = new JButton("顯示Chord");
	static JLabel chord_show = new JLabel(new ImageIcon("src/chord/0.gif"));
	static ImageIcon[] beats = new ImageIcon[6];
	static JTextField input = new JTextField(5);
	static Object abc = new Object();
	static guessNumber game = new guessNumber();
	static JButton[] speed = new JButton[3];
	static boolean[] speedcheck = new boolean[3];

	static Thread main = new Thread(new tempo());// //新東西
	static boolean first = true;
	static int sleeptime = 1;
	static boolean checkstop = false;
	static String[] chord_text = { "C", "D", "Dm", "E", "Em", "F", "F maj7",
			"G", "A", "Am", "B" };
	static JComboBox chord_select = new JComboBox(chord_text);
	static int clicks = 0;
	static long[] clickBPM = new long[4];
	
	

	public static void main(String[] args) {
		speedcheck[0] = true;
		jf.setSize(900, 700);
		jf.setLocationRelativeTo(null);
		// jf.setLayout(new GridLayout(3,1));
		jf.setLayout(new BorderLayout());
		speed[0] = new JButton("四分音符");
		speed[1] = new JButton("八分音符");
		speed[2] = new JButton("十六分音符");
		p[0] = new JPanel(new GridLayout(1, 4));
		p[1] = new JPanel(new GridLayout(4, 1));
		p[2] = new JPanel(new GridLayout());
		pp[0] = new JPanel();
		pp[1] = new JPanel();
		pp[2] = new JPanel();
		pp[3] = new JPanel();
		chord_Panel.setLayout(new BorderLayout());
		for (int i = 0; i < 6; i++)///////圖片編號
			beats[i] = new ImageIcon("src/" + i + ".png");
		for (int i = 0; i < 4; i++) {// ////////////////////////放上面的排版
			beat[i] = new JLabel(beats[0]);
			p[0].add(beat[i]);
		}

		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				if (start.getText().equals("大")) {

					game.checkbigbutton();
					smallgame_show.setText(game.gameText(start));
					if (game.round >= 7) {// //////////////////////回合數超過七時
						System.out.println(game.randomTempo);
						sleeptime = (60000 / game.randomTempo);
						show.setText("輸入選項(1~4)>>>>");
						unit.setText("");
						if (first) {
							main.start();
							first = false;
						}
						if (tempo.pauseThreadFlag) {
							tempo.resumeThread();
						}
						start.setText("提交答案");
						stop.setEnabled(false);
					}

				} else if (start.getText().equals("提交答案")) {
					System.out.println("dddd");
					int answer = Integer.parseInt(input.getText());
					System.out.println(answer);
					if (answer >= 1 && answer <= 4) {

						if (game.checkwin(answer)) {
							smallgame_show.setText(game.gameText(start));
							sleeptime = (60000 / game.randomTempo);
						}

					} else {
						JOptionPane.showMessageDialog(null, "請輸入選項1~4",
								"輸入錯誤?", JOptionPane.WARNING_MESSAGE);
					}

				} else {
					boolean checkinput = false;

					if (Integer.parseInt(input.getText()) >= 1) {
						checkinput = true;
						sleeptime = (60000 / Integer.parseInt(input.getText()));
					}
					if (checkinput) {
						if (first) {
							main.start();
							first = false;
						}
						if (tempo.pauseThreadFlag) {
							tempo.resumeThread();
						}
					} else {
						JOptionPane.showMessageDialog(null, "BPM 請輸入大於0的數字",
								"輸入錯誤?", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		stop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {

				if (stop.getText().equals("小")) {

					game.checksmallbutton();
					smallgame_show.setText(game.gameText(start));

					if (game.round >= 7) {// //////////////////////回合數超過七時
						System.out.println(game.randomTempo);
						sleeptime = (60000 / game.randomTempo);
						show.setText("輸入選項(1~4)>>>>");
						unit.setText("");
						if (first) {
							main.start();
							first = false;
						}
						if (tempo.pauseThreadFlag) {
							tempo.resumeThread();
						}
						start.setText("提交答案");
						stop.setEnabled(false);
					}
				}

				else if (stop.getText().equals("暫停")) {

					// /////////////////////////////
					try {
						tempo.pauseThread();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// ////////////////////////////////

				}
			}
		});
		chord_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {

				if (chord_select.getSelectedIndex() == 0){
						chord_show.setIcon(new ImageIcon("src/chord/0.gif"));
						music("src/chord/C.wav");
						
					}
				if (chord_select.getSelectedIndex() == 1){
						chord_show.setIcon(new ImageIcon("src/chord/1.gif"));
						music("src/chord/D.wav");
					}
				if (chord_select.getSelectedIndex() == 2){
						chord_show.setIcon(new ImageIcon("src/chord/2.gif"));
						music("src/chord/Dm.wav");
				}
				if (chord_select.getSelectedIndex() == 3){
					chord_show.setIcon(new ImageIcon("src/chord/3.gif"));
					music("src/chord/E.wav");
				}
				if (chord_select.getSelectedIndex() == 4){
					chord_show.setIcon(new ImageIcon("src/chord/4.gif"));
					music("src/chord/Em.wav");
				}
				if (chord_select.getSelectedIndex() == 5){
					chord_show.setIcon(new ImageIcon("src/chord/5.gif"));
					music("src/chord/F.wav");
				}
				if (chord_select.getSelectedIndex() == 6){
					chord_show.setIcon(new ImageIcon("src/chord/6.gif"));
					music("src/chord/Fm.wav");
				}
				if (chord_select.getSelectedIndex() == 7){
					chord_show.setIcon(new ImageIcon("src/chord/7.gif"));
					music("src/chord/G.wav");
				}
				if (chord_select.getSelectedIndex() == 8){
					chord_show.setIcon(new ImageIcon("src/chord/8.gif"));
					music("src/chord/A.wav");
				}
				if (chord_select.getSelectedIndex() == 9){
					chord_show.setIcon(new ImageIcon("src/chord/9.gif"));
					music("src/chord/Am.wav");
				}
				if (chord_select.getSelectedIndex() == 10){
					chord_show.setIcon(new ImageIcon("src/chord/10.gif"));
					music("src/chord/B.wav");
				}
			}
		});

		clickBPM_button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent ev) {

				if (clicks == 0) {
					clickBPM[0] = System.currentTimeMillis();
					clickBPM_button.setText("1");
					clicks++;
				} else if (clicks == 1) {
					clickBPM[1] = System.currentTimeMillis();
					clickBPM_button.setText("2");
					clicks++;
				} else if (clicks == 2) {
					clickBPM[2] = System.currentTimeMillis();
					clickBPM_button.setText("3");
					clicks++;

				} else if (clicks == 3) {
					clickBPM[3] = System.currentTimeMillis();
					clickBPM_button.setText("請按我");
					clicks = 0;

					double ttest = (double) ((clickBPM[3] - clickBPM[2])
							+ (clickBPM[2] - clickBPM[1]) + (clickBPM[1] - clickBPM[0])) / 3000;
					double average = (double) 60 / ttest;

					double BPM = new BigDecimal(average).setScale(3,
							BigDecimal.ROUND_HALF_UP).doubleValue();

					clickBPM_showBPM.setText("平均 BPM = " + BPM);

				}

				System.out.println(clicks);
			}
		});
		clickBPM_showBPM.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				game.round = 7;
			}
		});

		smallgame_button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent ev) {
				if (smallgame_button.getText().equals("特別小遊戲!?")) {
					if(game.round<=12){
					smallgame_show.setEditable(false);
					
					try {
						AudioInputStream audioInputStream = AudioSystem
								.getAudioInputStream((new File("src\\cry.wav")));
						AudioFormat audioFormat = audioInputStream.getFormat();
						int bufferSize = (int) Math.min(
								audioInputStream.getFrameLength()
										* audioFormat.getFrameSize(),
								Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
						DataLine.Info dataLineInfo = new DataLine.Info(
								Clip.class, audioFormat, bufferSize);
						Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
						clip.open(audioInputStream);
						clip.start();
					} catch (Exception ex) {

					}
					
					JOptionPane.showMessageDialog(null, "正詳又偷睡覺了  趕快跟他玩小遊戲",
							"小小挑戰", JOptionPane.PLAIN_MESSAGE, beats[5]);
					

					start.setText("大");
					stop.setText("小");
					smallgame_show.setText(game.gameText(start));
					smallgame_button.setText("回復為原本節拍器");
					// ///////////////////////////////////////回和數 第7
					if (game.round >= 7) {
						sleeptime = (60000 / game.randomTempo);
						show.setText("輸入選項(1~4)>>>>");
						unit.setText("");
						if (first) {
							main.start();
						}
						if (tempo.pauseThreadFlag) {
							tempo.resumeThread();
						}
						start.setText("提交答案");
						stop.setEnabled(false);
					}
					// //////////////////////////////////////
				}
					else {
						JOptionPane.showMessageDialog(null, "你已經破台了 想玩的話 關掉程式再按一次!!",
								"貪玩的你!!!", JOptionPane.PLAIN_MESSAGE, beats[5]);						
					}
				
				} else if (smallgame_button.getText().equals("回復為原本節拍器")) {
					smallgame_button.setText("特別小遊戲!?");
					smallgame_show.setText("快點上面鈕 有遊戲");
					start.setText("開始");
					start.setEnabled(true);
					stop.setText("暫停");
					stop.setEnabled(true);
				}
			}
		});
		speed[0].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				speedcheck[0] = true;
				speedcheck[1] = false;
				speedcheck[2] = false;
			}
		});
		speed[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				speedcheck[0] = false;
				speedcheck[1] = true;
				speedcheck[2] = false;
			}
		});
		speed[2].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				speedcheck[0] = false;
				speedcheck[1] = false;
				speedcheck[2] = true;
			}
		});
		// //////////////////// 中間排版
		pp[0].add(show);
		pp[0].add(input);
		pp[0].add(unit);
		pp[1].add(clickBPM_show);
		pp[2].add(clickBPM_button);
		pp[2].add(clickBPM_showBPM);
		pp[3].add(speed[0]);
		pp[3].add(speed[1]);
		pp[3].add(speed[2]);
		p[1].add(pp[0]);
		p[1].add(pp[1]);
		p[1].add(pp[2]);
		p[1].add(pp[3]);
		
		// //////////////////////下面按鈕
		p[2].add(start);
		p[2].add(stop);
		
		// //////////////////右邊排版
		chord_Panel.add(chord_select, BorderLayout.NORTH);
		chord_Panel.add(chord_show, BorderLayout.CENTER);
		chord_Panel.add(chord_button, BorderLayout.SOUTH);
		
		// ///////////////////左邊排版
		smallgame.add(smallgame_button, BorderLayout.NORTH);
		smallgame.add(smallgame_show, BorderLayout.CENTER);

		jf.add(p[0], BorderLayout.NORTH);
		jf.add(p[1]);
		jf.add(p[2], BorderLayout.SOUTH);
		jf.add(chord_Panel, BorderLayout.EAST);
		jf.add(smallgame, BorderLayout.WEST);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 關閉視窗就關閉程式
	}

	public static class tempo implements Runnable {

		// /////////////////////
		
		private final static Object checkk = new Object();
		private static boolean pauseThreadFlag = false;
		static int now = 0;

		// //////////////////////

		public void run() {
			while (true) {
				checkForPaused(); // always check boolean value/////

				if (now == 0) {
					beat[1].setIcon(beats[0]);
					beat[2].setIcon(beats[0]);
					beat[3].setIcon(beats[0]);
					beat[now].setIcon(new ImageIcon("src/smile.jpg"));
					now++;
					try {
						AudioInputStream audioInputStream = AudioSystem
								.getAudioInputStream((new File("src\\first.wav")));
						AudioFormat audioFormat = audioInputStream.getFormat();
						int bufferSize = (int) Math.min(
								audioInputStream.getFrameLength()
										* audioFormat.getFrameSize(),
								Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
						DataLine.Info dataLineInfo = new DataLine.Info(
								Clip.class, audioFormat, bufferSize);
						Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
						clip.open(audioInputStream);
						clip.start();
					} catch (Exception ex) {

					}
					if (speedcheck[0]) {

					} else if (speedcheck[1]) {
						try {
							Thread.sleep(sleeptime / 2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						

						try {

							AudioInputStream audioInputStream = AudioSystem
									.getAudioInputStream((new File(
											"src\\Metronome1.wav")));
							AudioFormat audioFormat = audioInputStream
									.getFormat();
							int bufferSize = (int) Math.min(
									audioInputStream.getFrameLength()
											* audioFormat.getFrameSize(),
									Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
							DataLine.Info dataLineInfo = new DataLine.Info(
									Clip.class, audioFormat, bufferSize);
							Clip clip = (Clip) AudioSystem
									.getLine(dataLineInfo);
							clip.open(audioInputStream);
							clip.start();
						} catch (Exception ex) {

						}
					}
					else if (speedcheck[2]) {
						for (int i = 1; i <= 3; i++) {
							try {
								Thread.sleep(sleeptime / 4);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							
							try {

								AudioInputStream audioInputStream = AudioSystem
										.getAudioInputStream((new File(
												"src\\Metronome1.wav")));
								AudioFormat audioFormat = audioInputStream
										.getFormat();
								int bufferSize = (int) Math.min(
										audioInputStream.getFrameLength()
												* audioFormat.getFrameSize(),
										Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
								DataLine.Info dataLineInfo = new DataLine.Info(
										Clip.class, audioFormat, bufferSize);
								Clip clip = (Clip) AudioSystem
										.getLine(dataLineInfo);
								clip.open(audioInputStream);
								clip.start();
							} catch (Exception ex) {

							}
						}
					}
				} else if (now == 1) {
					beat[now - 1].setIcon(beats[0]);
					beat[now].setIcon(new ImageIcon("src/A.jpg"));
					now++;
					
					try {
						AudioInputStream audioInputStream = AudioSystem
								.getAudioInputStream((new File(
										"src\\Metronome.wav")));
						AudioFormat audioFormat = audioInputStream.getFormat();
						int bufferSize = (int) Math.min(
								audioInputStream.getFrameLength()
										* audioFormat.getFrameSize(),
								Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
						DataLine.Info dataLineInfo = new DataLine.Info(
								Clip.class, audioFormat, bufferSize);
						Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
						clip.open(audioInputStream);
						clip.start();
					} catch (Exception ex) {

					}
					if (speedcheck[0]) {

					} else if (speedcheck[1]) {
						try {
							Thread.sleep(sleeptime / 2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						try {

							AudioInputStream audioInputStream = AudioSystem
									.getAudioInputStream((new File(
											"src\\Metronome1.wav")));
							AudioFormat audioFormat = audioInputStream
									.getFormat();
							int bufferSize = (int) Math.min(
									audioInputStream.getFrameLength()
											* audioFormat.getFrameSize(),
									Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
							DataLine.Info dataLineInfo = new DataLine.Info(
									Clip.class, audioFormat, bufferSize);
							Clip clip = (Clip) AudioSystem
									.getLine(dataLineInfo);
							clip.open(audioInputStream);
							clip.start();
						} catch (Exception ex) {

						}
					}
					else if (speedcheck[2]) {
						for (int i = 1; i <= 3; i++) {
							try {
								Thread.sleep(sleeptime / 4);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							try {

								AudioInputStream audioInputStream = AudioSystem
										.getAudioInputStream((new File(
												"src\\Metronome1.wav")));
								AudioFormat audioFormat = audioInputStream
										.getFormat();
								int bufferSize = (int) Math.min(
										audioInputStream.getFrameLength()
												* audioFormat.getFrameSize(),
										Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
								DataLine.Info dataLineInfo = new DataLine.Info(
										Clip.class, audioFormat, bufferSize);
								Clip clip = (Clip) AudioSystem
										.getLine(dataLineInfo);
								clip.open(audioInputStream);
								clip.start();
							} catch (Exception ex) {

							}
						}
					}
				} else if (now == 2) {
					beat[now - 1].setIcon(beats[0]);
					beat[now].setIcon(new ImageIcon("src/sleep.jpg"));
					now++;
					try {
						AudioInputStream audioInputStream = AudioSystem
								.getAudioInputStream((new File(
										"src\\Metronome.wav")));
						AudioFormat audioFormat = audioInputStream.getFormat();
						int bufferSize = (int) Math.min(
								audioInputStream.getFrameLength()
										* audioFormat.getFrameSize(),
								Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
						DataLine.Info dataLineInfo = new DataLine.Info(
								Clip.class, audioFormat, bufferSize);
						Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
						clip.open(audioInputStream);
						clip.start();
					} catch (Exception ex) {

					}
					if (speedcheck[0]) {

					} else if (speedcheck[1]) {
						try {
							Thread.sleep(sleeptime / 2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						try {

							AudioInputStream audioInputStream = AudioSystem
									.getAudioInputStream((new File(
											"src\\Metronome1.wav")));
							AudioFormat audioFormat = audioInputStream
									.getFormat();
							int bufferSize = (int) Math.min(
									audioInputStream.getFrameLength()
											* audioFormat.getFrameSize(),
									Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
							DataLine.Info dataLineInfo = new DataLine.Info(
									Clip.class, audioFormat, bufferSize);
							Clip clip = (Clip) AudioSystem
									.getLine(dataLineInfo);
							clip.open(audioInputStream);
							clip.start();
						} catch (Exception ex) {

						}
					} else if (speedcheck[2]) {
						for (int i = 1; i <= 3; i++) {
							try {
								Thread.sleep(sleeptime / 4);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							try {

								AudioInputStream audioInputStream = AudioSystem
										.getAudioInputStream((new File(
												"src\\Metronome1.wav")));
								AudioFormat audioFormat = audioInputStream
										.getFormat();
								int bufferSize = (int) Math.min(
										audioInputStream.getFrameLength()
												* audioFormat.getFrameSize(),
										Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
								DataLine.Info dataLineInfo = new DataLine.Info(
										Clip.class, audioFormat, bufferSize);
								Clip clip = (Clip) AudioSystem
										.getLine(dataLineInfo);
								clip.open(audioInputStream);
								clip.start();
							} catch (Exception ex) {

							}
						}
					}
				} else if (now == 3) {
					beat[now - 1].setIcon(beats[0]);
					beat[now].setIcon(new ImageIcon("src/horrble.jpg"));
					now = 0;
					try {
						AudioInputStream audioInputStream = AudioSystem
								.getAudioInputStream((new File(
										"src\\Metronome.wav")));
						AudioFormat audioFormat = audioInputStream.getFormat();
						int bufferSize = (int) Math.min(
								audioInputStream.getFrameLength()
										* audioFormat.getFrameSize(),
								Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
						DataLine.Info dataLineInfo = new DataLine.Info(
								Clip.class, audioFormat, bufferSize);
						Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
						clip.open(audioInputStream);
						clip.start();
					} catch (Exception ex) {

					}
					if (speedcheck[0]) {

					} else if (speedcheck[1]) {
						try {
							Thread.sleep(sleeptime / 2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						try {

							AudioInputStream audioInputStream = AudioSystem
									.getAudioInputStream((new File(
											"src\\Metronome1.wav")));
							AudioFormat audioFormat = audioInputStream
									.getFormat();
							int bufferSize = (int) Math.min(
									audioInputStream.getFrameLength()
											* audioFormat.getFrameSize(),
									Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
							DataLine.Info dataLineInfo = new DataLine.Info(
									Clip.class, audioFormat, bufferSize);
							Clip clip = (Clip) AudioSystem
									.getLine(dataLineInfo);
							clip.open(audioInputStream);
							clip.start();
						} catch (Exception ex) {

						}
					}
					else if (speedcheck[2]) {
						for (int i = 1; i <= 3; i++) {
							try {
								Thread.sleep(sleeptime / 4);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							try {

								AudioInputStream audioInputStream = AudioSystem
										.getAudioInputStream((new File(
												"src\\Metronome1.wav")));
								AudioFormat audioFormat = audioInputStream
										.getFormat();
								int bufferSize = (int) Math.min(
										audioInputStream.getFrameLength()
												* audioFormat.getFrameSize(),
										Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
								DataLine.Info dataLineInfo = new DataLine.Info(
										Clip.class, audioFormat, bufferSize);
								Clip clip = (Clip) AudioSystem
										.getLine(dataLineInfo);
								clip.open(audioInputStream);
								clip.start();
							} catch (Exception ex) {

							}
						}
					}
				}

				try {

					int tempsleep = sleeptime;
					if (speedcheck[1])
						tempsleep /= 2;
					if (speedcheck[2])
						tempsleep /= 4;

					Thread.sleep(tempsleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		// ////////////////////////////////////////////////////////
		private void checkForPaused() {
			synchronized (checkk) {
				while (pauseThreadFlag) {
					try {
						checkk.wait();
					} catch (Exception e) {
					}
				}
			}
		}

		public static void resumeThread() {
			synchronized (checkk) {
				pauseThreadFlag = false;
				checkk.notify();
			}
		}

		public static void pauseThread() throws InterruptedException {
			now = 0;
			pauseThreadFlag = true;
		}
		// /////////////////////////////////////////////////

	}
	static public void music (String path){
		try {

			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream((new File(path)));
			AudioFormat audioFormat = audioInputStream.getFormat();
			int bufferSize = (int) Math.min(audioInputStream.getFrameLength()* audioFormat.getFrameSize(),Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
			DataLine.Info dataLineInfo = new DataLine.Info(Clip.class, audioFormat, bufferSize);
			Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {

		}
	}

	
}
