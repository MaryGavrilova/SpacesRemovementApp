import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static final int SERVER_PORT = 8888;

    public static void main(String[] args) {
        try {
            final ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress("localhost", SERVER_PORT));

            try (SocketChannel socketChannel = serverChannel.accept()) {

                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1) break;
                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim();
                    inputBuffer.clear();
                    socketChannel.write(ByteBuffer.wrap((msg.replaceAll("\\s", "")).getBytes(StandardCharsets.UTF_8)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
