package org.packages.GUI;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;


class signInPage{
    public void showSignInPage(){

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf.");
        }

        JFrame signInWindow = new JFrame("MakeDo - Sign in");
        signInWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signInWindow.setSize(325, 475);

        JPanel signInPanel = new JPanel(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        position.fill = GridBagConstraints.NONE;
        position.gridy = 0;
        position.gridx = 0;
        position.weightx = 0;
        position.insets = new Insets(1, 1, 0,1);
        position.anchor = GridBagConstraints.CENTER;


        ImageIcon signIN_img = new ImageIcon("G:\\Dev Softwares\\MakeDo\\assets\\user-light.png");
        Image scaledImage = signIN_img.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        JLabel signIN_imgLabel = new JLabel(new ImageIcon(scaledImage));
        signInPanel.add(signIN_imgLabel);


        JLabel spacer = new JLabel("      ");
        position.gridy = 0;
        signInPanel.add(spacer, position);

        JLabel spacer2 = new JLabel("      ");
        position.gridy = 1;
        signInPanel.add(spacer2, position);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 9, 9));

        JButton loginBtn = new JButton("Log In");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 12));
        loginBtn.addActionListener(e -> {
            userLogin userLogin = new userLogin();
            userLogin.showUserLogin();
            signInWindow.setVisible(false);
        });
        JLabel slashTxt = new JLabel("or");
        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.setFont(new Font("Arial", Font.BOLD, 12));
        signUpBtn.addActionListener(e -> {
            newSignUp signUp = new newSignUp();
            signUp.showNewSignUp();
            signInWindow.setVisible(false);
        });

        loginBtn.setBackground(Color.lightGray);
        signUpBtn.setBackground(Color.lightGray);

        buttonsPanel.add(loginBtn);
        buttonsPanel.add(slashTxt);
        buttonsPanel.add(signUpBtn);

        position.gridy = 2;
        signInPanel.add(buttonsPanel, position);

        JLabel dash = new JLabel("               ");
        position.gridy = 3;
        signInPanel.add(dash, position);


        JButton contAsGuest = new JButton("Continue as guest");
        contAsGuest.setBackground(Color.lightGray);
        contAsGuest.setFont(new Font("Arial", Font.BOLD, 10));
        contAsGuest.setPreferredSize(new Dimension(175,15));
        signInPanel.add(contAsGuest, position);

        Font fontMod = new Font("Arial", Font.PLAIN, 11);
        UIManager.put("OptionPane.messageFont", fontMod);

        contAsGuest.addActionListener(e ->
        {   int result = JOptionPane.showConfirmDialog(
                null,
                "You CAN'T sell or make purchases as a GUEST . \n             Do you want to proceed?",
                "Continue as Guest",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
            if(result == JOptionPane.YES_OPTION){
                mainPage mainPage = new mainPage();
                mainPage.showMainPage();
            }else if(result == JOptionPane.NO_OPTION){
                showSignInPage();
            }
        });

        Color originalBg = UIManager.getColor("Button.background");
        UIManager.put("Button.background", Color.LIGHT_GRAY);


        JPanel spacedOut = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 40));

        JLabel lang = new JLabel("Choose language ");
        lang.setFont(new Font("Arial", Font.PLAIN, 10));

        String[] languages = {"English", "Amharic"};
        JComboBox<String> langDropDown = new JComboBox<>(languages);
        langDropDown.setSelectedIndex(0);
        langDropDown.setPreferredSize(new Dimension(70, 18));
        langDropDown.setBackground(Color.lightGray);
        langDropDown.setForeground(Color.BLACK);
        langDropDown.setFont(new Font("Arial", Font.PLAIN, 11));

        spacedOut.add(lang);
        spacedOut.add(langDropDown);
        position.gridy= 4;
        signInPanel.add(spacedOut, position);

        JPanel spacedCombo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        JLabel nothing = new JLabel("   ");

        JLabel devCredit = new JLabel("Developed by Bonsa Dereje");
        devCredit.setFont(new Font("Arial", Font.PLAIN, 7));


        spacedCombo.add(nothing);
        spacedCombo.add(devCredit);
        position.gridy = 5;
        signInPanel.add(spacedCombo, position);



        signInWindow.add(signInPanel);
        signInWindow.setVisible(true);
        signInWindow.setLocationRelativeTo(null);
    }
}
class userLogin{
    public void showUserLogin(){
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf.");
        }

