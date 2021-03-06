# Taskmaster  
This android application can be used to handle tasks.  

# ScreenShots


![image description](ScreenShots/mainView.png)  

![image description](ScreenShots/addTaskView.png)  

![image description](ScreenShots/AllTaskView.png)  

# Lab33: Related Data  

In this lab I created a second entity for a team, which has a name and a list of tasks. and I did relations between Task and Team models to assign a bunch of tasks for each team in my application.  

![image description](ScreenShots/Team1.png)  

![image description](ScreenShots/Team2.png)  

![image description](ScreenShots/Team3.png)  

# Lab34: Publishing to the Play Store  

How to create a developer account: 

1- You should visit "https://play.google.com/console/signup/playSignup".  
2- then you choose "Yourself" choice, for personal use.  
3- Then you should fill out the application:  

- Developer name
- Contact name
- Contact email address
- Contact address
- Contact phone number
- Website "Enter the url of your website or social media profile"

after that you should specify your card information to pay.

# Lab36: Cognito  

In this lab I updated my application with authentication using cognito in amazon. I added signup page, login page and verfication page.  
In addition the user can logout from application using logout button.  

![alt text](ScreenShots/sign_up_page.png)  
![alt text](ScreenShots/login_page.png)  
![alt text](ScreenShots/verfication_page.png)  
![alt text](ScreenShots/logout_settings.png)  
![alt text](ScreenShots/Inkedmain_username_LI.jpg) 


# Lab: 37 - S3  

In this lab I added an upload button to add task form, the user can upload photos to the AmazonS3.  
On the Task detail activity, the user can see an image associated with the task, that image should be displayed within that activity.  

![alt text](ScreenShots/form_add_task.png)  
![alt text](ScreenShots/taskdetailsInfo.png)  

# Lab: 38 - Notifications 

In this lab I added an intent filter to my application that a user can hit the “share” button on an image in another application, then choose TaskMaster as the app to share that image with, and be taken directly to the Add a Task activity with that image pre-selected. 

![InkeduploadFromMobile_LI](https://user-images.githubusercontent.com/97670198/172723153-ffe7c134-5eb4-4bb1-990e-d40ad47ef627.jpg)

# Lab: 39 - Location

The application will add the user’s location to a task automatically when that task is created and on the Task Detail activity,
the location of a Task should be displayed if it exists.

![alt text](ScreenShots/permissions.png)  
![alt text](ScreenShots/locations.png)

# Lab: 41 - Intent Filters

The application will start collecting basic, aggregated analytics on user usage, translate text from english to arabic and will also
read task descriptions to users out loud.

On the “Main” activity, start recording at least one AnalyticsEvent.
On the Task Detail activity, I added a button to read out the task’s description using the Amplify Predictions library.




![alt text](ScreenShots/analytic.png)
![alt text](ScreenShots/read_btn.png)
![alt text](ScreenShots/translate.png)


# Lab: 42 - Monetization And AdMob Ads

On the “Main” activity, I added a banner ad to the bottom of the page and display a Google test ad there, in addition , I added two buttons
the first one allows users to see an interstitial ad and the second allows users to see a rewarded ad.

![alt text](ScreenShots/Ads.png)  
![alt text](ScreenShots/Interstitial_ads.png)  
![alt text](ScreenShots/RewardedAds.png)


