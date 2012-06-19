package de.akquinet.android.jms.activities;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.akquinet.android.jms.util.ConnectionUtility;
import de.akquinet.android.roboject.RobojectActivity;
import de.akquinet.android.roboject.annotations.InjectLayout;
import de.akquinet.android.roboject.annotations.InjectView;
import pk.aamir.stompj.Message;
import pk.aamir.stompj.MessageHandler;

@InjectLayout("result")
public class MessagingActivity extends RobojectActivity implements MessageHandler {
    @InjectView
    TextView receiveMessageText;

    @InjectView
    Button sendMessageButton;

    ConnectionUtility connectionUtility;

    @Override
    public void onReady() {
        super.onReady();

        connectionUtility = new ConnectionUtility("10.0.2.2", 6163, "guest", "password");

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectionUtility.sendMessage("jms.queue.testQueue", "This is a message!");
            }
        });

        connectionUtility.addMessageHandler("jms.topic.testTopic", this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectionUtility.shutdown();
    }

    @Override
    public void onMessage(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                receiveMessageText.setText(message.getContentAsString());
            }
        });
    }
}
