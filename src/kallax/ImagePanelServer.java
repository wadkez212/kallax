package kallax;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class ImagePanelServer extends JPanel {
    public static BufferedImage myImage;
    public static Image visibleImage;	
    
    public ImagePanelServer(JButton buttons[])
    {
        setLayout(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
        
        
    	c.weightx = 0.5;							//растяжение кнопок одинакого на 0,5
    	c.ipady = 20;      							//высота кнопок
    	int j = 0;
    	for(int i = 12; i > 6; i--)					//6 лунок противника
        {
            c.gridx = j;
            c.gridy = 0;
            j++;
        	buttons[i] = new JButton("" + 6); 
        	buttons[i].setContentAreaFilled(false);
        	c.fill = GridBagConstraints.HORIZONTAL;		
        	this.add(buttons[i], c);
        }
        
        c.gridwidth = 2;
        buttons[13] = new JButton("" + 0);								//левый 0
        buttons[13].setContentAreaFilled(false);
        c.ipady = 30;
        c.gridx = 0;
        c.gridy = 1;
        //c.weighty = 1.0;   //request any extra vertical space
        this.add(buttons[13], c);
        
        ImageIcon img = new ImageIcon("strelkaVniz.jpg");					//установили стрелку
        JLabel l1 = new JLabel();
        buttons[14] = new JButton();
        buttons[14].setContentAreaFilled(false);
        JButton strelka = new JButton();
        ImageIcon icon = new ImageIcon(img.getImage().getScaledInstance(25, 18, img.getImage().SCALE_DEFAULT));
        buttons[14].setIcon(icon);
        c.ipady = 30;
        c.gridx = 2;
        c.gridy = 1;
        this.add(buttons[14], c);
        
        buttons[6] = new JButton("" + 0); 						//правый 0
        buttons[6].setForeground(Color.BLUE);
        buttons[6].setContentAreaFilled(false);
        c.ipady = 30;
        c.gridx = 4;
        c.gridy = 1;
        this.add(buttons[6], c);
        

        c.gridwidth = 1;
    	c.ipady = 20;      							//высота кнопок
    	j = 0;
    	for(int i = 0; i < 6; i++)					//мои лунки с 1 по 6
        {
            c.gridx = j;
            c.gridy = 2;
            j++;
        	buttons[i] = new JButton("" + 6); 
        	buttons[i].setForeground(Color.BLUE);
        	buttons[i].setContentAreaFilled(false);
        	c.fill = GridBagConstraints.HORIZONTAL;		
        	this.add(buttons[i], c);
        }
    }
    
    public Image getImage() {
        return visibleImage;
    }
    
    public void setImage(Image image) {
        this.visibleImage = image;
    }
    
    public void setImageFile(File imageFile) throws IOException
    {
    	visibleImage  = ImageIO.read(imageFile);
    	repaint();
    }
    
    //рисует картинку
    @Override
    public void paintComponent(Graphics g) {
    	//Font font = new Font("Times new Roman", Font.BOLD, 10);
    	//g.setFont(font);
    	super.paintComponent(g);
    	
    	/*Вставка картинки*/
    	try {
			visibleImage = ImageIO.read(new File("poleKalax.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        Font font1 = new Font("Times New Roman", Font.CENTER_BASELINE, 16);
        g.setFont(font1);
        
        g.drawImage(visibleImage, 0, 0, 2000, 1000, null); 					//нарисовали картинку-фон в размер панели deskPanel
    }
}