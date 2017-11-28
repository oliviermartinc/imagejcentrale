/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.mycompany.imagej;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import ij.IJ;
import ij.plugin.PlugIn;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.frame.PlugInFrame;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;
import ij.gui.*;

/**
 * A template for processing each pixel of either
 * GRAY8, GRAY16, GRAY32 or COLOR_RGB images.
 *
 * @author Olivier Martin
 */
public class Cytokinesis extends PlugInFrame implements ij.plugin.PlugIn, ActionListener, MouseListener {
	
	static Frame Instance;
	protected ImagePlus image;
	protected ImagePlus image1;
	private Panel panel;
	java.awt.Checkbox AddOnClick;
	boolean done = false;
	private ImageCanvas previousCanvas = null;
	private Roi AutoROI = null;
	private int position_X = 0;
	protected int position_Y = 0;
	protected int slice_1 = 0;
	protected int slice_2 = 0;
	protected int slice_3 = 0;
	public Button button_1 = null;
	public Button button_2 = null;
	public Button button_3 = null;
	public Button button_4 = null;
	public Button button_5 = null;
	//private boolean button1_ready = false;
	//private ij.gui.Roi Roi;
	
	public Cytokinesis () {
		super("Cytokinesis");
		if (Instance != null){
            Instance.toFront();
        }
		else{
		Instance = this;
		//ImagePlus.addImageListener(this);
        WindowManager.addWindow(this);
        setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        
        panel = new Panel();
        panel.setLayout(new GridLayout(10, 0, 0, 0));
        
        button_1 = new Button("Start Mitosis");
        button_1.addActionListener(this);
        button_1.setEnabled(slice_1 == 0 );
		panel.add("Start Mitosis",button_1);
		
		button_2 = new Button("Start abscission");
        button_2.addActionListener(this);
        button_2.setEnabled(slice_1 != 0);
		panel.add("Start abscission",button_2);
		
		button_3 = new Button("End abscission");
        button_3.addActionListener(this);
        button_3.setEnabled(slice_1 != 0 && slice_2 != 0);
		panel.add("End abscission",button_3);
        
        Panel p = new Panel();
        p.setLayout(new BorderLayout());
        button_4 = new Button("cancel");
        button_4.addActionListener(this);
        button_5 = new Button("Add ROI");
        button_5.addActionListener(this);
        p.add(button_5, BorderLayout.EAST);
        p.add(button_4, BorderLayout.WEST);
        button_5.setEnabled(false);
        panel.add(p);
		
        add(panel);
        this.setSize(200, 150);
        this.setVisible(true);
        getManager();
		}
	}
	
	RoiManager getManager() {
        RoiManager instance = RoiManager.getInstance();
        if (instance == null)
            return new RoiManager ();
        else
            return instance;
    }
	
	public void actionPerformed(ActionEvent e) {
		String label = e.getActionCommand();
		if (label==null)
			return;
		String command = label;
        		if(command.equals("Start Mitosis"))
        			startMitosis();
        		if(command.equals("Start abscission"))
        			startAbscission();
        		if(command.equals("End abscission"))
        			endAbscission();
        		if(command.equals("cancel"))
        			cancel();
        		if(command.equals("Add ROI"))
        			addRoi();
	}
	
	public void startMitosis() {
		IJ.setTool("point");
		ImagePlus imp = WindowManager.getCurrentImage();
		//slice_1 = imp.getCurrentSlice();
		//IJ.log(Integer.toString(slice_1));
    }
	
	public void startAbscission() {
		ImagePlus imp = WindowManager.getCurrentImage();
		slice_2 = imp.getCurrentSlice();
		button_3.setEnabled(true);
		button_2.setEnabled(false);
    }
	
	public void endAbscission() {
		ImagePlus imp = WindowManager.getCurrentImage();
		slice_3 = imp.getCurrentSlice();
		button_3.setEnabled(false);
		button_5.setEnabled(true);
    }
	public void cancel() {
		button_5.setEnabled(false);
		button_3.setEnabled(false);
		button_2.setEnabled(false);
		button_1.setEnabled(true);
		slice_1 = 0;
		slice_2 = 0;
		slice_3 = 0;
		position_X = 0;
		position_Y = 0;
    }
	public void addRoi() {
		AutoROI = new Roi(position_X,position_Y,1,1);
		String position = slice_1 +"_"+slice_2 +"_"+slice_3;
		AutoROI.setName(position);
		getManager().addRoi(AutoROI);
		button_3.setEnabled(false);
		button_2.setEnabled(false);
		button_1.setEnabled(true);
		slice_1 = 0;
		slice_2 = 0;
		slice_3 = 0;
		position_X = 0;
		position_Y = 0;
    }
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		ImagePlus imp = WindowManager.getCurrentImage();
		position_X = (int) imp.getRoi().getXBase();
		position_Y = (int) imp.getRoi().getYBase();
		slice_1 = imp.getCurrentSlice();
		button_2.setEnabled(true);
		button_1.setEnabled(false);
        //IJ.log(Integer.toString(x));
        //IJ.log(Integer.toString(y));
        //IJ.log(Integer.toString(imp.getCurrentSlice()));
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	
	public void run(String string) {
		//IJ.log("test");
		while (!done) {
			try {Thread.sleep(500);}
			catch(InterruptedException e) {}
			ImagePlus imp = WindowManager.getCurrentImage();
			if (imp != null){
				ImageCanvas canvas = imp.getCanvas();
                          	if (canvas != previousCanvas){
					if(previousCanvas != null)
                                               previousCanvas.removeMouseListener(this);
					canvas.addMouseListener(this);
					previousCanvas = canvas;
				}
			}
                        else{
                            if(previousCanvas != null)
                                previousCanvas.removeMouseListener(this);
                            previousCanvas = null;
                        }
                           
		}
	}
	
	public static void main(String[] args) {
		// set the plugins.dir property to make the plugin appear in the Plugins menu
		Class<?> clazz = Cytokinesis.class;
		String url = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class").toString();
		String pluginsDir = url.substring("file:".length(), url.length() - clazz.getName().length() - ".class".length());
		System.setProperty("plugins.dir", pluginsDir);

		// start ImageJ 
		// doesn't work here
		new ImageJ();
		
		// open the Clown sample
		ImagePlus image = IJ.openImage("/Users/olivier/Downloads/1_LucDMSO-10.tif");
		image.show();
		
		// run the plugin
		IJ.runPlugIn(clazz.getName(), "");
	}

}