        JFrame loginPage = new JFrame("MakeDo - Log in");
        loginPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginPage.setSize(350, 500);


        JPanel logInPanel = new JPanel(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        position.fill = GridBagConstraints.NONE;
        position.gridy = 0;
        position.gridx = 0;
        position.weightx = 0;
        position.insets = new Insets(1, 1, 0,1);
        position.anchor = GridBagConstraints.WEST;

        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 7));

        JLabel login = new JLabel("Login");
        login.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel space = new JLabel(" ");

        ImageIcon loginImg = new ImageIcon("G:\\Dev Softwares\\MakeDo\\assets\\login.png");
        Image scaledImage = loginImg.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel signIN_imgLabel = new JLabel(new ImageIcon(scaledImage));

        subPanel.add(login);
        subPanel.add(space);
        subPanel.add(signIN_imgLabel);
        position.gridy = 0;
        logInPanel.add(subPanel, position);


        JLabel userName = new JLabel("Full Name");
        userName.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 1;
        logInPanel.add(userName, position);


        JTextField userNameIn = new JTextField(20);
        position.gridy = 2;
        logInPanel.add(userNameIn, position);

        JLabel password = new JLabel("Password");
        password.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 3;
        logInPanel.add(password, position);


        JPasswordField passwordIn = new JPasswordField(20);
        position.gridy = 4;
        logInPanel.add(passwordIn, position);
        position.anchor = GridBagConstraints.EAST;
        JButton forgotPass = new JButton("Forgot Password?");
        forgotPass.setFont(new Font("Arial", Font.PLAIN, 8));
        forgotPass.setPreferredSize(new Dimension(70, 10));
        forgotPass.setContentAreaFilled(false);
        forgotPass.setBorderPainted(false);
        forgotPass.setMargin(new Insets(0, 0, 0, 0));
        position.gridy = 5;
        logInPanel.add(forgotPass, position);
/*
        position.gridy = 0;
        logInPanel.add(subPanel, position);

 */
        JPanel credit = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 50));

        JLabel space2 = new JLabel(" ");
        position.gridy = 6;
        logInPanel.add(space2, position);

        position.anchor = GridBagConstraints.CENTER;
        JButton proceedLogin = new JButton("Login");
        proceedLogin.setBackground(Color.lightGray);
        proceedLogin.setFocusPainted(false);
        position.gridy = 7;
        logInPanel.add(proceedLogin, position);

        JPanel switchToLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 3));

        JLabel dontHave = new JLabel("Don't have an account?");
        dontHave.setFont(new Font("Arial", Font.PLAIN, 9));

        JButton signUpInstead = new JButton("Sign Up");
        signUpInstead.setFont(new Font("Arial", Font.BOLD, 11));
        signUpInstead.setContentAreaFilled(false);
        signUpInstead.setPreferredSize(new Dimension(47, 15));
        signUpInstead.setBorderPainted(false);
        signUpInstead.addActionListener(e -> {
            newSignUp signUp = new newSignUp();
            signUp.showNewSignUp();
            loginPage.setVisible(false);
        });
        signUpInstead.setMargin(new Insets(0, 0, 0, 0));

        switchToLogin.add(dontHave);
        switchToLogin.add(signUpInstead);
        position.gridy = 8;
        logInPanel.add(switchToLogin, position);


        JLabel nothing = new JLabel("   ");

        JPanel spacer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        spacer.add(nothing);
        position.gridy = 9;
        logInPanel.add(spacer, position);


        loginPage.add(logInPanel);
        loginPage.setVisible(true);
        loginPage.setLocationRelativeTo(null);
    }
}

class newSignUp{
    public void showNewSignUp(){

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf.");
        }

        JFrame signUpPage = new JFrame("MakeDo - Sign-Up");
        signUpPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpPage.setSize(350, 500);


