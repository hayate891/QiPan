package io.nibby.qipan.ogs;

import io.nibby.qipan.settings.Settings;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OgsService {

    private static final String SERVER = "https://online-go.com";
    private Socket socket;

    public OgsService() {
        IO.Options options = new IO.Options();
        options.reconnection = true;
        options.reconnectionDelay = 500;
        options.reconnectionDelayMax = 60000;
        options.transports = new String[]{ "websocket" };

        try {
            socket = IO.socket(SERVER, options);
            socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    Transport transport = (Transport) objects[0];
                    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {

                        @Override
                        public void call(Object... objects) {
                            String auth = Settings.ogsAuth.getTokenType() + " " + Settings.ogsAuth.getAuthToken();
                            Map<String, List<String>> mHeaders = (Map<String, List<String>>) objects[0];
                            mHeaders.put("Authorization", Arrays.asList(auth));
                        }
                    });
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        if (!socket.connected()) {
            socket.connect();
        }
    }

    public void disconnect() {
        if (socket.connected()) {
            socket.disconnect();
            socket.close();
        }
    }
}
