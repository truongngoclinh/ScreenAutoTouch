# ScreenAutoTouch -Summoner War Auto
This is a sample for screen auto touch using UIAutomator
Also be auto touch for Summoner War game. There are some versions:
 - Auto get
 - Auto sell
 - Auto summon
 
## How to use

**1. Clone source code into your folder**

**2. Using ant build to build jar file**

**3. Put on your device:**

adb -s emulator-5554 push E:\SMW\jar\BLexpfarmget.jar /data/local/tmp/

**4. Running function**

adb -s emulator-5554 shell uiautomator runtest BLexpfarmget.jar -c com.samsung.android.tests.snote.SNoteZoomTests 
