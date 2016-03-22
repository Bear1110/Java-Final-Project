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
			JOptionPane.showMessageDialog(null, "�q��F ! �Ʀr�O " + nextNumber
					+ " ���U�@�^�X�e�i", "����!!", JOptionPane.PLAIN_MESSAGE);
			checkwin = true;

		} else {
			round = 1;
			defeatsound();
			JOptionPane.showMessageDialog(null, "�q���F ! �Ʀr�O " + nextNumber
					+ "�����^��Ĥ@�^�X", "�i��!!", JOptionPane.WARNING_MESSAGE);
			checkwin = false;
		}
	}

	void checksmallbutton() {
		if (nextNumber <= ranumber) {
			winsound();
			round++;
			JOptionPane.showMessageDialog(null, "�q��F ! �Ʀr�O " + nextNumber
					+ "���U�@�^�X�e�i", "����!!", JOptionPane.PLAIN_MESSAGE);
			checkwin = true;

		} else {
			defeatsound();
			round = 1;
			JOptionPane.showMessageDialog(null, "�q���F ! �Ʀr�O " + nextNumber
					+ " �����^��Ĥ@�^�X", "�i��!!", JOptionPane.WARNING_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "����F", "����!!",
					JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
		defeatsound();
		JOptionPane.showMessageDialog(null, "�����F!!�A�դ@��", "�i��",
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

	// ////////////////////////////////////////////////////////////////
	
	String gameText(JButton abc) {
		if (round <= 6) {
			if (checkwin) {
				String show = "�ثe��  " + round + " �^�X\n�Ʀr�O \" " + newGame()
						+ "\"\n �аݧA�q���U�@�ӼƦr��L�j�٤�o�p?";
				return show;
			} else {
				String show = "���U�ӷ|�X�{�@�ӼƦr\n�A�������L\"��\"�^�X����~���~�� \n�ثe�� " + round
						+ " �^�X\n�Ʀr�O \" " + newGame()
						+ "\"\n �аݧA�q���U�@�ӼƦr��L�j�٤�o�p?";
				return show;
			}
		} else if(round>=7&&round<=12){
			newGame2();
			String show = "���U���ܱo���I�� �A�����n�q���U�Ӫ���l\n��\""+round+"\"�^�X\n��ܥH�U�|�ӿﶵ�����䤤�@��\n1. "
					+ selection[0] + "\n2. " + selection[1] + "\n3. "
					+ selection[2] + "\n4. " + selection[3];
			return show;
		}else{
			JOptionPane.showMessageDialog(null, "��! ���Թ�A�t���۬ݭC", "����!!",
					JOptionPane.INFORMATION_MESSAGE,new ImageIcon("src/candy.jpg"));
			
			String show = "���ߧA �����o�@�s�ꪺ�p�C��\n�񥨤H�ܶ}��!!!\n�ֱM�߹�`�羹�a!!!!!!";
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
					* audioFormat.getFrameSize(), Integer.MAX_VALUE); // �w�Ĥj�p�A�p�G���T�ɮפ��j�A�i�H�����s�J�w�ĪŶ��C�o�Ӽƭ����ӭn���ӥγ~�ӨM�w
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
					* audioFormat.getFrameSize(), Integer.MAX_VALUE); // �w�Ĥj�p�A�p�G���T�ɮפ��j�A�i�H�����s�J�w�ĪŶ��C�o�Ӽƭ����ӭn���ӥγ~�ӨM�w
			DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,
					audioFormat, bufferSize);
			Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {

		}
	}

}
