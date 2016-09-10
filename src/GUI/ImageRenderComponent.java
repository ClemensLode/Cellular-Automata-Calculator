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

// TODO: scrollable?
class ImageRenderComponent extends JPanel {
    BufferedImage image;
    Dimension size;
 
    public ImageRenderComponent(BufferedImage image) {
        this.image = image;
        size = new Dimension(image.getWidth(), image.getHeight());
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 0;//(getWidth() - size.width)/2;
        int y = 0;//(getHeight() - size.height)/2;
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
// TODO update beim Groessenveraendern 
    public Dimension getPreferredSize() {
        return size;
    }
}

