/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Faces_Manifest_Project;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 *
 * @author User
 */
public class CustomOutputStream extends OutputStream {
        String textArea;
        JTextArea  jTextArea1;
	//private jTextArea1  textArea;

	public CustomOutputStream(JTextArea textArea) {
      
		this.jTextArea1 = textArea;
	}

	@Override
	public void write(int b) throws IOException {
		// redirects data to the text area
        jTextArea1.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
	}
}