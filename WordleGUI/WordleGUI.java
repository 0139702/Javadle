/**
 *Wordle Game "Javadle(자바들)" ver.2
 *자바 프로그래밍 수업 시간에 나왔거나 자바 프로그래밍 언어와 관련된 
*6글자 단어를 맞추는 단어 퍼즐 게임입니다.
 *
 *정답 단어의 알파벳 종류와 위치가 모두 일치하는 경우 초록색,
 *정답 단어에 들어가는 알파벳이지만 위치가 불일치하는 경우 노란색,
 *그리고 정답 단어의 알파벳이 아니라면 회색을 표시합니다.

 *제출일: 2024년06월14일
 *제출번호: 기말고사 발표 과제_최종 구현본
**/

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WordleGUI extends JFrame {
    private JPanel inputPanel, guessPanel, keyboardPanel;
    private List<String> words;
    private String answer;
    private int tries;
    private StringBuilder currentGuess;
    private JLabel[][] guessBoxes = new JLabel[6][6]; //6x6의 정사각형 박스 배열
    private JLabel[] keyboardBoxes; //키보드 버튼 배열

    public WordleGUI() {
        setTitle("Javadle Game");
        setSize(500, 700); //패널이 아래쪽으로 확장되도록 크기 조정
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputPanel = new JPanel();
        guessPanel = new JPanel(new GridLayout(6, 6)); //6x6 박스를 표시하는 패널 생성
        keyboardPanel = new JPanel(new GridLayout(3, 1)); //키보드 패널 생성
        setVisible(true);
        setLocationRelativeTo(null);

        Font Pretendard = new Font("Pretendard", Font.BOLD, 24);
        Font littlePretendard = new Font("Pretendard", Font.BOLD, 18);

        //6x6 박스를 미리 생성
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                guessBoxes[i][j] = new JLabel("", SwingConstants.CENTER);
                guessBoxes[i][j].setOpaque(true); //박스를 채우기 위해 투명도 설정
                guessBoxes[i][j].setPreferredSize(new Dimension(60, 60)); //박스 크기 설정
                guessBoxes[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE)); //박스 테두리 설정
                guessBoxes[i][j].setFont(Pretendard); //커스텀 폰트 설정
                guessBoxes[i][j].setBackground(new Color(226, 232, 240, 255));
                guessBoxes[i][j].setForeground(Color.BLACK); //텍스트 색상 기본 설정
                guessPanel.add(guessBoxes[i][j]); //패널에 박스 추가
            }
        }

        String[] keyboardRows = {"QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM"}; //키보드 배열
        keyboardBoxes = new JLabel[26]; //키보드 박스를 담을 배열
        int index = 0;

        //키보드 버튼 생성
        for (String row : keyboardRows) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); //중간 정렬
            for (char key : row.toCharArray()) {
                JLabel keyLabel = new JLabel(String.valueOf(key), SwingConstants.CENTER);
                keyLabel.setFont(littlePretendard); //커스텀 폰트 설정
                keyLabel.setForeground(Color.BLACK); //기본 텍스트 색상 검정색
                keyLabel.setPreferredSize(new Dimension(40, 40)); //박스 크기 설정
                keyLabel.setOpaque(true); //배경색 변경을 위해 투명도 설정
                keyLabel.setBackground(Color.WHITE);
                keyboardBoxes[index++] = keyLabel;
                rowPanel.add(keyLabel); //패널에 박스 추가
            }
            keyboardPanel.add(rowPanel); //키보드 패널에 행 패널 추가
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = Character.toUpperCase(e.getKeyChar());
                if (currentGuess != null && currentGuess.length() < 6 && Character.isLetter(keyChar)) {
                    currentGuess.append(keyChar);
                    guessBoxes[tries][currentGuess.length() - 1].setText(String.valueOf(keyChar));
                    guessBoxes[tries][currentGuess.length() - 1].setForeground(Color.BLACK); //텍스트 색상 입력 중일 때 검은색으로 변경
                }
                if (currentGuess != null && currentGuess.length() == 6) {
                    createFeedbackBoxes();
                    currentGuess.setLength(0);
                }
            }
        });

        inputPanel.add(new JLabel("Javadle (Java Word Game)"));
        inputPanel.setFont(Pretendard);
        inputPanel.setBackground(Color.WHITE);
        add(inputPanel, BorderLayout.NORTH);
        
        add(new JScrollPane(guessPanel), BorderLayout.CENTER); //guessPanel을 스크롤 가능하도록 변경
        add(keyboardPanel, BorderLayout.SOUTH); //키보드 패널을 하단에 추가

        currentGuess = new StringBuilder(); //새 게임을 시작할 때만 리셋
        initializeWords(); //단어 리스트 초기화 및 무작위로 정답을 선택

        setFocusable(true);
        requestFocusInWindow(); //포커스 설정
    }

    private void initializeWords() {
        words = new ArrayList<>();
        words.add("arrays");
        words.add("append");
        words.add("buffer");
        words.add("button");
        words.add("coding");
        words.add("double");
        words.add("delete");
        words.add("equals");
        words.add("random");
        words.add("result");
        words.add("remove");
        words.add("method");
        words.add("number");
        words.add("length");
        words.add("object");
        words.add("public");
        words.add("string");
        words.add("search");
        words.add("thread");
        words.add("static");
        words.add("import");
        words.add("target");
        words.add("throws");
        words.add("turing");
        words.add("symbol");
        words.add("vector");
        
        // 단어 목록에서 무작위로 정답 선택
        Random random = new Random();
        answer = words.get(random.nextInt(words.size())).toUpperCase(); //정답 단어도 대문자로 변환
    }

    private void createFeedbackBoxes() {
        String input = currentGuess.toString();
        for (int i = 0; i < 6; i++) {
            guessBoxes[tries][i].setText("" + input.charAt(i));
            guessBoxes[tries][i].setForeground(Color.WHITE); //텍스트 색상을 항상 흰색으로 설정

            Color chorok = new Color(34, 197, 94, 255);
            Color norang = new Color(234, 179, 8, 255);
            Color grey = new Color(148, 163, 184, 255);

            if (input.charAt(i) == answer.charAt(i)) {
                guessBoxes[tries][i].setBackground(chorok); //정답과 위치가 일치할 경우 초록색
                updateKeyboardColor(input.charAt(i), chorok);
            } else if (answer.indexOf(input.charAt(i)) != -1) { //사용자 입력 문자가 정답에 포함되어 있으면
                guessBoxes[tries][i].setBackground(norang); //위치는 틀렸지만 문자가 일치할 경우 노란색
                updateKeyboardColor(input.charAt(i), chorok);
            } else {
                guessBoxes[tries][i].setBackground(grey); //그 외의 경우 회색
                updateKeyboardColor(input.charAt(i), grey);
            }
        }

        tries++;

        if (input.equals(answer)) {
            JOptionPane.showMessageDialog(null, "Congratulations! You've guessed the word.", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        } else if (tries >= 6 && !input.equals(answer)) {
            JOptionPane.showMessageDialog(null, "Game over! The correct word was " + answer, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateKeyboardColor(char key, Color color) {
        for (JLabel keyLabel : keyboardBoxes) {
            if (keyLabel.getText().charAt(0) == key) {
                keyLabel.setBackground(color);
                keyLabel.setForeground(Color.WHITE); //색상 변경 시 텍스트 색상 흰색으로 변경
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WordleGUI().setVisible(true);
            }
        });
    }
}
