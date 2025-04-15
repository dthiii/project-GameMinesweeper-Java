/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gameminesweeper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Bong cai xanh
 */
public class GameMinesweeper extends JFrame {

    private JMenuBar menuBar;
    private JMenu menuLevel;
    private JMenuItem easyItem, mediumItem, hardItem;
    private JMenuItem exitItem;
    private JPanel pnlInfor;
    private JPanel pnlBody;
    private JLabel lbFlag;
    private JLabel lbScore;
    private JTextField txtFlag;
    private JTextField txtScore;

    private int points = 0;
    private byte[][] positions;
    private JButton[][] buttonList;

    public GameMinesweeper() {
        setTitle("Minesweeper");
        setSize(250, 340);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);

        //MenuBar
        menuBar = new JMenuBar();
        menuLevel = new JMenu("Level");

        easyItem = new JMenuItem("Easy");
        mediumItem = new JMenuItem("Medium");
        hardItem = new JMenuItem("Hard");

        easyItem.addActionListener(e -> setupGame(9, 9, 10));
        mediumItem.addActionListener(e -> setupGame(16, 16, 40));
        hardItem.addActionListener(e -> setupGame(16, 30, 99));

        menuLevel.add(easyItem);
        menuLevel.add(mediumItem);
        menuLevel.add(hardItem);

        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        menuBar.add(menuLevel);
        menuBar.add(exitItem);

