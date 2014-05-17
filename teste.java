import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class teste {

	private static Socket socket;
	private static String HOST = "200.144.254.99";
	private static int PORT = 3280;

	public static void main(String args[]) {
		try {

			InetAddress address = InetAddress.getByName(HOST);
			socket = new Socket(address, PORT);

			String json = "{\"usuario\" : \"exemplo\", \"hash\" : \"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF\"}";

			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.write(json);
			out.flush();
			
			InputStream is = socket.getInputStream();
			int message = 0;
			while ((message = is.read()) != -1) {
				System.out.print((char) (message));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}
}
