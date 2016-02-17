package com.samsung.android.tests.snote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.RemoteException;

import com.android.uiautomator.core.Configurator;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class SNoteZoomTests extends UiAutomatorTestCase {

    private BufferedReader reader;
    private Process process;

    protected static final int FINGER_TOUCH_HALF_WIDTH = 20;

    public void testZoomOut() throws UiObjectNotFoundException, IOException, ParseException, InterruptedException {
    	
		try {
			while (getUiDevice().isScreenOn()) {

				getUiDevice().click(
						UiDevice.getInstance().getDisplayWidth(),
						UiDevice.getInstance().getDisplayHeight());

				Thread.sleep(500);

				getUiDevice().click(
						UiDevice.getInstance().getDisplayWidth(),
						UiDevice.getInstance().getDisplayHeight());
				Thread.sleep(500);
				/* Note 2
				490;570)- bán rune
				243:419 ) - replay
				980:513)- bắt đầu đánh
				703:590) - thu rune
				*/

				// t3 610 665 - 187 445 - 1077 575
				// note 3 1255 1141 - 950 812 - 2150 1102
				getUiDevice().click(490, 570); //
				Thread.sleep(500);

				// t3 
				getUiDevice().click(243, 419); // 
				Thread.sleep(500);
				
				// t3 
				getUiDevice().click(980, 513); // 
				Thread.sleep(500);
			}

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    	
    private void analyzeLogCat(long startTime) throws IOException, ParseException {

        long firstFPSTime = 0;
        double currentFPS = 0.0;

        double frameCount = 0;
        long previousFPSTime = 0;
        long currentFPSTime = 0;

        double minFPS = 0;
        double maxFPS = 0;

        System.out.print("\n\n");

        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.contains("FPS")) {
                continue;
            }

            currentFPS = Double.parseDouble(line.substring(line.lastIndexOf(" ") + 1));
            currentFPSTime = stringToDate(line.substring(0, 18));

            if (currentFPSTime < startTime) {
                continue;
            }

            // First FPS
            if (firstFPSTime == 0) {
                firstFPSTime = currentFPSTime;

                minFPS = currentFPS;
            } else {

                // Stop if pinch event finishes
                if (currentFPSTime - previousFPSTime > 1000) {
                    break;
                }

                frameCount += (currentFPSTime - previousFPSTime) * currentFPS / 1000;
            }

            if (currentFPS < minFPS) {
                minFPS = currentFPS;
            }

            if (maxFPS < currentFPS) {
                maxFPS = currentFPS;
            }

            previousFPSTime = currentFPSTime;

            System.out.println(line);
            // System.out.print(new Date(currentFPSTime));
            // System.out.println("     " + new Date());
        }

        DecimalFormat decimalFormat = new DecimalFormat("00.000");

        // Print results
        System.out.print("\n\n\n");
        System.out.println("---- Duration:    " + decimalFormat.format((previousFPSTime - firstFPSTime) / 1000)
                + " (s)");
        System.out.println("---- Frame Count: " + decimalFormat.format(frameCount) + " (frames)");
        System.out.println("");
        System.out.println("---- AVG FPS:     "
                + decimalFormat.format(frameCount / (previousFPSTime - startTime) * 1000));
        System.out.println("---- MIN FPS:     " + decimalFormat.format(minFPS));
        System.out.println("---- MAX FPS:     " + decimalFormat.format(maxFPS));
        System.out.print("\n\n\n");
    }

    private void listenLogCat() {
        try {
            Runtime.getRuntime().exec("logcat -c").waitFor();
            process = Runtime.getRuntime().exec("logcat -v time -s SurfaceFlinger");

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (InterruptedException e1) {
            e1.printStackTrace();
            assertTrue(false);
        } catch (IOException e1) {
            e1.printStackTrace();
            assertTrue(false);
        }
    }


    private long stringToDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        return format.parse(Calendar.getInstance().get(Calendar.YEAR) + "-" + dateString).getTime();
    }
}