        //Panel pnlInfor
        pnlInfor = new JPanel();
        pnlInfor.setLayout(null);
        pnlInfor.setBounds(0, 0, 308, 46);
        pnlInfor.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        //Label lbFlag
        lbFlag = new JLabel("Flag:");
        lbFlag.setBounds(10, 20, 45, 22);
        lbFlag.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 15));
        pnlInfor.add(lbFlag);

        //Label lbScore
        lbScore = new JLabel("Score:");
        lbScore.setBounds(120, 20, 60, 22);
        lbScore.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 15));
        pnlInfor.add(lbScore);

        //TextField txtFlag
        txtFlag = new JTextField();
        txtFlag.setBounds(55, 7, 50, 35);
        txtFlag.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 15));
        txtFlag.setForeground(Color.RED);
        txtFlag.setHorizontalAlignment(JTextField.CENTER);
        txtFlag.setEditable(false);
        txtFlag.setFocusable(false);
        pnlInfor.add(txtFlag);

        //TextField txtScore
        txtScore = new JTextField();
        txtScore.setBounds(180, 7, 50, 35);
        txtScore.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 15));
        txtScore.setForeground(Color.RED);
        txtScore.setHorizontalAlignment(JTextField.CENTER);
        txtScore.setEditable(false);
        txtScore.setFocusable(false);
        pnlInfor.add(txtScore);

        //Panel pnlBody
        pnlBody = new JPanel();
        pnlBody.setBounds(5, 50, 308, 259);
        pnlBody.setLayout(null);

        setJMenuBar(menuBar);
        add(pnlInfor);
        add(pnlBody);
        setVisible(true);
    }

    //setup game
    private void setupGame(int rows, int cols, int mines) {
        pnlBody.removeAll();
        pnlBody.repaint();

        points = 0;
        txtScore.setText("0");
        txtFlag.setText(String.valueOf(mines));

        positions = new byte[rows][cols];
        buttonList = new JButton[rows][cols];

        GenerateMines(rows, cols, mines);
        GeneratePositionValue(rows, cols);
        GenerateButtons(rows, cols);

        //Tính kích thước cho pnlBody
        int btnSize = 25;
        int pnlWidth = cols * btnSize;
        int pnlHeight = rows * btnSize;

        pnlBody.setPreferredSize(new Dimension(pnlWidth, pnlHeight));
        pnlBody.setSize(pnlWidth, pnlHeight);

        int frameWidth = pnlWidth + 25;
        int frameHeight = pnlHeight + 115;
        setSize(frameWidth, frameHeight);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    //đặt mìn
    private void GenerateMines(int rows, int cols, int mines) {
        Random random = new Random();
        int countMines = 0;
        while (countMines < mines) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);
            if (positions[x][y] == 0) {
                positions[x][y] = 10;
                countMines++;
            }
        }
    }

    //tạo giá trị cho các ô không chứa mìn
    private void GeneratePositionValue(int rows, int cols) {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                if (positions[x][y] == 10) {
                    continue;
                }

                byte mineCounts = 0;
                for (int counterX = -1; counterX <= 1; counterX++) {
                    int checkerX = x + counterX;
                    for (int counterY = -1; counterY <= 1; counterY++) {
                        int checkerY = y + counterY;

                        if (checkerX == -1 || checkerY == -1 || checkerX > rows - 1 || checkerY > cols - 1) //các ô nằm ngoài giới hạn bảng trò chơi
                            continue;

                        if (checkerX == x && checkerY == y) //đây là ô đang xét để đặt giá trị
                            continue;

                        if (positions[checkerX][checkerY] == 10) 
                            mineCounts++;
                    }
                }

                if (mineCounts == 0) {
                    positions[x][y] = 20; //ô trống
                } else {
                    positions[x][y] = mineCounts;
                }
            }
        }
    }

    //tạo các ô trong bảng trò chơi
    private void GenerateButtons(int rows, int cols) {
        int btnWidth = 25;
        int btnHeight = 25;

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                JButton btn = new JButton();
                btn.setBounds(y * btnWidth, x * btnHeight, btnWidth, btnHeight);
                btn.setMargin(new Insets(0, 0, 0, 0));
                int finalX = x;
                int finalY = y; //Do Java yêu cầu biến sử dụng trong lambda hoặc anonymous class phải là final hoặc effectively final, nên bạn cần tạo bản sao finalX và finalY để dùng trong MouseListener.

                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    //Ghi đè phương thức mouseClicked để xử lý khi người chơi nhấp chuột.
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            toggleFlag(btn); //Nếu người chơi nhấn chuột phải → gắn hoặc gỡ cờ trên ô này (chức năng đánh dấu mìn)
                        } else {
                            revealTile(btn, finalX, finalY); //Nếu nhấn chuột trái → mở ô tại vị trí (finalX, finalY)
                        }
                    }
                });

                pnlBody.add(btn);
                buttonList[x][y] = btn;
            }
        }
    }

    //Hàm xử lý khi click chuột phải vào ô trò chơi
    private void toggleFlag(JButton btn) {
        int flags = Integer.parseInt(txtFlag.getText());
        if (btn.getIcon() == null && btn.isEnabled() && flags > 0) {
            btn.setIcon(new ImageIcon(getClass().getResource("/resources/flag.PNG")));
            flags--;
        } else if (btn.getIcon() != null) {
            btn.setIcon(null);
            flags++;
        }
        txtFlag.setText(String.valueOf(flags));
    }

    //Hàm xử lý khi click chuột trái vào ô trò chơi
    private void revealTile(JButton btn, int x, int y) {        
        if (!btn.isEnabled())
            return;
        
        if (positions[x][y] == 10) {
            btn.setIcon(new ImageIcon(getClass().getResource("/resources/mine.PNG")));
            btn.setForeground(Color.BLACK);           
            ShowAllMines();
            JOptionPane.showMessageDialog(this, "GameOver");            
            pnlBody.setEnabled(false);
            disableAllButtonsInPanel(pnlBody);
        } else if (positions[x][y] == 20) {
            btn.setEnabled(false);
            btn.setText("");
            btn.setForeground(Color.BLACK);
            points++;
            OpenAdjacentEmptyTile(x, y);
        } else {
            btn.setEnabled(false);
            btn.setText(String.valueOf(positions[x][y]));
            btn.setForeground(Color.BLACK);
            points++;
        }

        //Loại bỏ MouseListener
        MouseListener[] listeners = btn.getMouseListeners();
        for (MouseListener l : listeners) {
            btn.removeMouseListener(l);
        }

        txtScore.setText(String.valueOf(points));

        int mineCount = 0;
        for (byte[] row : positions) {
            for (byte cell : row) {
                if (cell == 10) {
                    mineCount++;
                }
            }
        }

        if (points == positions.length * positions[0].length - mineCount) {
            ShowAllMines();
            JOptionPane.showMessageDialog(this, "You win");
            pnlBody.setEnabled(false);
            disableAllButtonsInPanel(pnlBody);
        }
    }

    private void ShowAllMines() {
        for (int x = 0; x < positions.length; x++) {
            for (int y = 0; y < positions[0].length; y++) {
                if (positions[x][y] == 10) {
                    buttonList[x][y].setIcon(new ImageIcon(getClass().getResource("/resources/mine.PNG")));
                }
            }
        }
    }

    private void OpenAdjacentEmptyTile(int x, int y) {
        for (int counterX = -1; counterX <= 1; counterX++) {
            int checkerX = x + counterX;
            
            for (int counterY = -1; counterY <= 1; counterY++) {
                int checkerY = y + counterY;
                
                if (checkerX >= 0 && checkerY >= 0 && checkerX < positions.length && checkerY < positions[0].length) {
                    JButton btn = buttonList[checkerX][checkerY];
                    if (!btn.isEnabled()) {
                        continue;
                    }

                    if (positions[checkerX][checkerY] == 20) {
                        btn.setEnabled(false);
                        btn.setText("");
                        points++;
                        OpenAdjacentEmptyTile(checkerX, checkerY);
                        
                    } else if (positions[checkerX][checkerY] != 10 && btn.getText().equals("")) {
                        btn.setText(String.valueOf(positions[checkerX][checkerY]));
                        btn.setEnabled(false);
                        points++;
                    }
                }
            }
        }
    }

    private void disableAllButtonsInPanel(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton) 
                component.setEnabled(false);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMinesweeper::new);
    }
}