        JPanel signUpPanel = new JPanel(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        position.fill = GridBagConstraints.NONE;
        position.gridy = 0;
        position.gridx = 0;
        position.weightx = 0;
        position.insets = new Insets(1, 1, 0,1);
        position.anchor = GridBagConstraints.WEST;

        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 7));

        JLabel login = new JLabel("SignUp");
        login.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel space = new JLabel("  ");

        ImageIcon loginImg = new ImageIcon("G:\\Dev Softwares\\MakeDo\\assets\\add-user.png");
        Image scaledImage = loginImg.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel signIN_imgLabel = new JLabel(new ImageIcon(scaledImage));

        subPanel.add(login);
        subPanel.add(space);
        subPanel.add(signIN_imgLabel);
        position.gridy = 0;
        signUpPanel.add(subPanel, position);


        JLabel userName = new JLabel("Full Name");
        userName.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 1;
            signUpPanel.add(userName, position);
        JTextField userNameIn = new JTextField(20);
        position.gridy = 2;
            signUpPanel.add(userNameIn, position);


        JLabel address = new JLabel("Address(City)");
        address.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 3;
            signUpPanel.add(address, position);
        JTextField addressIn = new JTextField(20);
        position.gridy = 4;
        signUpPanel.add(addressIn, position);

        JLabel idNo = new JLabel("National ID No");
        idNo.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 5;
            signUpPanel.add(idNo, position);
        JTextField idNoIn = new JTextField(20);
        position.gridy = 6;
            signUpPanel.add(idNoIn, position);

        JLabel phoneNo = new JLabel("Phone Number");
        phoneNo.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 7;
            signUpPanel.add(phoneNo, position);
        JTextField phoneNoIn = new JTextField(20);
        position.gridy = 8;
            signUpPanel.add(phoneNoIn, position);

        JLabel password = new JLabel("Password");
        password.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 9;
            signUpPanel.add(password, position);
        JPasswordField passwordIn = new JPasswordField(20);
        position.gridy = 10;
            signUpPanel.add(passwordIn, position);

        JLabel confirmPassword = new JLabel("Confirm Password");
        confirmPassword.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 11;
            signUpPanel.add(confirmPassword, position);
        JPasswordField confrimPasswordIn = new JPasswordField(20);
        position.gridy = 12;
            signUpPanel.add(confrimPasswordIn, position);

        JLabel space2 = new JLabel(" ");
        position.gridy = 13;
            signUpPanel.add(space2, position);

        position.anchor = GridBagConstraints.CENTER;
        JButton proceedLogin = new JButton("Sign UP");
        proceedLogin.setBackground(Color.lightGray);
        proceedLogin.setFocusPainted(false);
        position.gridy = 14;
        signUpPanel.add(proceedLogin, position);

        JPanel switchToLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 3));

        JLabel alreadyHave = new JLabel("Already have an account?");
        alreadyHave.setFont(new Font("Arial", Font.PLAIN, 9));

        JButton signUpInstead = new JButton("Log In");
        signUpInstead.setFont(new Font("Arial", Font.BOLD, 11));
        signUpInstead.setContentAreaFilled(false);
        signUpInstead.setPreferredSize(new Dimension(47, 15));
        signUpInstead.setBorderPainted(false);
        signUpInstead.addActionListener(e -> {
            userLogin userLogin = new userLogin();
            userLogin.showUserLogin();
            signUpInstead.setVisible(false);
        });
        signUpInstead.setMargin(new Insets(0, 0, 0, 0));

        switchToLogin.add(alreadyHave);
        switchToLogin.add(signUpInstead);
        position.gridy = 15;
        signUpPanel.add(switchToLogin, position);

        signUpPage.add(signUpPanel);
        signUpPage.setVisible(true);
        signUpPage.setLocationRelativeTo(null);
    }
}


class mainPage{
    public void showMainPage(){
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf.");
        }
        JFrame mainWindow = new JFrame("MakeDo");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(350,500);

