CS282ContentSynchronizer
========================

Repo for the CS282 at Vanderbilt


====================================================
How to use:
1. Lanch two emulators -5554 and -5556

2. Input command in adb tool in the android platform-tools: adb -s emulator-5554 forward tcp:7777 tcp:7777

3. Click the Sync Button in emulator -5556. Then the emulator -5554 will automatically keep its content same with emulator -5556.

4. Sync button on -5554 is invalid until emulator -5556 release its connection with -5554. This is because both emulator listen to the same port.

	The first device trying to connect to port:7777 would take up the port.
	And you need to use command in step 2(modifying conresponding parameters) to sync in inverse direction.

#The first step to launch two emulators.
![Image Alt](/Demo/twoemulator.png)

#The second step to setup emulator to listen to port 7777 of the host
![Image Alt](/Demo/Command.png)

#Then you can input any URL that contains images. The app will parse the url and download all the images in an asynchronized way.
![Image Alt](/Demo/Downloads.png)

#When you click the setup synchronization button. One emulator will connect another one to sychronize the content in their content provider.
![Image Alt](/Demo/Perform.png)



