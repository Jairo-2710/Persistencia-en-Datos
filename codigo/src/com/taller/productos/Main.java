package com.taller.productos;

import com.taller.productos.vista.ProductoFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorada) {
            // Se usa el aspecto de Swing
        }
        SwingUtilities.invokeLater(() -> new ProductoFrame().setVisible(true));
    }
}
