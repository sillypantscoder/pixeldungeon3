package com.sillypantscoder.window;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.sillypantscoder.window.Surface.DummyImageObserver;

/**
 * A panel that automatically redraws itself.
 */
public class RepaintingPanel extends JPanel {
	private static final long serialVersionUID = 7148504528835036003L;
	protected static JFrame frame;
	public BiFunction<Integer, Integer, BufferedImage> painter;
	public Consumer<MouseEvent> mouseClicked;
	/**
	* Called by the runtime system whenever the panel needs painting.
	*/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(painter.apply(getWidth(), getHeight()), 0, 0, new DummyImageObserver());
	}
	// public abstract void mouseClicked(MouseEvent e);
	// public abstract void mouseMoved(MouseEvent e);
	// public abstract void keyPressed(KeyEvent e);
	public static void startAnimation() {
		SwingWorker<Object, Object> sw = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() throws Exception {
				while (true) {
					frame.revalidate();
					frame.getContentPane().repaint();
					Thread.sleep(16);
				}
			}
		};
		sw.execute();
	}
	public void run(String title, int width, int height) {
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.setVisible(true);
		startAnimation();
		// Start mouse listener
		myMouseListener ml = new myMouseListener(this);
		this.addMouseListener(ml);
		mouseMotionListener mml = new mouseMotionListener(this);
		this.addMouseMotionListener(mml);
		// // Add keyboard listener
		// RepaintingPanel thePanel = this;
		// frame.addKeyListener(new KeyListener() {
		// 	@Override
		// 	public void keyPressed(KeyEvent e){ thePanel.keyPressed(e); }
		// 	@Override
		// 	public void keyReleased(KeyEvent e) { }
		// 	@Override
		// 	public void keyTyped(KeyEvent e) { }
		// });
	}
}
class myMouseListener implements MouseListener {
	protected RepaintingPanel srcPanel;
	public myMouseListener(RepaintingPanel p) {
		srcPanel = p;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) { }

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0) {
		srcPanel.mouseClicked.accept(arg0);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) { }

}
class mouseMotionListener implements MouseMotionListener {
	protected RepaintingPanel srcPanel;
	public mouseMotionListener(RepaintingPanel p) {
		srcPanel = p;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) { }

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// srcPanel.mouseMoved.accept(arg0);
	}
}