package de.akquinet.android.jms.util;

import android.os.AsyncTask;
import de.akquinet.android.androlog.Log;
import pk.aamir.stompj.Connection;
import pk.aamir.stompj.Message;
import pk.aamir.stompj.MessageHandler;
import pk.aamir.stompj.StompJException;

public class ConnectionUtility {
    private static final String TAG = ConnectionUtility.class.getSimpleName();
    private static final int TIMEOUT = 1000 * 20;

    private Connection connection;
    private AsyncTask stayConnected = new AsyncTask() {
        @Override
        protected Object doInBackground(Object... objects) {
            while (true) {
                try {
                    Thread.sleep(TIMEOUT);
                    reconnect();
                } catch (InterruptedException e) {
                }
            }
        }
    };

    private final String host;
    private final int port;
    private final String user;
    private final String password;

    @SuppressWarnings("unchecked")
    public ConnectionUtility(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;

        stayConnected.execute();
    }

    public void sendMessage(String destination, Message message) {
        reconnect();
        connection.send(message, destination);
    }

    public void sendMessage(String destination, String message) {
        reconnect();
        connection.send(message, destination);
    }

    public void addMessageHandler(String destination, MessageHandler handler) {
        reconnect();
        connection.subscribe(destination, true);
        connection.addMessageHandler(destination, handler);
    }

    private final void reconnect() {
        if (connection == null) {
            connection = new Connection(host, port, user, password);
        }

        if (!connection.isConnected()) {
            try {
                connection.connect();
            } catch (StompJException e) {
                Log.e(TAG, "Connection could not be established", e);
            }
        }
    }

    public void shutdown() {
        stayConnected.cancel(true);
        connection.disconnect();
    }
}
