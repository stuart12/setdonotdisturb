/*
    This file is part of quiet4sleep. Copyright 2016 Stuart Pook

    Quiet4sleep is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Quiet4sleep is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
    */
package it.pook.setdonotdisturb;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.content.Context;

/**
 * Created by Stuart Pook (www.pook.it) on 9/12/16.
 * adb shell am startservice -n it.pook.setdonotdisturb/.NotificationFilterChangerService --es mode alarms
 * adb shell am startservice -n it.pook.setdonotdisturb/.NotificationFilterChangerService --ez quiet false

 * http://www.xgouchet.fr/android/index.php?article42/launch-intents-using-adb
 */
public class NotificationFilterChangerService extends IntentService {
    private static final String TAG = "NotifFilterService";

    public NotificationFilterChangerService() {
        // Used to name the worker thread
        // Important only for debugging
        super(NotificationFilterChangerService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        Log.i(TAG, ".onCreate");
        // If a Context object is needed, call getApplicationContext() here.
     }


    @Override
    protected void onHandleIntent(Intent in) {
        Log.i(TAG, ".onHandleIntent " + in);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /*if (!notificationManager.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else */{
                String mode = in.getStringExtra("mode");
                final int filter;
                if (mode == null) {
                    boolean quiet = in.getBooleanExtra("quiet", true);
                    filter = quiet ? NotificationManager.INTERRUPTION_FILTER_PRIORITY : NotificationManager.INTERRUPTION_FILTER_ALL;
                    notificationManager.setInterruptionFilter(filter);
                } else {
                    Log.i(TAG, ".onHandleIntent mode " + mode);
                    if (mode.equals("all"))
                        filter = NotificationManager.INTERRUPTION_FILTER_ALL;
                    else if (mode.equals("none"))
                        filter = NotificationManager.INTERRUPTION_FILTER_NONE;
                    else if (mode.equals("priority"))
                        filter = NotificationManager.INTERRUPTION_FILTER_PRIORITY;
                    else if (mode.equals("alarms"))
                        filter = NotificationManager.INTERRUPTION_FILTER_ALARMS;
                    else {
                        Log.w(TAG, ".onHandleIntent unknown mode " + mode);
                        return;
                    }
                }
                Log.i(TAG, ".onHandleIntent setInterruptionFilter " + filter);
                notificationManager.setInterruptionFilter(filter);
            }
        }
    }
}
