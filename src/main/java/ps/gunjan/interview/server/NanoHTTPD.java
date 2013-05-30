package ps.gunjan.interview.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;


public abstract class NanoHTTPD {

	public static final String MIME_PLAINTEXT = "text/plain";
	public static final String MIME_HTML = "text/html";
	public static final String MIME_DEFAULT_BINARY = "application/octet-stream";
	
	private final String myhostname;
	private final int myport;
	private ServerSocket myServerSocket;
	private Thread myThread;
	
	private static final String QUERY_STRING_PARAMETER = "NanoHTTPD.QUERY_STRING";
	
	public NanoHTTPD(int port){
		this(null, port);
	}
	
	//construct httpserver on a given hostname and port
	public NanoHTTPD(String hostname, int port){
		this.myhostname = hostname;
		this.myport = port;
	}
	
	//start the server
	//throws IOException if the socket in use
	public void start() throws IOException{
		myServerSocket = new ServerSocket();
		myServerSocket.bind((myhostname!=null)?new InetSocketAddress(myhostname, myport):
			new InetSocketAddress(myport));
		
		myThread = new Thread( new Runnable(){
			
			@Override
			public void run(){
				do{					
					try{
						final Socket finalAccept = myServerSocket.accept();						
						InputStream inputStream = finalAccept.getInputStream();
						OutputStream outputStream = finalAccept.getOutputStream();						
						TempFileManager tempFileMgr = tempFileManagerFactory.create();	
						
						final HTTPSession session = new HTTPSession(tempFileMgr,inputStream,outputStream);
						
						
					}catch(IOException ignore){
						
					}					
				} while(!myServerSocket.isClosed());
			}
		});
		
	}
	
	
	//pluggable strategy for creating and cleaning up temp files
	private TempFileManagerFactory tempFileManagerFactory;
	public void setTempFileManagerFactory(TempFileManagerFactory tmpFileMgrFac){
		this.tempFileManagerFactory = tmpFileMgrFac;
	}
	//Factory to create temp file managers
	public interface TempFileManagerFactory {
		TempFileManager create();
	}
	//temp file mgrs are created to 1-to-1 with incoming requests
	//to create and cleanup temp files created as a result of handling requests
	public interface TempFileManager{
		TempFile createTempFile() throws Exception;
		void clear();
	}
	//for temp storage and clean up themselves when no longer necessary
	public interface TempFile{
		OutputStream open() throws Exception;
		void delete() throws Exception;
		String getName();
	}
	
	private class DefaultTempFileManagerFactory implements TempFileManagerFactory{
		@Override
		public TempFileManager create(){
			return new DefaultTempFileManager();
		}
	}
	
	public static class DefaultTempFileManager implements TempFileManager{
		private final String tmpdir;
		private final List<TempFile> tempFiles;
		
		public DefaultTempFileManager(){
			tmpdir = System.getProperty("java.io.tmpdir");
			tempFiles = new ArrayList<TempFile>();
		}
		
		@Override
		public TempFile createTempFile() throws Exception{
			DefaultTempFile tempFile = new DefaultTempFile(tmpdir);
			tempFiles.add(tempFile);
			return tempFile;
		}
		
		@Override
		public void clear(){
			for(TempFile file: tempFiles){
				try{
					file.delete();
				}catch (Exception ignore){
					
				}
			}
			tempFiles.clear();
		}
	}
	
	public static class DefaultTempFile implements TempFile{
		
        private File file;
        private OutputStream fstream;

        public DefaultTempFile(String tempdir) throws IOException {
            file = File.createTempFile("NanoHTTPD-", "", new File(tempdir));
            fstream = new FileOutputStream(file);
        }

        @Override
        public OutputStream open() throws Exception {
            return fstream;
        }

        @Override
        public void delete() throws Exception {
            file.delete();
        }

        @Override
        public String getName() {
            return file.getAbsolutePath();
        }
	}
	
	//parses HTTP request and return response
	protected class HTTPSession implements Runnable{
		public static final int BUFSIZE = 8192;
		private final TempFileManager tempFileManager;
		private final InputStream inputStream;
		private final OutputStream outputStream;
		
		public HTTPSession(TempFileManager tempFileManager, InputStream inputStream, 
				OutputStream outputStream){
			this.tempFileManager = tempFileManager;
            this.inputStream = inputStream;
            this.outputStream = outputStream;
		}
		
		@Override
		public void run(){
			try{
				//TODO
				
			}catch (Exception ie){
				//Response.error(outputStream, Response.Status.INTERNAL_ERROR,"SERVER INTERNAL ERROR : IOException" + ie.getMessage());
				
				//throw new InterruptedException();
			} finally{
				tempFileManager.clear();
			}
		}
	}
	
	//HTTP Response return one of these from the server
	public static class Response{
		
		//some common HTTP Status Codes
		/**public enum Status {
			OK(200,"OK"),CREATED(201,"CREATED"),INTERNAL_ERROR(201,"INTERNAL_ERROR");
		}**/
		//private Status status;
		
	}
	/***********************************************/
}
