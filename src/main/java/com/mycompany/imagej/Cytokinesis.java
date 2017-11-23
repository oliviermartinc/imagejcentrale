/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.mycompany.imagej;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;

import ij.IJ;
import ij.plugin.PlugIn;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.frame.PlugInFrame;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;

/**
 * A template for processing each pixel of either
 * GRAY8, GRAY16, GRAY32 or COLOR_RGB images.
 *
 * @author Olivier Martin
 */
public class Cytokinesis extends PlugInFrame implements ij.plugin.PlugIn {
	
	static Frame Instance;
	
	protected ImagePlus image;
	protected ImagePlus image1;
	private Panel panel;
	
	public Cytokinesis () {
		super ("Cytokinesis");
		if (Instance != null){
            Instance.toFront();
        }
		else{
		Instance = this;
		//ImagePlus.addImageListener(this);
        WindowManager.addWindow(this);
        setLayout(new FlowLayout(FlowLayout.CENTER,5,5));

        panel = new Panel();
        panel.setLayout(new GridLayout(10, 0, 0, 0));
		}
	}
	
	void addButton(String label) {
		Button b = new Button(label);
		//b.addActionListener(this);
		panel.add(b);
	}
	
	RoiManager getManager() {
        RoiManager instance = RoiManager.getInstance();
        if (instance == null)
            return new RoiManager ();
        else
            return instance;
    }

	@Override
	public void run(String string) {
		IJ.log("test");
		ImagePlus imp = WindowManager.getCurrentImage();
		getManager();
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
		ImagePlus image = IJ.openImage("http://imagej.net/images/clown.jpg");
		image.show();
		
		// run the plugin
		IJ.runPlugIn(clazz.getName(), "");
	}

}
