import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class guessNumber {
	int round = 1;
	int ranumber = 0;
	int nextNumber = 0;
	boolean checkwin = false;
	int randomTempo = 0;
	int[] selection = new int[4];
	Random ran = new Random();

	// ///////////////////////////////////////////////////////////////////////////////
	public int newGame() {
		if (round == 1) {
			ranumber = ran.nextInt(100);
			nextNumber = ran.nextInt(100);

			return ranumber;
		} else {
			ranumber = nextNumber;
			nextNumber = ran.nextInt(100);
			return ranumber;
		}
	}

	void checkbigbutton() {
		if (nextNumber >= ranumber) {
			winsound();
			round++;
			JOptionPane.showMessageDialog(null, "猜對了 ! 數字是 " + nextNumber
					+ " 往下一回合前進", "恭喜!!", JOptionPane.PLAIN_MESSAGE);
			checkwin = true;

		} else {
			round = 1;
			defeatsound();
			JOptionPane.showMessageDialog(null, "猜錯了 ! 數字是 " + nextNumber
					+ "必須回到第一回合", "可惜!!", JOptionPane.WARNING_MESSAGE);
			checkwin = false;
		}
	}

	void checksmallbutton() {
		if (nextNumber <= ranumber) {
			winsound();
			round++;
			JOptionPane.showMessageDialog(null, "猜對了 ! 數字是 " + nextNumber
					+ "往下一回合前進", "恭喜!!", JOptionPane.PLAIN_MESSAGE);
			checkwin = true;

		} else {
			defeatsound();
			round = 1;
			JOptionPane.showMessageDialog(null, "猜錯了 ! 數字是 " + nextNumber
					+ " 必須回到第一回合", "可惜!!", JOptionPane.WARNING_MESSAGE);
			checkwin = false;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	void newGame2() {
		boolean[] ifselect = { false, false, false, false };
		boolean answer = true;
		randomTempo = ran.nextInt(249) + 20;
		int count = 0;

		while (count != 4) {
			int i = ran.nextInt(4);

			if (!ifselect[i] && answer) {
				selection[i] = randomTempo;
				ifselect[i] = true;
				answer = false;
				count++;
			}
			if (!ifselect[i] && count < 2) {

				selection[i] = randomTempo + ran.nextInt(50);
				ifselect[i] = true;
				count++;
			}
			if (!ifselect[i]) {
				
				do{
				selection[i] = randomTempo - ran.nextInt(50);
				}
				while(selection[i]<=0);
				
				
				ifselect[i] = true;
				count++;
			}

		}

	}

	boolean checkwin(int choose) {
		choose--;
		if (selection[choose] == randomTempo) {
			round++;
			winsound();
			JOptionPane.showMessageDialog(null, "答對了", "恭喜!!",
					JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
		defeatsound();
		JOptionPane.showMessageDialog(null, "答錯了!!再試一次", "可惜",
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

	// ////////////////////////////////////////////////////////////////
	
	String gameText(JButton abc) {
		if (round <= 6) {
			if (checkwin) {
				String show = "目前第  " + round + " 回合\n數字是 \" " + newGame()
						+ "\"\n 請問你猜測下一個數字比他大還比她小?";
				return show;
			} else {
				String show = "接下來會出現一個數字\n你必須玩過\"六\"回合之後才能繼續 \n目前第 " + round
						+ " 回合\n數字是 \" " + newGame()
						+ "\"\n 請問你猜測下一個數字比他大還比她小?";
				return show;
			}
		} else if(round>=7&&round<=12){
			newGame2();
			String show = "接下來變得有點難 你必須要猜接下來的拍子\n第\""+round+"\"回合\n選擇以下四個選項中的其中一個\n1. "
					+ selection[0] + "\n2. " + selection[1] + "\n3. "
					+ selection[2] + "\n4. " + selection[3];
			return show;
		}else{
			JOptionPane.showMessageDialog(null, "挖! 正詳對你另眼相看耶", "恭喜!!",
					JOptionPane.INFORMATION_MESSAGE,new ImageIcon("src/candy.jpg"));
			
			String show = "恭喜你 完成這一連串的小遊戲\n綠巨人很開心!!!\n快專心對節拍器吧!!!!!!";
			abc.setEnabled(false);
			return show;
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	void winsound() {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream((new File("src\\Victory.wav")));
			AudioFormat audioFormat = audioInputStream.getFormat();
			int bufferSize = (int) Math.min(audioInputStream.getFrameLength()
					* audioFormat.getFrameSize(), Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
			DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,
					audioFormat, bufferSize);
			Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {

		}
	}

	void defeatsound() {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream((new File("src\\Defeat.wav")));
			AudioFormat audioFormat = audioInputStream.getFormat();
			int bufferSize = (int) Math.min(audioInputStream.getFrameLength()
					* audioFormat.getFrameSize(), Integer.MAX_VALUE); // 緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
			DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,
					audioFormat, bufferSize);
			Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {

		}
	}

}
