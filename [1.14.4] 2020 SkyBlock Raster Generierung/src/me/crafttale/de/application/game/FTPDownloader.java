package me.crafttale.de.application.game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

 
public class FTPDownloader {
	
	private static final int BUFFER_SIZE = 4096 ;
	
	public void download(){    //this is a function
		long startTime = System.currentTimeMillis() ;
		String pass = "ariano0105" ; // password of the ftp server
		String ftpUrl = "ftp://IchMagOmasKekse.146843:ariano0105@/plugins/SkyBlock/Insel-Index-File.yml";//ftp://**username**:**password**@filePath ;
		@SuppressWarnings("unused")
		String file= "Insel-Index-File.yml" ; // name of the file which has to be download
		String host = "node22.mc-host24.de" ; //ftp server
		String user = "IchMagOmasKekse.146843" ; //user name of the ftp server

		String savePath = "C:\\" ;
		ftpUrl = String.format(ftpUrl, user, pass, host) ;
		System.out.println("Connecting to FTP server") ;

		try{
			URL url = new URL(ftpUrl) ;
			URLConnection conn = url.openConnection() ;
			InputStream inputStream = conn.getInputStream() ;
			long filesize = conn.getContentLength() ;
			System.out.println("Size of the file to download in kb is:-" + filesize/1024 ) ;

			FileOutputStream outputStream = new FileOutputStream(savePath) ;

			byte[] buffer = new byte[BUFFER_SIZE] ;
			int bytesRead = -1 ;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead) ;
			}
			long endTime = System.currentTimeMillis() ;
			System.out.println("File downloaded") ;
			System.out.println("Download time in sec. is:-" + (endTime-startTime)/1000)  ;
			outputStream.close() ;
			inputStream.close() ;
		}
		catch (IOException ex){
			ex.printStackTrace() ;
		}
	}
}