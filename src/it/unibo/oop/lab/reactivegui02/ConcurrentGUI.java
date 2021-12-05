package it.unibo.oop.lab.reactivegui02;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Builds a new ConcurrentGUI
 */
public class ConcurrentGUI extends JFrame {
    private static  final double WIDTH_PERC = 0.2;
    private static  final double HEIGHT_PERC = 0.1;
    private final JLabel display = new JLabel();
    
    public ConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int)(screenSize.getWidth()* WIDTH_PERC),(int)(screenSize.getWidth()* HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(display);
        final JButton up = new JButton("up");
        panel.add(up);
        final JButton down = new JButton("down");
        panel.add(down);
        final JButton stop = new JButton("stop");
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);
        final Agent agent = new Agent();
        new Thread(agent).start();
        up.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.upTime();
            }
        });
        down.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.downTime();
            }
        });
        stop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.stopCounting();
                up.setEnabled(false);
                down.setEnabled(false);
                stop.setEnabled(false);
            }
        });
    }
    private class Agent implements Runnable {       
        private volatile boolean stop;
        private volatile boolean up = true;
        private volatile int counter; 

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    counter += up ? 1 : -1;
                    final var todisplay = Integer.toString(counter);
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            display.setText(todisplay);
                        }
                    });
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        public void stopCounting() {
                this.stop = true;
        }
        
        public void downTime() {
            this.up =false;
        }
            
        public void upTime() {
            this.up =true;
        }    
    }
}
  
