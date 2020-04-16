package com.csasc.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;

public class TestJMF {
	public static void main(String[] args) {

		String videoFilePath = "C:/Users/haha/Desktop/hello.avi";
		URL url = null;
		try {
			url = new URL("file:/" + videoFilePath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(url);
		}
		Player player = null;
		try {
			player = Manager.createPlayer(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.prefetch();
		player.start();
	}
}
