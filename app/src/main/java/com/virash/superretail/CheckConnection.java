package com.virash.superretail;

import android.graphics.Color;

public class CheckConnection {
    public boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    private String showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected)
        {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        }
        else
            {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }
      return message;
    }
}
