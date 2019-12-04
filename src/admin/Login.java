package admin;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



public class Login {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Creating the Frame
        JFrame frame = new JFrame("Login");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);		
        frame.setSize(300, 200);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        
        JPanel panelLogin = new JPanel();
        JLabel lbLogin = new JLabel("Login");
        Font loginFont = new Font("airal", 1, 32);
        lbLogin.setFont(loginFont);
        panelLogin.add(lbLogin);
        
        JLabel lbUsername = new JLabel("Username");
        JTextField tfUsername = new JTextField(15); // accepts up to 10 characters
        JLabel lbPassword = new JLabel("Password");
        JPasswordField tfPassword = new JPasswordField(15); // accepts up to 10 characters
        panel.add(lbUsername);
        panel.add(tfUsername);
        panel.add(lbPassword);
        panel.add(tfPassword);
        
        JButton btLogin = new JButton("Login");
        btLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String user = tfUsername.getText();
				String pass = tfPassword.getText();
				if(user.equals("admin") && pass.equals("admin")) {
					System.out.println("login success");
					AdminWindow admin = new AdminWindow();
					admin.main(null);
					frame.setVisible(false);
					//System.exit(0);
				}
				else {
					System.out.println("Wrong pass");
					//int dialogButton = JOptionPane.WARNING_MESSAGE;
            		//int dialogResult = JOptionPane.showConfirmDialog (null, "The username or password you entered is incorrect","Warning",dialogButton);
            		JOptionPane.showMessageDialog(null,"The username or password you entered is incorrect");
					
//            		if(dialogResult == JOptionPane.OK_OPTION){
//            			System.out.println("Yes");
//            		}
//            		else {
//            			System.out.println("No");
//            		}
				}
			}
		});
        JButton btCancel = new JButton("Cancel");

        
        
        panel.add(btLogin);
        panel.add(btCancel);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.NORTH, panelLogin);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        //frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}

}
