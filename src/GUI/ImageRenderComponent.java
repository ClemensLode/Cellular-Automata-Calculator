/*
 * ImageRenderComponent.java
 *
 * Created on February 17, 2007, 10:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GUI;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;


class ImageRenderComponent extends JPanel {
    BufferedImage image;
    Dimension size;
 
    public ImageRenderComponent(BufferedImage image) {
        this.image = image;
        size = new Dimension(image.getWidth(), image.getHeight());
    }
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = (getWidth() - size.width)/2;
        int y = (getHeight() - size.height)/2;
        g.drawImage(image, x, y, this);
    }
 
    public Dimension getPreferredSize() {
        return size;
    }
}

