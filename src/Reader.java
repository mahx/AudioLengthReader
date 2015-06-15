import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Reader extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JButton selBtn;

    private JButton selResultBtn;

    private JButton doWork;

    private ActionListener selAudioFileActionListener;

    private ActionListener selResultFileActionListener;

    private ActionListener doWorkActionListener;

    JFileChooser jfc;

    JLabel resultLabel;

    String selAudioPath;

    String selResultPath;

    String resultFile = "traceLength.txt";

    BufferedWriter bw = null;

    private void initWriter() throws IOException {
        File tmp = new File(selResultPath + File.separator + resultFile);
        tmp.delete();
        tmp.createNewFile();
        FileWriter resultFileWriter = new FileWriter(tmp, true);
        bw = new BufferedWriter(resultFileWriter);
    }

    private void writeFile(String fileName, int traceLen) throws IOException {
        System.out.println(fileName + " " + traceLen);
        bw.write(fileName + " " + traceLen);
        bw.newLine();
        bw.flush();
    }

    private void closeWriter() {
        if (bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Reader() {
        this.setSize(400, 200);
        this.setTitle("获取音频时长");
        init();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void setDoWorkButtonCickStatus() {
        if (selAudioPath != null && new File(selAudioPath).exists() && selResultPath != null
                && new File(selResultPath).exists()) {
            doWork.setEnabled(true);
        } else {
            doWork.setEnabled(false);
        }
    }

    private int getTraceLength(File file) throws CannotReadException, IOException, TagException,
            ReadOnlyFileException, InvalidAudioFrameException {
        AudioFile af = AudioFileIO.read(file);
        AudioHeader ah = af.getAudioHeader();
        return ah.getTrackLength();
    }

    public void init() {
        selBtn = new JButton("请选择音频文件夹/文件");
        selResultBtn = new JButton("请选择结果文件");
        doWork = new JButton("计算");
        jfc = new JFileChooser();
        resultLabel = new JLabel();

        selAudioFileActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showDialog(new JLabel(), "选择");
                File f = jfc.getSelectedFile();
                if (f == null) {
                    return;
                } else {
                    selAudioPath = f.getAbsolutePath();
                    selBtn.setText("音频文件／文件夹：" + selAudioPath);
                    setDoWorkButtonCickStatus();
                }
            }

        };

        selResultFileActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.showDialog(new JLabel(), "选择");
                File f = jfc.getSelectedFile();
                if (f == null) {
                    return;
                } else {
                    selResultPath = f.getAbsolutePath();
                    selResultBtn.setText("输出文件：" + selResultPath);
                    setDoWorkButtonCickStatus();
                }
            }

        };
        doWorkActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File audioFile = new File(selAudioPath);
                    initWriter();
                    if (audioFile.isDirectory()) {
                        for (File tmp : audioFile.listFiles()) {
                            writeFile(tmp.getName(), getTraceLength(tmp));
                        }
                    } else {
                        writeFile(audioFile.getName(), getTraceLength(audioFile));
                    }
                    resultLabel.setText("获取完毕");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    resultLabel.setText("错误发生");
                } finally {
                    closeWriter();
                }
            }

        };
        GridLayout myLayout = new GridLayout(4, 1);
        this.setLayout(myLayout);
        this.add(selBtn);
        this.add(selResultBtn);
        this.add(doWork);
        this.add(resultLabel);
        selBtn.addActionListener(selAudioFileActionListener);
        selResultBtn.addActionListener(selResultFileActionListener);
        doWork.addActionListener(doWorkActionListener);
        setDoWorkButtonCickStatus();
    }

    public static void main(String args[]) {
        new Reader();
    }
}
