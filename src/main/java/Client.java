import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", Server.SERVER_PORT);

            try (Scanner scanner = new Scanner(System.in);
                 final SocketChannel socketChannel = SocketChannel.open()) {

                socketChannel.connect(socketAddress);
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                String msg;
                while (true) {
                    System.out.println("Enter message for server to delete spaces: ");
                    msg = scanner.nextLine();
                    socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                    if ("end".equals(msg)) break;

                    int bytesCount = socketChannel.read(inputBuffer);
                    String resultMsg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim();
                    System.out.println("SERVER: result without spaces: " + resultMsg);
                    inputBuffer.clear();
                }
                socketChannel.finishConnect();
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
}