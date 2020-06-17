/**
 * 
 */
package logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Joseph Monk
 *
 * Provides a simple method to log session data, to be used in a seperate thread.
 */
public class KLogger implements Runnable {
	private static final String SESSION_LOG = "session.log";
	private LinkedBlockingQueue<String> messages;
	
	public KLogger() {
		messages = new LinkedBlockingQueue<String>();
	}
	
	public void log(String msg) {
		this.messages.add(LocalDateTime.now() + " " + msg);
	}
	
	@Override
	public void run() {
		BufferedWriter bw = null;
		String msg = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(SESSION_LOG, true)); // Open with append

			while (true) {
				msg = messages.poll(5, TimeUnit.SECONDS); // Wait up to 5 seconds for a message;
				
				if (msg != null) {
					bw.write(msg);  // Save to file
		
					bw.newLine();
					bw.flush();
				}
			}
		} 
		catch (IOException e) {
			System.out.println("Could not append to " + SESSION_LOG);
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Interrupt Exception.");
			e.printStackTrace();
		} 
		finally { // (close file)
			if (bw != null) 
				try {
					bw.close();
				} 
			catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
	}

}
