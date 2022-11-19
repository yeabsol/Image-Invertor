
package image.invertor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageInvertor {

    private BufferedImage img = null;
    private File f = null;
    
    public static void main(String[] args) {
        ImageInvertor i = new ImageInvertor();
        i.createWindow();        
    }
    
    private void createWindow() {    
      JFrame frame = new JFrame("Image Invertor");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(null);
      frame.setSize(600, 400); 
      createUI(frame);
           
      frame.setLocationRelativeTo(null);  
      frame.setVisible(true);
   }

   private void createUI(final JFrame frame){  
      JPanel panel = new JPanel();
      JPanel panel2 = new JPanel();
      JPanel panel3 = new JPanel();
    
      panel.setLayout(new FlowLayout(FlowLayout.CENTER));   
      panel2.setLayout(new FlowLayout(FlowLayout.CENTER)); 
      panel3.setLayout(new FlowLayout(FlowLayout.CENTER));
      
      JButton ImageSelector = new JButton("Select Image..");
      final JLabel label = new JLabel();
      
      JButton ChangeBtn = new JButton("CONVERT");
      final JLabel errLabel = new JLabel();
//      errLabel.setForeground(Color.RED);
      
      panel.add(ImageSelector);
      panel.add(label);
      panel2.add(ChangeBtn);
      panel3.add(errLabel);
     
      panel.setBounds(130,20,300,100);
      
      panel2.setBounds(130,120,300,80); 
      
      panel3.setBounds(0,200,600,100);     
      
      frame.getContentPane().add(panel, BorderLayout.CENTER);
      frame.getContentPane().add(panel2, BorderLayout.CENTER); 
      frame.getContentPane().add(panel3, BorderLayout.CENTER);
      
      ImageSelector.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            //clear labels
            errLabel.setText("");
            label.setText("");
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new ImageFilter());
            fileChooser.setAcceptAllFileFilterUsed(false);

            int option = fileChooser.showOpenDialog(frame);
            if(option == JFileChooser.APPROVE_OPTION){
                if(fileChooser.getSelectedFile() != null){
                    f = fileChooser.getSelectedFile();
                    label.setText("File Selected: " + f.getName());
                }               
            }else{
               label.setText("Open command canceled");
            }
         }
      });
      
      ChangeBtn.addActionListener(new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
            
            if (f == null){
                errLabel.setForeground(Color.red);
                errLabel.setText("You Must First select Image ");                
                return;
            }
            else{
             
            try{
                img = ImageIO.read(f);
            }
            catch (Exception ex) {
                errLabel.setForeground(Color.red);
                errLabel.setText("ERROR: "+ex.toString());                
                return;
            }
                         
            int width = img.getWidth();
            int height = img.getHeight();
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int p = img.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;

                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;

                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    img.setRGB(x, y, p);
                }
            }
                    
            
            try{
                File file = new File(f.getParent()+"\\inverted-"+f.getName()); ///new File(f.getParent()+"inverted-abc.png");//new File("C:\\Users\\yeabs\\OneDrive\\Desktop\\abc.PNG");
                
                ImageIO.write(img, "png", file);
                errLabel.setForeground(Color.blue);
                errLabel.setText("SUCCESSFULLY CONVERTED AND SAVED TO \n "+file.getParent());         
                
            }
            catch (IOException ex) {
                errLabel.setForeground(Color.RED);
                errLabel.setText("ERROR: "+ex);
                return;
            }            
         }
        }
      });
   }
    
}
