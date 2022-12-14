package frames;

import models.MakaoTalk;
import models.User;
import utils.OverlapValidator;
import utils.loader.UserLoader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

public class FriendAddFrame extends JFrame {
    private JTextField inputNameField;
    private JTextField inputPhoneNumberField;
    private JPanel addFriendPanel;
    private JPanel inputFriendsInformationPanel;
    private JTextField inputIdField;

    private String mode = "PhoneNumber";
    private final MakaoTalk makaoTalk;
    private JPanel contentPanel;

    public FriendAddFrame(MakaoTalk makaoTalk, JPanel contentPanel) {
        this.makaoTalk = makaoTalk;
        this.contentPanel = contentPanel;

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(300, 420);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(addFriendPanel());
    }

    private JPanel addFriendPanel() {
        addFriendPanel = new JPanel();
        addFriendPanel.setBackground(new Color(45,45,45));
        addFriendPanel.setLayout(new BorderLayout());
        addFriendPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        addFriendPanel.add(addFriendMenuPanel(), BorderLayout.NORTH);
        addFriendPanel.add(inputFriendsInformationPanel());
        addFriendPanel.add(addFriendButton(), BorderLayout.SOUTH);
        return addFriendPanel;
    }

    private JPanel addFriendMenuPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 70));
        panel.setLayout(new GridLayout(2, 1));
        panel.add(addFriendTitleLabel());
        panel.add(friendRequestPanel());
        return panel;
    }

    private JLabel addFriendTitleLabel() {
        JLabel label = new JLabel("?????? ??????");
        label.setFont(new Font("Serif", Font.BOLD, 16));
        label.setForeground(new Color(0xEFEBEB));
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    private JPanel friendRequestPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(1, 2));
        panel.add(addFriendByPhoneNumberButton());
        panel.add(addFriendByIdButton());
        return panel;
    }

    private JButton addFriendByPhoneNumberButton() {
        JButton button = new JButton("?????????");
        button.addActionListener(event -> {
            mode = "PhoneNumber";

            inputFriendsInformationPanel.removeAll();
            inputFriendsInformationPanel.add(inputInformationPanel(), BorderLayout.NORTH);
            inputFriendsInformationPanel.add(descriptionPanel("????????? ????????? ??????????????? ??????????????????."));
            updateDisplay();
        });
        return button;
    }

    private JButton addFriendByIdButton() {
        JButton button = new JButton("ID");
        button.addActionListener(event -> {
            mode = "ID";

            inputFriendsInformationPanel.removeAll();
            inputFriendsInformationPanel.add(inputIdPanel(), BorderLayout.NORTH);
            inputFriendsInformationPanel.add(descriptionPanel("????????? ID??? ??????????????????"));

            updateDisplay();
        });
        return button;
    }

    private void updateDisplay() {
        addFriendPanel.setVisible(false);
        addFriendPanel.setVisible(true);
        this.setVisible(true);
    }

    private JPanel inputIdPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 80));
        panel.add(inputIdField());
        return panel;
    }

    private JTextField inputIdField() {
        inputIdField = new JTextField(10);
        inputIdField.setText("?????? ???????????? ID");
        return inputIdField;
    }

    private JPanel inputFriendsInformationPanel() {
        inputFriendsInformationPanel = new JPanel();
        inputFriendsInformationPanel.setOpaque(false);
        inputFriendsInformationPanel.setLayout(new BorderLayout());
        inputFriendsInformationPanel.add(inputInformationPanel(), BorderLayout.NORTH);
        inputFriendsInformationPanel.add(descriptionPanel("????????? ????????? ??????????????? ??????????????????."));
        return inputFriendsInformationPanel;
    }

    private JPanel inputInformationPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 80));
        panel.setLayout(new GridLayout(2, 1));
        panel.add(inputNameField());
        panel.add(inputPhoneNumberField());
        return panel;
    }

    private JTextField inputNameField() {
        inputNameField = new JTextField(10);
        inputNameField.setText("?????? ??????");
        return inputNameField;
    }

    private JTextField inputPhoneNumberField() {
        inputPhoneNumberField = new JTextField(10);
        inputPhoneNumberField.setText("?????? ??????");
        return inputPhoneNumberField;
    }

    private JPanel descriptionPanel(String description) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(descriptionLabel(description));
        return panel;
    }

    private JLabel descriptionLabel(String description) {
        JLabel label = new JLabel(description);
        label.setForeground(new Color(0x9B9B9B));
        label.setFont(new Font("Serif", Font.PLAIN, 10));
        return label;
    }

    private JButton addFriendButton() {
        JButton button = new JButton("?????? ??????");
        button.setPreferredSize(new Dimension(0, 50));
        button.addActionListener(event -> {
            User loginUser = makaoTalk.user(makaoTalk.loginUserId());
            if (mode.equals("ID")) {
                String userName = inputIdField.getText();

                for (User user : makaoTalk.undeletedUsers()) {
                    if (userName.equals(user.userName()) && !userName.equals(loginUser.userName())) {
                        // TODO : ?????? ??????, ?????? ????????? ????????? ?????? ???????????? ??????.
                        addFriend(user);
                    }
                }
            }

            if (mode.equals("PhoneNumber")) {
                String name = inputNameField.getText();
                String phoneNumber = inputPhoneNumberField.getText();

                for (User user : makaoTalk.undeletedUsers()) {
                    if (name.equals(user.name()) &&
                            !name.equals(loginUser.name()) &&
                            phoneNumber.equals(user.phoneNumber())&&
                            !phoneNumber.equals(loginUser.phoneNumber())) {
                        addFriend(user);
                    }
                }
            }
        });
        return button;
    }

    private void addFriend(User friend) {
        OverlapValidator overlapVaildator = new OverlapValidator();
        boolean friendAlreadyExist = overlapVaildator.
                validateUserRelations(friend, makaoTalk);

        if (!friendAlreadyExist) {
            makaoTalk.relation().requestFriend(friend.id());

            UserLoader userLoader = new UserLoader();

            try {
                userLoader.saveUsersRelations(makaoTalk.relation().usersRelations());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            JFrame alertFrame = new AlertFrame(friend.name() + "?????? ?????????????????????.");
            alertFrame.setVisible(true);
            dispose();

            contentPanel.setVisible(false);
            contentPanel.setVisible(true);
        }

        if (friendAlreadyExist) {
            JFrame alertFrame = new AlertFrame(friend.name() + "?????? ?????? ???????????????.");
            alertFrame.setVisible(true);
        }
    }
}