        JPanel mainPanel = new JPanel( new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        position.fill = GridBagConstraints.NONE;
        position.gridx= 0;
        position.gridy =0;
        position.weightx =0;
        position.insets = new Insets(1, 1, 1,1);
        //mainPanel.setLayout(new BoxLayout((mainPanel, BoxLayout.Y_AXIS)));


        position.anchor = GridBagConstraints.CENTER;
        JLabel appName = new JLabel("MakeDo");
        position.gridx = 0;
        position.gridy = 0;
        appName.setFont(new Font("Arial", Font.PLAIN,25));
        mainPanel.add(appName, position);


        JLabel appDesc = new JLabel("a comprehensive pc stat and benchmarking software");
        position.gridx = 0;
        position.gridy = 1;
        appDesc.setFont(new Font("Arial", Font.PLAIN,12));
        mainPanel.add(appDesc, position);

        JLabel br = new JLabel("             ");
        position.gridx = 0;
        position.gridy = 2;
        //mainPanel.add(br, position);

        JCheckBox hardDisk = new JCheckBox("HardDisk ");
        hardDisk.setOpaque(true);
        JCheckBox cpu = new JCheckBox("CPU ");
        JCheckBox ram = new JCheckBox("RAM ");
        JCheckBox os = new JCheckBox("OS ");
        JCheckBox screen = new JCheckBox("Screen ");
        JCheckBox battery = new JCheckBox("Battery ");
        JCheckBox peripherals = new JCheckBox("Peripherals");
        JCheckBox gpu = new JCheckBox("GPU ");

        position.gridy =3;

        hardDisk.setSelected(true);
        mainPanel.add(hardDisk, position);

        position.gridy = 4;
        cpu.setSelected(true);
        mainPanel.add(cpu, position);

        position.gridy = 5;
        ram.setSelected(true);
        mainPanel.add(ram,position);

        position.gridy = 6;
        os.setSelected(true);
        mainPanel.add(os, position);

        position.gridy = 7;
        screen.setSelected(true);
        mainPanel.add(screen, position);

        position. gridy = 8;
        battery.setSelected(true);
        mainPanel.add(battery, position);

        position.gridy =9;
        peripherals.setSelected(true);
        mainPanel.add(peripherals, position);

        position.gridy = 10;
        gpu.setSelected(true);
        mainPanel.add(gpu, position);


        JButton selectAll = new JButton("All");
        position.gridy = 11;
        selectAll.setPreferredSize(new Dimension(70,15));
        selectAll.setFont(new Font("Arial", Font.BOLD, 10));
        selectAll.setBackground(Color.lightGray);
        //mainPanel.add(selectAll, position);

        JLabel br3 = new JLabel("---------");
        position.gridy = 11;
        //mainPanel.add(br3, position);

        position.anchor = GridBagConstraints.CENTER;


        JButton clear = new JButton("Clear");
        position.gridy = 12;
        position.gridx = 0;
        clear.setPreferredSize(new Dimension(70,15));
        clear.setFont(new Font("Arial", Font.BOLD, 10));
        clear.setBackground(Color.lightGray);
        mainPanel.add(clear, position);

        clear.addActionListener(e -> {
            hardDisk.setSelected(false);
            cpu.setSelected(false);
            os.setSelected(false);
            ram.setSelected(false);
            screen.setSelected(false);
            battery.setSelected(false);
            peripherals.setSelected(false);
            gpu.setSelected(false);
        });


        JLabel br2 = new JLabel("       ");
        position.gridy= 13;
        mainPanel.add(br2,position);

        JButton run = new JButton("RUN");
        position.gridx = 0;
        position.gridy = 15;
        run.setFont(new Font("Arial", Font.BOLD,14));
        run.setOpaque(true);
        run.setBackground(Color.lightGray);
        run.setPreferredSize(new Dimension(120,40));
        mainPanel.add(run, position);
        run.addActionListener(e -> System.out.println("RUN"));
        run.addActionListener(e -> {
            if(
                    hardDisk.isSelected() &&
                            cpu.isSelected() &&
                            os.isSelected() &&
                            ram.isSelected() &&
                            screen.isSelected() &&
                            battery.isSelected() &&
                            peripherals.isSelected() &&
                            gpu.isSelected()

            ){
                System.out.println("All checkboxes are selected");

            }
        });



        for(FileStore store : FileSystems.getDefault().getFileStores()){
            try {
                System.out.println("Drive: " + store.name());
                double total = store.getTotalSpace();
                double free = store.getUsableSpace();
                double used = total - free;

                System.out.println("Total: " + total + " GB");
                System.out.println("Used " + used + " GB" );
                System.out.println("Free: "+ free + " GB");
            } catch (Exception e){
                System.err.println("Error reading partition: " + e.getMessage());
            }
        }

        JFrame basicInfo = new JFrame("Leap");



        mainWindow.add(mainPanel);
        mainWindow.setVisible(true);
        mainWindow.setLocationRelativeTo(null);

    }
}

class PCTester{
    public static void main(String[] args){
        signInPage signIn = new signInPage();
        signIn.showSignInPage();
        userLogin login = new userLogin();
        //login.showUserLogin();
        mainPage mainpage = new mainPage();
        //mainpage.showMainPage();



    }
}

